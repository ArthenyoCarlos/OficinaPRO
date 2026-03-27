package br.com.oficinapro.ordemservico.dto.response;

import br.com.oficinapro.financeiro.dto.response.ReceiptResponse;
import br.com.oficinapro.financeiro.domain.enums.FinancialStatus;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.domain.enums.ApprovalMethod;
import br.com.oficinapro.ordemservico.domain.enums.ChargeType;
import br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ServiceOrderResponse(
        UUID id,
        String code,
        String number,
        String clientCode,
        String clientName,
        String vehicleCode,
        String vehiclePlate,
        String responsibleTechnicianCode,
        String responsibleTechnicianName,
        LocalDateTime openedAt,
        LocalDateTime expectedDelivery,
        ServiceOrderStatus status,
        String reportedProblem,
        String diagnosis,
        String internalNotes,
        String customerNotes,
        Long entryMileage,
        String fuelLevel,
        String accessories,
        ChargeType chargeType,
        BigDecimal subtotalParts,
        BigDecimal subtotalServices,
        BigDecimal subtotalAmount,
        BigDecimal productsDiscountAmount,
        BigDecimal productsDiscountPercent,
        BigDecimal servicesDiscountAmount,
        BigDecimal servicesDiscountPercent,
        BigDecimal discount,
        BigDecimal totalDiscountAmount,
        BigDecimal totalDiscountPercent,
        BigDecimal totalParts,
        BigDecimal totalServices,
        BigDecimal totalAmount,
        FinancialStatus financialStatus,
        String approvedByCode,
        String approvedByName,
        LocalDateTime approvalDateTime,
        ApprovalMethod approvalMethod,
        String approvalNotes,
        LocalDateTime finalizedAt,
        LocalDateTime deliveredAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ServiceOrderServiceItemResponse> serviceItems,
        List<ServiceOrderProductItemResponse> productItems,
        List<ServiceOrderStatusHistoryResponse> statusHistory,
        List<ReceiptResponse> receipts
) {
    public ServiceOrderResponse(ServiceOrder entity) {
        this(
                entity.getId(),
                entity.getCode(),
                entity.getNumber(),
                entity.getClient() != null ? entity.getClient().getCode() : null,
                entity.getClient() != null ? entity.getClient().getName() : null,
                entity.getVehicle() != null ? entity.getVehicle().getCode() : null,
                entity.getVehicle() != null ? entity.getVehicle().getPlate() : null,
                entity.getResponsibleTechnician() != null ? entity.getResponsibleTechnician().getCode() : null,
                entity.getResponsibleTechnician() != null ? entity.getResponsibleTechnician().getFullName() : null,
                entity.getOpenedAt(),
                entity.getExpectedDelivery(),
                entity.getStatus(),
                entity.getReportedProblem(),
                entity.getDiagnosis(),
                entity.getInternalNotes(),
                entity.getCustomerNotes(),
                entity.getEntryMileage(),
                entity.getFuelLevel(),
                entity.getAccessories(),
                entity.getChargeType(),
                entity.getSubtotalParts(),
                entity.getSubtotalServices(),
                entity.getSubtotalAmount(),
                entity.getProductsDiscountAmount(),
                entity.getProductsDiscountPercent(),
                entity.getServicesDiscountAmount(),
                entity.getServicesDiscountPercent(),
                entity.getDiscount(),
                entity.getDiscount(),
                entity.getTotalDiscountPercent(),
                entity.getTotalParts(),
                entity.getTotalServices(),
                entity.getTotalAmount(),
                entity.getFinancialStatus(),
                entity.getApprovedBy() != null ? entity.getApprovedBy().getCode() : null,
                entity.getApprovedBy() != null ? entity.getApprovedBy().getFullName() : null,
                entity.getApprovalDateTime(),
                entity.getApprovalMethod(),
                entity.getApprovalNotes(),
                entity.getFinalizedAt(),
                entity.getDeliveredAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getServiceItems().stream().map(ServiceOrderServiceItemResponse::new).toList(),
                entity.getProductItems().stream().map(ServiceOrderProductItemResponse::new).toList(),
                entity.getStatusHistory().stream().map(ServiceOrderStatusHistoryResponse::new).toList(),
                entity.getReceipts().stream().map(ReceiptResponse::new).toList()
        );
    }
}
