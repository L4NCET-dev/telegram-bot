package org.l4ncet.telegrambot.bot;

import org.l4ncet.telegrambot.bot.handler.callback.CallbackHandler;
import org.l4ncet.telegrambot.bot.handler.command.*;
import org.l4ncet.telegrambot.service.ProposalSessionService;
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
    private final List<CallbackHandler> callbackHandlers;
    private final ProposalSessionService proposalSessionService;
    private final ProposalMessageHandler proposalMessageHandler;

    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                       List<CommandHandler> commandHandlers,
                       List<CallbackHandler> callbackHandlers,
                       ProposalMessageHandler proposalMessageHandler,
                       ProposalSessionService proposalSessionService) {

        this.botToken = botToken;
        this.commandHandlers = commandHandlers;
        this.callbackHandlers = callbackHandlers;
        this.proposalMessageHandler = proposalMessageHandler;
        this.proposalSessionService = proposalSessionService;
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
        String text = update.getMessage().getText();

        if(proposalSessionService.exists(telegramId) && !text.startsWith("/")){
            proposalMessageHandler.handle(update);
            return;
        }
        commandHandlers.stream()
                .filter(handler -> handler.supports(text))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }

    private void handleCallbackQuery(Update update) {

        String callbackData = update.getCallbackQuery().getData();

        callbackHandlers.stream()
                .filter(handler -> handler.supports(callbackData))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }
}
