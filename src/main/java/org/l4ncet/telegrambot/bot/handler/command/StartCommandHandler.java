package org.l4ncet.telegrambot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.bot.keyboard.reply.MainMenuReplyKeyboard;
import org.l4ncet.telegrambot.dto.CreateUserRequestDto;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.l4ncet.telegrambot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {

    private final UserService userService;
    private final TelegramMessageService telegramMessageService;
    private final MainMenuReplyKeyboard mainMenuReplyKeyboard;

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        Long chatId = update.getMessage().getChatId();

        CreateUserRequestDto request = new CreateUserRequestDto(telegramId, username, firstName);
        userService.createUser(request);

        telegramMessageService.sendMessage(chatId,
                "Привет, " + firstName + "!\n\nВыбери действие",
                mainMenuReplyKeyboard.create());
    }

    @Override
    public boolean supports(String command) {
        return command.equals("/start");
    }
}
