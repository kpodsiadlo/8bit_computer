import websockets
import time
import json
import asyncio
from computer import Computer

uri = "ws://localhost:8080/server/computer"

time_interval_in_seconds = 0.001

computer = Computer()
computer.clock.start(computer)

async def receive(socket):
    input_data = await socket.recv()
    json.loads(input_data)
    print("Data received" +input_data.__str__())


async def loop():
    async with websockets.connect(uri) as websocket:
        while True:
            if computer.clock.clock_running == 1:
                time.sleep(float(time_interval_in_seconds))
                data = next(computer.execute_one_click_and_yield_computer_state(computer))
                data_json = json.dumps(data)
                print(data_json)
                await websocket.send(data_json)
                await receive(websocket)


asyncio.get_event_loop().run_until_complete(loop())