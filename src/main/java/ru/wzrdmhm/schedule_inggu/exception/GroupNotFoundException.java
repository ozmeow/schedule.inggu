package ru.wzrdmhm.schedule_inggu.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String groupCode) {
        super("Группа не найдена: " + groupCode);
    }
}
