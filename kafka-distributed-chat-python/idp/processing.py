from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from nltk.tokenize import word_tokenize
from konlpy.tag import Mecab
from nltk.corpus import stopwords
from openai import AsyncOpenAI, OpenAI
from sentence_transformers import SentenceTransformer
from fastapi.middleware.cors import CORSMiddleware
from typing import List
from langdetect import detect
from dotenv import load_dotenv
import pdfplumber
import docx
import chromadb
import nltk
import sys, os
import uuid
import spacy
import asyncio

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
import fastapi_server

load_dotenv()

app = FastAPI()

API_KEY = os.environ.get('API_KEY')
client = AsyncOpenAI(api_key=API_KEY)

nltk.download("punkt")  # 단어 토큰화를 위한 라이브러리 다운로드
nltk.download("stopwords")  # 불용어 리스트 다운로드

MECAB_KO_DIC = os.environ.get('MECAB_KO_DIC')
nlp_en = spacy.load("en_core_web_sm")   # spaCy 영어 모델 로드
mecab = Mecab(dicpath=MECAB_KO_DIC)   # MeCab 한국어 로드

# 벡터화 모델 로드 (SBERT 임베딩 모델)
embedding_model = SentenceTransformer("all-MiniLM-L6-v2")

# ChromaDB 설정
# 영구 저장을 위한 PersistentClient (로컬 DB 저장)
chroma_client = chromadb.PersistentClient(path="./chroma_db")
# 벡터 데이터를 그룹화
collection = chroma_client.get_or_create_collection(name="documents")

# PDF 텍스트 추출
async def extract_text_from_pdf(pdf_file):
    loop = asyncio.get_event_loop()
    return await loop.run_in_executor(None, lambda: "\n".join(
        page.extract_text() for page in pdfplumber.open(pdf_file).pages if page.extract_text()
    ))
            
# Word(DOCX) 텍스트 추출
async def extract_text_from_docx(docx_file):
    loop = asyncio.get_event_loop()
    return await loop.run_in_executor(None, lambda: "\n".join(
        para.text for para in docx.Document(docx_file).paragraphs
    ))

# 전처리
async def preprocess_nlp(text):
    lang = detect(text)
    try: 
        if lang == "en":
            doc = nlp_en(text)
            tokens = [token.text for token in doc]
        elif lang == "ko":
            tokens = mecab.morphs(text)
   
        stop_words = {"은", "는", "이", "가", "의", "들", "을", "를", "에게", "에서", "하다"}
        filtered_tokens = [word for word in tokens if word not in stop_words]
        # print(f"⚙️ 텍스트 전처리 및 형태소 분석: {filtered_tokens}")
    
    except Exception as e:
        print(f"❌ 오류 발생: {e}")
    
    return " ".join(filtered_tokens)

# 문서 요약 & 분류 (GPT 활용)
async def process_document(text):
    response = await client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "문서를 분석하여 요약하고, 카테고리를 지정하세요."},
            {"role": "user", "content": f"문서 내용: {text}\n\n1. 요약:\n2. 카테고리:"}
        ]
    )
    
    send_to_springboot = await fastapi_server.send_response_to_springboot(response.choices[0].message.content)
    print(f"response from spring boot: {send_to_springboot}")
        
    return response.choices[0].message.content


# 파일 업로드 & 벡터화 후 DB 저장
# 1) 파일 업로드(React -> Fast API)
@app.post("/file/upload")
async def upload_file(files: List[UploadFile] = File(...)):
    try:
        tasks = []
        
        for file in files:
            # 2) 텍스트 추출
            if file.filename.endswith(".pdf"):
                tasks.append(extract_text_from_pdf(file.file))
            elif file.filename.endswith(".docx"):
                tasks.append(extract_text_from_docx(file.file))
            else:
                raise HTTPException(status_code=400, detail="지원되지 않는 파일 형식")
        
        texts = await asyncio.gather(*tasks)
        
        # 3) 문서 요약 및 분류 & 전처리
        for text, file in zip(texts, files):
            summary_result, preprocessed = await asyncio.gather(
                process_document(text),
                preprocess_nlp(text)
            )
            # print(f"⚙️ 문서 요약 및 분류:\n{summary_result}")
        
        # 4) 벡터화
        loop = asyncio.get_event_loop()
        vector = await loop.run_in_executor(None, lambda: embedding_model.encode(preprocessed).tolist())
        
        # UUID 생성
        doc_id = str(uuid.uuid4())
        
        await loop.run_in_executor(None, lambda: collection.add(
            ids=[doc_id],
            embeddings=[vector],
            metadatas=[{"name": file.filename, "summary": summary_result, "text": text}]
        ))
    
        return {"message": "파일이 성공적으로 업로드 및 저장되었습니다", "summary": summary_result}
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# 사용자 질문 벡터화 후 유사 문서 검색
@app.post("/query")
async def query_vector_db(query: str):
    loop = asyncio.get_event_loop()

    # 6) 사용자 질문 -> 전처리 -> 벡터화
    query_vector = await loop.run_in_executor(None, lambda: embedding_model.encode(query).tolist())

    # 7) 벡터 스토어에서 유사한 문서 검색
    search_results = await loop.run_in_executor(None, lambda: collection.query(query_embeddings=[query_vector], n_results=3))

    # 검색된 문서 내용 추출
    matched_texts = [match["text"] for match in search_results["metadatas"][0]] if search_results["metadatas"] else []
    if not matched_texts:
        return {"answer": "관련 문서를 찾을 수 없습니다."}

    # # 8) GPT에게 질문 + 문서 내용 전달하여 응답 생성
    response = await client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "다음 문서를 참고하여 사용자의 질문에 답변하세요."},
            {"role": "user", "content": query + "\n\n관련 문서:\n" + "\n".join(matched_texts)}
        ]
    )
    
    return {"answer": response.choices[0].message.content}

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)