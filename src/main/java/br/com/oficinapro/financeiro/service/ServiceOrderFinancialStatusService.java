package br.com.oficinapro.financeiro.service;

import br.com.oficinapro.financeiro.domain.enums.FinancialStatus;
import br.com.oficinapro.financeiro.repository.ReceiptRepository;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus;
import br.com.oficinapro.ordemservico.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ServiceOrderFinancialStatusService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ReceiptRepository receiptRepository;

    public ServiceOrderFinancialStatusService(ServiceOrderRepository serviceOrderRepository,
                                             ReceiptRepository receiptRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.receiptRepository = receiptRepository;
    }

    public void recalculate(ServiceOrder serviceOrder) {
        serviceOrder.setFinancialStatus(resolveFinancialStatus(serviceOrder));
    }

    @Transactional
    public void recalculateAndSave(ServiceOrder serviceOrder) {
        recalculate(serviceOrder);
        serviceOrderRepository.save(serviceOrder);
    }

    private FinancialStatus resolveFinancialStatus(ServiceOrder serviceOrder) {
        if (serviceOrder.getStatus() == ServiceOrderStatus.CANCELED) {
            return FinancialStatus.CANCELED;
        }

        BigDecimal totalAmount = defaultZero(serviceOrder.getTotalAmount());
        BigDecimal receivedAmount = serviceOrder.getId() == null
                ? BigDecimal.ZERO
                : defaultZero(receiptRepository.sumAmountByServiceOrderId(serviceOrder.getId()));

        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return FinancialStatus.PAID;
        }

        if (receivedAmount.compareTo(BigDecimal.ZERO) == 0) {
            return FinancialStatus.PENDING;
        }

        if (receivedAmount.compareTo(totalAmount) < 0) {
            return FinancialStatus.PARTIALLY_PAID;
        }

        return FinancialStatus.PAID;
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
