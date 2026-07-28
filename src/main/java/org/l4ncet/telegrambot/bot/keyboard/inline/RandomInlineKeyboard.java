package org.l4ncet.telegrambot.bot.keyboard.inline;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class RandomInlineKeyboard {

    public InlineKeyboardMarkup create(){
        InlineKeyboardButton randomButton = InlineKeyboardButton.builder()
                .text("🎲 Сгенерировать число")
                .callbackData("GENERATE_RANDOM")
                .build();

        InlineKeyboardRow row =  new InlineKeyboardRow(randomButton);

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .build();
    }
}
