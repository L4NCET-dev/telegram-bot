package org.l4ncet.telegrambot.bot.handler.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {
    void handle(Update update);

    boolean supports(String command);
}
