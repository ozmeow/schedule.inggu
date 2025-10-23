package ru.wzrdmhm.schedule_inggu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wzrdmhm.schedule_inggu.model.Schedule;
import ru.wzrdmhm.schedule_inggu.model.ScheduleRequest;
import ru.wzrdmhm.schedule_inggu.model.ScheduleResponse;
import ru.wzrdmhm.schedule_inggu.service.ScheduleService;
import ru.wzrdmhm.schedule_inggu.service.UserService;
import ru.wzrdmhm.schedule_inggu.model.User;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/schedule")
    public ScheduleResponse getSchedule(@RequestBody ScheduleRequest request) {
        User user = userService.findOrCreateUser(request.getUserId(), "User");
        String groupName = user.getGroupName();
        List<Schedule> todaySchedule = scheduleService.getScheduleForGroup(groupName, "20.10.2025");
        ScheduleResponse response = new ScheduleResponse();
        response.setResponse(formatSchedule(todaySchedule));
        response.setSuccess(true);
        return response;
    }

    private String formatSchedule(List<Schedule> schedules) {
        if(schedules.isEmpty()) {
            return "Сала1 хьейн, тахан параш дац ❌";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\uD83D\uDCDA Расписание на сегодня:\n");

        for (int i = 0; i < schedules.size(); i++) {
            Schedule s = schedules.get(i);
            sb.append(i + 1).append(". ").append(s.getTime()).append(" - ").append(s.getSubject()).append(" (").append(
            s.getClassroom()).append(")\n");
        }
        return sb.toString();
    }

    @PostMapping("/user/setgroup")
    public String setUserGroup(@RequestBody Map<String, String> request) {
        Long userId = Long.parseLong(request.get("userId"));
        String groupName = request.get("groupName");

        userService.setUserGroup(userId, groupName);
        return "Группа установлена: " + groupName;
    }
}
