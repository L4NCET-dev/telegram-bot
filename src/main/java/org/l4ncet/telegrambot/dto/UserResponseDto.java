package org.l4ncet.telegrambot.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class UserResponseDto {
    Long id;
    Long telegramId;
    String username;
    String firstName;
    LocalDateTime createdAt;
}
