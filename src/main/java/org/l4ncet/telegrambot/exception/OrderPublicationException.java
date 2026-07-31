package org.l4ncet.telegrambot.exception;

public class OrderPublicationException extends RuntimeException {
    public OrderPublicationException(String message) {
        super(message);
    }

    public OrderPublicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
