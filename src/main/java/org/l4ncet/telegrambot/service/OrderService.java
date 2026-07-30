package org.l4ncet.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.l4ncet.telegrambot.dto.CreateOrderRequestDto;
import org.l4ncet.telegrambot.dto.OrderResponseDto;
import org.l4ncet.telegrambot.entity.Order;
import org.l4ncet.telegrambot.mapper.OrderMapper;
import org.l4ncet.telegrambot.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        Order order = orderMapper.toEntity(requestDto);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponseDto(savedOrder);
    }
}
