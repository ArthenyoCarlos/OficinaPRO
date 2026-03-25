package br.com.oficinapro.ordemservico.dto.request;

import br.com.oficinapro.ordemservico.domain.enums.ApprovalMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ApproveServiceOrderRequest(

        @NotBlank(message = "Approved by code is required")
        @Size(max = 255, message = "Approved by code must be less than 255 characters")
        String approvedByCode,

        LocalDateTime approvalDateTime,

        @NotNull(message = "Approval method is required")
        ApprovalMethod approvalMethod,

        @Size(max = 2000, message = "Approval notes must be less than 2000 characters")
        String approvalNotes
) {
}
