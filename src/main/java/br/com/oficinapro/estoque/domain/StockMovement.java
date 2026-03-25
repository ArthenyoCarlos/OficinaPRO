package br.com.oficinapro.estoque.domain;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.estoque.domain.enums.StockMovementOriginType;
import br.com.oficinapro.estoque.domain.enums.StockMovementType;
import br.com.oficinapro.produto.domain.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_stock_movement")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private StockMovementType movementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin_type", nullable = false)
    private StockMovementOriginType originType;

    @Column(name = "origin_id")
    private UUID originId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "previous_balance", nullable = false)
    private Integer previousBalance;

    @Column(name = "later_balance", nullable = false)
    private Integer laterBalance;

    @Column(name = "unit_cost", precision = 19, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moved_by")
    private User movedBy;

    @Column(name = "moved_at", nullable = false)
    private LocalDateTime movedAt;
}
