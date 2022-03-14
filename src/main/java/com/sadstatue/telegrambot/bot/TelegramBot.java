package com.sadstatue.telegrambot.bot;

import com.sadstatue.telegrambot.service.BotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramWebhookBot {

    @Value("${bot.botToken}")
    private String botToken;

    @Value("${bot.botPath}")
    private String botPath;

    @Value("${bot.botUserName}")
    private String botUserName;

    private BotService botService;

    public TelegramBot(BotService botService) {
        this.botService = botService;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        return botService.checkUpdate(update);

    }

    @Override
    public String getBotPath() {
        return botPath;
    }
}
