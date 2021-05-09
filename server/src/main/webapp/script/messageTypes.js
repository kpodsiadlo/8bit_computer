const MESSAGE_SOURCES =
    {
        server: "SERVER",
        engine: "ENGINE",
        webpage: "WEBPAGE"
    }

const MESSAGE_TYPES = {
    Engine: {
        display_update : "displayUpdate",
        connection_request : "connectToWebpage",
        clock_stopped : "clockStopped"
    },
    Server: {
        target_assignment : "targetAssigment",
        id_assignment : "idAssignment"
    },
    Webpage: {
        register_request : "registerRequest",
        clock_enabled : "clockEnabled",
        advance_clock : "advanceClock",
        reset : "reset",
        ram_update : "ramUpdate"
    }
}