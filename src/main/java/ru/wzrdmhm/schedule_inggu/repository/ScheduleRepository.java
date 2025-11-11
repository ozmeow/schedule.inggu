package ru.wzrdmhm.schedule_inggu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.wzrdmhm.schedule_inggu.model.Schedule;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE " +
            "s.groupName = :groupName AND " +
            "s.dayOfWeek = :dayOfWeek AND " +
            "(s.weekType = 'BOTH' OR s.weekType = :weekType) " +
            "ORDER BY s.time")
    List<Schedule> findByGroupAndDayAndWeek(
            @Param("groupName") String groupName,
            @Param("dayOfWeek") int DayOfWeek,
            @Param("weekType") String weekType);

    // Для команды /now
    @Query("SELECT s FROM Schedule s WHERE " +
            "s.groupName = :groupName AND " +
            "s.dayOfWeek = :dayOfWeek " +
            "ORDER BY s.time")
    List<Schedule> findByGroupNameAndDayOfWeek(
            @Param("groupName") String groupName,
            @Param("dayOfWeek") int dayOfWeek);
}