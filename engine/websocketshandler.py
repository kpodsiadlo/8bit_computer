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
    while True:
        if computer.clock.clock_running == True:
            await asyncio.sleep(float(period))
            await execute_one_cycle_and_send_update_to_server(websocket)
        else:
            await asyncio.sleep(1)
            await get_computer_state_and_send_to_server(websocket)


async def send_to_server(websocket, data):
    print(data)
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
    await get_computer_state_and_send_to_server(websocket)


async def receive(message, websocket):
    message_json = json.loads(message)
    print("Data received" + message_json.__str__())
    await process_incoming_message(message_json, websocket)


async def process_incoming_message(message_json, websocket):
    clockRunning = None
    reset = None
    tick = None
    ramUpdate = None

    try:
        clockRunning = message_json["clockRunning"]
    except KeyError:
        print("No Clock Information in JSON")
    try:
        reset = message_json["reset"]
    except KeyError:
        print("No Reset in Json")
    try:
        tick = message_json["tick"]
    except KeyError:
        print("No tick in json")
    try:
        ramUpdate = message_json["ramUpdate"]
    except KeyError:
        print("This is not a RamUpdate")

    if clockRunning is not None:
        if not clockRunning:
            computer.clock.stop(computer)
            print("STOP")
        if clockRunning:
            computer.clock.start(computer)
            print("START")
    elif tick is not None:
        await execute_one_cycle_and_send_update_to_server(websocket)
    elif reset is not None:
        await resetComputer(websocket)
    elif ramUpdate is not None:
        computer.ram.state = message_json['memoryContents']
        await get_computer_state_and_send_to_server(websocket)


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
