package org.l4ncet.telegrambot.bot.keyboard.inline;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class OrderPublicationKeyboard {

    private final String botUsername;

    public OrderPublicationKeyboard(@Value("${telegram.bot.username}") String botUsername) {
        this.botUsername = botUsername;
    }

    public InlineKeyboardMarkup create(Long orderId){
        String orderUrl = buildOrderUrl(orderId);

        InlineKeyboardButton takeOrderButton = InlineKeyboardButton.builder()
                .text("📩 Взяти замовлення")
                .url(orderUrl)
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(takeOrderButton);
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .build();
    }

    private String buildOrderUrl(Long orderId){
        return "https://t.me/%s?start=order_%d"
                .formatted(botUsername, orderId);
    }
}
