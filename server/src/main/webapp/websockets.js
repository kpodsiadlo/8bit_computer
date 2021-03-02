const socket = new WebSocket("ws://localhost:8080/server/computer");
socket.onmessage = onMessage;

function onMessage(event) {
    console.log("Event received");
    let computerData = JSON.parse(event.data);
    console.log(computerData);
    updateProgramCounterDisplay(computerData.programCounter);
    updateRegisterADisplay(computerData.registerA);
}

function updateProgramCounterDisplay(data) {
    document.getElementById("program-counter-display").value = data;
}

function updateRegisterADisplay(data) {
    document.getElementById("register-a-display").value = data;
}

function onProgramCounterIncrease() {
    var currentValue = parseInt(document.getElementById("program-counter-display").value);
    updateProgramCounterDisplay(currentValue + 1)
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

function onToggleClock() {
    console.log("onToggleClock")
    let toggleButton = document.getElementById("toggle-clock-button");
    switch (toggleButton.value) {
        case "STOP": {
            let jsonMessage = {"clockRunning": false};
            socket.send(JSON.stringify(jsonMessage));
            toggleButton.value = "START";
            break;

        }
        case "START": {
            let jsonMessage = {"clockRunning": true};
            socket.send(JSON.stringify(jsonMessage));
            toggleButton.value = "STOP";
            break;
        }
    }

}