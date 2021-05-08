def process_incoming_message(message_json, controller):

    def start_or_stop_computer():
        clockRunning = message_json['clockEnabled']
        if clockRunning:
            controller.start_computer()
            print("START")
        else:
            controller.stop_computer()
            print("STOP")

    def updateRam():
        clockRunning = controller.get_clock_running_state()
        if clockRunning:
            controller.set_computer_ram(message_json['memoryContents'])
        else:
            controller.set_computer_ram(message_json['memoryContents'])

    message_type = None

    try:
        message_type = message_json['type']
    except KeyError:
        print("Json contains no \"type\" field")

    if message_type is not None:
        if message_type == 'advanceClock':
            data = controller.execute_one_computer_cycle_and_return_state()
            return data

        if message_type == 'clockEnabled':
            start_or_stop_computer()
            return None

        if message_type == 'ramUpdate':
            updateRam()
            data = controller.get_computer_state()
            return data

        if message_type == 'reset':
            controller.stop_computer()
            data = controller.reset_computer_and_return_state()
            return data

        if message_type == 'getUpdate':
            data = controller.get_computer_state()
            return data

    return None



