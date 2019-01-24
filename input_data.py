data = [0b00000000 for x in range(16)]
data[0] =  0b00011111    # lda %15
data[1] =  0b00101110    # add %14
data[2] =  0b01111100    # jc %11
data[3] =  0b11100000    # out
data[4] =  0b01001110    # sta %14
data[5] =  0b00101111    # add %15
data[6] =  0b01111100    # jc %11
data[7] =  0b11100000    # out
data[10] = 0b01001111    # sta %15
data[11] = 0b01100001    # jmp %1
data[12] = 0b11110000    # hlt
data[13] = 0             #
data[14] = 0b00000001    # data (=1)
data[15] = 0b00000001    # data (=1)

# 0  # 0000 - nop
# 1  # 0001 - lda
# 2  # 0010 - add
# 3  # 0011 - sub
# 4  # 0100 - sta
# 5  # 0101 - ldi
# 6  # 0110 - jmp
# 7  # 0111 - jc
# 8  # 1000 - jz
#
# 14 # 1110 - out
# 15 # 1111 - hlt