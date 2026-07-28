package org.l4ncet.telegrambot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.service.RandomService;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class RandomCommandHandler implements CommandHandler {

    private final RandomService randomService;
    private final TelegramMessageService telegramMessageService;

    @Override
    public void handle(Update update) {

        Long chatId = update.getMessage().getChatId();
        int randomNumber = randomService.generate();
        telegramMessageService.sendMessage(chatId, "🎲 Твоё случайное число от 1 до 2: " + randomNumber);
    }

    @Override
    public boolean supports(String command) {
        return command.equals("🎲 Случайное число");
    }
}
