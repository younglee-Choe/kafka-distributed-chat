import os
import asyncio
import json
from fastapi import FastAPI
from openai import AsyncOpenAI
from aiokafka import AIOKafkaConsumer
from contextlib import asynccontextmanager
from dotenv import load_dotenv
from fastapi_server import receive_topic_name, send_response_to_springboot

load_dotenv()

API_KEY = os.environ.get('API_KEY')
client = AsyncOpenAI(api_key=API_KEY)

# execute FastAPI Server and Kafka Consumer
@asynccontextmanager
async def lifespan(app: FastAPI):
    # When service starts
    task = asyncio.create_task(consume_messages())

    yield
    
    # When service is stopped
    task.cancel()
       
app = FastAPI(lifespan=lifespan)

KAFKA_BOOTSTRAP_SERVERS = os.environ.get('KAFKA_BOOTSTRAP_SERVERS')
KAFKA_CONSUMER_TOPIC = {room_id}   # topic name is {room_id}

consumer = AIOKafkaConsumer(
    KAFKA_CONSUMER_TOPIC,
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
    group_id=os.environ.get('KAFKA_GROUP_ID'),
    enable_auto_commit=True,
    auto_commit_interval_ms=1000,
)

# call the OpenAI API to provide an automated response to the user
async def generate_gpt_response(user_message: str) -> str:
    response = await client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": user_message}],
    )
    
    return response.choices[0].message.content

async def consume_messages():
    await consumer.start()
    print("⚙️ Consumer start")
    try:
        async for msg in consumer:
            consumed = msg.value.decode("utf-8")
            print(f"Consumed from Kafka: {consumed}")
            
            user_message = json.loads(consumed).get('message')
            print(f"📩 받은 메시지: {user_message}")
            
            # create automated response with gpt-3.5-turbo model
            response_message = await generate_gpt_response(user_message)
            print(f"🤖 AI 응답: {response_message}")
            send_to_springboot = await send_response_to_springboot(response_message)
            
    finally:
        await consumer.stop()
        print("⚙️ Consumer Stop")