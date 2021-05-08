from engine.computer_controller import ComputerController
from message_processor import process_incoming_message
import json
import asyncio
import websockets

uri = "ws://localhost:8080/server/computer"

controller = ComputerController()
clock_speed = 10
period = 1 / clock_speed


async def run_computer(websocket):
    data = controller.get_computer_state()
    await send_to_server(websocket, data)
    while True:
        if controller.get_clock_running_state() == True:
            data = controller.execute_one_computer_cycle_and_return_state()
            await send_to_server(websocket, data)
            await asyncio.sleep(float(period))
        else:
            await send_ping(websocket)
            # print(cycles_elapsed)
            await asyncio.sleep(1)


async def send_to_server(websocket, data):
    print("Data Being Sent:")
    print(data)
    data_json = json.dumps(data)
    await websocket.send(data_json)


async def send_ping(websocket):
    data = {'type': 'clockStopped'}
    await send_to_server(websocket, data)


async def receive(message, websocket):
    message_json = json.loads(message)
    print("Data received" + message_json.__str__())
    data = process_incoming_message(message_json, controller)
    if data != None:
        await send_to_server(websocket, data)


async def producer_handler(websocket):
    await run_computer(websocket)


async def consumer_handler(websocket):
    async for message in websocket:
        await receive(message, websocket)


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
