package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.exception.GroupNotFoundException;
import ru.wzrdmhm.schedule_inggu.model.entity.Group;
import ru.wzrdmhm.schedule_inggu.model.entity.Schedule;
import ru.wzrdmhm.schedule_inggu.repository.GroupRepository;
import ru.wzrdmhm.schedule_inggu.repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {

    @Autowired
    private WeekService weekService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private GroupRepository groupRepository;

    // ОСНОВНОЙ МЕТОД: получает расписание из БД с учетом чередующихся недель
    public List<Schedule> getScheduleForGroupAndDate(Group groupName, LocalDate date) {
        String weekType = weekService.getWeekType(date);
        int dayOfWeek = date.getDayOfWeek().getValue();

        return scheduleRepository.findByGroupAndDayAndWeek(groupName, dayOfWeek, weekType);
    }

    /**
     * 📅 Получает расписание на сегодня для группы (по ID группы)
     * Пока не используется, но оставим для будущего
     */
    public List<Schedule> getTodaySchedule(String groupCode) {
        Group group = groupRepository.findByCode(groupCode)
                .orElseThrow(() -> new GroupNotFoundException(groupCode));
        LocalDate today = LocalDate.now();
        String weekType = weekService.getWeekType(today);
        int dayOfWeek = today.getDayOfWeek().getValue();

        return scheduleRepository.findByGroupAndDayOfWeekAndWeekType(group, dayOfWeek, weekType);
    }

//     * ⏰ Для команды /now - все пары на сегодня без учета недели
    public List<Schedule> getAllTodaySchedule(Group group) {
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        return scheduleRepository.findByGroupAndDayOfWeek(group, dayOfWeek);
    }


    //     * 📊 Получает расписание на всю неделю
    public Map<LocalDate, List<Schedule>> getWeeklySchedule(String groupCode, LocalDate startDate) {

        Group group = groupRepository.findByCode(groupCode)
                .orElseThrow(() -> new GroupNotFoundException(groupCode));

        Map<LocalDate, List<Schedule>> weeklySchedule = new LinkedHashMap<>();

        for (int i = 0; i < 7; ++i) {
            LocalDate currentDate = startDate.plusDays(i);
            String weekType = weekService.getWeekType(currentDate);
            int dayOfWeek = currentDate.getDayOfWeek().getValue();

            List<Schedule> daySchedule = scheduleRepository.findByGroupAndDayAndWeek(group, dayOfWeek, weekType);
            weeklySchedule.put(currentDate, daySchedule);
        }
        return weeklySchedule;
    }
}
