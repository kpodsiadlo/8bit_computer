import time


class Display:
    def __init__(self):
        self.state = 0

    def do(self):
        if clock.state == 1:
            if logic.display_IN == 1:
                self.state = buss.state
        print("Display: " + self.state.__str__())
        

class MAR:
    def __init__(self):
        self.state = 0

    def do(self):
        if clock.state == 1:
            if logic.MAR_IN == 1:
                self.state = buss.state
        print("MAR: " + self.state.__str__())


class RAM:
    def __init__(self):
            self.state = [0 for x in range(16)]

    def do(self):
        if clock.state == 1:
            if logic.RAM_IN == 1:
                self.state[mar.state] = buss.state
            if logic.RAM_OUT == 1:
                buss.state = self.state[mar.state]


class ALU:
    def __init__(self):
        self.regA = regA.state
        self.regb = regB.state
        self.state = regA.state + regB.state
        if logic.ALU_Substract == 1:
            self.state = regA.state - regB.state
        if self.state > 255:
            flag_register.carry = 1


    def do(self):
            self.regA = regA.state
            self.regb = regB.state
            self.state = regA.state + regB.state
            if logic.ALU_Substract == 1:
                self.state = regA.state - regB.state
            if self.state > 255:
                flag_register.carry = 1
            if logic.ALU_OUT == 1:
                buss.state = self.state


class ProgramCounter:
    def __init__(self):
        self.state = 0

    def do(self):
        if clock.state == 1:
            if logic.count_enable == 1:
                self.state += 1
                print("PC: " + self.state.__str__())
            if logic.PC_OUT == 1:
                buss.state = self.state
            if logic.PC_Jump == 1:
                self.state = buss.state


class Logic:
    def __init__(self):
        self.AregisterIN = 0
        self.AregisterOUT = 0
        self.BregisterIN = 0
        self.BregisterOUT = 0
        self.IregisterIN = 0
        self.IregisterOUT = 0
        self.count_enable = 0
        self.PC_OUT = 0
        self.PC_Jump = 0
        self.ALU_OUT = 0
        self.ALU_Substract = 0
        self.RAM_IN = 0
        self.RAM_OUT = 0
        self.MAR_IN = 0
        self.display_IN = 0

    def reset(self):
        self.__init__()


class Clock:  # start(time interval in seconds) .stop
    def __init__(self):
        self.state = 0
        self.timer = 0

    def tick(self):
        self.state = not self.state
        print("Tick: " + self.state.__str__())
        do()

    def start(self, time_interval_in_s):
        self.timer = 1
        while self.timer == 1:
            time.sleep(float(time_interval_in_s))
            self.tick()

    def stop(self):
        self.timer = 0


class Buss:
    def __init__(self):
        self.state = 0


class RegisterA:
    def __init__(self):
        self.state = 0

    def do(self):
        if clock.state == 1:
            if logic.AregisterIN == 1:
                self.state = buss.state
            if logic.AregisterOUT == 1:
                buss.state = self.state
        print("RegA: " + self.state.__str__())


class RegisterB:
    def __init__(self):
        self.state = 0b00000000

    def do(self):
        if clock.state == 1:
            if logic.BregisterIN == 1:
                self.state = buss.state
            if logic.BregisterOUT == 1:
                buss.state = self.state
        print("RegB: " + self.state.__str__())


class InstructionRegister:
    def __init__(self):
        self.state = 0b00000000

    def do(self):
        if clock.state == 1:
            if logic.IregisterIN == 1:
                self.state = buss.state
            if logic.IregisterOUT == 1:
                buss.state = self.state
        print("RegB: " + self.state.__str__())


class FlagRegister():
    def __init__(self):
        self.carry = 0

def do():
    pc.do()
    mar.do()
    ram.do()
    alu.do()
    regA.do()
    regB.do()
    display.do()


def bprint(output):
        print(format(output, '#010b'))


display = Display()
mar = MAR()
ram = RAM()
buss = Buss()
pc = ProgramCounter()
logic = Logic()
clock = Clock()
regA = RegisterA()
regB = RegisterB()
flag_register = FlagRegister()
alu = ALU()
# clock.start(2)
#### EMULACJA SZYNY BDZIE SIE MUSIALA ODBYWAC PRZEZ ODPALANIE TYLKO AKTUALNIE POTRZEBNYCH MODUŁÓW.