import time
import input_data


class Display:
    def __init__(self):
        self.state = 0

    def do_in(self):
        if clock.state == 1:
            if logic.display_IN == 1:
                self.state = buss.state
                print("\n#############")
                print("Display: " + self.state.__str__())
                print("#############")


class MAR:
    def __init__(self):
        self.state = 0

    def do_in(self):
        if clock.state == 1:
            if logic.MAR_IN == 1:
                self.state = buss.state
#                print("MAR: " + self.state.__str__())


class RAM:
    def __init__(self):
        self.state = [x for x in input_data.data]

    def do_in(self):
        if clock.state == 1:
            if logic.RAM_IN == 1:
                self.state[mar.state] = buss.state

    def do_out(self):
        if clock.state == 1:
            if logic.RAM_OUT == 1:
                buss.state = self.state[mar.state]


class ALU:
    def __init__(self):
        # self.regA = regA.state
        # self.regB = regB.state
        self.state = regA.state + regB.state
        self.calculate()
        self.zero = 0
        self.carry = 0

    def do_out(self):
        # self.regA = regA.state
        # self.regB = regB.state
        self.state = regA.state + regB.state
        self.calculate()
        if logic.ALU_OUT == 1:
            buss.state = self.state

    def calculate(self):
        self.zero = 0
        self.carry = 0
        if logic.ALU_Substract == 1:
            self.state = regA.state - regB.state

        if self.state > 255:
            self.state -= 256
            self.carry = 1
#            print("ALU Carry")
        if self.state == 0:
            self.zero = 1
#            print("ALU ZERO")
        if self.state < 0:
            self.state +=256
            self.carry = 1



class ProgramCounter:
    def __init__(self):
        self.state = 0

    def increase(self):
        if clock.state == 1:
            if logic.PC_enable == 1:
                self.state += 1
#               print("PC: " + (self.state-1).__str__())
                print(".", end="", flush=True)

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
        self.flag_IN = 0

    def reset(self):
        self.__init__()


class Clock:  # start(time interval in seconds) .stop
    def __init__(self):
        self.state = 0
        self.timer = 0

    def tick(self):
        self.state = not self.state
        if self.state == 1:                         ### only true tick
        ##    print("Tick: " + self.state.__str__())
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
#               print("RegA: " + self.state.__str__())

    def do_out(self):
        if clock.state == 1:
            if logic.AregisterOUT == 1:
                buss.state = self.state
#               print("RegA: " + self.state.__str__())


class RegisterB:
    def __init__(self):
        self.state = 0b00000000

    def do_in(self):
        if clock.state == 1:
            if logic.BregisterIN == 1:
                self.state = buss.state
#               print("RegB: " + self.state.__str__())

    def do_out(self):
        if clock.state == 1:
            if logic.BregisterOUT == 1:
                buss.state = self.state
#               print("RegB: " + self.state.__str__())


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
              #  print("Instruction register: " + self.state.__str__())

    def do_out(self):
        if clock.state == 1:
            if logic.IregisterOUT == 1:
                buss.state = self.lower_bits
              #  print("Instruction register:: " + self.state.__str__())

    def split(self):
        self.higher_bits = self.state >> 4
#       print("Instruction: " + self.higher_bits.__str__())
        self.lower_bits = self.state & 0b00001111
#       print("Data         " + self.lower_bits.__str__())


class FlagRegister:
    def __init__(self):
        self.carry = 0
        self.zero = 0

    def do_in(self):
        if logic.flag_IN == 1:
            self.carry = alu.carry
            if self.carry == 1:
#                print("Carry Flag")
                pass
            self.zero = alu.zero
            if self.zero == 1:
#                print("Zero Flag")
                pass

class InstructionCounter:
    def __init__(self):
        self.state = 0

    def increase(self):
        if clock.state == 1:
            self.state += 1
            if self.state > 4:
                self.state = 0


class Decoder:
    def __init__(self):
        self.do()

    @staticmethod
    def do():
        # if clock.state == 1:
        #     print("T: " + ic.state.__str__())

        logic.reset()
        if ic.state == 0:  # Microinstruction 0 (Fetch)
            logic.PC_OUT = 1
            logic.MAR_IN = 1
        elif ic.state == 1:  # Microinstruction 1 (Fetch)
            logic.RAM_OUT = 1
            logic.IregisterIN = 1
            logic.PC_enable = 1
        elif ic.state > 1:
            decoder.execute()

    @staticmethod
    def execute():

        if instruction_register.higher_bits == 0:
            pass

        elif instruction_register.higher_bits == 1:  ## lda  0001mmmm
            if ic.state == 2:
                # print("LDA")
                logic.IregisterOUT = 1
                logic.MAR_IN = 1
            elif ic.state == 3:
                logic.RAM_OUT = 1
                logic.AregisterIN = 1

        elif instruction_register.higher_bits == 2:  # add    0010 mmmm
            if ic.state == 2:
#                 # print("ADD")
                logic.IregisterOUT = 1
                logic.MAR_IN = 1
            elif ic.state == 3:
                logic.RAM_OUT = 1
                logic.BregisterIN = 1
            elif ic.state == 4:
                logic.ALU_OUT = 1
                logic.AregisterIN = 1
                logic.flag_IN = 1

        elif instruction_register.higher_bits == 3:  # subtract
            if ic.state == 2:
                logic.IregisterOUT = 1
                logic.MAR_IN = 1
            elif ic.state == 3:
                logic.RAM_OUT = 1
                logic.BregisterIN = 1
            if ic.state == 4:
                logic.ALU_Substract = 1
                logic.ALU_OUT = 1
                logic.AregisterIN = 1
                logic.flag_IN = 1


        elif instruction_register.higher_bits == 4:  # sta
            if ic.state == 2:
                # print("STA")
                logic.IregisterOUT = 1
                logic.MAR_IN = 1
            elif ic.state == 3:
                logic.AregisterOUT = 1
                logic.RAM_IN = 1

        elif instruction_register.higher_bits == 5:  # ldi
            if ic.state == 2:
                # print("LDI")
                logic.IregisterOUT = 1
                logic.AregisterIN = 1

        elif instruction_register.higher_bits == 6:  # jmp
            if ic.state == 2:
                # print("JMP")
                logic.IregisterOUT = 1
                logic.PC_Jump = 1

        elif instruction_register.higher_bits == 7:  # jc
            if ic.state == 2:
                # print("JC")
                if flag_register.carry == 1:
                    logic.IregisterOUT = 1
                    logic.PC_Jump = 1

        elif instruction_register.higher_bits == 8:  # jz
            if ic.state == 2:
                if flag_register.zero == 1:
                    # print("JZ")
                    logic.IregisterOUT = 1
                    logic.PC_Jump = 1

        #########

        elif instruction_register.higher_bits == 14:  # display
            if ic.state == 2:
                # print("OUT")
                logic.AregisterOUT = 1
                logic.display_IN = 1

        elif instruction_register.higher_bits == 15:  # halt
            if ic.state == 2:
                # print("HLT")
                clock.stop()


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
    flag_register.do_in()
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



clock = Clock()
pc = ProgramCounter()
ic = InstructionCounter()
flag_register = FlagRegister()
logic = Logic()
decoder = Decoder()
buss = Buss()
regA = RegisterA()
regB = RegisterB()
alu = ALU()
instruction_register = InstructionRegister()
mar = MAR()
ram = RAM()
display = Display()

clock.start(0.0001)


