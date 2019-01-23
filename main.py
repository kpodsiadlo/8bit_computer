import time
import input_data

class Display:
    def __init__(self):
        self.state = 0

    def do_in(self):
        if clock.state == 1:
            if logic.display_IN == 1:
                self.state = buss.state
                print("Display: " + self.state.__str__())


class MAR:
    def __init__(self):
        self.state = 0

    def do_in(self):
        if clock.state == 1:
            if logic.MAR_IN == 1:
                self.state = buss.state
                print("MAR: " + self.state.__str__())


class RAM:
    def __init__(self):
        self.state = [x for x in input_data.data]
        print("RAM: ")
        for idx, x in enumerate(self.state):
            print("{0:02d} {1:08b}".format(idx, x))

    def do_in(self):
        if clock.state == 1:
            if logic.RAM_OUT == 1:
                buss.state = self.state[mar.state]

    def do_out(self):
        if clock.state == 1:
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

    def do_out(self):
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

    def increase(self):
        if clock.state == 1:
            if logic.PC_enable == 1:
                self.state += 1
                print("PC: " + self.state.__str__())

    def do_in(self):
        if logic.PC_Jump == 1:
            self.state = buss.state

    def do_out(self):
        if logic.PC_OUT == 1:
            buss.state = self.state


class Logic:
    def __init__(self):
        self.AregisterIN = 0
        self.AregisterOUT = 0
        self.BregisterIN = 0
        self.BregisterOUT = 0
        self.IregisterIN = 0
        self.IregisterOUT = 0
        self.PC_enable = 0
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
        self.state = None

    def do(self):
        self.state = None


class RegisterA:
    def __init__(self):
        self.state = 0

    def do_in(self):
        if clock.state == 1:
            if logic.AregisterIN == 1:
                self.state = buss.state
                print("RegA: " + self.state.__str__())

    def do_out(self):
        if clock.state == 1:
            if logic.AregisterOUT == 1:
                buss.state = self.state
                print("RegA: " + self.state.__str__())


class RegisterB:
    def __init__(self):
        self.state = 0b00000000

    def do_in(self):
        if clock.state == 1:
            if logic.BregisterIN == 1:
                self.state = buss.state
                print("RegB: " + self.state.__str__())

    def do_out(self):
        if clock.state == 1:
            if logic.BregisterOUT == 1:
                buss.state = self.state
                print("RegB: " + self.state.__str__())


class InstructionRegister:
    def __init__(self):
        self.state = 0b00000000
        self.higher_bits = 0
        self.lower_bits = 0

    def do_in(self):
        if clock.state == 1:
            if logic.IregisterIN == 1:
                self.state = buss.state
                self.split()
                print("Instruction register: " + self.state.__str__())

    def do_out(self):
        if clock.state == 1:
            if logic.IregisterOUT == 1:
                buss.state = self.lower_bits
                print("Instruction register:: " + self.state.__str__())

    def split(self):
        self.higher_bits = self.state >> 4
        print("IR_HIGHER:" + self.higher_bits.__str__())
        self.lower_bits = self.state & 0b00001111
        print("IR_LOWER: " + self.lower_bits.__str__())


class FlagRegister:
    def __init__(self):
        self.carry = 0


class InstructionCounter:
    def __init__(self):
        self.state = 0

    def increase(self):
        if clock.state == 1:
            self.state += 1
            if self.state > 5:
                self.state = 0


class Decoder:
    def __init__(self):
        self.do()

    @staticmethod
    def do():
        print("T: " + ic.state.__str__())

        logic.reset()
        if ic.state == 0:  # Microinstruction 0 (Fetch)
            logic.PC_OUT = 1
            logic.MAR_IN = 1
        if ic.state == 1:  # Microinstruction 1 (Fetch)
            logic.RAM_OUT = 1
            logic.IregisterIN = 1
            logic.PC_enable = 1
        if ic.state > 1:
            execute()



def do():
    buss.do()
    decoder.do()

    # outs

    pc.do_out()
    regA.do_out()
    regB.do_out()
    alu.do_out()
    instruction_register.do_out()
    ram.do_out()

    # ins
    pc.do_in()
    regA.do_in()
    regB.do_in()
    instruction_register.do_in()
    mar.do_in()
    ram.do_in()
    display.do_in()

    # increase:

    pc.increase()
    ic.increase()


def bprint(output):
    print(format(output, '#010b'))


clock = Clock()
pc = ProgramCounter()
ic = InstructionCounter()
logic = Logic()
decoder = Decoder()
buss = Buss()
regA = RegisterA()
regB = RegisterB()
alu = ALU()
instruction_register = InstructionRegister()

flag_register = FlagRegister()
mar = MAR()
ram = RAM()
display = Display()

# clock.start(2)


def execute():
    if instruction_register.higher_bits == 1:  ## lda
        if ic.state == 2:
            logic.IregisterOUT = 1
            logic.MAR_IN = 1

        if ic.state == 3:
            logic.RAM_OUT = 1
            logic.AregisterIN = 1

    if instruction_register.higher_bits == 2:  ## add
        if ic.state == 2:
            logic.IregisterOUT = 1
            logic.MAR_IN = 1
        if ic.state == 3:
            logic.RAM_OUT = 1
            logic.BregisterIN = 1
        if ic.state == 4:
            logic.ALU_OUT = 1
            logic.AregisterIN = 1


    if instruction_register.higher_bits == 3: ## display
        if ic.state == 2:
            logic.AregisterOUT = 1
            logic.display_IN = 1
