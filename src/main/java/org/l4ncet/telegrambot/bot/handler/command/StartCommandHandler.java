package org.l4ncet.telegrambot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.bot.keyboard.reply.MainMenuReplyKeyboard;
import org.l4ncet.telegrambot.dto.CreateUserRequestDto;
import org.l4ncet.telegrambot.service.OrderProposalStartService;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.l4ncet.telegrambot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {

    private static final String START_COMMAND = "/start";
    private static final String ORDER_PAYLOAD_PREFIX = "order_";

    private final UserService userService;
    private final TelegramMessageService telegramMessageService;
    private final MainMenuReplyKeyboard mainMenuReplyKeyboard;
    private final OrderProposalStartService orderProposalStartService;

    @Override
    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

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
