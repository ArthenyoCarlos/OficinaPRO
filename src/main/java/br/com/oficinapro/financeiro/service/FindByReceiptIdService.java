package br.com.oficinapro.financeiro.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.financeiro.dto.response.ReceiptResponse;
import br.com.oficinapro.financeiro.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FindByReceiptIdService {

    private final ReceiptRepository receiptRepository;

    public FindByReceiptIdService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Transactional(readOnly = true)
    public ReceiptResponse findById(UUID id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found with id: " + id));
        return new ReceiptResponse(receipt);
    }
}
