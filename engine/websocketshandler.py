from computer import Computer
import json
import asyncio
import websockets

uri = "ws://localhost:8080/server/computer"
time_interval_in_seconds = 1
computer = Computer()


# computer.clock.start(computer)


async def run_computer(websocket):
    while True:
        if computer.clock.clock_running == True:
            await asyncio.sleep(float(time_interval_in_seconds))
            await execute_one_cycle_and_send_update_to_server(websocket)
        else:
            await asyncio.sleep(1)
            print("SLEEPING")


async def execute_one_cycle_and_send_update_to_server(websocket):
    data = next(computer.execute_one_click_and_yield_computer_state(computer))
    print(data)
    data_json = json.dumps(data)
    await websocket.send(data_json)


async def receive(message, websocket):
    message_json = json.loads(message)
    print("Data received" + message_json.__str__())
    clockRunning = None;

    try:
        clockRunning = message_json["clockRunning"]
    except KeyError:
        print("No Clock Information in JSON")

    if clockRunning is not None:
        if not clockRunning:
            computer.clock.stop(computer)
            print("STOP")
        if clockRunning:
            computer.clock.start(computer)
            print("START")
    elif message_json["tick"]:
        await execute_one_cycle_and_send_update_to_server(websocket)


#  except KeyError:
#     print("KeyError: clockRunning")


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
