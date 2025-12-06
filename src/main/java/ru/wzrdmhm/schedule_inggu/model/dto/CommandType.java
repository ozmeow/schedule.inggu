package ru.wzrdmhm.schedule_inggu.model.dto;

public enum CommandType {
    START,
    HELP,
    TODAY_SCHEDULE,  /// today, "расписание сегодня"
    TOMORROW_SCHEDULE, /// tomorrow, "завтра"
    WEEK_SCHEDULE,   /// week
    NOW_SCHEDULE,
    SET_GROUP,       /// groups
    SHOW_GROUPS,    /// Показать список групп
    UNKNOWN
}
