import input_data

filename = 'fibonacci'

class Computer:

    def execute_one_click_and_yield_computer_state(self, computer):
        computer.clock.tick(computer)
        yield computer.get_state(computer)

    @staticmethod
    def get_state(computer):
        state = {
            "programCounter": computer.pc.state,
            "registerA": computer.regA.state}
        return state

    def __init__(self):
        self.clock = self.Clock()
        self.pc = self.ProgramCounter()
        self.ic = self.InstructionCounter()
        self.flag_register = self.FlagRegister()
        self.logic = self.Logic()
        self.decoder = self.Decoder()
        self.buss = self.Buss()
        self.regA = self.RegisterA()
        self.regB = self.RegisterB()
        self.alu = self.ALU(self.regA, self.regB)
        self.instruction_register = self.InstructionRegister()
        self.mar = self.MAR()
        self.ram = self.RAM()
        self.display = self.Display()

    def do(self, computer):
        self.buss.do(computer)
        self.decoder.do(computer)

        # outs

        self.pc.do_out(computer)
        self.regA.do_out(computer)
        self.regB.do_out(computer)
        self.alu.do_out(computer)
        self.instruction_register.do_out(computer)
        self.ram.do_out(computer)

        # ins
        self.flag_register.do_in(computer)
        self.pc.do_in(computer)
        self.regA.do_in(computer)
        self.regB.do_in(computer)
        self.instruction_register.do_in(computer)
        self.mar.do_in(computer)
        self.ram.do_in(computer)
        self.display.do_in(computer)

        # increase:

        self.pc.increase(computer)
        self.ic.increase(computer)



    class Display:
        def __init__(self):
            self.state = 0

        def do_in(self, computer):
            if computer.clock.state == 1:
                if computer.logic.display_IN == 1:
                    self.state = computer.buss.state
                    print("\n#############")
                    print("Display: " + self.state.__str__())
                    print("#############")

    class MAR:
        def __init__(self):
            self.state = 0

        def do_in(self, computer):
            if computer.clock.state == 1:
                if computer.logic.MAR_IN == 1:
                    self.state = computer.buss.state

    #                print("MAR: " + self.state.__str__())

    class RAM:
        def __init__(self):
                self.state = input_data.get_input_data(filename)

        def do_in(self, computer):
            if computer.clock.state == 1:
                if computer.logic.RAM_IN == 1:
                    self.state[computer.mar.state] = computer.buss.state

        def do_out(self, computer):
            if computer.clock.state == 1:
                if computer.logic.RAM_OUT == 1:
                    computer.buss.state = self.state[computer.mar.state]

    class ALU:
        def __init__(self, regA, regB):
            # self.regA = regA.state
            # self.regB = regB.state
            self.state = regA.state + regB.state
            # self.calculate()
            self.zero = 0
            self.carry = 0

        def do_out(self, computer):
            # self.regA = regA.state
            # self.regB = regB.state
            self.state = computer.regA.state + computer.regB.state
            self.calculate(computer)
            if computer.logic.ALU_OUT == 1:
                computer.buss.state = self.state

        def calculate(self, computer):
            self.zero = 0
            self.carry = 0
            if computer.logic.ALU_Substract == 1:
                self.state = computer.regA.state - computer.regB.state

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

    class ProgramCounter:
        def __init__(self):
            self.state = 0

        def increase(self, computer):
            if computer.clock.state == 1:
                if computer.logic.PC_enable == 1:
                    self.state += 1
                    #               print("PC: " + (self.state-1).__str__())
                    print(".", end="", flush=True)
                if self.state > 15:
                    self.state = 0

        def do_in(self, computer):
            if computer.logic.PC_Jump == 1:
                self.state = computer.buss.state

        def do_out(self, computer):
            if computer.logic.PC_OUT == 1:
                computer.buss.state = self.state

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

    class Clock:
        def __init__(self):
            self.state = 0
            self.clock_running = 0

        def tick(self, computer):
            computer.clock.state = not computer.clock.state
            if computer.clock.state == 1:  ### only true tick
                # print("Tick: " + computer.clock.state.__str__())
                computer.do(computer)

        def start(self, computer):
            computer.clock.clock_running = 1

        def stop(self, computer):
            computer.clock.clock_running = 0

    class Buss:
        def __init__(self):
            self.state = None

        def do(self, computer):
            computer.buss.state = None

    class RegisterA:
        def __init__(self):
            self.state = 0

        def do_in(self, computer):
            if computer.clock.state == 1:
                if computer.logic.AregisterIN == 1:
                    self.state = computer.buss.state

                    # print("RegA: " + self.state.__str__())

        def do_out(self, computer):
            if computer.clock.state == 1:
                if computer.logic.AregisterOUT == 1:
                    computer.buss.state = self.state

                    # print("RegA: " + self.state.__str__())

    class RegisterB:
        def __init__(self):
            self.state = 0b00000000

        def do_in(self, computer):
            if computer.clock.state == 1:
                if computer.logic.BregisterIN == 1:
                    self.state = computer.buss.state

                    # print("RegB: " + self.state.__str__())

        def do_out(self, computer):
            if computer.clock.state == 1:
                if computer.logic.BregisterOUT == 1:
                    computer.buss.state = self.state

                    # print("RegB: " + self.state.__str__())

    class InstructionRegister:
        def __init__(self):
            self.state = 0b00000000
            self.higher_bits = 0
            self.lower_bits = 0

        def do_in(self, computer):
            if computer.clock.state == 1:
                if computer.logic.IregisterIN == 1:
                    self.state = computer.buss.state
                    self.split()
                    # print("Instruction register: " + self.state.__str__())

        def do_out(self, computer):
            if computer.clock.state == 1:
                if computer.logic.IregisterOUT == 1:
                    computer.buss.state = self.lower_bits
                    # print("Instruction register:: " + self.state.__str__())

        def split(self):
            self.higher_bits = self.state >> 4
            #       print("Instruction: " + self.higher_bits.__str__())
            self.lower_bits = self.state & 0b00001111

            # print("Data         " + self.lower_bits.__str__())

    class FlagRegister:
        def __init__(self):
            self.carry = 0
            self.zero = 0

        def do_in(self, computer):
            if computer.logic.flag_IN == 1:
                self.carry = computer.alu.carry
                if self.carry == 1:
                    #                print("Carry Flag")
                    pass
                self.zero = computer.alu.zero
                if self.zero == 1:
                    #                print("Zero Flag")
                    pass

    class InstructionCounter:
        def __init__(self):
            self.state = 0

        def increase(self, computer):
            if computer.clock.state == 1:
                self.state += 1
                if self.state > 4:
                    self.state = 0

                # print("Instruction counter:" + self.state.__str__())

    class Decoder:
        def __init__(self):
            pass

        @staticmethod
        def do(computer):
            # if clock.state == 1:
            #     print("T: " + ic.state.__str__())

            computer.logic.reset()
            if computer.ic.state == 0:  # Microinstruction 0 (Fetch)
                computer.logic.PC_OUT = 1
                computer.logic.MAR_IN = 1
            elif computer.ic.state == 1:  # Microinstruction 1 (Fetch)
                computer.logic.RAM_OUT = 1
                computer.logic.IregisterIN = 1
                computer.logic.PC_enable = 1
            elif computer.ic.state > 1:
                computer.decoder.execute(computer)

        @staticmethod
        def execute(computer):

            if computer.instruction_register.higher_bits == 0:
                print("NOP")
                pass

            elif computer.instruction_register.higher_bits == 1:  ## lda  0001mmmm
                if computer.ic.state == 2:
                    print("LDA")
                    computer.logic.IregisterOUT = 1
                    computer.logic.MAR_IN = 1
                elif computer.ic.state == 3:
                    computer.logic.RAM_OUT = 1
                    computer.logic.AregisterIN = 1

            elif computer.instruction_register.higher_bits == 2:  # add    0010 mmmm
                if computer.ic.state == 2:
                    print("ADD")
                    computer.logic.IregisterOUT = 1
                    computer.logic.MAR_IN = 1
                elif computer.ic.state == 3:
                    computer.logic.RAM_OUT = 1
                    computer.logic.BregisterIN = 1
                elif computer.ic.state == 4:
                    computer.logic.ALU_OUT = 1
                    computer.logic.AregisterIN = 1
                    computer.logic.flag_IN = 1

            elif computer.instruction_register.higher_bits == 3:  # subtract  0011
                if computer.ic.state == 2:
                    computer.logic.IregisterOUT = 1
                    computer.logic.MAR_IN = 1
                elif computer.ic.state == 3:
                    computer.logic.RAM_OUT = 1
                    computer.logic.BregisterIN = 1
                if computer.ic.state == 4:
                    computer.logic.ALU_Substract = 1
                    computer.logic.ALU_OUT = 1
                    computer.logic.AregisterIN = 1
                    computer.logic.flag_IN = 1


            elif computer.instruction_register.higher_bits == 4:  # sta 0100
                if computer.ic.state == 2:
                    print("STA")
                    computer.logic.IregisterOUT = 1
                    computer.logic.MAR_IN = 1
                elif computer.ic.state == 3:
                    computer.logic.AregisterOUT = 1
                    computer.logic.RAM_IN = 1

            elif computer.instruction_register.higher_bits == 5:  # ldi 0101
                if computer.ic.state == 2:
                    print("LDI")
                    computer.logic.IregisterOUT = 1
                    computer.logic.AregisterIN = 1

            elif computer.instruction_register.higher_bits == 6:  # jmp 0110
                if computer.ic.state == 2:
                    print("JMP")
                    computer.logic.IregisterOUT = 1
                    computer.logic.PC_Jump = 1

            elif computer.instruction_register.higher_bits == 7:  # jc 0111
                if computer.ic.state == 2:
                    print("JC")
                    if computer.flag_register.carry == 1:
                        computer.logic.IregisterOUT = 1
                        computer.logic.PC_Jump = 1

            elif computer.instruction_register.higher_bits == 8:  # jz 1000
                if computer.ic.state == 2:
                    if computer.flag_register.zero == 1:
                        print("JZ")
                        computer.logic.IregisterOUT = 1
                        computer.logic.PC_Jump = 1

            #########

            elif computer.instruction_register.higher_bits == 14:  # display 1110
                if computer.ic.state == 2:
                    print("OUT")
                    computer.logic.AregisterOUT = 1
                    computer.logic.display_IN = 1

            elif computer.instruction_register.higher_bits == 15:  # halt 1111
                if computer.ic.state == 2:
                    print("HLT")
                    computer.clock.stop(computer)

