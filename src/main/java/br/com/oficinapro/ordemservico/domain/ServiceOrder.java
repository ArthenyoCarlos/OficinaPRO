package br.com.oficinapro.ordemservico.domain;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.common.audit.entity.BaseEntity;
import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.ordemservico.domain.enums.ChargeType;
import br.com.oficinapro.financeiro.domain.enums.FinancialStatus;
import br.com.oficinapro.ordemservico.domain.enums.ApprovalMethod;
import br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus;
import br.com.oficinapro.veiculo.domain.Vehicle;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_service_order")
public class ServiceOrder extends BaseEntity {

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_technician_id")
    private User responsibleTechnician;

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "expected_delivery")
    private LocalDateTime expectedDelivery;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ServiceOrderStatus status;

    @Column(name = "reported_problem", columnDefinition = "TEXT")
    private String reportedProblem;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @Column(name = "customer_notes", columnDefinition = "TEXT")
    private String customerNotes;

    @Column(name = "entry_mileage")
    private Long entryMileage;

    @Column(name = "fuel_level")
    private String fuelLevel;

    @Column(name = "accessories", columnDefinition = "TEXT")
    private String accessories;

    @Column(name = "discount", precision = 19, scale = 2)
    private BigDecimal discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_type", nullable = false)
    private ChargeType chargeType;

    @Column(name = "subtotal_parts", precision = 19, scale = 2)
    private BigDecimal subtotalParts;

    @Column(name = "subtotal_services", precision = 19, scale = 2)
    private BigDecimal subtotalServices;

    @Column(name = "subtotal_amount", precision = 19, scale = 2)
    private BigDecimal subtotalAmount;

    @Column(name = "products_discount_amount", precision = 19, scale = 2)
    private BigDecimal productsDiscountAmount;

    @Column(name = "products_discount_percent", precision = 10, scale = 4)
    private BigDecimal productsDiscountPercent;

    @Column(name = "services_discount_amount", precision = 19, scale = 2)
    private BigDecimal servicesDiscountAmount;

    @Column(name = "services_discount_percent", precision = 10, scale = 4)
    private BigDecimal servicesDiscountPercent;

    @Column(name = "total_discount_percent", precision = 10, scale = 4)
    private BigDecimal totalDiscountPercent;

    @Column(name = "total_parts", precision = 19, scale = 2)
    private BigDecimal totalParts;

    @Column(name = "total_services", precision = 19, scale = 2)
    private BigDecimal totalServices;

    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "financial_status")
    private FinancialStatus financialStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approval_datetime")
    private LocalDateTime approvalDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_method")
    private ApprovalMethod approvalMethod;

    @Column(name = "approval_notes", columnDefinition = "TEXT")
    private String approvalNotes;

    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceOrderServiceItem> serviceItems = new ArrayList<>();

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceOrderProductItem> productItems = new ArrayList<>();

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceOrderStatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Receipt> receipts = new ArrayList<>();
}
