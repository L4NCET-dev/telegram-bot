package org.l4ncet.telegrambot.bot.keyboard.inline;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class ProposalCreationInlineKeyboard {

    private static final String SKIP_PREFIX = "PROPOSAL_SKIP:";
    private static final String CANCEL_PREFIX = "PROPOSAL_CANCEL:";

    public InlineKeyboardMarkup create(Long orderId) {
        InlineKeyboardButton skipButton = InlineKeyboardButton.builder()
                .text("Пропустити")
                .callbackData(SKIP_PREFIX + orderId)
                .build();

        InlineKeyboardButton cancelButton = InlineKeyboardButton.builder()
                .text("Скасувати")
                .callbackData(CANCEL_PREFIX + orderId)
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow(skipButton, cancelButton);

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .build();
    }

}
