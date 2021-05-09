from engine.computer_components.components import RAM, Display, MAR, ProgramCounter, Clock, Bus, \
    InstructionRegister, InstructionCounter
from engine.computer_components.alu import ALU
from engine.computer_components.decoder import Decoder
from engine.utilities.input_data import get_input_data

class Computer:

    def __init__(self):
        self.reset_computer_except_ram()
        self.ram = RAM()

    def reset_computer_except_ram(self):
        self.bus = Bus()
        self.clock = Clock()
        self.instruction_counter = InstructionCounter()
        self.decoder = Decoder()
        self.program_counter = ProgramCounter()
        self.mar = MAR()
        self.alu = ALU()
        self.instruction_register = InstructionRegister()
        self.display = Display()

    def emulate_components(self):
        self.bus.reset()

        # increase:
        self.instruction_counter.increase(self.clock.state)
        self.decoder.set_instruction_register(self.instruction_counter.state, self.clock, self.alu.flag_register,
                                              self.instruction_register)
        self.program_counter.increase(self.clock.state, self.decoder.logic.PC_enable)

        # outs
        self.program_counter.output_to_bus(self.decoder.logic.PC_OUT, self.bus)
        self.alu.register_a.output_to_bus(self.clock.state, self.decoder.logic.AregisterOUT, self.bus)
        self.alu.output_to_bus(self.decoder.logic.ALU_OUT, self.decoder.logic.ALU_Substract, self.decoder.logic.flag_IN,
                        self.bus)
        self.instruction_register.output_to_bus(self.clock.state, self.decoder.logic.IregisterOUT, self.bus)
        self.ram.output_to_bus(self.clock.state, self.decoder.logic.RAM_OUT, self.mar.state, self.bus)

        # ins
        self.program_counter.get_data_from_bus(self.decoder.logic.PC_Jump, self.bus)
        self.alu.register_a.get_data_from_bus(self.clock.state, self.decoder.logic.AregisterIN, self.bus)
        self.alu.register_b.get_data_from_bus(self.clock.state, self.decoder.logic.BregisterIN, self.bus)
        self.instruction_register.get_data_from_bus(self.clock.state, self.decoder.logic.IregisterIN, self.bus)
        self.mar.get_data_from_bus(self.clock.state, self.decoder.logic, self.bus)
        self.ram.get_data_from_bus(self.clock.state, self.decoder.logic.RAM_IN, self.mar.state, self.bus)
        self.display.get_data_from_bus(self.clock.state, self.decoder.logic.display_IN, self.bus)

    def toggle_clock_and_yield_computer_state(self):
        self.clock.toggle(self)
        yield self.get_state()

    def yield_computer_state(self):
        yield self.get_state()

    def get_state(self):
        state = {
            # Names use Java convention for interoperability
            "type": "displayUpdate",
            "clockRunning": self.clock.clock_running,
            "memoryAddress": self.mar.state,
            "memoryContents": self.ram.state,
            "instructionRegisterHigherBits": self.instruction_register.higher_bits,
            "instructionRegisterLowerBits": self.instruction_register.lower_bits,
            "microinstructionCounter": self.instruction_counter.state,
            "programCounter": self.program_counter.state,
            "registerA": self.alu.register_a.state,
            "alu": self.alu.state,
            "registerB": self.alu.register_b.state,
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
        flags = {'carry': self.alu.flag_register.carry, "zero": self.alu.flag_register.zero}
        return flags

    def load_program(self, program_name):
        self.ram.state = get_input_data(program_name)
