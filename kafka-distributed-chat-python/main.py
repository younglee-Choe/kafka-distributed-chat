import asyncio
from fastapi import FastAPI
from aiokafka import AIOKafkaConsumer
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
from consumer import consume_messages, app as consumer_app
from idp.processing import app as processing_app
from pydantic import BaseModel
from dotenv import load_dotenv
import os

load_dotenv()

app = FastAPI()
task = None
KAFKA_BOOTSTRAP_SERVERS = os.environ.get('KAFKA_BOOTSTRAP_SERVERS')

class TopicRequest(BaseModel):
    topicName: str

@asynccontextmanager
@app.post("/chat/topic")
async def receive_topic_name(request: TopicRequest):
    global task
        
    print(f"⚙️ Received Topic Name: {request.topicName}")
    
    consumer = AIOKafkaConsumer(
        request.topicName,
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        group_id=os.environ.get('KAFKA_GROUP_ID'),
        enable_auto_commit=True,
        auto_commit_interval_ms=1000,
    )

    # 기존 task가 실행 중이면 취소
    if task and not task.done():
        task.cancel()

    # 새로운 task 시작
    task = asyncio.create_task(consume_messages(consumer))

    return {"message": f"Received topic: {request.topicName}"}


# `consumer.py`의 라우트 포함
app.mount("/consumer", consumer_app)

# `processing.py`의 라우트 포함
app.mount("/idp", processing_app)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
