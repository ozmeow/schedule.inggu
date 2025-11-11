package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class WeekService {
    /**
     * Определяет тип недели для указанной даты
     *
     * @return 'ODD' или 'EVEN'
     */

    public String getWeekType(LocalDate date) {
        LocalDate monday = date.with(DayOfWeek.MONDAY);
        int dayOfMonth = monday.getDayOfMonth();

        // Если день понедельника четный - неделя ЧЕТНАЯ, иначе НЕЧЕТНАЯ
        return (dayOfMonth % 2 == 0) ? "EVEN" : "ODD";
    }

    /**
     * Определяет тип текущей недели
     */
    public String getCurrentWeekType() {
        return getWeekType(LocalDate.now());
    }

    /**
     * Проверяет, подходит ли расписание для текущей недели
     */
    public boolean isScheduleApplicable(String scheduleWeekType, String currentWeekType) {
        return scheduleWeekType.equals("BOTH") ||
                scheduleWeekType.equals(currentWeekType);

    }
}
