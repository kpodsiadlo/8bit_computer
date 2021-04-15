from engine.computer_components.components import RAM, Display, MAR, ALU, ProgramCounter, Clock, Bus, \
    RegisterA, RegisterB, InstructionRegister, InstructionCounter
from engine.computer_components.logic_and_flags import FlagRegister, Logic
from engine.computer_components.decoder import Decoder

filename = 'fibonacci'

class Computer:

    def __init__(self):
        self.reset_computer_except_ram()
        self.ram = RAM()

    def reset_computer_except_ram(self):
        self.clock = Clock(computer=self)
        self.pc = ProgramCounter()
        self.ic = InstructionCounter(computer=self)
        self.flag_register = FlagRegister(computer=self)
        self.logic = Logic()
        self.decoder = Decoder(computer=self)
        self.bus = Bus()
        self.regA = RegisterA()
        self.regB = RegisterB()
        self.alu = ALU(self.regA, self.regB)
        self.instruction_register = InstructionRegister()
        self.mar = MAR()
        self.display = Display()

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
            "bus": self.bus.state,
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
        self.bus.reset()

        # increase:
        self.ic.increase()

        self.decoder.do()
        self.pc.increase(self.clock.state, self.logic.PC_enable)

        # outs
        self.pc.do_out(self.logic.PC_OUT, self.bus)
        self.regA.do_out(self.clock.state, self.logic.AregisterOUT, self.bus)
        self.regB.do_out(self.clock.state, self.logic.BregisterOUT, self.bus)
        self.alu.do_out(self.regA.state, self.regB.state, self.logic.ALU_OUT, self.logic.ALU_Substract, self.bus)
        self.instruction_register.do_out(self.clock.state, self.logic.IregisterOUT, self.bus)
        self.ram.do_out(self.clock.state, self.logic.RAM_OUT, self.mar.state, self.bus)

        # ins
        self.flag_register.do_in()
        self.pc.do_in(self.logic.PC_Jump, self.bus)
        self.regA.do_in(self.clock.state, self.logic.AregisterIN, self.bus)
        self.regB.do_in(self.clock.state, self.logic.BregisterIN, self.bus)
        self.instruction_register.do_in(self.clock.state, self.logic.IregisterIN, self.bus)
        self.mar.do_in(self.clock, self.logic, self.bus)
        self.ram.do_in(self.clock.state, self.logic.RAM_IN, self.mar.state, self.bus)
        self.display.do_in(self.clock.state, self.logic.display_IN, self.bus)

    def __eq__(self, other):
        if isinstance(other, Computer):
            return vars(self) == vars(other)
        return False


