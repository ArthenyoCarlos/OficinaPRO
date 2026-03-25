package br.com.oficinapro.financeiro.service;

import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.financeiro.dto.response.ReceiptResponse;
import br.com.oficinapro.financeiro.repository.ReceiptRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllReceiptService {

    private final ReceiptRepository receiptRepository;

    public FindAllReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Transactional(readOnly = true)
    public Page<ReceiptResponse> findAll(Pageable pageable) {
        Page<Receipt> receipts = receiptRepository.findAll(pageable);
        return receipts.map(ReceiptResponse::new);
    }
}
