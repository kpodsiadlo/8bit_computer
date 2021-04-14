class FlagRegister():
    def __init__(self, computer):
        self.carry = 0
        self.zero = 0
        self.computer = computer

    def do_in(self):
        if self.computer.logic.flag_IN == 1:
            self.carry = self.computer.alu.carry
            if self.carry == 1:
                #                print("Carry Flag")
                pass
            self.zero = self.computer.alu.zero
            if self.zero == 1:
                #                print("Zero Flag")
                pass


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