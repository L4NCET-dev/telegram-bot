package org.l4ncet.telegrambot.formatter;

import org.l4ncet.telegrambot.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class ProposalRequestFormatter {

    public String format (Order order){
        return """
                Ви подаєте заявку на замовлення №%d.
                
                Напишіть орієнтовну вартість на коментар або натисніть "Пропустити", ваша заявка сформується автоматично
                
                Наприклад:
                Виконаю за 400 грн протягом двох днів.
                """.formatted(order.getId());
    }
}
