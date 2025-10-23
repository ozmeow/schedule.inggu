package ru.wzrdmhm.schedulebot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SimpleBot extends TelegramLongPollingBot {

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
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            // Эхо-ответ
            sendMessage(chatId, "Вы сказали: " + messageText);
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
