package ru.wzrdmhm.schedule_inggu.service;

import org.springframework.stereotype.Service;
import ru.wzrdmhm.schedule_inggu.model.Schedule;

import java.time.DayOfWeek;
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
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        Schedule genetic = new Schedule();
        genetic.setGroupName("Bio-19");
        genetic.setDate(monday.toString());
        genetic.setSubject("Генетика животных");
        genetic.setTime("12:20-13:40");
        genetic.setClassroom("411");
        genetic.setTeacher("Дзармотова З.И.");

        Schedule genetic2 = new Schedule();
        genetic2.setGroupName("Bio-19");
        genetic2.setDate(monday.toString());
        genetic2.setSubject("Генетика животных");
        genetic2.setTime("13:50-15:10");
        genetic2.setClassroom("411");
        genetic2.setTeacher("Плиева А.М.");

        Schedule ecologyTomorrow = new Schedule();
        ecologyTomorrow.setGroupName("Bio-19");
        ecologyTomorrow.setDate(monday.plusDays(2).toString());
        ecologyTomorrow.setSubject("Экология человека");
        ecologyTomorrow.setTime("09:00-10:20");
        ecologyTomorrow.setClassroom("412");
        ecologyTomorrow.setTeacher("Точиев Т.Ю.");

        Schedule ecologyTomorrow2 = new Schedule();
        ecologyTomorrow2.setGroupName("Bio-19");
        ecologyTomorrow2.setDate(monday.plusDays(2).toString());
        ecologyTomorrow2.setSubject("Экология человека");
        ecologyTomorrow2.setTime("10:30-11:50");
        ecologyTomorrow2.setClassroom("412");
        ecologyTomorrow2.setTeacher("Точиев Т.Ю.");

        Schedule rastenia = new Schedule();
        rastenia.setGroupName("Bio-19");
        rastenia.setDate(monday.plusDays(2).toString());
        rastenia.setSubject("Физиология растений");
        rastenia.setTime("12:20-13:40");
        rastenia.setClassroom("404");
        rastenia.setTeacher("Хашиева Л.Д.");


        Schedule beps = new Schedule();
        beps.setGroupName("Bio-19");
        beps.setDate(monday.plusDays(2).toString());
        beps.setSubject("БЭПС");
        beps.setTime("13:50-15:10");
        beps.setClassroom("412");
        beps.setTeacher("Дзармотова З.И");

        Schedule beps2 = new Schedule();
        beps2.setGroupName("Bio-19");
        beps2.setDate(monday.plusDays(4).toString());
        beps2.setSubject("БЭПС");
        beps2.setTime("09:00-10:20");
        beps2.setClassroom("412");
        beps2.setTeacher("Дзармотова З.И");

        Schedule beps3 = new Schedule();
        beps2.setGroupName("Bio-19");
        beps2.setDate(monday.plusDays(4).toString());
        beps2.setSubject("БЭПС");
        beps2.setTime("10:30-11:50");
        beps2.setClassroom("412");
        beps2.setTeacher("Дзармотова З.И");

        Schedule ochp = new Schedule();
        ochp.setGroupName("Bio-19");
        ochp.setDate(monday.plusDays(4).toString());
        ochp.setSubject("ОЧП");
        ochp.setTime("12:20-13:40");
        ochp.setClassroom("412");
        ochp.setTeacher("Дзармотова З.И");

        Schedule genetic3 = new Schedule();
        genetic3.setGroupName("Bio-19");
        genetic3.setDate(monday.plusDays(4).toString());
        genetic3.setSubject("Генетика животных");
        genetic3.setTime("13:50-15:10");
        genetic3.setClassroom("411");
        genetic3.setTeacher("Плиева А.М.");

        schedules.add(genetic);
        schedules.add(genetic2);

        schedules.add(ecologyTomorrow);
        schedules.add(ecologyTomorrow2);
        schedules.add(rastenia);
        schedules.add(beps);

        schedules.add(beps2);
        schedules.add(beps3);
        schedules.add(ochp);
        schedules.add(genetic3);

    }

    public List<Schedule> getScheduleForGroup(String groupName, String date) {
        List<Schedule> result = new ArrayList<>();

        for (Schedule schedule : schedules) {

            if (schedule.getGroupName() != null &&
                    schedule.getGroupName().equals(groupName) &&
                    schedule.getDate() != null &&
                    schedule.getDate().equals(date) &&
                    schedule.getGroupName().equals(groupName) &&
                    schedule.getDate().equals(date)
            ) {
                result.add(schedule);
            }
        }
        return result;
    }
}
