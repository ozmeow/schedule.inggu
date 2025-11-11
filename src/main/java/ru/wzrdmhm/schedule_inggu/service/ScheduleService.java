package ru.wzrdmhm.schedule_inggu.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.model.Schedule;
import ru.wzrdmhm.schedule_inggu.repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private WeekService weekService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private List<Schedule> testSchedules = new ArrayList<>();

    @PostConstruct
    public void initTestData() {
        // 🎯 ВРЕМЕННЫЕ ДАННЫЕ ДЛЯ ТЕСТИРОВАНИЯ
        testSchedules.add(createSchedule("ХББ", "Математика", "09:00-10:20", "101", 1, "BOTH"));
        testSchedules.add(createSchedule("ХББ", "Физика", "10:30-11:50", "205", 3, "BOTH"));
        testSchedules.add(createSchedule("ХББ", "Химия", "09:00-10:20", "301", 3, "ODD"));
        testSchedules.add(createSchedule("ХББ", "Биология", "09:00-10:20", "301", 3, "EVEN"));
    }

    private Schedule createSchedule(String group, String subject, String time,
                                    String classroom, int day, String weekType) {
        Schedule s = new Schedule();
        s.setGroupName(group);
        s.setSubject(subject);
        s.setTime(time);
        s.setClassroom(classroom);
        s.setDayOfWeek(day);
        s.setWeekType(weekType);
        return s;
    }

    public List<Schedule> getScheduleForGroupAndDate(String groupName, LocalDate date) {
        // 🎯 ВРЕМЕННО ИСПОЛЬЗУЕМ ТЕСТОВЫЕ ДАННЫЕ
        String weekType = weekService.getWeekType(date);
        int dayOfWeek = date.getDayOfWeek().getValue();

        return testSchedules.stream()
                .filter(s -> s.getGroupName().equals(groupName))
                .filter(s -> s.getDayOfWeek() == dayOfWeek)
                .filter(s -> s.getWeekType().equals("BOTH") || s.getWeekType().equals(weekType))
                .collect(Collectors.toList());
    }
}
    /*@Autowired
    private WeekService weekService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    //Получает расписание на конкретную дату с учетом чередования недель
    public List<Schedule> getScheduleForGroupAndDate(String groupName, LocalDate date) {
        String weekType = weekService.getWeekType(date);
        int dayOfWeek = date.getDayOfWeek().getValue();  // 1-7

        return scheduleRepository.findByGroupAndDayAndWeek(groupName, dayOfWeek, weekType);
    }

    public List<Schedule> getTodaySchedule(Long groupId) {
        LocalDate today = LocalDate.now();
        String weekType = weekService.getWeekType(today);
        int dayOfWeek = today.getDayOfWeek().getValue();

        return scheduleRepository.findByGroupIdAndDayAndWeekType(
                groupId, dayOfWeek, weekType);
    }

    public List<Schedule> getAllTodaySchedule(Long groupId) {
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();

        return scheduleRepository.findByGroupIdAndDayOfWeek(groupId, dayOfWeek);
    }


    public Map<LocalDate, List<Schedule>> getWeeklySchedule(String groupName, LocalDate startDate) {
        Map<LocalDate, List<Schedule>> weeklySchedule = new LinkedHashMap<>();
        String weekType = weekService.getWeekType(startDate);

        for (int i = 0; i < 7; ++i) {
            LocalDate currentDate = startDate.plusDays(i);
            int dayOfWeek = currentDate.getDayOfWeek().getValue();

            List<Schedule> daySchedule = scheduleRepository.findByGroupAndDayAndWeek(groupName, dayOfWeek, weekType);
            weeklySchedule.put(currentDate, daySchedule);
        }
        return weeklySchedule;
    }
    */
    /*
    @Service
public class ScheduleService {
    private List<Schedule> testSchedules = new ArrayList<>();

    @PostConstruct
    public void initTestData() {
        // 🎯 ВРЕМЕННЫЕ ДАННЫЕ ДЛЯ ТЕСТИРОВАНИЯ
        testSchedules.add(createSchedule("Bio-19", "Математика", "09:00-10:20", "101", 1, "BOTH"));
        testSchedules.add(createSchedule("Bio-19", "Физика", "10:30-11:50", "205", 3, "BOTH"));
        testSchedules.add(createSchedule("Bio-19", "Химия", "09:00-10:20", "301", 3, "ODD"));
        testSchedules.add(createSchedule("Bio-19", "Биология", "09:00-10:20", "301", 3, "EVEN"));
    }

    private Schedule createSchedule(String group, String subject, String time,
                                  String classroom, int day, String weekType) {
        Schedule s = new Schedule();
        s.setGroupName(group);
        s.setSubject(subject);
        s.setTime(time);
        s.setClassroom(classroom);
        s.setDayOfWeek(day);
        s.setWeekType(weekType);
        return s;
    }

    public List<Schedule> getScheduleForGroupAndDate(String groupName, LocalDate date) {
        // 🎯 ВРЕМЕННО ИСПОЛЬЗУЕМ ТЕСТОВЫЕ ДАННЫЕ
        String weekType = weekService.getWeekType(date);
        int dayOfWeek = date.getDayOfWeek().getValue();

        return testSchedules.stream()
            .filter(s -> s.getGroupName().equals(groupName))
            .filter(s -> s.getDayOfWeek() == dayOfWeek)
            .filter(s -> s.getWeekType().equals("BOTH") || s.getWeekType().equals(weekType))
            .collect(Collectors.toList());
    }
}
     */

