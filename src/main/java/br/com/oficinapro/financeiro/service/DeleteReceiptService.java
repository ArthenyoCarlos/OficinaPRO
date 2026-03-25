package br.com.oficinapro.financeiro.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.financeiro.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ServiceOrderFinancialStatusService serviceOrderFinancialStatusService;

    public DeleteReceiptService(ReceiptRepository receiptRepository,
                                ServiceOrderFinancialStatusService serviceOrderFinancialStatusService) {
        this.receiptRepository = receiptRepository;
        this.serviceOrderFinancialStatusService = serviceOrderFinancialStatusService;
    }

    @Transactional
    public void delete(UUID id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found with id: " + id));
        var serviceOrder = receipt.getServiceOrder();
        receiptRepository.delete(receipt);
        serviceOrderFinancialStatusService.recalculateAndSave(serviceOrder);
    }
}
