from typing import Any

from computer import Computer
import json
import asyncio
import websockets

uri = "ws://localhost:8080/server/computer"
clock_speed = 10
period = 1/clock_speed
computer = Computer()

async def run_computer(websocket):
    await get_computer_state_and_send_to_server(websocket)
    while True:
        if computer.clock.clock_running == True:
            await execute_one_cycle_and_send_update_to_server(websocket)
            await asyncio.sleep(float(period))
        else:
            await send_ping(websocket)
            await asyncio.sleep(1)


async def send_to_server(websocket, data):
    #print(data)
    data_json = json.dumps(data)
    await websocket.send(data_json)

async def get_computer_state_and_send_to_server(websocket):
    data = next(computer.yield_computer_state(computer))
    await send_to_server(websocket, data)

async def execute_one_cycle_and_send_update_to_server(websocket):
    for i in range (2) :
        data = next(computer.execute_one_click_and_yield_computer_state(computer))
    await send_to_server(websocket, data)

async def resetComputer(websocket):
        computer.reset_computer_except_ram()
        await execute_one_cycle_and_send_update_to_server(websocket)


async def receive(message, websocket):
    message_json = json.loads(message)
    print("Data received" + message_json.__str__())
    await process_incoming_message(message_json, websocket)


async def process_incoming_message(message_json, websocket):
    message_type = None

    try:
        message_type = message_json['type']
    except KeyError:
        print("Json contains no \"type\" field")

    if message_type is not None:
        if message_type == 'advanceClock':
            await execute_one_cycle_and_send_update_to_server(websocket)

        if message_type == 'clockEnabled':
            clockRunning = message_json['clockEnabled']
            if clockRunning:
                computer.clock.start(computer)
                print("START")
            else:
                computer.clock.stop(computer)
                print("STOP")

        if message_type == 'ramUpdate':
            clockRunning = computer.clock.clock_running
            if clockRunning:
                computer.ram.state = message_json['memoryContents']
            else:
                computer.ram.state = message_json['memoryContents']
                await resetComputer(websocket)

        if message_type == 'reset':
            computer.clock.stop(computer)
            await resetComputer(websocket)

        if message_type == 'getUpdate':
            await get_computer_state_and_send_to_server(websocket)


async def send_ping(websocket):
    data={'type': 'clockStopped'}
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
