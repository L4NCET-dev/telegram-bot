package org.l4ncet.telegrambot.entity;

public enum ProposalStatus {

    PENDING, // В ОЖИДАНИИ
    DISCUSSION, // ОБСУЖДЕНИЕ
    PRICE_CONFIRMATION_PENDING, // ОЖИДАЕТСЯ ПОДТВЕРЖДЕНИЕ ЦЕНЫ
    PRICE_AGREED, // ЦЕНА СОГЛАСОВАНА
    SELECTED, // ИЗБРАННОЕ
    REJECTED, // ОТКЛОНЕННЫЙ
    WITHDRAWN, // ОТОЗВАНО
    EXPIRED // ИСТЕКШИЙ
}
