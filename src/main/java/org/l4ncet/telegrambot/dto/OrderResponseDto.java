package org.l4ncet.telegrambot.dto;

import lombok.Value;
import org.l4ncet.telegrambot.entity.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
public class OrderResponseDto {
    Long id;
    String title;
    String description;
    Integer price;
    LocalDate deadline;
    OrderStatus status;
    LocalDateTime createdAt;
}
