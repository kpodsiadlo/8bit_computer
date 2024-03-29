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

function updateMemoryAddress(data) {
    document.getElementById("memory-address-display").value = data;
}

function updateCurrentMemoryValue(address, contents) {
    document.getElementById("memory-value-display").value =
        decTo8DigitBinString(contents[address]);
}

function highlightCurrentMemoryAddress(memoryAddress) {
    let rows = document.querySelectorAll(".memory-cell-container");
    rows.forEach(row => row.classList.remove("current-memory-address"))
    document.getElementById("memory-cell-container-" + memoryAddress).classList.add("current-memory-address");
}

function updateProgramCounterDisplay(data) {
    document.getElementById("program-counter-display").value = data;
}


function updateInstructionRegisterHigherBits(data) {
    let instructionInBinary = decTo8DigitBinString(data).substring(4);
    document.getElementById("instruction-register-display-higher-bits").value = instructionInBinary;
    let opcode = machine_code[instructionInBinary];
    let opcodeDisplay = document.querySelector("#instruction-register-opcode")
    if (opcode !== undefined) {
        opcodeDisplay.innerText = ("(" + opcode + ")").toUpperCase();
    } else {
        opcodeDisplay.innerText = "";
    }
}

function updateInstructionRegisterLowerBits(data) {
    document.getElementById("instruction-register-display-lower-bits").value = data;
}

function updateMicroinstructionCounter(data) {
    document.getElementById("microinstruction-counter-display").value = data;
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

function updateControlLights(logic) {
    Object.keys(logic).forEach(function (key) {
        changeColor(key, logic[key]);
    })
}

function updateFlags(flags) {
    Object.keys(flags).forEach(function (key) {
        changeColor(key, flags[key]);
    })
}

function changeColor(element, boolean) {
    if (document.getElementById(element) != null) {
        if (boolean === 1) {
            document.getElementById(element).classList.remove("off");
            document.getElementById(element).classList.add("on");
        }
        if (boolean === 0) {
            document.getElementById(element).classList.remove("on");
            document.getElementById(element).classList.add("off");
        }
    }
}

function updateMemoryContentsDisplay(data) {
    updateBinaryValue(data);
    updateDecimalValue(data);
    updateInstruction(data);
    updateInstructionValue(data);

    function updateBinaryValue(data) {
        let binaryMemoryLocations = document.querySelectorAll(".memory-cell-binary")
        binaryMemoryLocations.forEach((element, idx) => {
            element.value = decTo8DigitBinString(data[idx])
        })
    }

    function updateDecimalValue(data) {
        let decimalMemoryLocations = document.querySelectorAll(".memory-cell-decimal")
        decimalMemoryLocations.forEach((element, idx) => {
            element.value = data[idx];
        })
    }

    function updateInstruction(data) {
        let instructionNames = document.querySelectorAll(".memory-cell-instruction")
        instructionNames.forEach((element, idx) => {
            let instructionBinaryData = decTo8DigitBinString(data[idx]).substring(0, 4);
            let opcode = machine_code[instructionBinaryData];
            if (opcode !== undefined) {
                element.textContent = opcode;
                }
        })
    }

    function updateInstructionValue(data) {
        let instructionValues = document.querySelectorAll(".memory-cell-value")
        instructionValues.forEach((element, idx) => {
            element.value = parseInt(decTo8DigitBinString(data[idx]).substring(4), 2)
        })
    }
}

function decTo8DigitBinString(dec) {
    let bin = dec.toString(2);
    return bin.padStart(8, "0");
}


function updateClockRunning(data) {
    let clockStatus = document.getElementById("clock-running-display");
    if (data === true) {
        clockStatus.innerText = "Clock: Running";
        disableManualClockIncrease();

    } else if (data === false) {
        clockStatus.innerText = "Clock: Stopped";
        enableManualClockIncrease();
    }
}

function enableManualClockIncrease() {
    let manualClock = document.getElementById("manual-clock-button");
    manualClock.classList.remove("btn-secondary");
    manualClock.classList.add("btn-success");
    manualClock.disabled = false;
}

function disableManualClockIncrease() {
    let manualClock = document.getElementById("manual-clock-button");
    manualClock.classList.remove("btn-success");
    manualClock.classList.add("btn-secondary");
    manualClock.disabled = true;
}
