package org.l4ncet.telegrambot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StatisticsCommandHandler implements  CommandHandler {

    private final TelegramMessageService telegramMessageService;

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();

        telegramMessageService.sendMessage(chatId, "Раздел статистики пока в разработке.");
    }

    @Override
    public boolean supports(String command) {
        return command.equals("📊 Статистика");
    }
}
