package org.l4ncet.telegrambot.formatter;

import org.l4ncet.telegrambot.entity.Order;
import org.l4ncet.telegrambot.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class OrderFormatter {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String format(Order order) {
        return """
                %s
                📚 %s 
                <b>%s</b>
                %s
                💰 Ціна: %s
                📅 Термін: %s
                """.formatted(
                    formatStatus(order.getStatus()),
                    escapeHtml(order.getSubject()),
                    escapeHtml(order.getTitle()),
                    escapeHtml(order.getDescription()),
                    formatPrice(order.getPrice()),
                    formatDeadline(order.getDeadline())
                );
    }

    private String formatStatus(OrderStatus status) {
        return switch (status) {
            case ACTIVE -> "🟢 Активний";

            case IN_PROGRESS -> "🟡 В роботі";

            case COMPLETED -> "✅ Завершено";

            case CANCELLED -> "❌ Скасовано";
        };
    }

    private String formatPrice(Long price) {
        if (price == null) {
            return "Договірна";
        }
        return price + " грн";
    }

    private String formatDeadline(LocalDate deadline) {
        if (deadline == null) {
            return "Не вказано";
        }
        return deadline.format(DATE_FORMAT);
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

}
