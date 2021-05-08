from websocket_client import WebsocketClient

clock_speed = 10  #Hz
uri = "ws://localhost:8080/server/computer"

client = WebsocketClient(clock_speed=clock_speed, uri=uri)
client.start()