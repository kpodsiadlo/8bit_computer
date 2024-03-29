<%@page language="java" contentType="text/html; ISO-8859-1" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.kpodsiadlo.eightbitcomputer.model.Compiler" %>
<%@ page import="java.util.Set" %>
<!DOCTYPE HTML>
<html lang="EN">
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="css/style.css">
    <title>8bit computer</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-5">
            <div class="row" id="indicators">
                <span class="badge badge-info" id="connection-indicator"></span>
                <span id="clock-running-display">Clock: Stopped</span>
                <input class="btn btn-primary" type=button id="start-engine"
                       value="Power-up"/>
            </div>
            <div class="row" id="clock">
                <input class="btn btn-danger" type=button id="toggle-clock-button" value="START">
                <input class="btn btn-success" type=button id="manual-clock-button" value="TICK">
                <input class="btn btn-outline-warning" type=button id="reset" value="RESET">
            </div>
            <div class="row" id="memory-address">
                <label for="memory-address-display">Memory Address: </label>
                <input type="number" value="0" id="memory-address-display">
                <span class="badge badge-secondary" id="MAR_IN">IN</span>
            </div>
            <div class="row" id="memory-value">
                <label for="memory-value-display">Memory Contents: </label>
                <input type="number" value="0" id="memory-value-display" class="binary">
                <span class="badge badge-secondary" id="RAM_OUT">OUT</span>
                <span class="badge badge-secondary" id="RAM_IN">IN</span>
            </div>
            <div class="row" id="instruction-register">
                <table class="table-instruction-register">
                    <tr>
                        <td>
                            <label for="instruction-register-display-higher-bits">Instruction:</label>
                            <input type="number" class="binary-4-digits" value="0"
                                   id="instruction-register-display-higher-bits">
                            <span id="instruction-register-opcode"></span>
                        </td>
                        <td>
                            <label for="instruction-register-display-lower-bits">Value:</label>
                            <input type="number" size="4" value="0" id="instruction-register-display-lower-bits">
                            <span class="badge badge-secondary" id="IregisterOUT">VALUE OUT</span>
                            <span class="badge badge-secondary" id="IregisterIN">IN</span>
                        </td>
                    </tr>
                </table>


            </div>
            <div class="row" id="microinstruction-counter">
                <label for="microinstruction-counter-display">Microinstruction Counter: </label>
                <input type="number" value="0" id="microinstruction-counter-display">
            </div>
            <div class="row" id="decoder">Decoder
            </div>
        </div>
        <div class="col-2" id="bus">
            <label for="bus-display">BUS DATA:</label>
            <input type="number" value="0" id="bus-display">
        </div>
        <div class="col-5">
            <div class="row" id="program-counter">
                <label for="program-counter-display">Program Counter: </label><input type="number" value="0"
                                                                                     id="program-counter-display">
                <span class="badge badge-secondary" id="PC_enable">INCREASE</span>
                <span class="badge badge-secondary" id="PC_OUT">OUT</span>
                <span class="badge badge-secondary" id="PC_Jump">JUMP(IN)</span>

            </div>
            <div class="row" id="register-a">
                <label for="register-a-display">Register A: </label>
                <input type="number" value="0" id="register-a-display">
                <span class="badge badge-secondary" id="AregisterOUT">OUT</span>
                <span class="badge badge-secondary" id="AregisterIN">IN</span>

            </div>
            <div class="row" id="alu">

                <label for="alu-display">ALU</label>
                <input type="number" value="0" id="alu-display">
                <span class="badge badge-secondary" id="ALU_OUT">OUT</span>
                <span class="badge badge-secondary" id="ALU_Substract">SUBTRACT</span>
                <span>Flags: </span>
                <span class="badge badge-secondary" id="carry">CARRY</span>
                <span class="badge badge-secondary" id="zero">ZERO</span>

            </div>
            <div class="row" id="register-b">
                <label for="register-b-display">Register B: </label>
                <input type="number" value="0" id="register-b-display">
                <span class="badge badge-secondary" id="BregisterOUT">OUT</span>
                <span class="badge badge-secondary" id="BregisterIN">IN</span>
            </div>
            <div class="row" id="output">
                <label for="output-display">Output </label>
                <input type="number" value="0" id="output-display">
                <span class="badge badge-secondary" id="display_IN">IN</span>
            </div>
            <div class="row" id="instruction-display">Instructions:
            </div>

        </div>
    </div>
    <div class="row">
        <div class=col-5>
            <select id="program-selector" name="Select Program:" size="1">

            </select>
            <table class="tg">
                <thead>
                <tr>
                    <th class="tg-0lax">Address</th>
                    <th class="tg-0lax">Binary</th>
                    <th class="tg-0lax">Decimal</th>
                    <th class="tg-0lax">Instruction</th>
                    <th class="tg-0lax">Value</th>

                </tr>
                </thead>
                <%
                    List<String> cellNumberList = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8",
                            "9", "10", "11", "12", "13", "14", "15");
                    for (String cellNumber : cellNumberList) {
                %>
                <tr class="memory-cell-container" id="memory-cell-container-<%=cellNumber%>">
                    <td class="tg-0lax"><span class="memory-cell-number"><%=cellNumber%></span></td>
                    <td class="tg-0lax"><input type="number" class="memory-cell-binary"
                                               id="mem<%=cellNumber%>-binary-value"></td>
                    <td class="tg-0lax"><input type="number" class="memory-cell-decimal"
                                               id="mem<%=cellNumber%>-decimal-value"></td>
                    <td class="tg-0lax"><span class="memory-cell-instruction" id="mem<%=cellNumber%>-instruction"></span></td>
                    <td class="tg-0lax"><input type="number" class="memory-cell-value"
                                               id="mem<%=cellNumber%>-instruction-value"></td>

                </tr>
                <%
                    }
                %>

            </table>
        </div>
        <div class=col-3>
            <span>Create your own program:</span>
            <form>
                <table class=tg id="compiler-input">
                    <thead>
                    <tr>
                        <th class="tg-0lax">Address</th>
                        <th class="tg-0lax">Instruction</th>
                        <th class="tg-0lax">Value</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        for (int i = 0; i < cellNumberList.size(); i++) {
                    %>
                    <tr>
                        <td class="tg-0lax"><%=i%>
                        </td>
                        <td class=tg-0lax>
                            <Select class="assembly-instruction" id="assembly-instruction-<%=i%>">
                                <% Set<String> instructions = Compiler.getInstructionsFromOpcodes();
                                    for (String instruction : instructions) {%>
                                <option <%if (instruction.equals("nop")) {
                                %>selected="" <% }
                                %>value="<%=instruction%>"><%=instruction%>
                                </option>
                                <% } %>
                                <option value="assembly-value">value</option>
                            </Select>
                        </td>
                        <td class="tg-0lax">
                            <input type=number class="assembly-value" id=assembly-value-<%=i%>>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </form>
            <input type="button" class="btn btn-block" value="Compile!" id="compile-button">
        </div>
        <div class="col-4">
            <div><pre id="compiler-display"></pre></div>
            <input type="button" class="btn btn-block" value="Send To Computer!" id="send-to-computer-button">
        </div>
    </div>
</div>
</body>
<script src="script/main.js"></script>
<script src="script/messageTypes.js"></script>
<script src="script/update.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="script/compiler.js"></script>
</html>