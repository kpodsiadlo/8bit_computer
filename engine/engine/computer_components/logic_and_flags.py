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


