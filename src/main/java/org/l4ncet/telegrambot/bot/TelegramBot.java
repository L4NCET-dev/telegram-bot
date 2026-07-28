package org.l4ncet.telegrambot.bot;

import org.l4ncet.telegrambot.bot.handler.command.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final String botToken;
    private final List<CommandHandler> commandHandlers;

    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                       List<CommandHandler> commandHandlers
                       ) {
        this.botToken = botToken;
        this.commandHandlers = commandHandlers;
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

//        if (update.hasCallbackQuery()) {
//            handleCallbackQuery(update);
//        }
    }

    private void handleMessage(Update update) {
        String text = update.getMessage().getText();

        commandHandlers.stream()
                .filter(handler -> handler.supports(text))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }

//    private void handleCallbackQuery(Update update) {
//
//        String callbackData = update.getCallbackQuery().getData();
//
//        Long chatId = update.getCallbackQuery().getMessage().getChatId();
//
//        if (callbackData.equals("GENERATE_RANDOM")) {
//            int randomNumber = randomService.generate();
//
//            telegramMessageService.sendMessage(chatId, "🎲 Твоё случайное число от 1 до 2: " + randomNumber);
//        }
//    }
}
