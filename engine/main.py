from websocket_client import WebsocketClient
from sys import argv

target_id = argv[1]
clock_speed = 10  #Hz
uri = "ws://localhost:8080/server/computer"

client = WebsocketClient(clock_speed=clock_speed, uri=uri, targetId = target_id)
client.start()