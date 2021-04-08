from computer import Computer
import json
import asyncio
import websockets

uri = "ws://localhost:8080/server/computer"
time_interval_in_seconds = 0.1
computer = Computer()
computer.clock.start(computer)

async def run_computer(websocket):
    while True:
        if computer.clock.clock_running == 1:
            await asyncio.sleep(float(time_interval_in_seconds))
            data = next(computer.execute_one_click_and_yield_computer_state(computer))
            data_json = json.dumps(data)
            await websocket.send(data_json)
        else:
            await asyncio.sleep(1)
            print("SLEEPING")


async def receive(message):
        message_json = json.loads(message)
        print("Data received" + message_json.__str__())
     #   try:
        if message_json["clockRunning"] == False:
            computer.clock.stop(computer)
            print("STOP")
        if message_json["clockRunning"] == True:
            computer.clock.start(computer)
            print("START")
      #  except KeyError:
    #     print("KeyError: clockRunning")


async def producer_handler(websocket):
    await run_computer(websocket)

async def consumer_handler(websocket):
    async for message in websocket:
        await receive(message)



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