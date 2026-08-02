package org.l4ncet.telegrambot.repository;

import org.l4ncet.telegrambot.entity.OrderProposal;
import org.l4ncet.telegrambot.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderProposalRepository extends JpaRepository<OrderProposal, Long> {

    boolean existsByOrderIdAndExecutorId(Long orderId, Long executorId);

    Optional<OrderProposal> findByOrderIdAndExecutorId(Long orderId, Long executorId);

    List<OrderProposal>findAllByOrderIdOrderByCreatedAtDesc(Long orderId);

    long countByOrderIdAndStatus(Long orderId, OrderStatus orderStatus);
}
