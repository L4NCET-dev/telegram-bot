package org.l4ncet.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.entity.User;
import org.l4ncet.telegrambot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(Long telegramId, String username, String firstName) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> {
                    User user = User.builder()
                            .telegramId(telegramId)
                            .username(username)
                            .firstName(firstName)
                            .createdAt(LocalDateTime.now())
                            .build();

                    return userRepository.save(user);
                });
    }
}
