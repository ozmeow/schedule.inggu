package ru.wzrdmhm.schedulebot.service;

import ru.wzrdmhm.schedule_inggu.model.dto.CommandRequest;
import ru.wzrdmhm.schedule_inggu.model.dto.CommandType;
import org.springframework.stereotype.Service;

@Service
public class TelegramCommandParser {

    public CommandRequest parseCommand(String text) {
        String lowerText = text.toLowerCase().trim();

        if (lowerText.startsWith("/start") ||
                lowerText.contains("/cтарт")) {
            return new CommandRequest(CommandType.START);

        } else if (lowerText.startsWith("/help") ||
                lowerText.contains("/помощь")) {
            return new CommandRequest(CommandType.HELP);

        } else if (lowerText.startsWith("/now") ||
                lowerText.contains("/сейчас")) {
            return new CommandRequest(CommandType.NOW_SCHEDULE);

        } else if (lowerText.startsWith("/today") ||
                lowerText.contains("/сегодня")) {
            CommandRequest commandRequest = new CommandRequest(CommandType.TODAY_SCHEDULE);
            commandRequest.addParameters("date", "today");
            return commandRequest;

        } else if (lowerText.startsWith("/tomorrow") ||
                lowerText.contains("/завтра")) {
            CommandRequest commandRequest = new CommandRequest(CommandType.TOMORROW_SCHEDULE);
            commandRequest.addParameters("date", "tomorrow");
            return commandRequest;

        } else if (lowerText.startsWith("/week") ||
                lowerText.contains("/неделя")) {
            return new CommandRequest(CommandType.WEEK_SCHEDULE);

        } else if (lowerText.startsWith("/group")) {
            System.out.println("✅ Определил как GROUP команда");

            // Убираем команду, оставляем только параметр
            String afterCommand = text.substring("/group".length()).trim();
            System.out.println("🔍 После команды: '" + afterCommand + "'");

            if (afterCommand.isEmpty()) {
                System.out.println("🔍 Без параметра → SHOW_GROUPS");
                return new CommandRequest(CommandType.SHOW_GROUPS);
            } else {
                System.out.println("🔍 С параметром '" + afterCommand + "' → SET_GROUP");
                CommandRequest request = new CommandRequest(CommandType.SET_GROUP);
                request.addParameters("groupCode", afterCommand);
                return request;
            }
        } else {
            return new CommandRequest(CommandType.UNKNOWN);
        }
    }
}
