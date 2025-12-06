package ru.wzrdmhm.schedulebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.wzrdmhm.schedule_inggu.model.dto.BotResponse;
import ru.wzrdmhm.schedule_inggu.model.dto.CommandRequest;
import ru.wzrdmhm.schedulebot.service.TelegramCommandParser;
import ru.wzrdmhm.schedule_inggu.service.CommandProcessorService;


@Component
public class ScheduleBot extends TelegramLongPollingBot {

    @Autowired
    private TelegramCommandParser telegramCommandParser;

    @Autowired
    private CommandProcessorService commandProcessorService;

    @Override
    public String getBotUsername() {
        return "raspisanie_inggu_bot";
    }

    @Override
    public String getBotToken() {
        return "8436353483:AAH_f0Ac-kSgXl4-MXtjUrBb4ZIQ25MOzos";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                Long chatId = update.getMessage().getChatId();
                Long userId = update.getMessage().getFrom().getId();
                String messageText = update.getMessage().getText();

                System.out.println("📨 Получено сообщение: " + messageText + " от " + userId);

                /*CommandRequest request = telegramCommandParser.parseCommand(messageText);
                request.setUserId(userId);

                BotResponse botResponse = commandProcessorService.commandProcessorService(request);
                sendMessage(chatId, botResponse);
*/

                // 1. Парсим команду
                CommandRequest request = telegramCommandParser.parseCommand(messageText);
                System.out.println("✅ Request создан: " + request);
                System.out.println("✅ CommandType: " + request.getCommandType());

                // 2. Устанавливаем userId
                request.setUserId(userId);
                System.out.println("✅ UserId установлен: " + userId);

                // 3. Обрабатываем команду
                BotResponse botResponse = commandProcessorService.commandProcessorService(request);
                System.out.println("✅ Ответ получен: " + botResponse.getResponse());

                // 4. Отправляем ответ
                sendMessage(chatId, botResponse.getResponse());

            } catch (Exception e) {
                System.err.println("❌ Ошибка обработки сообщения: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
            System.out.println("✅ Ответ отправлен пользователю " + chatId);
        } catch (TelegramApiException e) {
            System.out.println("❌ Ошибка отправки сообщения: " + e.getMessage());
        }
    }
}