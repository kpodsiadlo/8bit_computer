import time
from computer import Computer

time_interval_in_seconds = 0.001

machine = Computer()
machine.clock.start(machine)

while True:
    if machine.clock.clock_running == 1:
        time.sleep(float(time_interval_in_seconds))
        #print(next(machine.execute_one_click_and_yield_computer_state(machine)))
    else:
        break