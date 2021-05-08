var userProgramAsIntArray = null;
const compileButton = document.querySelector("#compile-button")
const sendToComputerButton = document.querySelector("#send-to-computer-button")
const assemblyInstructions = document.querySelectorAll(".assembly-instruction")
const assemblyValues = document.querySelectorAll(".assembly-value")
const compilerDisplay = document.querySelector("#compiler-display")

compileButton.addEventListener("click", onCompile)
sendToComputerButton.addEventListener("click", onSendProgramToComputer)

function onCompile() {
    let program = getUserProgramFromInput();
    sendToCompilerJS(program);
}

function getUserProgramFromInput() {
    let instructions = [];
    let values = [];

    assemblyInstructions.forEach((element) => {
        instructions.push(element.value)
    });
    assemblyValues.forEach((element) => {
        value = parseInt(element.value, 10);
        if (isNaN(value)) {
            values.push(0)
        } else {
            values.push(value)
        }
    });

    jsonObject = {"instructions": instructions, "values": values}
    return jsonObject;
}

function sendToCompilerJS(jsonObject) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var incomingJson = JSON.parse(this.responseText);
            console.log(incomingJson);

            let formattedProgram = formatIncomingProgramForDisplaying(incomingJson.contents)
            compilerDisplay.innerText = formattedProgram;
            userProgramAsIntArray = incomingJson.contents.map(value => {
                return parseInt(value, 2)
            });
        }
    }
    xmlhttp.open("POST", "http://localhost:8080/server/api/compiler/", true);
    xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xmlhttp.send(JSON.stringify(jsonObject));
}

function onSendProgramToComputer() {
    sendProgramAsIntArrayToServer(userProgramAsIntArray);
}


function formatIncomingProgramForDisplaying(program) {
    let formattedProgram = "";
    program.forEach(value => addToFormattedProgram(value));
    formattedProgram.substr(0, -2);
    return formattedProgram;

    function addToFormattedProgram(value) {
        formattedProgram += value + "\n"
    }
}