from engine.computer_components.components import RAM, Display, MAR, ALU, ProgramCounter, Clock, Bus, \
    RegisterA, RegisterB, InstructionRegister, InstructionCounter, FlagRegister
from engine.computer_components.decoder import Decoder
from engine.utilities.input_data import get_input_data

#filename = 'fibonacci'

class Computer:

    def __init__(self):
        self.reset_computer_except_ram()
        self.ram = RAM()

    def reset_computer_except_ram(self):
        self.bus = Bus()
        self.clock = Clock()
        self.ic = InstructionCounter()
        self.decoder = Decoder()

        self.pc = ProgramCounter()
        self.regA = RegisterA()
        self.regB = RegisterB()
        self.mar = MAR()
        self.alu = ALU(self.regA, self.regB)
        self.instruction_register = InstructionRegister()
        self.display = Display()
        self.flag_register = FlagRegister()

    def execute_one_click_and_yield_computer_state(self):
        self.clock.tick(self)
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
        logic = self.decoder.logic.__dict__
        return logic

    def get_flags(self):
        flags = {'carry': self.flag_register.carry, "zero": self.flag_register.zero}
        return flags

    def do(self):
        self.bus.reset()

        # increase:
        self.ic.increase(self.clock.state)

        self.decoder.do(self.ic.state, self.clock, self.instruction_register, self.flag_register)
        self.pc.increase(self.clock.state, self.decoder.logic.PC_enable)

        # outs
        self.pc.do_out(self.decoder.logic.PC_OUT, self.bus)
        self.regA.do_out(self.clock.state, self.decoder.logic.AregisterOUT, self.bus)
        self.regB.do_out(self.clock.state, self.decoder.logic.BregisterOUT, self.bus)
        self.alu.do_out(self.regA.state, self.regB.state, self.decoder.logic.ALU_OUT, self.decoder.logic.ALU_Substract, self.bus)
        self.flag_register.do_in(self.decoder.logic.flag_IN, self.alu.carry, self.alu.zero)
        self.instruction_register.do_out(self.clock.state, self.decoder.logic.IregisterOUT, self.bus)
        self.ram.do_out(self.clock.state, self.decoder.logic.RAM_OUT, self.mar.state, self.bus)

        # ins
        self.pc.do_in(self.decoder.logic.PC_Jump, self.bus)
        self.regA.do_in(self.clock.state, self.decoder.logic.AregisterIN, self.bus)
        self.regB.do_in(self.clock.state, self.decoder.logic.BregisterIN, self.bus)
        self.instruction_register.do_in(self.clock.state, self.decoder.logic.IregisterIN, self.bus)
        self.mar.do_in(self.clock, self.decoder.logic, self.bus)
        self.ram.do_in(self.clock.state, self.decoder.logic.RAM_IN, self.mar.state, self.bus)
        self.display.do_in(self.clock.state, self.decoder.logic.display_IN, self.bus)

    def load_program(self, program_name):
        self.ram.state = get_input_data(program_name)


