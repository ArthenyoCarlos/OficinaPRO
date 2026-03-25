package br.com.oficinapro.financeiro.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.financeiro.dto.request.ReceiptRequest;
import br.com.oficinapro.financeiro.dto.response.ReceiptResponse;
import br.com.oficinapro.financeiro.mapper.ReceiptMapper;
import br.com.oficinapro.financeiro.repository.ReceiptRepository;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CreateReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;
    private final ServiceOrderFinancialStatusService serviceOrderFinancialStatusService;
    private final ServiceOrderReceiptValidationService serviceOrderReceiptValidationService;

    public CreateReceiptService(ReceiptRepository receiptRepository,
                                ReceiptMapper receiptMapper,
                                ServiceOrderRepository serviceOrderRepository,
                                UserRepository userRepository,
                                ServiceOrderFinancialStatusService serviceOrderFinancialStatusService,
                                ServiceOrderReceiptValidationService serviceOrderReceiptValidationService) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.serviceOrderRepository = serviceOrderRepository;
        this.userRepository = userRepository;
        this.serviceOrderFinancialStatusService = serviceOrderFinancialStatusService;
        this.serviceOrderReceiptValidationService = serviceOrderReceiptValidationService;
    }

    @Transactional
    public ReceiptResponse create(ReceiptRequest request) {
        Receipt receipt = receiptMapper.toEntity(request);
        receipt.setServiceOrder(findServiceOrder(request.serviceOrderCode()));
        receipt.setReceivedBy(findUser(request.receivedByCode()));
        receipt.setReceiptDate(request.receiptDate() != null ? request.receiptDate() : LocalDateTime.now());
        serviceOrderReceiptValidationService.validateStandaloneReceipt(receipt.getServiceOrder(), receipt.getAmount(), null);
        Receipt saved = receiptRepository.save(receipt);
        serviceOrderFinancialStatusService.recalculateAndSave(saved.getServiceOrder());
        return receiptMapper.toResponse(saved);
    }

    private ServiceOrder findServiceOrder(String code) {
        return serviceOrderRepository.findByCode(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Service order not found with code: " + code));
    }

    private User findUser(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return userRepository.findByCode(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with code: " + code));
    }
}
