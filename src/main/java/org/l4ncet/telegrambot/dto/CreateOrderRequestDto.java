package org.l4ncet.telegrambot.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class CreateOrderRequestDto {
    String title;
    String description;
    Integer price;
    LocalDate deadline;
}
