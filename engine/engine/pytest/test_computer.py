import copy

from ..computer import Computer

def test_computer_initiates_at_all():
    computer = Computer()
    assert(computer)

# def test_computer_resets_logic_correctly():
#     new_computer = Computer()
#     test_computer = copy.copy(new_computer)
#     new_computer.execute_one_click_and_yield_computer_state()
#     new_computer.execute_one_click_and_yield_computer_state()
#     new_computer.reset_computer_except_ram()
#     new_computer_vars = [vars(i) for i in vars(new_computer)]
#     test_computer_vars = [vars(i) for i in vars(test_computer)]
#
#     assert(new_computer_vars == test_computer_vars)

