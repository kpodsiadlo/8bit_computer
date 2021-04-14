import random

class Component:
    def __init__(self, computer):
        self.computer = computer
        self.state = 0


class RAM(Component) :
    def __init__(self, computer):
        super().__init__(computer)
        self.state = [random.randint(0, 255) for memory_cell in range(16)]

    def do_in(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.RAM_IN == 1:
                self.state[self.computer.mar.state] = self.computer.buss.state

    def do_out(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.RAM_OUT == 1:
                self.computer.buss.state = self.state[self.computer.mar.state]


class Display(Component):
    def do_in(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.display_IN == 1:
                self.state = self.computer.buss.state
                print("\n#############")
                print("Display: " + self.state.__str__())
                print("#############")

class MAR(Component):

    def do_in(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.MAR_IN == 1:
                self.state = self.computer.buss.state

class ALU(Component):
    def __init__(self, regA, regB, computer):
        super().__init__(computer)
        self.state = regA.state + regB.state
        self.zero = 0
        self.carry = 0

    def do_out(self):
        self.state = self.computer.regA.state + self.computer.regB.state
        self.calculate()
        if self.computer.logic.ALU_OUT == 1:
            self.computer.buss.state = self.state

    def calculate(self):
        self.zero = 0
        self.carry = 0
        if self.computer.logic.ALU_Substract == 1:
            self.state = self.computer.regA.state - self.computer.regB.state

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

class ProgramCounter(Component):

    def increase(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.PC_enable == 1:
                self.state += 1
                #               print("PC: " + (self.state-1).__str__())
                print(".", end="", flush=True)
            if self.state > 15:
                self.state = 0

    def do_in(self):
        if self.computer.logic.PC_Jump == 1:
            self.state = self.computer.buss.state

    def do_out(self):
        if self.computer.logic.PC_OUT == 1:
            self.computer.buss.state = self.state

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


class Bus(Component):
    def __init__(self, computer):
        super().__init__(computer)
        self.state = None

    def reset(self, computer):
        computer.buss.state = None


class RegisterA(Component):
    def do_in(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.AregisterIN == 1:
                self.state = self.computer.buss.state

                # print("RegA: " + self.state.__str__())

    def do_out(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.AregisterOUT == 1:
                self.computer.buss.state = self.state

                # print("RegA: " + self.state.__str__())

class RegisterB(Component):

    def do_in(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.BregisterIN == 1:
                self.state = self.computer.buss.state

                # print("RegB: " + self.state.__str__())

    def do_out(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.BregisterOUT == 1:
                self.computer.buss.state = self.state

                # print("RegB: " + self.state.__str__())

class InstructionRegister(Component):
    def __init__(self, computer):
        super().__init__(computer)
        self.higher_bits = 0
        self.lower_bits = 0

    def do_in(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.IregisterIN == 1:
                self.state = self.computer.buss.state
                self.split()
                # print("Instruction register: " + self.state.__str__())

    def do_out(self):
        if self.computer.clock.state == 1:
            if self.computer.logic.IregisterOUT == 1:
                self.computer.buss.state = self.lower_bits
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