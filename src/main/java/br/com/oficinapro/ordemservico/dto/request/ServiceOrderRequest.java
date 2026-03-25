package br.com.oficinapro.ordemservico.dto.request;

import br.com.oficinapro.ordemservico.domain.enums.ApprovalMethod;
import br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ServiceOrderRequest(

        @NotBlank(message = "Client code is required")
        @Size(max = 255, message = "Client code must be less than 255 characters")
        String clientCode,

        @NotBlank(message = "Vehicle code is required")
        @Size(max = 255, message = "Vehicle code must be less than 255 characters")
        String vehicleCode,

        @Size(max = 255, message = "Responsible technician code must be less than 255 characters")
        String responsibleTechnicianCode,

        LocalDateTime openedAt,
        LocalDateTime expectedDelivery,

        @NotNull(message = "Status is required")
        ServiceOrderStatus status,

        @Size(max = 4000, message = "Reported problem must be less than 4000 characters")
        String reportedProblem,

        @Size(max = 4000, message = "Diagnosis must be less than 4000 characters")
        String diagnosis,

        @Size(max = 4000, message = "Internal notes must be less than 4000 characters")
        String internalNotes,

        @Size(max = 4000, message = "Customer notes must be less than 4000 characters")
        String customerNotes,

        @PositiveOrZero(message = "Entry mileage must be zero or greater")
        Long entryMileage,

        @Size(max = 100, message = "Fuel level must be less than 100 characters")
        String fuelLevel,

        @Size(max = 2000, message = "Accessories must be less than 2000 characters")
        String accessories,

        @DecimalMin(value = "0.0", inclusive = true, message = "Discount must be zero or greater")
        BigDecimal discount,

        @Size(max = 255, message = "Approved by code must be less than 255 characters")
        String approvedByCode,

        LocalDateTime approvalDateTime,
        ApprovalMethod approvalMethod,

        @Size(max = 2000, message = "Approval notes must be less than 2000 characters")
        String approvalNotes,

        LocalDateTime finalizedAt,
        LocalDateTime deliveredAt,

        @Valid
        List<ServiceOrderServiceItemRequest> serviceItems,

        @Valid
        List<ServiceOrderProductItemRequest> productItems,

        @Valid
        List<ServiceOrderReceiptRequest> receipts
) {
}
