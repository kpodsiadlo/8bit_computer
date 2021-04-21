import websockets
import asyncio
import sys
import uuid
from message_processor import WebsocketMessageProcessor

# uuidSignature = sys.argv[1]
uuidSignature = uuid.uuid4()
protocols = [str(uuidSignature)]
print(str(protocols))
message_processor = WebsocketMessageProcessor()

uri = "ws://localhost:8080/server/computer "


async def producer_handler(websocket):
    await message_processor.produce(websocket)


async def consumer_handler(websocket):
    async for message in websocket:
        await message_processor.receive(message, websocket)


async def handler():
    async with websockets.connect(uri) as websocket:
        consumer_task = asyncio.ensure_future(
            consumer_handler(websocket))
    producer_task = asyncio.ensure_future(
        producer_handler(websocket))
    done, pending = await asyncio.wait(
        [consumer_task, producer_task],
        return_when=asyncio.FIRST_COMPLETED,
    )
    for task in pending:
        task.cancel()


asyncio.get_event_loop().run_until_complete(handler())
