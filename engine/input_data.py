from sys import argv, exit
from getopt import getopt

def get_input_data():
    options = getopt(argv, "", )
    try:
        filename = options[1][1]
    except IndexError:
        exit("Usage: python3 <filename>")

    filename = options[1][1]
    data = [0b00000000 for x in range(16)]
    with open(filename, "r") as f:
         for idx, x in enumerate(f.readlines()):
             x = x.rstrip()
             x = x.lstrip("0b")
             x = int(x, base=2)
             data[idx] = x
    return data




















