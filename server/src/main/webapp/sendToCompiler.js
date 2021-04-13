function compile() {
    let program = getUserProgram();
    sendToCompilerJS(program);
}



function getUserProgram() {
    let instructions = [];
    let values = [];

    for (i = 0; i < 16; i++) {
        instructions[i] = $('#assembly-instruction-' + i).children("option:selected").val();
        value = parseInt(($("#assembly-value-" + i).val()), 10);
        if (isNaN(value)) {
            values[i] = 0;
        } else {
            values[i] = value;
        }
    }
    jsonObject = {"instructions": instructions, "values": values};
    return jsonObject;
}

function sendToCompilerJS(jsonObject) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var incomingJson = JSON.parse(this.responseText);
            $('#compiler-display').html(incomingJson);
        }
    }
    xmlhttp.open("GET", "http://localhost:8080/server/api/compiler/", true);
    xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    console.log(jsonObject);
    console.log(JSON.stringify(jsonObject))
    xmlhttp.send(JSON.stringify(jsonObject));
}