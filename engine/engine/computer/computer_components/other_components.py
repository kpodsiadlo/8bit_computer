import random


class Clock():
    def __init__(self):
        self.state = 0
        self.clock_running = False

    def toggle(self, computer):
        self.state = not self.state
        if self.state == 1:  ### only true tick
            # print("Tick: " + computer.clock.state.__str__())
            computer.emulate_components()

    def start(self):
        self.clock_running = True

    def stop(self):
        self.clock_running = False


class ProgramCounter():
    def __init__(self):
        self.state = 0

    def increase(self, clock_state, logic_PC_enable):
        if clock_state == 1:
            if logic_PC_enable == 1:
                self.state += 1
                #               print("PC: " + (self.state-1).__str__())
                print(".", end="", flush=True)
            if self.state > 15:
                self.state = 0

    def get_data_from_bus(self, logic_PC_JUMP, bus):
        if logic_PC_JUMP == 1:
            self.state = bus.state

    def output_to_bus(self, logic_PC_OUT, bus):
        if logic_PC_OUT == 1:
            bus.state = self.state


class MAR():
    def __init__(self):
        self.state = 0

    def get_data_from_bus(self, clock_state, logic, bus):
        if clock_state == 1:
            if logic.MAR_IN == 1:
                self.state = bus.state


class RAM:
    def __init__(self):
        self.state = [random.randint(0, 255) for memory_cell in range(16)]

    def get_data_from_bus(self, clock_state, logic_RAM_IN, mar_state, bus):
        if clock_state == 1:
            if logic_RAM_IN == 1:
                self.state[mar_state] = bus.state

    def output_to_bus(self, clock_state, logic_RAM_OUT, mar_state, bus):
        if clock_state == 1:
            if logic_RAM_OUT == 1:
                bus.state = self.state[mar_state]


class Display:
    def __init__(self):
        self.state = 0

    def get_data_from_bus(self, clock_state, logic_display_IN, bus):
        if clock_state == 1:
            if logic_display_IN == 1:
                self.state = bus.state
                print("\n#############")
                print("Display: " + self.state.__str__())
                print("#############")


class Bus:
    def __init__(self):
        self.state = None

    def reset(self):
        self.__init__()


class InstructionRegister():
    def __init__(self):
        self.state = 0
        self.higher_bits = 0
        self.lower_bits = 0

    def get_data_from_bus(self, clock_state, logic_IregisterIN, bus):
        if clock_state == 1:
            if logic_IregisterIN == 1:
                self.state = bus.state
                self.split()
                # print("Instruction register: " + self.state.__str__())

    def output_to_bus(self, clockState, logic_IregisterOUT, bus):
        if clockState == 1:
            if logic_IregisterOUT == 1:
                bus.state = self.lower_bits
                # print("Instruction register:: " + self.state.__str__())

    def split(self):
        self.higher_bits = self.state >> 4
        #       print("Instruction: " + self.higher_bits.__str__())
        self.lower_bits = self.state & 0b00001111

        # print("Data         " + self.lower_bits.__str__())


class InstructionCounter():
    def __init__(self):
        self.state = 4

    def increase(self, clock_state):
        if clock_state == 1:
            self.state += 1
            if self.state > 4:
                self.state = 0

            # print("Instruction counter:" + self.state.__str__())
