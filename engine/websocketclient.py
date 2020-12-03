import asyncio
import websockets

async def get_message():
    uri = "ws://localhost:8080/server/computer"
    async with websockets.connect(uri) as websocket:

        message = await websocket.recv()
        print(message)

async def loop():
    while True:
        await get_message()

asyncio.get_event_loop().run_until_complete(loop())