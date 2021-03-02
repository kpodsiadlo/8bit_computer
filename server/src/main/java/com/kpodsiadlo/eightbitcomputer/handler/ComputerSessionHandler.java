package com.kpodsiadlo.eightbitcomputer.handler;

import com.kpodsiadlo.eightbitcomputer.model.Computer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class ComputerSessionHandler {

    private Set<Session> sessions = new HashSet<>();
    @Inject
    Computer computer;

    public void updateComputerModel(JsonObject message){
        Logger.getLogger(this.getClass().getName()).info("updateComputer");

        try { Integer programCounter = message.getInt("programCounter");
            Logger.getLogger(this.getClass().getName()).info("programCounter = " + programCounter);
            computer.setProgramCounter(programCounter);
        }
            catch (Exception ignored) {

        }

        try {
            Boolean clockRunning = message.getBoolean("clockRunning");
            Logger.getLogger(this.getClass().getName()).info("clockRunning = " + clockRunning);
            computer.setClockRunning(clockRunning);
        } catch (Exception ignored) {

        }

        try { Integer registerA = message.getInt("registerA");
            Logger.getLogger(this.getClass().getName()).info("registerA = " + registerA);
            computer.setRegisterA(registerA);
        }
        catch (Exception ignored) {

        }
    }

    public JsonObject getComputerModelState() {
        Logger.getLogger(this.getClass().getName()).info("getComputerState");
        Boolean clockRunning = computer.getClockRunning();
        Integer programCounter = computer.getProgramCounter();
        Integer registerA = computer.getRegisterA();
        return JsonProvider.provider().createObjectBuilder()
                .add("clockRunning", clockRunning)
                .add("programCounter", programCounter)
                .add("registerA", registerA)
                .build();
    }

    public void addSession(Session session){
        sessions.add(session);
    }

    public void removeSession(Session session){
        sessions.remove(session);
    }

    public void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            removeSession(session);
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
        }
    }

    public void sendToAllSessions(JsonObject message) {
        sessions.forEach(session -> sendToSession(session, message));
    }
}
