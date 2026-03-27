package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.estoque.service.ServiceOrderStockService;
import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.financeiro.service.ServiceOrderFinancialStatusService;
import br.com.oficinapro.financeiro.service.ServiceOrderReceiptValidationService;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.domain.ServiceOrderStatusHistory;
import br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus;
import br.com.oficinapro.ordemservico.dto.request.FinishServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import br.com.oficinapro.ordemservico.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FinishServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;
    private final ServiceOrderStockService serviceOrderStockService;
    private final ServiceOrderFinancialStatusService serviceOrderFinancialStatusService;
    private final ServiceOrderReceiptValidationService serviceOrderReceiptValidationService;

    public FinishServiceOrderService(ServiceOrderRepository serviceOrderRepository,
                                     UserRepository userRepository,
                                     ServiceOrderStockService serviceOrderStockService,
                                     ServiceOrderFinancialStatusService serviceOrderFinancialStatusService,
                                     ServiceOrderReceiptValidationService serviceOrderReceiptValidationService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.userRepository = userRepository;
        this.serviceOrderStockService = serviceOrderStockService;
        this.serviceOrderFinancialStatusService = serviceOrderFinancialStatusService;
        this.serviceOrderReceiptValidationService = serviceOrderReceiptValidationService;
    }

    @Transactional
    public ServiceOrderResponse finish(String code, FinishServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service order not found with code: " + code));

        validateTransition(serviceOrder.getStatus(), ServiceOrderStatus.FINISHED);

        User changedBy = findUserByCode(request.changedByCode(), "Changed by");
        var previousStatus = serviceOrder.getStatus();
        serviceOrder.setStatus(ServiceOrderStatus.FINISHED);
        serviceOrder.setFinalizedAt(request.finalizedAt() != null ? request.finalizedAt() : LocalDateTime.now());
        addReceiptIfInformed(serviceOrder, request, changedBy);
        serviceOrderReceiptValidationService.validateServiceOrderReceipts(serviceOrder);
        serviceOrderFinancialStatusService.recalculate(serviceOrder);
        serviceOrderStockService.outputProducts(serviceOrder, changedBy);
        serviceOrder.getStatusHistory().add(buildStatusHistory(serviceOrder, previousStatus, changedBy, defaultNotes(request.notes(), "Service order finished")));

        return new ServiceOrderResponse(serviceOrderRepository.save(serviceOrder));
    }

    private void validateTransition(ServiceOrderStatus currentStatus, ServiceOrderStatus targetStatus) {
        if (currentStatus == targetStatus) {
            throw new BusinessException("Service order is already in status " + targetStatus);
        }
        if (!currentStatus.canTransitionTo(targetStatus)) {
            throw new BusinessException("Invalid status transition from " + currentStatus + " to " + targetStatus);
        }
    }

    private User findUserByCode(String code, String fieldName) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return userRepository.findByCode(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException(fieldName + " user not found with code: " + code));
    }

    private void addReceiptIfInformed(ServiceOrder serviceOrder, FinishServiceOrderRequest request, User changedBy) {
        boolean hasPaymentMethod = request.paymentMethod() != null;
        boolean hasReceiptAmount = request.receiptAmount() != null;

        if (!hasPaymentMethod && !hasReceiptAmount) {
            return;
        }

        if (!hasPaymentMethod) {
            throw new BusinessException("Payment method is required when informing receipt amount on finish");
        }

        if (!hasReceiptAmount) {
            throw new BusinessException("Receipt amount is required when informing payment method on finish");
        }

        Receipt receipt = new Receipt();
        receipt.setServiceOrder(serviceOrder);
        receipt.setAmount(request.receiptAmount());
        receipt.setPaymentMethod(request.paymentMethod());
        receipt.setReceiptDate(request.receiptDate() != null ? request.receiptDate() : LocalDateTime.now());
        receipt.setNotes(trim(request.receiptNotes()));
        receipt.setReceivedBy(findUserByCode(request.receivedByCode(), "Received by"));
        serviceOrder.getReceipts().add(receipt);
    }

    private ServiceOrderStatusHistory buildStatusHistory(ServiceOrder serviceOrder,
                                                         ServiceOrderStatus previousStatus,
                                                         User changedBy,
                                                         String notes) {
        ServiceOrderStatusHistory history = new ServiceOrderStatusHistory();
        history.setServiceOrder(serviceOrder);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(serviceOrder.getStatus());
        history.setChangedBy(changedBy);
        history.setNotes(notes);
        history.setChangedAt(LocalDateTime.now());
        return history;
    }

    private String defaultNotes(String notes, String defaultValue) {
        return notes == null || notes.trim().isEmpty() ? defaultValue : notes.trim();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
