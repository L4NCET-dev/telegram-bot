package org.l4ncet.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.entity.Order;
import org.l4ncet.telegrambot.entity.OrderProposal;
import org.l4ncet.telegrambot.entity.OrderStatus;
import org.l4ncet.telegrambot.entity.User;
import org.l4ncet.telegrambot.repository.OrderProposalRepository;
import org.l4ncet.telegrambot.repository.OrderRepository;
import org.l4ncet.telegrambot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProposalService {

    private final OrderProposalRepository orderProposalRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderProposal createProposal(Long orderId, Long executorTelegramId, String message) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new IllegalStateException("Замовлення з ID " + orderId + "не знайдено"));

        if (order.getStatus() != OrderStatus.ACTIVE) {
            throw new IllegalStateException("Замовлення вже недоступне для нових заявок");
        }

        if(order.getCustomerTelegramId().equals(executorTelegramId)) {
            throw new IllegalStateException("Не можна подати заявку на власне замовлення");
        }

        User executor = userRepository.findByTelegramId(executorTelegramId)
                .orElseThrow(()-> new IllegalStateException("Користувача не знайдено"));

        boolean proposalExists = orderProposalRepository.existsByOrderIdAndExecutorId(orderId, executor.getId());

        if(proposalExists) {
            throw new IllegalStateException("Ви вже подали заявку на це замовлення");
        }

        OrderProposal proposal = OrderProposal.builder()
                .order(order)
                .executor(executor)
                .message(normalizeMessage(message))
                .build();

        return orderProposalRepository.save(proposal);
    }

    private String normalizeMessage(String message) {
        if (message == null) {
            return null;
        }
        String normalized = message.trim();

        return normalized.isEmpty() ? null : normalized;
    }
}
