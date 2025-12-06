package ru.wzrdmhm.schedulebot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.wzrdmhm.schedulebot.ScheduleBot;


@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(ScheduleBot scheduleBot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(scheduleBot);
            System.out.println("✅ Бот успешно зарегистрирован в Telegram API!");
            return botsApi;
        } catch (TelegramApiException e) {
            System.out.println("❌ Ошибка регистрации бота: " + e.getMessage());
            throw new RuntimeException("Ошибка регистрации бота", e);
        }
    }
}