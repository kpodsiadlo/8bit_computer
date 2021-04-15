from engine.computer_controller import ComputerController
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
    # print(data)
    data_json = json.dumps(data)
    await websocket.send(data_json)


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
            data = controller.execute_one_computer_cycle_and_return_state()
            await send_to_server(websocket, data)

        if message_type == 'clockEnabled':
            clockRunning = message_json['clockEnabled']
            if clockRunning:
                controller.start_computer()
                print("START")
            else:
                controller.stop_computer()
                print("STOP")

        if message_type == 'ramUpdate':
            clockRunning = controller.get_clock_running_state()
            if clockRunning:
                controller.set_computer_ram(message_json['memoryContents'])
            else:
                controller.set_computer_ram(message_json['memoryContents'])
                data = controller.reset_computer_and_return_state()
                await send_to_server(websocket, data)

        if message_type == 'reset':
            controller.stop_computer()
            data = controller.reset_computer_and_return_state()
            await send_to_server(websocket, data)

        if message_type == 'getUpdate':
            data = controller.get_computer_state()
            await send_to_server(data, websocket)


async def send_ping(websocket):
    data = {'type': 'clockStopped'}
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
