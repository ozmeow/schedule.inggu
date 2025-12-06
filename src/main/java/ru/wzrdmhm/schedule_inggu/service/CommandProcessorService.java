package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.exception.GroupNotFoundException;
import ru.wzrdmhm.schedule_inggu.model.dto.BotResponse;
import ru.wzrdmhm.schedule_inggu.model.dto.CommandRequest;
import ru.wzrdmhm.schedule_inggu.model.entity.Group;
import ru.wzrdmhm.schedule_inggu.model.entity.Schedule;
import ru.wzrdmhm.schedule_inggu.model.entity.User;
import ru.wzrdmhm.schedule_inggu.repository.GroupRepository;

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

    @Autowired
    private WeekService weekService;

    @Autowired
    private GroupRepository groupRepository;

    public BotResponse commandProcessorService(CommandRequest request) {
        try {
            switch (request.getCommandType()) {
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
                    return handleHelpCommand();
                case SET_GROUP:
                    return handleSetGroupCommand(request.getUserId(), request.getParameters());
                case SHOW_GROUPS:
                    return handleShowGroupsCommand(request.getUserId());
                default:
                    return new BotResponse("❌ Неизвестная команда", false);
            }
        } catch (Exception e) {
            return new BotResponse("❌ Ошибка обработки команды в парсере: " + e.getMessage(), false);
        }
    }

    private BotResponse handleStartCommand(Long userId) {
        User user = userService.findOrCreateUser(userId, "User");
        String response = "👋 Добро пожаловать в бот расписания!\n" +
                "📚 Ваша группа: " + user.getGroup() + "\n" +
                "ℹ️ Используйте /help для списка команд";
        return new BotResponse(response, true);
    }

    private BotResponse handleNowSchedule(Long userId) {
        try {
            userService.validateUserHasGroup(userId);
            Group userGroup = userService.getUserGroup(userId);
            LocalDate today = LocalDate.now();
            List<Schedule> todaySchedule = scheduleService.getScheduleForGroupAndDate(userGroup, today);

            LocalTime now = LocalTime.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                return new BotResponse(
                        "🎉 Сегодня " + getRussianDayName(dayOfWeek) + "!\nВыходной день 😊",
                        true
                );
            }

            if (now.isBefore(LocalTime.of(8, 0))) {
                return new BotResponse("🌅 Еще слишком рано для пар! Первая пара в 9:00", true);
            }
            if (now.isAfter(LocalTime.of(20, 0))) {
                return new BotResponse("🌙 Уже поздно! Пары закончились на сегодня", true);
            }

            // 📭 5. ЕСЛИ ПАР НЕТ - СООБЩАЕМ С УЧЕТОМ НЕДЕЛИ
            if (todaySchedule.isEmpty()) {
                String weekType = weekService.getWeekType(today);
                return new BotResponse(
                        String.format("📭 Сегодня пар нет! (%s) 🎉", getRussianWeekType(weekType)),
                        true
                );
            }


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

        // Сортируем по времени
        List<Schedule> sortedSchedule = schedules.stream()
                .sorted(Comparator.comparing(Schedule::getTime))
                .collect(Collectors.toList());

        // 🎯 ИЩЕМ ТЕКУЩУЮ ПАРУ (ИДЕТ СЕЙЧАС)
        for (Schedule s : sortedSchedule) {
            String[] timeParts = s.getTime().split("-");
            if (timeParts.length == 2) {
                LocalTime start = LocalTime.parse(timeParts[0]);
                LocalTime end = LocalTime.parse(timeParts[1]);

                if (!now.isBefore(start) && !now.isAfter(end)) {
                    long minutesLeft = Duration.between(now, end).toMinutes();
                    return String.format(
                            "🎯 СЕЙЧАС ИДЕТ:%n📚 %s%n👨‍🏫 %s%n🕐 %s (%d мин. до конца)%n📍 Ауд. %s",
                            s.getSubject(),
                            s.getTeacher() != null ? s.getTeacher() : "Преподаватель не указан",
                            s.getTime(),
                            minutesLeft,
                            s.getClassroom()
                    );
                }
            }
        }

        // ⏰ ИЩЕМ СЛЕДУЮЩУЮ ПАРУ
        for (Schedule s : sortedSchedule) {
            String[] timeParts = s.getTime().split("-");
            if (timeParts.length == 2) {
                LocalTime start = LocalTime.parse(timeParts[0]);

                if (now.isBefore(start)) {
                    Duration duration = Duration.between(now, start);
                    long minutes = duration.toMinutes();

                    // 🚀 СРОЧНОЕ УВЕДОМЛЕНИЕ (меньше 15 минут)
                    if (minutes <= 15) {
                        return String.format(
                                "🚀 СКОРО НАЧНЕТСЯ (%d мин):%n📚 %s%n👨‍🏫 %s%n🕐 %s%n📍 Ауд. %s",
                                minutes, s.getSubject(),
                                s.getTeacher() != null ? s.getTeacher() : "Преподаватель не указан",
                                s.getTime(), s.getClassroom()
                        );
                    }
                    // ⏰ ОБЫЧНОЕ УВЕДОМЛЕНИЕ
                    else {
                        return String.format(
                                "⏰ СЛЕДУЮЩАЯ ПАРА ЧЕРЕЗ %d мин:%n📚 %s%n👨‍🏫 %s%n🕐 %s%n📍 Ауд. %s",
                                minutes, s.getSubject(),
                                s.getTeacher() != null ? s.getTeacher() : "Преподаватель не указан",
                                s.getTime(), s.getClassroom()
                        );
                    }
                }
            }
        }
        return "✅ Пары на сегодня закончились! Можно отдыхать 🎉";
    }

    private BotResponse handleTodaySchedule(Long userId) {

        try {
            userService.validateUserHasGroup(userId);

            Group userGroup = userService.getUserGroup(userId);
            LocalDate today = LocalDate.now();
            List<Schedule> schedule = scheduleService.getScheduleForGroupAndDate(userGroup, today);

            if (schedule.isEmpty()) {
                String weekType = weekService.getWeekType(today);
                return new BotResponse(
                        String.format("📭 Сегодня пар нет! (%s) 🎉 отдохни",
                                getRussianWeekType(weekType)), true);
            }


            String response = formatBeautifulSchedule(schedule, today, "cегодня");
            return new BotResponse(response, true);
        } catch (UserService.UserGroupNotSetException e) {
            //  пока что одобрено решение, которое выкидывается тут:
            //  расписание недоступно нажми старт и все заработает (возможно он сам находит тебя снова id)
            return new BotResponse("❌ Ошибка Получения расписания /handleToday " + e.getMessage(), false);
        } catch (Exception e) {
            return new BotResponse("🚫 Ошибка: " + e.getMessage(), false);
        }
    }

    // Вспомогательный метод для русских названий типов недель
    private String getRussianWeekType(String weekType) {
        switch (weekType) {
            case "ODD":
                return "1 неделя";
            case "EVEN":
                return "2 неделя";
            default:
                return weekType;
        }
    }

    private BotResponse handleTomorrowSchedule(Long userId) {
        try {
            userService.validateUserHasGroup(userId);

            Group userGroup = userService.getUserGroup(userId);

            LocalDate tomorrow = LocalDate.now().plusDays(1);
            DayOfWeek dayOfWeek = tomorrow.getDayOfWeek();
            String dayNameRussia = getRussianDayName(dayOfWeek);

            List<Schedule> schedule = scheduleService.getScheduleForGroupAndDate(userGroup, tomorrow);


            if (isWeekend(dayOfWeek)) {
                return new BotResponse("Завтра " + dayNameRussia +
                        "\uD83C\uDF89\n🛌 Наб е хьейн дика-m дар хьун)", false);

            }

            if (schedule.isEmpty()) {
                return new BotResponse("   \uD83C\uDF89 На завтра " +
                        dayNameRussia + " пар нет!", true);
            }

            String response = formatBeautifulSchedule(schedule, tomorrow, "завтра");
            return new BotResponse(response, true);
        } catch (UserService.UserGroupNotSetException e) {
            return new BotResponse("❌ Ошибка получения расписания /handleTomorrow: \n" + e.getMessage(), false);
        } catch (Exception e) {
            return new BotResponse("🚫 Ошибка " + e.getMessage(), false);
        }
    }

    private boolean isWeekend(DayOfWeek weekend) {
        return weekend == DayOfWeek.SATURDAY || weekend == DayOfWeek.SUNDAY;
    }

    private BotResponse handleWeekSchedule(Long userId) {
        try {
            userService.validateUserHasGroup(userId);

            Group groupName = userService.getUserGroup(userId);
            LocalDate today = LocalDate.now();
            LocalDate monday = today.with(DayOfWeek.MONDAY);
            String whatWeek = weekService.getWeekType(today);
            String weekType = (whatWeek.equals("ODD")) ? "1" : "2";


                    StringBuilder weekSchedule = new StringBuilder();
            weekSchedule.append("📅 РАСПИСАНИЕ НА " + weekType + " НЕДЕЛЮ\n\n");

            for (int i = 0; i < 7; ++i) {
                LocalDate currentDay = monday.plusDays(i);
                String dayName = getRussianDayName(currentDay.getDayOfWeek());

                List<Schedule> daySchedule = scheduleService.getScheduleForGroupAndDate(groupName, currentDay);

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
            return new BotResponse("❌ Ошибка, не установлена группа " + e.getMessage(), false);
        } catch (Exception e) {
            return new BotResponse("🚫 Ошибка получения расписания на неделю: : " + e.getMessage(), false);
        }
    }

    private String getRussianDayName(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return "Понедельник";
            case TUESDAY:
                return "Вторник";
            case WEDNESDAY:
                return "Среда";
            case THURSDAY:
                return "Четверг";
            case FRIDAY:
                return "Пятница";
            case SATURDAY:
                return "суббота";
            case SUNDAY:
                return "воскресенье";
            default:
                return day.toString();
        }
    }

    private BotResponse handleHelpCommand() {
        String response = "📋 Доступные команды:\n" +
                "/start - Начало работы\n" +
                "/group  - Задать/Сменить группу\n" +
                "/now - пары сейчас\n" +
                "/today - Расписание на сегодня\n" +
                "/tomorrow - Расписание на завтра\n" +
                "/week - Расписание на неделю\n" +
                "/help - Справка по командам";
        return new BotResponse(response, true);
    }

    private BotResponse handleSetGroupCommand(Long userId, Map<String, String> parameters) {
        String groupCode = parameters.get("groupCode");

        if (groupCode == null || groupCode.trim().isEmpty()) {
            return new BotResponse("❌ Укажите код группы, например: /group ХББм-2", false);
        }

        Group group = groupRepository.findByCode(groupCode.trim())
                .orElseThrow(() -> new GroupNotFoundException(groupCode));

        userService.setUserGroup(userId, group);

        return new BotResponse("✅ Группа установлена: " + group.getCode(), true);
    }

    private BotResponse handleShowGroupsCommand(Long userId) {
        List<Group> groups = groupRepository.findAllByOrderByCode();

        StringBuilder response = new StringBuilder("📚 Доступные группы:\n\n");

        Map<Character, List<Group>> groupsByCourse = groups.stream()
                .collect(Collectors.groupingBy(g -> g.getCode().charAt(3)));

        groupsByCourse.forEach((course, courseGroups) -> {
            response.append("🎓 Курс ").append(course).append(":\n");
            courseGroups.forEach(g ->
                    response.append("  • ").append(g.getCode())
                            .append(" - ").append(g.getFullName()).append("\n"));
            response.append("\n");
        });

        response.append("📝 Выберите группу: /group [код]\n");
        response.append("Пример: /group ХББм-2");

        return new BotResponse(response.toString(), true);
    }

    private String formatBeautifulSchedule(List<Schedule> schedule, LocalDate date, String period) {
        String dayName = getRussianDayName(date.getDayOfWeek());

        String header = String.format("\uD83D\uDCC5 Расписание %s (%s)%n\uD83D\uDCCAВсего пар: %d%n────────────────%n", period, dayName, schedule.size());
        StringBuilder body = new StringBuilder(header);

        for (int i = 0; i < schedule.size(); ++i) {
            Schedule s = schedule.get(i);

            body.append(String.format("%d пара%n", i + 1, i + 1));
            body.append(String.format("   \uD83D\uDD50 %s%n", s.getTime()));
            body.append(String.format("   \uD83D\uDCDA %s%n", s.getSubject()));

            if (s.getClassroom() != null && !s.getClassroom().isEmpty()) {
                body.append(String.format("   📍 Ауд. %s%n", s.getClassroom()));
            }

            if (s.getTeacher() != null && !s.getTeacher().isEmpty()) {
                body.append(String.format("    Препод. %s%n", s.getTeacher()));
            }

            if (i < schedule.size() - 1) {
                body.append(String.format("───────────────%n"));
            }

        }
        return body.toString();
    }
}
