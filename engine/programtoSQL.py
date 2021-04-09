filename = "divisor"
filename_to_write = filename + "_SQL"

with open(filename, "r") as f:
    insides = f.readlines()

result = ""
for line in insides:
    result += line.rstrip() + ","

with open(filename_to_write, "w") as f:
    f.write(result)