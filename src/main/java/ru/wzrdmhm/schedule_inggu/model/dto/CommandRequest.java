package ru.wzrdmhm.schedule_inggu.model.dto;

import java.util.HashMap;
import java.util.Map;

public class CommandRequest {
    private Long userId;
    private CommandType commandType;
    private Map<String, String> parameters = new HashMap<>();

    public CommandRequest(Long userId, CommandType commandType) {
        this.userId = userId;
        this.commandType = commandType;
    }

    public CommandRequest(CommandType commandType) {
        this.commandType = commandType; // или this.commandType = commandType?
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    public void addParameters(String key, String value) {
        parameters.put(key, value);
    }
}
