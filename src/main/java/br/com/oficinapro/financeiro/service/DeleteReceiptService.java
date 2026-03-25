package br.com.oficinapro.financeiro.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.financeiro.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteReceiptService {

    private final ReceiptRepository receiptRepository;

    public DeleteReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Transactional
    public void delete(UUID id) {
        if (!receiptRepository.existsById(id)) {
            throw new ResourceNotFoundException("Receipt not found with id: " + id);
        }
        receiptRepository.deleteById(id);
    }
}
