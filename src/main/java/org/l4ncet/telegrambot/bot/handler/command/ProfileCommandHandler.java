package org.l4ncet.telegrambot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.dto.UserResponseDto;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.l4ncet.telegrambot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ProfileCommandHandler implements CommandHandler {

    private final UserService userService;
    private final TelegramMessageService telegramMessageService;

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();

        UserResponseDto user = userService.getUserByTelegramId(telegramId);

        String username = user.getUsername() != null
                ? "@" + user.getUsername()
                : "не указан";

        String profileText = """
                👤 Профиль
                
                ID: %d
                Telegram ID: %d
                Имя: %s
                Username: %s
                Дата регистрации: %s
                """.formatted(
                user.getId(),
                user.getTelegramId(),
                user.getFirstName(),
                username,
                user.getCreatedAt()
        );

        telegramMessageService.sendMessage(chatId, profileText);
    }

    @Override
    public boolean supports(String command) {
        return command.equals("👤 Профиль");
    }


}
