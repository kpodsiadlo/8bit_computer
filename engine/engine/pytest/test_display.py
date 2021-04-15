from ..computer_components.components import Display, Bus
import pytest

@pytest.mark.parametrize("logic_display_in", [1, pytest.param(0, marks=pytest.mark.xfail)])
def test_display_listens_to__logic_display_in__state(logic_display_in):
    display = Display()
    display.state = 234
    clock_state = 1
    logic_display_in = 1
    bus = Bus()
    bus.state = 123

    display.get_data_from_bus(clock_state, logic_display_in, bus)
    assert(display.state == 123)
