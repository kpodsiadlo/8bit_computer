from components import RAM, Display, MAR, ALU, ProgramCounter, Clock, Bus, \
    RegisterA, RegisterB, InstructionRegister, InstructionCounter
from logic_and_flags import FlagRegister, Logic

filename = 'fibonacci'

class Computer:

    def __init__(self):
        self.reset_computer_except_ram()
        self.ram = RAM(computer=self)

    def reset_computer_except_ram(self):
        self.clock = Clock(computer=self)
        self.pc = ProgramCounter(computer=self)
        self.ic = InstructionCounter(computer=self)
        self.flag_register = FlagRegister(computer=self)
        self.logic = Logic()
        self.decoder = self.Decoder()
        self.buss = Bus(computer=self)
        self.regA = RegisterA(computer=self)
        self.regB = RegisterB(computer=self)
        self.alu = ALU(self.regA, self.regB, computer=self)
        self.instruction_register = InstructionRegister(computer=self)
        self.mar = MAR(computer=self)
        self.display = Display(computer=self)

    def execute_one_click_and_yield_computer_state(self, computer):
        self.clock.tick()
        yield self.get_state()

    def yield_computer_state(self, computer):
        yield self.get_state()


    def get_state(self):

        state = {
            # Names use Java convention for interoperability
            "source": "Engine",
            "type": "displayUpdate",
            "clockRunning": self.clock.clock_running,
            "memoryAddress": self.mar.state,
            "memoryContents": self.ram.state,
            "instructionRegisterHigherBits": self.instruction_register.higher_bits,
            "instructionRegisterLowerBits": self.instruction_register.lower_bits,
            "microinstructionCounter": self.ic.state,
            "programCounter": self.pc.state,
            "registerA": self.regA.state,
            "alu": self.alu.state,
            "registerB": self.regB.state,
            "output": self.display.state,
            "bus": self.buss.state,
            "logic": self.get_logic(),
            "flags": self.get_flags()
        }
        return state

    def get_logic(self):
        logic = self.logic.__dict__
        return logic

    def get_flags(self):
        flags = {'carry': self.flag_register.carry, "zero": self.flag_register.zero}
        return flags

    def do(self, computer):
        self.buss.reset(computer=self)
        # increase:

        self.ic.increase()
        self.decoder.do(computer)
        self.pc.increase()

        # outs

        self.pc.do_out()
        self.regA.do_out()
        self.regB.do_out()
        self.alu.do_out()
        self.instruction_register.do_out()
        self.ram.do_out()

        # ins
        self.flag_register.do_in()
        self.pc.do_in()
        self.regA.do_in()
        self.regB.do_in()
        self.instruction_register.do_in()
        self.mar.do_in()
        self.ram.do_in()
        self.display.do_in()

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
                    computer.clock.stop()
