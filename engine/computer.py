from components import RAM, Display, MAR, ALU, ProgramCounter, Clock, Bus, \
    RegisterA, RegisterB, InstructionRegister, InstructionCounter
from logic_and_flags import FlagRegister, Logic
from decoder import Decoder

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
        self.decoder = Decoder(computer=self)
        self.buss = Bus(computer=self)
        self.regA = RegisterA(computer=self)
        self.regB = RegisterB(computer=self)
        self.alu = ALU(self.regA, self.regB, computer=self)
        self.instruction_register = InstructionRegister(computer=self)
        self.mar = MAR(computer=self)
        self.display = Display(computer=self)

    def execute_one_click_and_yield_computer_state(self):
        self.clock.tick()
        yield self.get_state()

    def yield_computer_state(self):
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

    def do(self):
        self.buss.reset(computer=self)

        # increase:
        self.ic.increase()

        self.decoder.do()
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


