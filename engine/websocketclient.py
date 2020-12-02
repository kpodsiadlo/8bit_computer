import asyncio
import websockets

async def hello():
    uri = "ws://localhost:8080/server/computer"
    async with websockets.connect(uri) as websocket:

        greeting = await websocket.recv()
        print(f"< {greeting}")

async def loop():
    while True:
        await hello()

asyncio.get_event_loop().run_until_complete(loop())