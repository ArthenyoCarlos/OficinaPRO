package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.domain.ServiceOrderStatusHistory;
import br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus;
import br.com.oficinapro.ordemservico.dto.request.ApproveServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import br.com.oficinapro.ordemservico.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ApproveServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;

    public ApproveServiceOrderService(ServiceOrderRepository serviceOrderRepository, UserRepository userRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ServiceOrderResponse approve(String code, ApproveServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service order not found with code: " + code));

        validateTransition(serviceOrder.getStatus(), ServiceOrderStatus.APPROVED);

        User approvedBy = userRepository.findByCode(request.approvedByCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Approved by user not found with code: " + request.approvedByCode()));

        var previousStatus = serviceOrder.getStatus();
        serviceOrder.setStatus(ServiceOrderStatus.APPROVED);
        serviceOrder.setApprovedBy(approvedBy);
        serviceOrder.setApprovalMethod(request.approvalMethod());
        serviceOrder.setApprovalDateTime(request.approvalDateTime() != null ? request.approvalDateTime() : LocalDateTime.now());
        serviceOrder.setApprovalNotes(trim(request.approvalNotes()));
        serviceOrder.getStatusHistory().add(buildStatusHistory(serviceOrder, previousStatus, approvedBy, "Service order approved"));

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

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
