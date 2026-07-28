package org.l4ncet.telegrambot.bot.keyboard.reply;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class MainMenuReplyKeyboard {

    public ReplyKeyboardMarkup create() {

        KeyboardButton randomButton = new KeyboardButton("🎲 Случайное число");
        KeyboardButton profileButton = new KeyboardButton("👤 Профиль");
        KeyboardButton statisticsButton = new KeyboardButton("📊 Статистика");
        KeyboardButton settingsButton = new KeyboardButton("⚙️ Настройки");

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(randomButton);
        firstRow.add(profileButton);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(statisticsButton);
        secondRow.add(settingsButton);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(firstRow, secondRow))
                .resizeKeyboard(true)
                .build();
    }
}
