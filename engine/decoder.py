class Decoder:
    def __init__(self, computer):
        self.computer = computer

    def do(self):
        # if clock.state == 1:
        #     print("T: " + ic.state.__str__())

        self.computer.logic.reset()
        if self.computer.ic.state == 0:  # Microinstruction 0 (Fetch)
            self.computer.logic.PC_OUT = 1
            self.computer.logic.MAR_IN = 1
        elif self.computer.ic.state == 1:  # Microinstruction 1 (Fetch)
            self.computer.logic.RAM_OUT = 1
            self.computer.logic.IregisterIN = 1
            self.computer.logic.PC_enable = 1
        elif self.computer.ic.state > 1:
            self.computer.decoder.execute()

    def execute(self):

        if self.computer.instruction_register.higher_bits == 0:
            print("NOP")
            pass

        elif self.computer.instruction_register.higher_bits == 1:  ## lda  0001mmmm
            if self.computer.ic.state == 2:
                print("LDA")
                self.computer.logic.IregisterOUT = 1
                self.computer.logic.MAR_IN = 1
            elif self.computer.ic.state == 3:
                self.computer.logic.RAM_OUT = 1
                self.computer.logic.AregisterIN = 1

        elif self.computer.instruction_register.higher_bits == 2:  # add    0010 mmmm
            if self.computer.ic.state == 2:
                print("ADD")
                self.computer.logic.IregisterOUT = 1
                self.computer.logic.MAR_IN = 1
            elif self.computer.ic.state == 3:
                self.computer.logic.RAM_OUT = 1
                self.computer.logic.BregisterIN = 1
            elif self.computer.ic.state == 4:
                self.computer.logic.ALU_OUT = 1
                self.computer.logic.AregisterIN = 1
                self.computer.logic.flag_IN = 1

        elif self.computer.instruction_register.higher_bits == 3:  # subtract  0011
            if self.computer.ic.state == 2:
                self.computer.logic.IregisterOUT = 1
                self.computer.logic.MAR_IN = 1
            elif self.computer.ic.state == 3:
                self.computer.logic.RAM_OUT = 1
                self.computer.logic.BregisterIN = 1
            if self.computer.ic.state == 4:
                self.computer.logic.ALU_Substract = 1
                self.computer.logic.ALU_OUT = 1
                self.computer.logic.AregisterIN = 1
                self.computer.logic.flag_IN = 1


        elif self.computer.instruction_register.higher_bits == 4:  # sta 0100
            if self.computer.ic.state == 2:
                print("STA")
                self.computer.logic.IregisterOUT = 1
                self.computer.logic.MAR_IN = 1
            elif self.computer.ic.state == 3:
                self.computer.logic.AregisterOUT = 1
                self.computer.logic.RAM_IN = 1

        elif self.computer.instruction_register.higher_bits == 5:  # ldi 0101
            if self.computer.ic.state == 2:
                print("LDI")
                self.computer.logic.IregisterOUT = 1
                self.computer.logic.AregisterIN = 1

        elif self.computer.instruction_register.higher_bits == 6:  # jmp 0110
            if self.computer.ic.state == 2:
                print("JMP")
                self.computer.logic.IregisterOUT = 1
                self.computer.logic.PC_Jump = 1

        elif self.computer.instruction_register.higher_bits == 7:  # jc 0111
            if self.computer.ic.state == 2:
                print("JC")
                if self.computer.flag_register.carry == 1:
                    self.computer.logic.IregisterOUT = 1
                    self.computer.logic.PC_Jump = 1

        elif self.computer.instruction_register.higher_bits == 8:  # jz 1000
            if self.computer.ic.state == 2:
                if self.computer.flag_register.zero == 1:
                    print("JZ")
                    self.computer.logic.IregisterOUT = 1
                    self.computer.logic.PC_Jump = 1

        #########

        elif self.computer.instruction_register.higher_bits == 14:  # display 1110
            if self.computer.ic.state == 2:
                print("OUT")
                self.computer.logic.AregisterOUT = 1
                self.computer.logic.display_IN = 1

        elif self.computer.instruction_register.higher_bits == 15:  # halt 1111
            if self.computer.ic.state == 2:
                print("HLT")
                self.computer.clock.stop()