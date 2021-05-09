const MESSAGE_SOURCES =
    {
        server: "SERVER",
        engine: "ENGINE",
        webpage: "WEBPAGE"
    }

const MESSAGE_TYPES = {
    Engine: {
        displayUpdate : "displayUpdate",
        connectionRequest : "connectToWebpage",
        clockStopped : "clockStopped"
    },
    Server: {
        targetAssignment : "targetAssignment",
        originAssignment : "originAssignment",
        handshakeComplete : "handShakeComplete"
    },
    Webpage: {
        registerRequest : "registerRequest",
        clockEnabled : "clockEnabled",
        advanceClock : "advanceClock",
        reset : "reset",
        ramUpdate : "ramUpdate",
        connectionACK : "connectionACK"
    }
}