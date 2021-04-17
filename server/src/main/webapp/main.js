const socket = new WebSocket("ws://localhost:8080/server/computer");
socket.onmessage = onMessage;
socket.onopen = onOpen;
let connectionTimer = 1;
setInterval(updateConnectionIndicator, 1000);
const enableIncomingMessageLogging = true;
const enableOutcomingMessageLogging = true;

const toggleClockButton = document.querySelector("#toggle-clock-button");
const advanceClockButton = document.querySelector("#manual-clock-button");
const resetButton = document.querySelector("#reset");
const programSelector = document.querySelector("#program-selector");
const connectionIndicator = document.querySelector("#connection-indicator")

window.addEventListener('load', (event) => {onLoad()});
toggleClockButton.addEventListener("click", onToggleClock)
advanceClockButton.addEventListener("click", onManualClockAdvance)
resetButton.addEventListener("click", onReset)
programSelector.addEventListener("change", (event) => onSelectProgram(event.target))


function onOpen() {
    updateConnectionIndicator();
    getComputerStatus();
}

function onLoad() {
    getProgramsfromDatabase();
}

function onMessage(event) {
    let incomingData = JSON.parse(event.data);
    if (enableOutcomingMessageLogging) {
        console.log("Event received:");
        console.log(incomingData);
    }
    resetTimer();
    processMessage(incomingData)

    function processMessage(processedData) {
        if (processedData.type === "clockStopped") {
            enableManualClockIncrease();
            updateClockRunning(false);
        }
        if (processedData.type === "displayUpdate") {
            updateDisplays(incomingData);
        }
    }
}

function onToggleClock() {
    switch (toggleClockButton.value) {
        case "STOP": {
            let jsonMessage = {"type": "clockEnabled",
                "clockEnabled": false,
            };
            sendJsonObjectToSocket(jsonMessage);
            toggleClockButton.value = "START";
            break;

        }
        case "START": {
            let jsonMessage = {"type": "clockEnabled",
                "clockEnabled": true,
            };
            toggleClockButton.value = "STOP";
            sendJsonObjectToSocket(jsonMessage);
            break;
        }
    }
}

function onManualClockAdvance() {
    let jsonMessage = {"type": "advanceClock"};
    sendJsonObjectToSocket(jsonMessage);
}

function onReset() {
    let resetMessage = {"type": "reset"};
    sendJsonObjectToSocket(resetMessage);
    if (toggleClockButton.value === "STOP") {
        toggleClockButton.value = "START";
        enableManualClockIncrease();
    }
}

function onSelectProgram(selector) {
    getProgramFromDatabaseAndSendToEngine(selector.value);
}


function getComputerStatus() {
    let jsonMessage = {
        "type": "getUpdate"
    }
    sendJsonObjectToSocket(jsonMessage);
}

function updateConnectionIndicator() {
    connectionTimer -= 1;
    if (checkConnectionStatus()) {
        connectionIndicator.innerText = "CONNECTED";
    } else {
        connectionIndicator.innerText = "DISCONNECTED";
    }
}


function checkConnectionStatus() {
    return (connectionTimer > 0);
}

function resetTimer() {
    connectionTimer = 3 ;
}

function sendProgramAsIntArrayToServer(program) {
    let jsonMessage = {};
    jsonMessage["type"] = "ramUpdate";
    jsonMessage["memoryContents"] = program;
    sendJsonObjectToSocket(jsonMessage);
}

function sendJsonObjectToSocket(jsonMessage) {
    if (enableOutcomingMessageLogging) {
        console.log("Sending: ")
        console.log(jsonMessage);
    }
    let jsonMessageWithSource = addSourceToJSONMessage(jsonMessage);
    socket.send(JSON.stringify(jsonMessageWithSource));
}


function addSourceToJSONMessage(jsonMessage) {
    jsonMessage["source"] = "WEBPAGE";
    return jsonMessage;

}

function getProgramFromDatabaseAndSendToEngine(databaseId) {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let incomingJson = JSON.parse(this.responseText);
            let program = convertMemoryContentsFromDatabaseToDecimalValuesArray(incomingJson.contents)
            sendProgramAsIntArrayToServer(program);
        }
    };
    xmlhttp.open("GET", "http://localhost:8080/server/api/program/" + databaseId, true);
    xmlhttp.send();
}

function convertMemoryContentsFromDatabaseToDecimalValuesArray(stringBinaryValues) {
    let decimalValues = [];
    let memorySize = 16
    for (let i = 0; i < memorySize; i++) {
        if (i < stringBinaryValues.length) {
            let decimalValue = parseInt(stringBinaryValues[i], 2);
            decimalValues[i] = decimalValue;
        } else {
            decimalValues[i] = 0;
        }
    }
    return decimalValues;
}

function getProgramsfromDatabase() {
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let incomingJson = JSON.parse(this.responseText);
            updateProgramList(incomingJson);
        }
    };
    xmlhttp.open("GET", "http://localhost:8080/server/api/program/", true);
    xmlhttp.send();

    function updateProgramList(incomingListOfPrograms) {
        let displayedListOfPrograms = programSelector;
        let result = "<option value=\"\" selected=\"\" disabled=\"\">Select program:</option>";
        for (let i = 0; i < incomingListOfPrograms.length; i++) {
            result += createOptionEntry(incomingListOfPrograms[i]);
        }
        displayedListOfPrograms.innerHTML = result;

        function createOptionEntry(program) {
            return "<option value=\"" + program.id + "\">" + program.name + "</option>";
        }
    }
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

