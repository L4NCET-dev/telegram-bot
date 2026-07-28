package org.l4ncet.telegrambot.mapper;

import org.l4ncet.telegrambot.dto.CreateUserRequestDto;
import org.l4ncet.telegrambot.dto.UserResponseDto;
import org.l4ncet.telegrambot.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public User toEntity(CreateUserRequestDto dto) {
        return User.builder()
                .telegramId(dto.getTelegramId())
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserResponseDto toResponse(User user) {

        return new UserResponseDto(
                user.getId(),
                user.getTelegramId(),
                user.getUsername(),
                user.getFirstName(),
                user.getCreatedAt()
        );
    }
}
