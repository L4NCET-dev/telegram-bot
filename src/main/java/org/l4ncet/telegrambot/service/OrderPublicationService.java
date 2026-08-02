package org.l4ncet.telegrambot.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.bot.keyboard.inline.OrderPublicationKeyboard;
import org.l4ncet.telegrambot.entity.Order;
import org.l4ncet.telegrambot.entity.OrderStatus;
import org.l4ncet.telegrambot.exception.OrderPublicationException;
import org.l4ncet.telegrambot.formatter.OrderFormatter;
import org.l4ncet.telegrambot.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@RequiredArgsConstructor
public class OrderPublicationService {


    private final OrderRepository orderRepository;
    private final OrderFormatter orderFormatter;
    private final OrderPublicationKeyboard orderPublicationKeyboard;
    private final TelegramMessageService telegramMessageService;

    @Value("${telegram.channel.id}")
    private String channelId;


    public void publishOrder(Long orderId) {
        Order order = findOrderById(orderId);

        validateOrderForPublication(order);

        String publicationText = orderFormatter.format(order);

        InlineKeyboardMarkup publicationKeyboard = orderPublicationKeyboard.create(order.getId());

        Message publishedMessage =
                telegramMessageService.sendHtmlMessage(channelId, publicationText, publicationKeyboard);

        saveChannelMessageId(order, publishedMessage.getMessageId());

    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Замовлення с ID " + orderId + " не знайдено"));
    }

    private void validateOrderForPublication(Order order) {
        if (order.getStatus() != OrderStatus.ACTIVE) {
            throw new OrderPublicationException("Не можна опублікувати замовлення с ID %d: статус замовлення – %s"
                    .formatted(order.getId(), order.getStatus()));
        }

        if (order.getChannelMessageId() != null) {
            throw new OrderPublicationException(
                    "Замовлення с ID %d вже опубліковано. channelMessageId: %d"
                            .formatted(order.getId(), order.getChannelMessageId()));
        }
    }

    private void saveChannelMessageId(Order order, Integer channelMessageId) {
        order.setChannelMessageId(channelMessageId);
        orderRepository.save(order);
    }

}
