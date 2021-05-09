class MessageSources:
    server = "SERVER"
    engine = "ENGINE"
    webpage = "WEBPAGE"

class MessageTypes:

    class Engine:
        display_update = "displayUpdate"
        connection_request = "connectToWebpage"
        clock_stopped = "clockStopped"

    class Server:
        target_assignment = "targetAssignment"
        origin_assignment = "originAssignment"
        handshake_complete = "handshakeComplete"

    class Webpage:
        register_request = "registerRequest"
        clock_enabled = "clockEnabled"
        advance_clock = "advanceClock"
        reset = "reset"
        ram_update = "ramUpdate"
        connection_ACK = "connectionACK"
