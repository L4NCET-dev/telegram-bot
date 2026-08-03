package org.l4ncet.telegrambot.bot.handler.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackHandler {

    boolean supports(String callbackData);

    void handle(Update update);
}
