package org.l4ncet.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.bot.keyboard.inline.ProposalCreationInlineKeyboard;
import org.l4ncet.telegrambot.entity.Order;
import org.l4ncet.telegrambot.entity.OrderStatus;
import org.l4ncet.telegrambot.entity.User;
import org.l4ncet.telegrambot.formatter.ProposalRequestFormatter;
import org.l4ncet.telegrambot.repository.OrderProposalRepository;
import org.l4ncet.telegrambot.repository.OrderRepository;
import org.l4ncet.telegrambot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProposalStartService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TelegramMessageService telegramMessageService;
    private final OrderProposalRepository orderProposalRepository;
    private final ProposalRequestFormatter proposalRequestFormatter;
    private final ProposalCreationInlineKeyboard proposalCreationInlineKeyboard;
    private final ProposalSessionService proposalSessionService;

    public void start(Long orderId, Long executorTelegramId, Long chatId) {
        Order order = findOrder(orderId);

        if (!isOrderAvailable(order, chatId)) {
            return;
        }

        User executor = findExecutor(executorTelegramId);

        if (isOrderOwner(order, executorTelegramId)) {
            telegramMessageService.sendMessage(chatId, "Ви не можете подати заявку на власне замовлення");
            return;
        }

        if (proposalAlreadyExists(order, executor)) {
            telegramMessageService.sendMessage(chatId, "Ви вже подали заявку на це замовлення");
            return;
        }

        proposalSessionService.start(executorTelegramId, order.getId());

        sendProposalRequest(chatId, order);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElse(null);
    }

    private User findExecutor(Long executorTelegramId) {
        return userRepository.findByTelegramId(executorTelegramId)
                .orElseThrow(() -> new IllegalStateException(
                        "Користувача з Telegram ID " + executorTelegramId + " не знайдено"));
    }

    private boolean isOrderAvailable(Order order, Long chatId) {
        if (order == null) {
            telegramMessageService.sendMessage(chatId, "Замовлення не знайдено");
            return false;
        }

        if (order.getStatus() != OrderStatus.ACTIVE) {
            telegramMessageService.sendMessage(chatId, "Це замовлення вже недоступне для нових заявок");
            return false;
        }
        return true;
    }

    private boolean isOrderOwner(Order order, Long executorTelegramId) {
        return order.getCustomerTelegramId().equals(executorTelegramId);
    }

    private boolean proposalAlreadyExists(Order order, User executor) {
        return orderProposalRepository.existsByOrderIdAndExecutorId(order.getId(), executor.getId());
    }

    private void sendProposalRequest(Long chatId, Order order) {
        telegramMessageService.sendMessage(chatId,
                proposalRequestFormatter.format(order),
                proposalCreationInlineKeyboard.create(order.getId()));

    }

}
