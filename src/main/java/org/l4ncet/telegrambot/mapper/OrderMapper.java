package org.l4ncet.telegrambot.mapper;

import org.l4ncet.telegrambot.dto.CreateOrderRequestDto;
import org.l4ncet.telegrambot.dto.OrderResponseDto;
import org.l4ncet.telegrambot.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toEntity(CreateOrderRequestDto dto) {
        return Order.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .deadline(dto.getDeadline())
                .build();
    }

    public OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getTitle(),
                order.getDescription(),
                order.getPrice(),
                order.getDeadline(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
