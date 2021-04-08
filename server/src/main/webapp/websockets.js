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
    updateCurrentMemoryValue(computerData.memoryAddress, computerData.memoryContents);
    updateProgramCounterDisplay(computerData.programCounter);
    updateInstructionRegisterHigherBits(computerData.instructionRegisterHigherBits);
    updateInstructionRegisterLowerBits(computerData.instructionRegisterLowerBits);
    updateMicroinstructionCounter(computerData.microinstructionCounter);
    updateRegisterADisplay(computerData.registerA);
    updateALUDisplay(computerData.alu);
    updateRegisterBDisplay(computerData.registerB);
    updateOutputDisplay(computerData.output);
    updateBus(computerData.bus);
    updateControlLights(computerData.logic);
    updateFlags(computerData.flags)
    updateMemoryContents(computerData.memoryContents);
    highlightCurrentMemoryAddress(computerData.memoryAddress, computerData.memoryContents.length)
}

function updateControlLights(logic) {
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

function updateCurrentMemoryValue(address, contents) {
    document.getElementById("memory-value-display").value =
        dec2bin(contents[address]);
}

function updateMemoryContents(data) {
    updateBinaryValue(data);
    updateDecimalValue(data);
    updateInstruction(data);
    updateInstructionValue(data);

    function updateBinaryValue(data) {
        for (let i = 0; i < data.length; i++) {
            document.getElementById("mem" + i + "-binary-value").value =
                dec2bin(data[i]);
        }
    }

    function updateDecimalValue(data) {
        for (let i = 0; i < data.length; i++) {
            document.getElementById("mem" + i + "-decimal-value").value =
                (data[i]);
        }
    }

    function updateInstruction(data) {
        for (let i = 0; i < data.length; i++) {
            instructionBinaryData = dec2bin(data[i]).substring(0, 4);
            opcode = machine_code[instructionBinaryData];
            if (opcode != undefined) {
                document.getElementById("mem" + i + "-instruction").innerText =
                    opcode.toUpperCase();
            }
        }
    }

    function updateInstructionValue(data) {
        for (let i = 0; i < data.length; i++) {
            document.getElementById("mem" + i + "-instruction-value").value =
                parseInt((dec2bin(data[i]).substring(4)), 2);
        }
    }



}

function dec2bin(dec) {
    let bin = dec.toString(2);
    return bin.padStart(8, "0");
}

function highlightCurrentMemoryAddress(memoryAddress, memoryLength) {

    var rows = document.querySelectorAll(".memory-cell-container");
    rows.forEach(row => row.classList.remove("current-memory-address"))
    document.getElementById("memory-cell-container-" + memoryAddress).classList.add("current-memory-address");

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

const machine_code = {
    "0000": "nop",
    "0001": "lda",
    "0010": "add",
    "0011": "sub",
    "0100": "sta",
    "0101": "ldi",
    "0110": "jmp",
    "0111": "jc",
    "1000": "jz",
    "1110": "out",
    "1111": "hlt"
}