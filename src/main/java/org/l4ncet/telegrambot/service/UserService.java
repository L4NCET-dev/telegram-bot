package org.l4ncet.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.dto.CreateUserRequestDto;
import org.l4ncet.telegrambot.dto.UserResponseDto;
import org.l4ncet.telegrambot.entity.User;
import org.l4ncet.telegrambot.mapper.UserMapper;
import org.l4ncet.telegrambot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request) {
        return userRepository.findByTelegramId(request.getTelegramId())
                .map(userMapper::toResponse)
                .orElseGet(() -> {
                    User user = userMapper.toEntity(request);
                    User savedUser = userRepository.save(user);
                    return userMapper.toResponse(savedUser);
                });
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found " + telegramId));
    }

}
