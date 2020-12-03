const socket = new WebSocket("ws://localhost:8080/server/computer");
socket.onmessage = onMessage;

function onMessage(event) {
    console.log("Event received");
    let computerData = JSON.parse(event.data);
    let programCounter = computerData.programCounter;
    updateProgramCounter(programCounter);
}

function updateProgramCounter(programCounter) {
    document.getElementById("program-counter-display").value = programCounter;
}

function onProgramCounterIncrease() {
    var currentValue = parseInt(document.getElementById("program-counter-display").value);
    updateProgramCounter(currentValue + 1)
    sendUpdate();
}

function sendUpdate() {
    console.log("sendUpdate");
    var programCounter = parseInt(document.getElementById("program-counter-display").value);
    var jsonMessage = {
        "programCounter": programCounter
    };
    console.log(jsonMessage);
    socket.send(JSON.stringify(jsonMessage));
}