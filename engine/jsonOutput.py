import json

class JsonOutput:
    def __init__(self, program_counter):
        computer_state = {}
        computer_state["programCounter"] = program_counter.state

    def update(self, program_counter):
        self.__init__(program_counter)

    def generate_json(self):
        json_message = json.dumps(self.computer_state, 4)
        return json_message


