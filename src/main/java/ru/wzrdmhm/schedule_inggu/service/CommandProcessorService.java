package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.dto.BotResponse;
import ru.wzrdmhm.schedule_inggu.dto.CommandRequest;
import ru.wzrdmhm.schedule_inggu.model.Schedule;
import ru.wzrdmhm.schedule_inggu.model.User;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommandProcessorService {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    public BotResponse commandProcessorService(CommandRequest request) {
        try {
            switch (request.getCommand()) {
                case START:
                    return handleStartCommand(request.getUserId());
                case NOW_SCHEDULE:
                    return handleNowSchedule(request.getUserId());
                case TODAY_SCHEDULE:
                    return handleTodaySchedule(request.getUserId());
                case TOMORROW_SCHEDULE:
                    return handleTomorrowSchedule(request.getUserId());
                case WEEK_SCHEDULE:
                    return handleWeekSchedule(request.getUserId());
                case HELP:
                    return HandleHelpCommand();
                case SET_GROUP:
                    return handleSetGroupCommand(request.getUserId(), request.getParameters());
                default:
                    return new BotResponse("❌ Неизвестная команда", false);
            }
        } catch (Exception e) {
            return new BotResponse("❌ Ошибка обработки команды: " + e.getMessage(), false);
        }
    }

    private BotResponse handleStartCommand(Long userId) {
        User user = userService.findOrCreateUser(userId, "User");
        String response = "👋 Добро пожаловать в бот расписания!\n" +
                "📚 Ваша группа: " + user.getGroupName() + "\n" +
                "ℹ️ Используйте /help для списка команд";
        return new BotResponse(response, true);
    }

    private BotResponse handleNowSchedule(Long userId) {
        try {
            String userGroup = userService.getUserGroup(userId);
            LocalDate today = LocalDate.now();
            List<Schedule> todaySchedule = scheduleService.getScheduleForGroup(userGroup, today.toString());

            if (todaySchedule.isEmpty()) {
                return new BotResponse("📭 Сегодня пар нет! Можно отдыхать \uD83C\uDF89", true);
            }

            LocalTime now = LocalTime.now();
            String result = findCurrentOrNextPair(todaySchedule, now);
            return new BotResponse(result, true);

        } catch (Exception e) {
            System.err.println("Ошибка в handleNowSchedule: " + e.getMessage());
            e.printStackTrace();
            return new BotResponse("❌ Ошибка команды /now: " + e.getMessage(), false);
        }
    }

    private String findCurrentOrNextPair(List<Schedule> schedules, LocalTime now) {
        System.out.println("🔍 ПОИСК ТЕКУЩЕЙ/СЛЕДУЮЩЕЙ ПАРЫ");
        System.out.println("Текущее время: " + now);

        List<Schedule> sortedSchedule = schedules.stream()
                .sorted(Comparator.comparing(Schedule::getTime))
                .collect(Collectors.toList());

        // Печатаем все пары для отладки
        for (Schedule s : sortedSchedule) {
            System.out.println("Пара: " + s.getSubject() + " | " + s.getTime());
        }

        for (Schedule s : sortedSchedule) {
            String[] timeParts = s.getTime().split("-");
            if (timeParts.length == 2) {
                LocalTime start = LocalTime.parse(timeParts[0]);
                LocalTime end = LocalTime.parse(timeParts[1]);

                if (!now.isBefore(start) && !now.isAfter(end)) {
                    return String.format("🎯 Сейчас идет:%n📚 %s%n🕐 %s%n📍 Ауд. %s",
                            s.getSubject(), s.getTime(), s.getClassroom());
                }
            }
        }

        for (Schedule s : sortedSchedule) {
            String[] timeParts = s.getTime().split("-");
            if (timeParts.length == 2) {
                LocalTime start = LocalTime.parse(timeParts[0]);

                if (now.isBefore(start)) {
                    Duration duration = Duration.between(now, start);
                    long minutes = duration.toMinutes();

                    if (minutes <= 60) {
                        return String.format("⏳ Следующая пара через %d мин:%n📚 %s%n🕐 %s",
                                minutes, s.getSubject(), s.getTime());
                    }
                }
            }
        }
        return "✅ Пары на сегодня закончились!";
    }

    private BotResponse handleTodaySchedule(Long userId) {

        try {
            userService.validateUserHasGroup(userId);

            String userGroup = userService.getUserGroup(userId);
            LocalDate today = LocalDate.now();
            List<Schedule> schedules = scheduleService.getScheduleForGroup(userGroup, today.toString());

            if (schedules.isEmpty()) {
                return new BotResponse("   \uD83C\uDF89 На сегодня пар нет!", true);
            }

            String response = formatBeautifulSchedule(schedules, today, "cегодня");
            return new BotResponse(response, true);
        } catch (UserService.UserGroupNotSetException e) {
            return new BotResponse("❌ Получения расписания сегодня " + e.getMessage(), false);
        } catch (Exception e) {
            return new BotResponse("❌ Ошибка: " + e.getMessage(), false);
        }
    }

    private BotResponse handleTomorrowSchedule(Long userId) {
        try {
            String userGroup = userService.getUserGroup(userId);

            LocalDate tomorrow = LocalDate.now().plusDays(1);
            DayOfWeek dayOfWeek = tomorrow.getDayOfWeek();
            String dayNameRussia = getRussianDayName(dayOfWeek);

            List<Schedule> schedule = scheduleService.getScheduleForGroup(userGroup, tomorrow.toString());


            if (isWeekend(dayOfWeek)) {
                return new BotResponse("Завтра " + dayNameRussia +
                        ". 🛌 Наб е хьейн, дика-m дар хьун)", false);
            }

            if (schedule.isEmpty()) {
                return new BotResponse("   \uD83C\uDF89 На завтра " +
                        dayNameRussia + " пар нет!", true);
            }

            String response = formatBeautifulSchedule(schedule, tomorrow, dayNameRussia);
            return new BotResponse(response, true);
        } catch (UserService.UserGroupNotSetException e) {
            return new BotResponse("❌ Ошибка получения расписания: " + e.getMessage(), false);
        } catch (Exception e) {
            return new BotResponse("❌ Ошибка: " + e.getMessage(), false);
        }
    }

    private boolean isWeekend(DayOfWeek weekend) {
        return weekend == DayOfWeek.SATURDAY || weekend == DayOfWeek.SUNDAY;
    }

    private BotResponse handleWeekSchedule(Long userId) {
        try {
            userService.validateUserHasGroup(userId);

            String groupName = userService.getUserGroup(userId);
            LocalDate today = LocalDate.now();
            LocalDate monday = today.with(DayOfWeek.MONDAY);

            StringBuilder weekSchedule = new StringBuilder();
            weekSchedule.append("📅 РАСПИСАНИЕ НА НЕДЕЛЮ\n\n");

            for (int i = 0; i < 7; ++i) {
                LocalDate currentDay = monday.plusDays(i);
                String dayName = getRussianDayName(currentDay.getDayOfWeek());

                List<Schedule> daySchedule = scheduleService.getScheduleForGroup(groupName, currentDay.toString());

                weekSchedule.append(String.format("%s (%s)%n", dayName, currentDay.format(DateTimeFormatter.ofPattern("dd.MM"))));

                if (daySchedule.isEmpty()) {
                    if (currentDay.getDayOfWeek() == DayOfWeek.SATURDAY || currentDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        weekSchedule.append("   \uD83C\uDF89 Выходной\n");
                    } else {
                        weekSchedule.append("   📭 Пар нет\n");
                    }
                } else {
                    for (Schedule s : daySchedule) {
                        weekSchedule.append(String.format("   📍%s - %s%n", s.getClassroom(), s.getSubject()));
                    }
                }
                weekSchedule.append("\n");
            }
            return new BotResponse(weekSchedule.toString(), true);

        } catch (UserService.UserGroupNotSetException e) {
            return new BotResponse(e.getMessage(), false);
        } catch (Exception e) {
            return new BotResponse("❌ Ошибка получения расписания на неделю: : " + e.getMessage(), false);
        }
    }

    private String getRussianDayName(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return "Понедельник";
            case TUESDAY:
                return "Вторник";
            case WEDNESDAY:
                return "Среду";
            case THURSDAY:
                return "Четверг";
            case FRIDAY:
                return "Пятницу";
            case SATURDAY:
                return "Субботу";
            case SUNDAY:
                return "Воскресенье";
            default:
                return day.toString();
        }
    }

    private BotResponse HandleHelpCommand() {
        String response = "📋 Доступные команды:\n" +
                "/start - Начало работы\n" +
                "/setgroup [группа] - Задать/Сменить группу\n" +
                "/now - пары сейчас\n" +
                "/today - Расписание на сегодня\n" +
                "/tomorrow - Расписание на завтра\n" +
                "/week - Расписание на неделю\n" +
                "/help - Справка по командам";
        return new BotResponse(response, true);
    }

    private BotResponse handleSetGroupCommand(Long userId, Map<String, String> parameters) {
        try {
            String groupName = parameters.get("groupName");
            if (groupName == null || groupName.trim().isEmpty()) {
                return new BotResponse("❌ Укажите название группы: /setgroup [ИВТ-21]", false);
            }
            userService.setUserGroup(userId, groupName.trim());
            return new BotResponse("✅ Группа установлена: " + groupName, true);
        } catch (Exception e) {
            return new BotResponse("❌ Ошибка смены группы: " + e.getMessage(), false);
        }
    }

    /*
    private String formatScheduleResponse(List<Schedule> schedules, LocalDate date) {
        StringBuilder sb = new StringBuilder();
        sb.append("\uD83D\uDCDA Расписание на ").append(date).append(":\n\n");
        for (int i = 0; i < schedules.size(); ++i) {
            Schedule s = schedules.get(i);
            sb.append(i + 1).append(".").append(s.getTime())
                    .append(" - ").append(s.getSubject())
                    .append(" (аудит.").append(s.getClassroom()).append(")\n");
        }
        return sb.toString();
    }
    */

    private String formatBeautifulSchedule(List<Schedule> schedule, LocalDate date, String period) {
        String dayName = getRussianDayName(date.getDayOfWeek());

        String header = String.format("\uD83D\uDCC5 Расписание на %s (%s)%n\uD83D\uDCC6 %s%n%n", period, dayName, date);

        StringBuilder body = new StringBuilder(header);

        for (int i = 0; i < schedule.size(); ++i) {
            Schedule s = schedule.get(i);

            body.append(String.format("\uD83D\uDD39 Пара %d%n", i + 1));
            body.append(String.format("   \uD83D\uDD50 %s%n", s.getTime()));
            body.append(String.format("   \uD83D\uDCDA %s%n", s.getSubject()));

            if (s.getClassroom() != null && !s.getClassroom().isEmpty()) {
                body.append(String.format("   📍Ауд. %s%n", s.getClassroom()));
            }

            if (s.getTeacher() != null && !s.getTeacher().isEmpty()) {
                body.append(String.format("   👨🏻‍🏫 Препод. %s%n", s.getTeacher()));
            }

            if (i < schedule.size() - 1) {
                body.append(String.format("   ──────────────%n"));
            }

        }

        body.append(String.format("%n\uD83D\uDCCAВсего пар: %d", schedule.size()));

        return body.toString();
    }

}

