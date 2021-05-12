from engine.computer.computer import Computer

class ComputerController():

    computer = Computer()

    def stop_computer(self):
        self.computer.clock.stop()

    def start_computer(self):
        self.computer.clock.start()

    def get_computer_state(self):
        return next(self.computer.yield_computer_state())

    def get_clock_running_state(self):
        return self.computer.clock.clock_running

    def set_computer_ram(self, data):
        self.computer.ram.state = data

    def execute_one_computer_cycle_and_return_state(self):
        for i in range(2):
            data = next(self.computer.toggle_clock_and_yield_computer_state())
        return data

    def reset_computer_and_return_state(self):
        self.computer.reset_computer_except_ram()
        data = next(self.computer.yield_computer_state())
        return data