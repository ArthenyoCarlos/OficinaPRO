package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.estoque.service.ServiceOrderStockService;
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

    public FinishServiceOrderService(ServiceOrderRepository serviceOrderRepository,
                                     UserRepository userRepository,
                                     ServiceOrderStockService serviceOrderStockService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.userRepository = userRepository;
        this.serviceOrderStockService = serviceOrderStockService;
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
}
