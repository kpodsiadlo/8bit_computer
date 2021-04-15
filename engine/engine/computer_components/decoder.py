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


class Decoder:

    def __init__(self):
        self.logic = Logic()

    def set_instruction_register(self, ic_state, clock_object, flag_register_object, instruction_register_object):
        # if clock.state == 1:
        #     print("T: " + ic.state.__str__())

        self.logic.reset()
        if ic_state == 0:  # Microinstruction 0 (Fetch)
            self.logic.PC_OUT = 1
            self.logic.MAR_IN = 1
        elif ic_state == 1:  # Microinstruction 1 (Fetch)
            self.logic.RAM_OUT = 1
            self.logic.IregisterIN = 1
            self.logic.PC_enable = 1
        elif ic_state > 1:
            self.execute(ic_state, instruction_register_object, flag_register_object, clock_object)

    def execute(self, ic_state, instruction_register_object, flag_register_object, clock_object):

        if instruction_register_object.higher_bits == 0:
            print("NOP")
            pass

        elif instruction_register_object.higher_bits == 1:  ## lda  0001
            if ic_state == 2:
                print("LDA")
                self.logic.IregisterOUT = 1
                self.logic.MAR_IN = 1
            elif ic_state == 3:
                self.logic.RAM_OUT = 1
                self.logic.AregisterIN = 1

        elif instruction_register_object.higher_bits == 2:  # add    0010
            if ic_state == 2:
                print("ADD")
                self.logic.IregisterOUT = 1
                self.logic.MAR_IN = 1
            elif ic_state == 3:
                self.logic.RAM_OUT = 1
                self.logic.BregisterIN = 1
            elif ic_state == 4:
                self.logic.ALU_OUT = 1
                self.logic.AregisterIN = 1
                self.logic.flag_IN = 1

        elif instruction_register_object.higher_bits == 3:  # subtract  0011
            if ic_state == 2:
                self.logic.IregisterOUT = 1
                self.logic.MAR_IN = 1
            elif ic_state == 3:
                self.logic.RAM_OUT = 1
                self.logic.BregisterIN = 1
            if ic_state == 4:
                self.logic.ALU_Substract = 1
                self.logic.ALU_OUT = 1
                self.logic.AregisterIN = 1
                self.logic.flag_IN = 1


        elif instruction_register_object.higher_bits == 4:  # sta 0100
            if ic_state == 2:
                print("STA")
                self.logic.IregisterOUT = 1
                self.logic.MAR_IN = 1
            elif ic_state == 3:
                self.logic.AregisterOUT = 1
                self.logic.RAM_IN = 1

        elif instruction_register_object.higher_bits == 5:  # ldi 0101
            if ic_state == 2:
                print("LDI")
                self.logic.IregisterOUT = 1
                self.logic.AregisterIN = 1

        elif instruction_register_object.higher_bits == 6:  # jmp 0110
            if ic_state == 2:
                print("JMP")
                self.logic.IregisterOUT = 1
                self.logic.PC_Jump = 1

        elif instruction_register_object.higher_bits == 7:  # jc 0111
            if ic_state == 2:
                print("JC")
                if flag_register_object.carry == 1:
                    self.logic.IregisterOUT = 1
                    self.logic.PC_Jump = 1

        elif instruction_register_object.higher_bits == 8:  # jz 1000
            if ic_state == 2:
                if flag_register_object.zero == 1:
                    print("JZ")
                    self.logic.IregisterOUT = 1
                    self.logic.PC_Jump = 1

        #########

        elif instruction_register_object.higher_bits == 14:  # display 1110
            if ic_state == 2:
                print("OUT")
                self.logic.AregisterOUT = 1
                self.logic.display_IN = 1

        elif instruction_register_object.higher_bits == 15:  # halt 1111
            if ic_state == 2:
                print("HLT")
                clock_object.stop()
