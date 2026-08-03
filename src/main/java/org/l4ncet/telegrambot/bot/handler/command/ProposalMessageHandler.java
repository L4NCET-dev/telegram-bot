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
import org.l4ncet.telegrambot.service.ProposalSessionService;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProposalMessageHandler {

    private final ProposalSessionService proposalSessionService;
    private final OrderRepository orderRepository;
    private final TelegramMessageService telegramMessageService;
    private final UserRepository userRepository;
    private final OrderProposalRepository orderProposalRepository;

    public void handle(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        ProposalCreationSession session = proposalSessionService.find(telegramId)
                .orElseThrow(() -> new IllegalStateException("Активна сесія подання заявк не знайдена."));

        Order order = orderRepository.findById(session.getOrderId())
                .orElse(null);

        if (order == null) {
            proposalSessionService.clear(telegramId);

            telegramMessageService.sendMessage(chatId, "Замовлення більше не існує.");
            return;
        }

        if (order.getStatus() != OrderStatus.ACTIVE) {
            proposalSessionService.clear(telegramId);

            telegramMessageService.sendMessage(chatId, "Замовлення вже недоступне для нових заявок.");
            return;
        }

        User executor = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new IllegalStateException("Користувача з Telegram ID " + telegramId + "не знайдено"));

        if (orderProposalRepository.existsByOrderIdAndExecutorId(order.getId(), executor.getId())) {
            proposalSessionService.clear(telegramId);

            telegramMessageService.sendMessage(chatId, "Ви вже подали заявку на це замовлення.");
            return;
        }

        String normalizedMessage = normalizeMessage(messageText);

        if (normalizedMessage == null) {
            telegramMessageService.sendMessage(chatId, "Повідомлення не може бути порожнім."
                    + "Напишіть вашу пропозицію або натисніть " + "Пропустити");
            return;
        }

        OrderProposal proposal = OrderProposal.builder()
                .order(order)
                .executor(executor)
                .message(normalizedMessage)
                .build();

        orderProposalRepository.save(proposal);

        proposalSessionService.clear(telegramId);

        telegramMessageService.sendMessage(chatId, "Вашу заявку на замовлення №"
                + order.getId() + " успішно надіслано.");
    }

    private String normalizeMessage(String message) {
        if (message == null) {
            return null;
        }

        String normalized = message.trim();

        if (normalized.isEmpty()) {
            return null;
        }

        return normalized;
    }
}
