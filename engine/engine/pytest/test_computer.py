import copy

from ..computer import Computer

def test_computer_initiates_at_all():
    computer = Computer()
    assert(computer)

def test_computer_runs_at_all():
    computer = Computer()
    for i in range(200):
        for i in computer.toggle_clock_and_yield_computer_state():
            computer_state = i
    assert(computer_state)

def test_computer_executes_fibonacci_correctly():
    cycles = 554
    computer = Computer()
    computer.load_program("engine/programs/fibonacci")
    for i in range(cycles):
        for j in computer.toggle_clock_and_yield_computer_state():
            pass
    assert(computer.display.state == 233)

