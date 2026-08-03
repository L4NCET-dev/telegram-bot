package org.l4ncet.telegrambot.bot.handler.callback;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.bot.session.ProposalCreationSession;
import org.l4ncet.telegrambot.service.OrderProposalService;
import org.l4ncet.telegrambot.service.ProposalSessionService;
import org.l4ncet.telegrambot.service.TelegramMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ProposalSkipCallbackHandler implements CallbackHandler {

    private static final String CALLBACK_PREFIX = "PROPOSAL_SKIP:";

    private final ProposalSessionService proposalSessionService;
    private final OrderProposalService orderProposalService;
    private final TelegramMessageService telegramMessageService;


    @Override
    public boolean supports(String callbackData) {
        return callbackData.startsWith(CALLBACK_PREFIX);
    }

    @Override
    public void handle(Update update) {
        String callbackQueryId = update.getCallbackQuery().getId();

        Long telegramId = update.getCallbackQuery().getFrom().getId();

        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        String callbackData = update.getCallbackQuery().getData();

        Long callbackOrderId = extractOrderId(callbackData);

        telegramMessageService.answerCallbackQuery(callbackQueryId);

        if (callbackOrderId == null) {
            telegramMessageService.sendMessage(chatId, "Не вдалося визначити замовлення.");
            return;
        }
        ProposalCreationSession session = proposalSessionService.find(telegramId)
                .orElse(null);
        if (session == null) {
            telegramMessageService.sendMessage(chatId, "Сесію подання заявки вже завершено.");
            return;
        }
        if (!session.getOrderId().equals(callbackOrderId)) {
            telegramMessageService.sendMessage(chatId, "Ця кнопка належить до неактуальної заявки.");
            return;
        }

        try {
            orderProposalService.createProposal(session.getOrderId(), telegramId, null);
            proposalSessionService.clear(telegramId);

            telegramMessageService.sendMessage(chatId, "✅ Заявку без коментаря успішно надіслано.");
        } catch (IllegalStateException e) {
            proposalSessionService.clear(telegramId);

            telegramMessageService.sendMessage(chatId, e.getMessage());
        }

    }

    private Long extractOrderId(String callbackData) {

        String orderIdPart = callbackData.substring(CALLBACK_PREFIX.length());

        if (orderIdPart.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(orderIdPart);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
