from engine.computer_controller import ComputerController
import asyncio
import json


class WebsocketMessageProcessor:
    def __init__(self):
        self.computer_controller = ComputerController()
        self.clock_speed = 10
        self.period = 1 / self.clock_speed

    async def receive(self, message, websocket):
        message_json = json.loads(message)
        print("Data received" + message_json.__str__())
        await self.process_incoming_message(message_json, websocket)

    async def produce(self, websocket):
        computer_state = self.computer_controller.get_computer_state()
        await self.send_to_server(websocket, computer_state)
        while True:
            if self.computer_controller.get_clock_running_state() == True:
                computer_state = self.computer_controller.execute_one_computer_cycle_and_return_state()
                await self.send_to_server(websocket, computer_state)
                await asyncio.sleep(float(self.period))
            else:
                await self.send_ping(websocket)
                # print(cycles_elapsed)
                await asyncio.sleep(1)

    async def send_to_server(self, websocket, data):
        # print(data)
        data_json = json.dumps(data)
        await websocket.send(data_json)

    async def send_ping(self, websocket):
        data = {'type': 'clockStopped'}
        await self.send_to_server(websocket, data)

    async def process_incoming_message(self, message_json, websocket):
        message_type = None

        try:
            message_type = message_json['type']
        except KeyError:
            print("Json contains no \"type\" field")

        if message_type is not None:
            if message_type == 'advanceClock':
                data = self.computer_controller.execute_one_computer_cycle_and_return_state()
                await self.send_to_server(websocket, data)

            if message_type == 'clockEnabled':
                self.start_or_stop_computer(message_json)

            if message_type == 'ramUpdate':
                self.computer_controller.set_computer_ram(message_json['memoryContents'])
                data = self.computer_controller.get_computer_state()
                await self.send_to_server(websocket, data)

            if message_type == 'reset':
                self.computer_controller.stop_computer()
                data = self.computer_controller.reset_computer_and_return_state()
                await self.send_to_server(websocket, data)

            if message_type == 'getUpdate':
                data = self.computer_controller.get_computer_state()
                await self.send_to_server(websocket, data)

    def start_or_stop_computer(self, message_json):
        clockRunning = message_json['clockEnabled']
        if clockRunning:
            self.computer_controller.start_computer()
            print("START")
        else:
            self.computer_controller.stop_computer()
            print("STOP")

