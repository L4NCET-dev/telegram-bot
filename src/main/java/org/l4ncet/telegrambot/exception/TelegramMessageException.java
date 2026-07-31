package org.l4ncet.telegrambot.exception;

public class TelegramMessageException extends RuntimeException {

    public TelegramMessageException(String message) {
        super(message);
    }

    public TelegramMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
