package ru.wzrdmhm.schedule_inggu.dto;

import java.util.HashMap;
import java.util.Map;

public class CommandRequest {
    private Long userId;
    private CommandType command;
    private Map<String, String> parameters = new HashMap<>();

    public CommandRequest() {
    }

    public CommandRequest(Long userId, CommandType command) {
        this.userId = userId;
        this.command = command;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CommandType getCommand() {
        return command;
    }

    public void setCommand(CommandType command) {
        this.command = command;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
