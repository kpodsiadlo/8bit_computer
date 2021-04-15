import pytest

from ..computer_components.components import RAM, Bus, Clock, MAR
from ..computer_components.logic_and_flags import Logic

@pytest.fixture(autouse=True)
def ram():
    return RAM()

def test_ram_memory_initializes_as_16_field_array(ram):
    assert(len(ram.state) == 16)

def test_values_between_0_and_255_in_ram_after_initialization(ram):
    assert(all(item >= 0 and item <= 255 for item in ram.state))

def test_no_None_values_in_ram_after_initialization(ram):
    assert(all(item is not None for item in ram.state))

def test_ram_updates_its_state_when_it_should(ram):
    clock = Clock()
    logic = Logic()
    mar = MAR()
    bus = Bus()
    bus.state = 135
    logic.RAM_IN = 1
    clock.state = 1
    mar.state = 7
    ram.do_in(clock, logic, mar, bus)
    assert(ram[mar.state] == bus.state)

