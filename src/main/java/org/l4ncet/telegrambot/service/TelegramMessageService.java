package org.l4ncet.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.l4ncet.telegrambot.exception.TelegramMessageException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
public class TelegramMessageService {

    private final TelegramClient telegramClient;

    public Message sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        return execute(message);
    }

    public Message sendMessage(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();

        return execute(message);
    }

    public Message sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();

        return execute(message);
    }

    public Message sendHtmlMessage(String chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("HTML")
                .replyMarkup(keyboard)
                .build();

        return execute(message);
    }

    public void answerCallbackQuery(String callbackQueryId) {
        AnswerCallbackQuery answer= AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .build();

        try {
            telegramClient.execute(answer);
        }catch (TelegramApiException e) {
            throw new TelegramMessageException("Не удалось подтвердить обработку callback-запроса", e);
        }
    }

    private Message execute(SendMessage message){
        try {
           return telegramClient.execute(message);
        } catch (TelegramApiException exception) {
            throw new TelegramMessageException(
                    "Не вдалось відправити повідомлення в Telegram-чат: " + message.getChatId(), exception);
        }
    }
}
