package org.l4ncet.telegrambot.bot.handler.command;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.bot.session.ProposalCreationSession;
import org.l4ncet.telegrambot.entity.Order;
import org.l4ncet.telegrambot.entity.OrderProposal;
import org.l4ncet.telegrambot.entity.OrderStatus;
import org.l4ncet.telegrambot.entity.User;
import org.l4ncet.telegrambot.repository.OrderProposalRepository;
import org.l4ncet.telegrambot.repository.OrderRepository;
import org.l4ncet.telegrambot.repository.UserRepository;
import org.l4ncet.telegrambot.service.OrderProposalService;
import org.l4ncet.telegrambot.service.ProposalSessionService;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProposalMessageHandler {

    private final ProposalSessionService proposalSessionService;
    private final TelegramMessageService telegramMessageService;
    private final OrderProposalService orderProposalService;

    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        ProposalCreationSession session = proposalSessionService.find(telegramId)
                .orElseThrow(() -> new IllegalStateException("Активна сесія не знайдена."));


        if (messageText == null || messageText.trim().isEmpty()) {
            telegramMessageService.sendMessage(chatId, "Повідомлення не може бути порожнім.");
            return;
        }
        try {
            orderProposalService.createProposal(session.getOrderId(), telegramId, messageText);

            proposalSessionService.clear(telegramId);
            telegramMessageService.sendMessage(chatId, "✅ Вашу заявку успішно надіслано.");
        } catch (IllegalArgumentException e) {
            proposalSessionService.clear(telegramId);
            telegramMessageService.sendMessage(chatId, e.getMessage());
        }
    }
}
