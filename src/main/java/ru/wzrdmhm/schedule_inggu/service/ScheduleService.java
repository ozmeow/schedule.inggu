package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.model.Schedule;

import java.time.LocalDate;
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
        ecology.setDate(LocalDate.now().toString());
        ecology.setSubject("Экология");
        ecology.setTime("9:00-10:20");
        ecology.setClassroom("412");

        Schedule genetic = new Schedule();
        genetic.setGroupName("Bio-19");
        genetic.setDate(LocalDate.now().toString());
        genetic.setSubject("Генетика");
        genetic.setTime("10:30-11:50");
        genetic.setClassroom("411");

        Schedule rastenia = new Schedule();
        rastenia.setGroupName("Bio-19");
        rastenia.setDate(LocalDate.now().toString());
        rastenia.setSubject("Физиология растений");
        rastenia.setTime("10:30-11:50");
        rastenia.setClassroom("404");

        schedules.add(ecology);
        schedules.add(genetic);
        schedules.add(rastenia);

        Schedule ecologyTomorrow = new Schedule();
        ecologyTomorrow.setGroupName("Bio-19");
        ecologyTomorrow.setDate(LocalDate.now().plusDays(1).toString());
        ecologyTomorrow.setSubject("Экология");
        ecologyTomorrow.setTime("9:00-10:20");
        ecologyTomorrow.setClassroom("412");

        Schedule ecologyTomorrow2 = new Schedule();
        ecologyTomorrow2.setGroupName("Bio-19");
        ecologyTomorrow2.setDate(LocalDate.now().plusDays(1).toString());
        ecologyTomorrow2.setSubject("Экология");
        ecologyTomorrow2.setTime("9:00-10:20");
        ecologyTomorrow2.setClassroom("412");
        ecologyTomorrow2.setTeacher("Туган Юнусович");

        Schedule beps = new Schedule();
        beps.setGroupName("Bio-19");
        beps.setDate(LocalDate.now().plusDays(1).toString());
        beps.setSubject("Экология");
        beps.setTime("9:00-10:20");
        beps.setClassroom("411");

        schedules.add(ecologyTomorrow);
        schedules.add(ecologyTomorrow2);
        schedules.add(beps);

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
