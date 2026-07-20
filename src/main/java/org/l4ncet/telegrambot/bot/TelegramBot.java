package org.l4ncet.telegrambot.bot;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.bot.keyboard.MainMenuKeyboard;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.l4ncet.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramMessageService telegramMessageService;
    private final UserService userService;
    private final String botToken;
    private final MainMenuKeyboard mainMenuKeyboard;

    public TelegramBot(UserService userService,
                       @Value("${telegram.bot.token}") String botToken,
                       TelegramMessageService telegramMessageService,
                       MainMenuKeyboard mainMenuKeyboard) {
        this.userService = userService;
        this.botToken = botToken;
        this.telegramMessageService = telegramMessageService;
        this.mainMenuKeyboard = mainMenuKeyboard;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingSingleThreadUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long telegramId = update.getMessage().getFrom().getId();
            String username = update.getMessage().getFrom().getUserName();
            String firstName = update.getMessage().getFrom().getFirstName();
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (text.equals("/start")) {
                userService.registerUser(
                        telegramId,
                        username,
                        firstName
                );

                telegramMessageService.sendMessage(chatId,
                        "Привет, " + firstName + "!\n\nВыбери действие",
                        mainMenuKeyboard.create());

            }
        }
    }
}
