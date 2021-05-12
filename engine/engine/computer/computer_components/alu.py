class ALU():
    def __init__(self):
        self.register_a = RegisterA()
        self.register_b = RegisterB()
        self.flag_register = FlagRegister()
        self.state = self.register_a.state + self.register_b.state
        self.zero = 0
        self.carry = 0

    def output_to_bus(self, logic_alu_out, logic_alu_subtract, logic_flag_IN, bus):
        self.state = self.register_a.state + self.register_b.state
        self.calculate(logic_alu_subtract, logic_flag_IN)
        if logic_alu_out == 1:
            bus.state = self.state

    def calculate(self, logic_alu_subtract, logic_flag_IN):
        self.zero = 0
        self.carry = 0
        if logic_alu_subtract == 1:
            self.state = self.register_a.state - self.register_b.state

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

        self.flag_register.do_in(logic_flag_IN, self.carry, self.zero)


class FlagRegister():
    def __init__(self):
        self.carry = 0
        self.zero = 0

    def do_in(self, logic_flag_IN, alu_carry, alu_zero):
        if logic_flag_IN == 1:
            self.carry = alu_carry
            if self.carry == 1:
                #                print("Carry Flag")
                pass
            self.zero = alu_zero
            if self.zero == 1:
                #                print("Zero Flag")
                pass


class RegisterA():
    def __init__(self):
        self.state = 0

    def get_data_from_bus(self, clock_state, logic_AregisterIN, bus):
        if clock_state == 1:
            if logic_AregisterIN == 1:
                self.state = bus.state

                # print("RegA: " + self.state.__str__())

    def output_to_bus(self, clock_state, logic_AregisterOUT, bus):
        if clock_state == 1:
            if logic_AregisterOUT == 1:
                bus.state = self.state

                # print("RegA: " + self.state.__str__())


class RegisterB():
    def __init__(self):
        self.state = 0

    def get_data_from_bus(self, clock_state, logic_BregisterIN, bus):
        if clock_state == 1:
            if logic_BregisterIN == 1:
                self.state = bus.state
