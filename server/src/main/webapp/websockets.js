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
    updateMemoryContentsDisplay(computerData.memoryContents);
    highlightCurrentMemoryAddress(computerData.memoryAddress)
    updateClockRunning(computerData.clockRunning)
}
/*
function sendComputerStateToServer() {
    let computerState = getComputerState();
    socket.send(JSON.stringify(computerState));
}
 */

function sendProgramToServer(program){
    let jsonObject = {};
    jsonObject["Source"] = "Webpage";
    jsonObject["ramUpdate"] = true;
    jsonObject["memoryContents"] = program;
    console.log(jsonObject);
    socket.send(JSON.stringify(jsonObject));
}
/*
function getComputerState() {
    computerState = {};
    ram = getRamState();
    memoryAddress = getMemoryAddress();
    computerState.memoryContents = ram;
    computerState.memoryAddress = memoryAddress;
}
 */

/*
function getRamState() {
    ramState = []
    for (i = 0; i < 16; i++) {
        ramState[i] = parseInt(document.getElementById("mem" + i + "-decimal-value").value);
    }
    return ramState
}

 */

/*function getMemoryAddress() {
    return document.getElementById("memory-address-display").value;
}

 */

function onReset() {
    resetMessage = {"reset": true};
    socket.send(JSON.stringify(resetMessage));
    var toggleClockButton = document.getElementById("toggle-clock-button");
    if (toggleClockButton.value === "STOP") {
        document.getElementById("toggle-clock-button").value = "START";
        enableManualClockIncrease();
    }
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
        if (boolean === 1) {
            //if (document.getElementById(element).classList.contains("off")) {
            document.getElementById(element).classList.remove("off");
            //}
            //if (!document.getElementById(element.classList.contains("on"))) {
            document.getElementById(element).classList.add("on");
            //}
        }
        if (boolean === 0) {
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
        decTo8DigitBin(contents[address]);
}

function updateMemoryContentsDisplay(data) {
    updateBinaryValue(data);
    updateDecimalValue(data);
    updateInstruction(data);
    updateInstructionValue(data);

    function updateBinaryValue(data) {
        for (let i = 0; i < data.length; i++) {
            document.getElementById("mem" + i + "-binary-value").value =
                decTo8DigitBin(data[i]);
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
            instructionBinaryData = decTo8DigitBin(data[i]).substring(0, 4);
            opcode = machine_code[instructionBinaryData];
            if (opcode !== undefined) {
                document.getElementById("mem" + i + "-instruction").innerText =
                    opcode.toUpperCase();
            }
        }
    }

    function updateInstructionValue(data) {
        for (let i = 0; i < data.length; i++) {
            document.getElementById("mem" + i + "-instruction-value").value =
                parseInt((decTo8DigitBin(data[i]).substring(4)), 2);
        }
    }
}

function decTo8DigitBin(dec) {
    let bin = dec.toString(2);
    return bin.padStart(8, "0");
}

function highlightCurrentMemoryAddress(memoryAddress) {

    var rows = document.querySelectorAll(".memory-cell-container");
    rows.forEach(row => row.classList.remove("current-memory-address"))
    document.getElementById("memory-cell-container-" + memoryAddress).classList.add("current-memory-address");

}


function updateInstructionRegisterHigherBits(data) {
    var instructionInBinary = decTo8DigitBin(data).substring(4);
    document.getElementById("instruction-register-display-higher-bits").value = instructionInBinary;
    var opcode = machine_code[instructionInBinary];
    document.getElementById("instruction-register-opcode").innerText = ("(" + opcode + ")").toUpperCase();
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

function updateClockRunning(data) {
    var clockStatus = document.getElementById("clock-running-display");
    console.log("clockStatus" + data);
    if (data === true) {
        clockStatus.innerText = "Clock: Running";
        disableManualClockIncrease();

    } else if (data === false) {
        clockStatus.innerText = "Clock: Stopped";
        enableManualClockIncrease();
    }
}

function enableManualClockIncrease() {
    var manualClock = document.getElementById("manual-clock-button");
    manualClock.classList.remove("btn-secondary");
    manualClock.classList.add("btn-success");
    manualClock.disabled = false;
}

function disableManualClockIncrease() {
    var manualClock = document.getElementById("manual-clock-button");
    manualClock.classList.remove("btn-success");
    manualClock.classList.add("btn-secondary");
    manualClock.disabled = true;

}

function onTick() {
    let jsonMessage = {"tick": true};
    jsonMessage = addSourceToJSONMessage(jsonMessage)
    socket.send(JSON.stringify(jsonMessage));
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


function getPrograms() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        console.log("onreadystatechange");
        if (this.readyState == 4 && this.status == 200) {
            var incomingJson = JSON.parse(this.responseText);
            updateProgramList(incomingJson);
            let decimalValues = convertMemoryContentsFromDatabaseAsDecimalValues(incomingJson[0].contents);
            updateMemoryContentsDisplay(decimalValues)
        }
    };
    xmlhttp.open("GET", "http://localhost:8080/server/api/program/", true);
    xmlhttp.send();

    function updateProgramList(incomingListOfPrograms) {
        var displayedListOfPrograms = document.getElementById("program-selector");
        result = "";
        for (i=0; i<incomingListOfPrograms.length; i++) {
            result += createOptionEntry(incomingListOfPrograms[i]);
        }
        displayedListOfPrograms.innerHTML = result;

        function createOptionEntry(program) {
            entry = "<option value=\"" + program.id + "\">" + program.name + "</option>"
            return entry;
        }
    }
}

function convertMemoryContentsFromDatabaseAsDecimalValues(stringBinaryValues){
    decimalValues = [];
    for (let i = 0; i < 16; i++) {
        if (i<stringBinaryValues.length) {
            decimalValue = parseInt(stringBinaryValues[i], 2);
            decimalValues[i] = decimalValue;
        }
        else {
            decimalValues[i] = 0;
        }
    }
    return decimalValues;
}

function onSelectProgram(selector){
    program = getProgramFromDatabase(selector.value);
    updateMemoryContentsDisplay(program);
    sendProgramToServer(program);
}


function getProgramFromDatabase(id) {
    var decimalValues = [];
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        console.log("onreadystatechange");
        if (this.readyState == 4 && this.status == 200) {
            var incomingJson = JSON.parse(this.responseText);
            decimalValues = convertMemoryContentsFromDatabaseAsDecimalValues(incomingJson.contents)
        }
    };
    xmlhttp.open("GET", "http://localhost:8080/server/api/program/" + id, true);
    xmlhttp.send();
    return decimalValues;
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