from engine.websocketclient.websocket_client import WebsocketClient

clock_speed = 10  #Hz

client = WebsocketClient(clock_speed=clock_speed, targetId = target_id)
client.start()