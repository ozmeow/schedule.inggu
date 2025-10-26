/* package ru.wzrdmhm.schedulebot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(SimpleBot simpleBot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(simpleBot);
            System.out.println("✅ Бот успешно зарегистрирован в Telegram API!");
            return botsApi;
        } catch (TelegramApiException e) {
            System.out.println("❌ Ошибка регистрации бота: " + e.getMessage());
            throw new RuntimeException("Ошибка регистрации бота", e);
        }
    }
}
*/