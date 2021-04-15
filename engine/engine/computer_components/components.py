import random

class Component:
    def __init__(self, computer):
        self.state = 0
        self.computer = computer


class RAM :
    def __init__(self):
        self.state = [random.randint(0, 255) for memory_cell in range(16)]

    def do_in(self, clock_state, logic_RAM_IN, mar_state, bus):
        if clock_state == 1:
            if logic_RAM_IN == 1:
                self.state[mar_state] = bus.state

    def do_out(self, clock_state, logic_RAM_OUT, mar_state, bus):
        if clock_state == 1:
            if logic_RAM_OUT == 1:
                bus.state = self.state[mar_state]


class Display:
    def __init__(self):
        self.state = 0

    def do_in(self, clock_state, logic_display_IN, bus):
        if clock_state == 1:
            if logic_display_IN == 1:
                self.state = bus.state
                print("\n#############")
                print("Display: " + self.state.__str__())
                print("#############")

class MAR():
    def __init__(self):
        self.state = 0

    def do_in(self, clock, logic, bus):
        if clock.state == 1:
            if logic.MAR_IN == 1:
                self.state = bus.state

class ALU():
    def __init__(self, regA, regB):
        self.state = regA.state + regB.state
        self.zero = 0
        self.carry = 0

    def do_out(self, register_a, register_b, logic_alu_out, logic_alu_subtract, bus):
        self.state = register_a + register_b
        self.calculate(register_a, register_b, logic_alu_subtract)
        if logic_alu_out == 1:
            bus.state = self.state

    def calculate(self, register_a, register_b, logic_alu_subtract):
        self.zero = 0
        self.carry = 0
        if logic_alu_subtract == 1:
            self.state = register_a - register_b

        if self.state > 255:
            self.state -= 256
            self.carry = 1
        #            print("ALU Carry")
        if self.state == 0:
            self.zero = 1
        #            print("ALU ZERO")
        if self.state < 0:
            self.state += 256
            self.carry = 1

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

    def do_in(self, logic_PC_JUMP, bus):
        if logic_PC_JUMP == 1:
            self.state = bus.state

    def do_out(self, logic_PC_OUT, bus):
        if logic_PC_OUT == 1:
            bus.state = self.state

class Clock(Component):
    def __init__(self, computer):
        super().__init__(computer)
        self.clock_running = False

    def tick(self):
        self.computer.clock.state = not self.computer.clock.state
        if self.computer.clock.state == 1:  ### only true tick
            # print("Tick: " + computer.clock.state.__str__())
            self.computer.do()

    def start(self):
        self.computer.clock.clock_running = True

    def stop(self):
        self.computer.clock.clock_running = False


class Bus:
    def __init__(self):
        self.state = None

    def reset(self):
        self.__init__()


class RegisterA():
    def __init__(self):
        self.state = 0

    def do_in(self, clock_state, logic_AregisterIN, bus):
        if clock_state == 1:
            if logic_AregisterIN == 1:
                self.state = bus.state

                # print("RegA: " + self.state.__str__())

    def do_out(self, clock_state, logic_AregisterOUT, bus):
        if clock_state == 1:
            if logic_AregisterOUT == 1:
                bus.state = self.state

                # print("RegA: " + self.state.__str__())

class RegisterB():
    def __init__(self):
        self.state = 0

    def do_in(self, clock_state, logic_BregisterIN, bus):
        if clock_state == 1:
            if logic_BregisterIN == 1:
                self.state = bus.state

                # print("RegB: " + self.state.__str__())

    def do_out(self, clock_state, logic_BregisterOUT, bus):
        if clock_state == 1:
            if logic_BregisterOUT == 1:
                bus.state = self.state

                # print("RegB: " + self.state.__str__())

class InstructionRegister():
    def __init__(self):
        self.state = 0
        self.higher_bits = 0
        self.lower_bits = 0

    def do_in(self, clock_state, logic_IregisterIN, bus):
        if clock_state == 1:
            if logic_IregisterIN == 1:
                self.state = bus.state
                self.split()
                # print("Instruction register: " + self.state.__str__())

    def do_out(self, clockState, logic_IregisterOUT, bus):
        if clockState == 1:
            if logic_IregisterOUT == 1:
                bus.state = self.lower_bits
                # print("Instruction register:: " + self.state.__str__())

    def split(self):
        self.higher_bits = self.state >> 4
        #       print("Instruction: " + self.higher_bits.__str__())
        self.lower_bits = self.state & 0b00001111

        # print("Data         " + self.lower_bits.__str__())


class InstructionCounter(Component):
    def __init__(self, computer):
        super().__init__(computer)
        self.state = 4

    def increase(self):
        if self.computer.clock.state == 1:
            self.state += 1
            if self.state > 4:
                self.state = 0

            # print("Instruction counter:" + self.state.__str__())