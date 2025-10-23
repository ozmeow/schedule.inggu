package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.model.Schedule;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {
    private List<Schedule> schedules = new ArrayList<>();

    public ScheduleService() {
        initializeTestData();
    }

    public void initializeTestData() {
        Schedule ecology = new Schedule();
        ecology.setGroupName("Bio-19");
        ecology.setDate("20.10.2025");
        ecology.setSubject("Экология");
        ecology.setTime("9:00-10:20");
        ecology.setClassroom("412");

        Schedule genetic = new Schedule();
        genetic.setGroupName("Bio-19");
        genetic.setDate("20.10.2025");
        genetic.setSubject("Генетика");
        genetic.setTime("10:30-11:50");
        genetic.setClassroom("411");

        Schedule rastenia = new Schedule();
        rastenia.setGroupName("Bio-19");
        rastenia.setDate("20.10.2025");
        rastenia.setSubject("Физиология растений");
        rastenia.setTime("10:30-11:50");
        rastenia.setClassroom("404");

        schedules.add(ecology);
        schedules.add(genetic);
        schedules.add(rastenia);
    }

    public List<Schedule> getScheduleForGroup(String groupName, String date) {
        List<Schedule> result = new ArrayList<>();

        for (Schedule schedule : schedules) {
            if (schedule.getGroupName().equals(groupName) && schedule.getDate().equals(date)) {
                result.add(schedule);
            }
        }
        return result;
    }
}
