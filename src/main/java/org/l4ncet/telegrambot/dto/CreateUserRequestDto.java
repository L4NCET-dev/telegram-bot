package org.l4ncet.telegrambot.dto;

import lombok.Value;

@Value
public class CreateUserRequestDto {
    Long telegramId;
    String username;
    String firstName;
}
