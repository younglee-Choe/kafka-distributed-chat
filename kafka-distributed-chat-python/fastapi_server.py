import os
import httpx
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware
from dotenv import load_dotenv

load_dotenv()

app = FastAPI()

SPRING_BOOT_URL = os.environ.get('SPRING_BOOT_URL')

# 요청 데이터 모델
# class ChatRequest(BaseModel):
#     topic_name: str
    
# class ChatResponse(BaseModel):
#     response: str

class TopicName(BaseModel):
    text: str
    
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 또는 ["http://localhost:8080"] 등 Spring Boot 도메인 설정
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
    
# Spring Boot -> FastAPI, Topic Name 전송 받음
@app.post("/chat/topic")
async def receive_topic_name(message: TopicName):
    print(f"📌 Received from Spring Boot: {message.text}")
    
    return {"status": "success", "message": f"Received: {message.text}"}
    # print(f"📩 Topic Name: {topic_name}")
    # return "🔥 successed receive the topic name!"

# FastAPI -> Spring Boot, 생성된 AI 응답 전송
@app.post("/chat/response")
async def send_response_to_springboot(message: str):
    try:
        async with httpx.AsyncClient() as client:
            response = await client.post(SPRING_BOOT_URL, data=message)
            if response.status_code == 200:
                return response.text
            else:
                raise HTTPException(status_code=response.status_code, detail="Failed to send AI response to Spring Boot")
    except Exception as e:
        raise HTTPException(status_code=500, defail=f"An error occurred: {str(e)}")
        # return response.text