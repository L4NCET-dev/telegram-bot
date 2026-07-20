package org.l4ncet.telegrambot.bot;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.bot.keyboard.MainMenuKeyboard;
import org.l4ncet.telegrambot.bot.keyboard.MainReplyKeyboard;
import org.l4ncet.telegrambot.service.RandomService;
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
    private final MainReplyKeyboard mainReplyKeyboard;
    private final RandomService randomService;

    public TelegramBot(UserService userService,
                       @Value("${telegram.bot.token}") String botToken,
                       TelegramMessageService telegramMessageService,
                       MainMenuKeyboard mainMenuKeyboard,
                       RandomService randomService,
                       MainReplyKeyboard mainReplyKeyboard) {

        this.userService = userService;
        this.botToken = botToken;
        this.telegramMessageService = telegramMessageService;
        this.mainMenuKeyboard = mainMenuKeyboard;
        this.mainReplyKeyboard = mainReplyKeyboard;
        this.randomService = randomService;
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
            handleMessage(update);
        }

        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    private void handleMessage(Update update) {
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
                    mainReplyKeyboard.create());
            return;
        }

        if (text.equals("🎲 Случайное число")) {

            int randomNumber = randomService.generate();
            telegramMessageService.sendMessage(chatId, "🎲 Твоё случайное число от 1 до 2: " + randomNumber);
            return;
        }

        if (text.equals("👤 Профиль")) {

            telegramMessageService.sendMessage(chatId, "Раздел профиля пока в разработке.");
            return;
        }

        if (text.equals("📊 Статистика")) {

            telegramMessageService.sendMessage(chatId, "Раздел статистики пока в разработке.");
            return;
        }

        if (text.equals("⚙️ Настройки")) {

            telegramMessageService.sendMessage(chatId, "Раздел настроек пока в разработке.");

        }


    }

    private void handleCallbackQuery(Update update) {

        String callbackData = update.getCallbackQuery().getData();

        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (callbackData.equals("GENERATE_RANDOM")) {
            int randomNumber = randomService.generate();

            telegramMessageService.sendMessage(chatId, "🎲 Твоё случайное число от 1 до 2: " + randomNumber);
        }
    }
}
