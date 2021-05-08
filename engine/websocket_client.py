from engine.computer_controller import ComputerController
from message_processor import process_incoming_message
import json
import asyncio
import websockets

class WebsocketClient():

    def __init__(self):
        self.uri = "ws://localhost:8080/server/computer"
        self.controller = ComputerController()
        self.clock_speed = 10
        self.period = 1 / self.clock_speed


    async def run_computer(self, websocket):
        data = self.controller.get_computer_state()
        await self.send_to_server(websocket, data)
        while True:
            if self.controller.get_clock_running_state() == True:
                data = self.controller.execute_one_computer_cycle_and_return_state()
                await self.send_to_server(websocket, data)
                await asyncio.sleep(float(self.period))
            else:
                await self.send_ping(websocket)
                # print(cycles_elapsed)
                await asyncio.sleep(1)


    async def send_to_server(self, websocket, data):
        print("Data Being Sent:")
        print(data)
        data_json = json.dumps(data)
        await websocket.send(data_json)


    async def send_ping(self, websocket):
        data = {'type': 'clockStopped'}
        await self.send_to_server(websocket, data)


    async def receive(self, message, websocket):
        message_json = json.loads(message)
        print("Data received" + message_json.__str__())
        data = process_incoming_message(message_json, self.controller)
        if data != None:
            await self.send_to_server(websocket, data)


    async def producer_handler(self, websocket):
        await self.run_computer(websocket)


    async def consumer_handler(self, websocket):
        async for message in websocket:
            await self.receive(message, websocket)


    async def handler(self):
        async with websockets.connect(self.uri) as websocket:
            consumer_task = asyncio.ensure_future(
                self.consumer_handler(websocket))
            producer_task = asyncio.ensure_future(
                self.producer_handler(websocket))
            done, pending = await asyncio.wait(
                [consumer_task, producer_task],
                return_when=asyncio.FIRST_COMPLETED,
            )
            for task in pending:
                task.cancel()

    def start(self):
        asyncio.get_event_loop().run_until_complete(self.handler())
