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
        decTo8DigitBinString(contents[address]);
}

function updateMemoryContentsDisplay(data) {
    updateBinaryValue(data);
    updateDecimalValue(data);
    updateInstruction(data);
    updateInstructionValue(data);

    function updateBinaryValue(data) {
        for (let i = 0; i < data.length; i++) {
            document.getElementById("mem" + i + "-binary-value").value =
                decTo8DigitBinString(data[i]);
        }
    }

    function updateDecimalValue(data) {
        for (let i = 0; i < data.length; i++) {
            document.getElementById("mem" + i + "-decimal-value").value =
                (data[i]);
        }
    }

    function updateInstruction(data) {
        let instructionBinaryData;
        for (let i = 0; i < data.length; i++) {
            instructionBinaryData = decTo8DigitBinString(data[i]).substring(0, 4);
            let opcode = machine_code[instructionBinaryData];
            if (opcode !== undefined) {
                document.getElementById("mem" + i + "-instruction").innerText =
                    opcode.toUpperCase();
            }
        }
    }

    function updateInstructionValue(data) {
        for (let i = 0; i < data.length; i++) {
            document.getElementById("mem" + i + "-instruction-value").value =
                parseInt((decTo8DigitBinString(data[i]).substring(4)), 2);
        }
    }
}

function decTo8DigitBinString(dec) {
    let bin = dec.toString(2);
    return bin.padStart(8, "0");
}

function highlightCurrentMemoryAddress(memoryAddress) {
    let rows = document.querySelectorAll(".memory-cell-container");
    rows.forEach(row => row.classList.remove("current-memory-address"))
    document.getElementById("memory-cell-container-" + memoryAddress).classList.add("current-memory-address");
}


function updateInstructionRegisterHigherBits(data) {
    let instructionInBinary = decTo8DigitBinString(data).substring(4);
    document.getElementById("instruction-register-display-higher-bits").value = instructionInBinary;
    let opcode = machine_code[instructionInBinary];
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