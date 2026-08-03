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

        registerUser(telegramId, username, firstName);

        if (text.equals(START_COMMAND)) {
            sendMainMenu(chatId, firstName);
            return;
        }
        handleStartPayload(text, telegramId, chatId);
    }


    @Override
    public boolean supports(String command) {
        return command.equals("/start") || command.startsWith(START_COMMAND + " ");
    }

    private void registerUser(Long telegramId, String username, String firstName) {
        CreateUserRequestDto request = new CreateUserRequestDto(telegramId, username, firstName);
        userService.createUser(request);
    }

    private void sendMainMenu(Long chatId, String firstName) {
        telegramMessageService.sendMessage(chatId,
                "Привет, " + firstName + "!\n\nВыбери действие",
                mainMenuReplyKeyboard.create());
    }

    private void handleStartPayload(String text, Long telegramId, Long chatId) {

        String payload = extractPayload(text);

        if (!payload.startsWith(ORDER_PAYLOAD_PREFIX)) {
            telegramMessageService.sendMessage(chatId, "Невідомий параметр запуску бота.");
            return;
        }
        Long orderId = extractOrderId(payload);

        if (orderId == null) {
            telegramMessageService.sendMessage(chatId, "Посилання на замовлення має неправильний формат.");
            return;
        }
        orderProposalStartService.start(orderId, telegramId, chatId);
    }

    private String extractPayload(String text) {
        return text.substring(START_COMMAND.length()).trim();
    }

    private Long extractOrderId(String payload) {
        String orderIdPart = payload.substring(ORDER_PAYLOAD_PREFIX.length());

        if (orderIdPart.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(orderIdPart);
        } catch (NumberFormatException e) {
            return null;
        }

    }
}
