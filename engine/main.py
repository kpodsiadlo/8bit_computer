import json
from engine.websocketclient.websocket_client import WebsocketClient
import quart
import asyncio

app = quart.Quart(__name__)
app.config["DEBUG"] = True
clients = {}
default_clock_speed = 10

@app.route('/', methods=['POST'])
async def start():
    target_uuid = quart.request.args.get("uuid")
    if target_uuid in clients.keys():
        return "This UUID already exists"
    client = WebsocketClient(default_clock_speed, target_uuid)
    clients[target_uuid] = client
    print(clients)
    loop = asyncio.get_event_loop()
    client.start(loop)
    if client:
        return "Computer with targetId " + target_uuid + " started"

@app.route("/", methods=['GET'])
def getAll():
    return json.dumps([key for key in clients.keys()])



@app.route("/", methods=['DELETE'])
def stop():
    targetUUID = quart.request.args.get("uuid")
    client = clients.pop(targetUUID)
    client.stop()
    del client
    return "Computer with targetId " + targetUUID + " destroyed."

asyncio.run(app.run_task())
