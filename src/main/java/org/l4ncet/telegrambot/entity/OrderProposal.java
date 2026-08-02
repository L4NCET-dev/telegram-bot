package org.l4ncet.telegrambot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "order_proposals", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_order_executor",
                columnNames = {
                        "order_id", "executor_id"
                }
        )
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "executor_id", nullable = false)
    private User executor;

    @Column(columnDefinition = "TEXT")
    private String message;

    private Long proposedPrice;

    private Long agreedPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = ProposalStatus.PENDING;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
