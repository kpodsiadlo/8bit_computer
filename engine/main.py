import json
from engine.websocketclient.websocket_client import WebsocketClient
import quart

app = quart.Quart(__name__)
app.config["DEBUG"] = True
computers = {}
default_clock_speed = 10


@app.route('/', methods=['POST'])

async def start():
    targetUUID = quart.request.args.get("uuid")
    computer = WebsocketClient(default_clock_speed, targetUUID)
    computers["uuid"] = computer
    computer.start()
    if computer:
        return {"Computer with targetId " + targetUUID + "started"}

@app.route("/", methods=['GET'])
def getAll():
    return json.dumps(computers)

@app.route("/", methods=['DELETE'])
def stop():
    targetUUID = quart.request.args.get("uuid")
    del computers[targetUUID]
    if computers[targetUUID] == None:
        return "Computer with targetId" + targetUUID + "destroyed"


app.run()