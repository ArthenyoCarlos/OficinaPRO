package br.com.oficinapro.financeiro.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.financeiro.repository.ReceiptRepository;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ServiceOrderReceiptValidationService {

    private final ReceiptRepository receiptRepository;

    public ServiceOrderReceiptValidationService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public void validateServiceOrderReceipts(ServiceOrder serviceOrder) {
        BigDecimal totalReceipts = serviceOrder.getReceipts().stream()
                .map(receipt -> defaultZero(receipt.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        validateAgainstTotal(serviceOrder, totalReceipts);
    }

    public void validateStandaloneReceipt(ServiceOrder serviceOrder, BigDecimal receiptAmount, UUID receiptIdToIgnore) {
        BigDecimal currentTotal = serviceOrder.getId() == null
                ? BigDecimal.ZERO
                : defaultZero(receiptRepository.sumAmountByServiceOrderIdIgnoringReceiptId(serviceOrder.getId(), receiptIdToIgnore));

        validateAgainstTotal(serviceOrder, currentTotal.add(defaultZero(receiptAmount)));
    }

    private void validateAgainstTotal(ServiceOrder serviceOrder, BigDecimal receiptsTotal) {
        BigDecimal serviceOrderTotal = defaultZero(serviceOrder.getTotalAmount());

        if (receiptsTotal.compareTo(serviceOrderTotal) > 0) {
            throw new BusinessException("Receipt total cannot exceed the service order total amount");
        }
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
