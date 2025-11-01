package ru.wzrdmhm.schedule_inggu.dto;

public enum CommandType {
    START,
    HELP,
    TODAY_SCHEDULE,  // /today, "расписание сегодня"
    TOMORROW_SCHEDULE, // /tomorrow, "завтра"
    WEEK_SCHEDULE,   // /week
    SET_GROUP,       // /setgroup
    UNKNOWN
}
