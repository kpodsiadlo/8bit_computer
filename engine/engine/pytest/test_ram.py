import pytest

from ..computer_components.components import RAM, Bus

@pytest.fixture(autouse=True)
def ram():
    return RAM()

def test_ram_memory_initializes_as_16_field_array(ram):
    assert(len(ram.state) == 16)

def test_values_between_0_and_255_in_ram_after_initialization(ram):
    assert(all(item >= 0 and item <= 255 for item in ram.state))

def test_no_None_values_in_ram_after_initialization(ram):
    assert(all(item is not None for item in ram.state))

@pytest.mark.parametrize("mar_state", list(range(15)))
def test_ram_updates_its_state_when_Logic_RAM_IN_is_set_for_all_correct_ram_values(ram, mar_state):
    clock_state = 1
    logic_RAM_IN = 1
    bus = Bus()
    bus.state = 135
    ram.do_in(clock_state, logic_RAM_IN, mar_state, bus)
    assert(ram.state[mar_state] == bus.state)

@pytest.mark.parametrize("mar_state", list(range(15)))
def test_ram_updates_buss_state_when_Logic_RAM_OUT_is_set_for_all_correct_ram_values(ram, mar_state):
    clock_state = 1
    logic_RAM_OUT = 1
    bus = Bus()
    bus.state = 135
    ram.do_in(clock_state, logic_RAM_OUT, mar_state, bus)
    assert(ram.state[mar_state] == bus.state)

