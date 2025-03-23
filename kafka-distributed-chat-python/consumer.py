import os
import asyncio
import json
from fastapi import FastAPI
from openai import AsyncOpenAI
from aiokafka import AIOKafkaConsumer
from dotenv import load_dotenv
from fastapi_server import send_response_to_springboot, app as fastapi_server_app

load_dotenv()

app = FastAPI()
app.mount("/fastapi", fastapi_server_app)

API_KEY = os.environ.get('API_KEY')
client = AsyncOpenAI(api_key=API_KEY)

# call the OpenAI API to provide an automated response to the user
async def generate_gpt_response(user_message: str) -> str:
    response = await client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": user_message}],
    )
    
    return response.choices[0].message.content

async def consume_messages(consumer):
    await consumer.start()
    print("⚙️ Consumer start")
    try:
        async for msg in consumer:
            consumed = msg.value.decode("utf-8")
            print(f"Consumed from Kafka: {consumed}")
                        
            if json.loads(consumed).get('memberId') == "AI":
                continue
            
            user_message = json.loads(consumed).get('message')
            
            if ".docx" in user_message or ".pdf" in user_message:
                print(f"📤 File Upload: {user_message}")
                continue
            elif "#문서" in user_message:
                continue
            else:
                print(f"📩 Received Message: {user_message}")
                # create automated response with gpt-3.5-turbo model
                response_message = await generate_gpt_response(user_message)
                print(f"🤖 AI Response: {response_message}")
                
                send_to_springboot = await send_response_to_springboot(response_message)
                print(f"Response from Spring Boot: {send_to_springboot}") 
    finally:
        await consumer.stop()
        print("⚙️ Consumer Stop")