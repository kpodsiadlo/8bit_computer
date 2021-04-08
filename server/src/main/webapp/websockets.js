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
    updateALUDisplay(computerData.alu);
    updateRegisterBDisplay(computerData.registerB);
    updateOutputDisplay(computerData.output);
    updateBus(computerData.bus);
    updateInstructions(computerData.logic);
    updateFlags(computerData.flags)
}

function updateInstructions(logic) {
    console.log("update instructions");
    Object.keys(logic).forEach(function (key) {
        changeColor(key, logic[key]);
    })
}

function updateFlags(flags) {
    console.log("update flags");
    Object.keys(flags).forEach(function (key) {
        changeColor(key, flags[key]);
    })
}

function changeColor(element, boolean) {
    if (document.getElementById(element) != null) {
        if (boolean == 1) {
            //if (document.getElementById(element).classList.contains("off")) {
            document.getElementById(element).classList.remove("off");
            //}
            //if (!document.getElementById(element.classList.contains("on"))) {
            document.getElementById(element).classList.add("on");
            //}
        }
        if (boolean == 0) {
            //if (document.getElementById(element).classList.contains("on")) {
            document.getElementById(element).classList.remove("on");
            //}
            //if (!document.getElementById(element.classList.contains("off"))) {
            document.getElementById(element).classList.add("off");
            //}
        }
    }
}

function updateMemoryAddress(data) {
    document.getElementById("memory-address-display").value = data;
}

function updateMemoryContents(data) {
    document.getElementById("memory-content-display").value = data;
}

function updateInstructionRegisterHigherBits(data) {
    document.getElementById("instruction-register-display-higher-bits").value = data;
}

function updateInstructionRegisterLowerBits(data) {
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

function updateALUDisplay(data) {
    document.getElementById("alu-display").value = data;
}

function updateRegisterBDisplay(data) {
    document.getElementById("register-b-display").value = data;
}

function updateOutputDisplay(data) {
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
    jsonMessage["source"] = "WEBPAGE";
    return jsonMessage;

}