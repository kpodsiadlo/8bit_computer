import sys

machine_code = {
    "nop": 0b0000,
    "lda": 0b0001,
    "add": 0b0010,
    "sub": 0b0011,
    "sta": 0b0100,
    "ldi": 0b0101,
    "jmp": 0b0110,
    "jc" : 0b0111,
    "jz" : 0b1000,
    "out": 0b1110,
    "hlt": 0b1111
}

output = []                                         # initialize output


print("Input source file name (.as8) without extension.")
print("The compiler fill create an executable .bin file for the Ben Eater's 8-bit computer")
name = input("File name:")
input_filename = (name + '.as8')
output_filename = (name)


with open(input_filename, 'r') as f:
    input_data = f.readlines()
instructions = [x.rstrip() for x in input_data]     # remove "\n"


if len(instructions) > 16:                          # check length
    sys.exit("Program too long")

for idx, x in enumerate(instructions):

    # separate instructions and values
    stripped_instructions = x.partition(',')

    # get instructions
    try:
        a = machine_code[stripped_instructions[0]]
    except KeyError:
        sys.exit("Invalid instruction: " + str([stripped_instructions[0]]) + " in line " + str(idx))
    output.append("{:04b}".format(a))

    # get values
    b = stripped_instructions[2]
    b = (b.lstrip(' %'))

    # if no value
    if b == "":
        b = 0

    # check if integer
    try:
        int(b)
    except Exception:
        sys.exit("Value '{}' in line {} is not a number".format(b, idx))

    # check in in range
    b = int(b)
    if b > 15 or b < 0:
        sys.exit("Value '{}' in line {} exceedes range [0, 15]".format(b, idx))

    # add to output
    output[idx] += ("{:04b}\n".format(b))


with open(output_filename, 'w') as f:
    f.writelines(output)
    print("Executable file created: " + output_filename)
    