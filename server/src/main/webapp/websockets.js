const socket = new WebSocket("ws://localhost:8080/server/computer");
socket.onmessage = onMessage;

function onMessage(event) {
    console.log("Event received");
    let computerData = JSON.parse(event.data);
    console.log(computerData);
    updateDisplays(computerData);
}

function updateDisplays(computerData) {
    updateMemoryAddress(computerData.memoryAddress);
    updateProgramCounterDisplay(computerData.programCounter);
    updateMemoryContents(computerData.memoryContents);
    updateInstructionRegisterHigherBits(computerData.instructionRegisterHigherBits);
    updateInstructionRegisterLowerBits(computerData.instructionRegisterLowerBits);
    updateMicroinstructionCounter(computerData.microinstructionCounter);
    updateRegisterADisplay(computerData.registerA);
    updateRegisterBDisplay(computerData.registerB);
    updateOutputDisplay(computerData.outputDisplay);
    updateBus(computerData.bus);
}

function updateMemoryAddress(data) {
    document.getElementById("memory-address-display").value = data;
}

function updateMemoryContents(data) {
    document.getElementById("memory-content-display").value = data;
}

function updateInstructionRegisterHigherBits (data) {
    document.getElementById("instruction-register-display-higher-bits").value = data;
}

function updateInstructionRegisterLowerBits (data) {
    document.getElementById("instruction-register-display-lower-bits").value = data;
}

function updateMicroinstructionCounter(data) {
    document.getElementById("microinstruction-counter-display").value = data;
}

function updateProgramCounterDisplay(data) {
    document.getElementById("program-counter-display").value = data;
}

function updateRegisterADisplay(data) {
    document.getElementById("register-a-display").value = data;
}

function updateRegisterBDisplay(data) {
    document.getElementById("register-b-display").value = data;
}

function updateOutputDisplay(data){
    document.getElementById("output-display").value = data;
}

function updateBus(data) {
    document.getElementById("bus-display").value = data;
}


function onProgramCounterIncrease() {
    var currentValue = parseInt(document.getElementById("program-counter-display").value);
    updateProgramCounterDisplay(currentValue + 1)
    sendUpdate();
}

function sendUpdate() {
    console.log("sendUpdate");
    let programCounter = parseInt(document.getElementById("program-counter-display").value);
    let jsonMessage = {
        "programCounter": programCounter
    };
    jsonMessage = addSourceToJSONMessage(jsonMessage)
    console.log(jsonMessage);
    socket.send(JSON.stringify(jsonMessage));
}

function onToggleClock() {
    console.log("onToggleClock")
    let toggleButton = document.getElementById("toggle-clock-button");
    switch (toggleButton.value) {
        case "STOP": {
            let jsonMessage = {"clockRunning": false};
            jsonMessage = addSourceToJSONMessage(jsonMessage)
            socket.send(JSON.stringify(jsonMessage));
            toggleButton.value = "START";
            break;

        }
        case "START": {
            let jsonMessage =
                {"clockRunning": true};
            jsonMessage = addSourceToJSONMessage(jsonMessage)
            socket.send(JSON.stringify(jsonMessage));
            toggleButton.value = "STOP";
            break;
        }
    }

}

function addSourceToJSONMessage(jsonMessage) {
    jsonMessage["source"] = "WEBPAGE2";
    return jsonMessage;

}