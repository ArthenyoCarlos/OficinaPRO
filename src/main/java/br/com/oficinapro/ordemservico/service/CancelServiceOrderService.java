package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.estoque.service.ServiceOrderStockService;
import br.com.oficinapro.financeiro.service.ServiceOrderFinancialStatusService;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.domain.ServiceOrderStatusHistory;
import br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus;
import br.com.oficinapro.ordemservico.dto.request.CancelServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import br.com.oficinapro.ordemservico.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CancelServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;
    private final ServiceOrderStockService serviceOrderStockService;
    private final ServiceOrderFinancialStatusService serviceOrderFinancialStatusService;

    public CancelServiceOrderService(ServiceOrderRepository serviceOrderRepository,
                                     UserRepository userRepository,
                                     ServiceOrderStockService serviceOrderStockService,
                                     ServiceOrderFinancialStatusService serviceOrderFinancialStatusService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.userRepository = userRepository;
        this.serviceOrderStockService = serviceOrderStockService;
        this.serviceOrderFinancialStatusService = serviceOrderFinancialStatusService;
    }

    @Transactional
    public ServiceOrderResponse cancel(String code, CancelServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service order not found with code: " + code));

        validateTransition(serviceOrder.getStatus(), ServiceOrderStatus.CANCELED);

        User changedBy = findUserByCode(request.changedByCode(), "Changed by");
        var previousStatus = serviceOrder.getStatus();
        if (previousStatus == ServiceOrderStatus.FINISHED) {
            serviceOrderStockService.returnProducts(serviceOrder, changedBy);
        }
        serviceOrder.setStatus(ServiceOrderStatus.CANCELED);
        serviceOrderFinancialStatusService.recalculate(serviceOrder);
        serviceOrder.getStatusHistory().add(buildStatusHistory(serviceOrder, previousStatus, changedBy, request.notes().trim()));

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
}
