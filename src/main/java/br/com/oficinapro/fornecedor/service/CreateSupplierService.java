package br.com.oficinapro.fornecedor.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceConflictException;
import br.com.oficinapro.common.util.ValidationCpfCnpj;
import br.com.oficinapro.fornecedor.domain.Supplier;
import br.com.oficinapro.fornecedor.dto.request.SupplierRequest;
import br.com.oficinapro.fornecedor.dto.response.SupplierResponse;
import br.com.oficinapro.fornecedor.mapper.SupplierMapper;
import br.com.oficinapro.fornecedor.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateSupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public CreateSupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Transactional
    public SupplierResponse create(SupplierRequest supplierRequest) {
        validateEmailUniqueness(supplierRequest.email());
        validateCnpjUniqueness(supplierRequest.cnpj());

        Supplier supplier = supplierMapper.toEntity(supplierRequest);
        supplier.setCnpj(ValidationCpfCnpj.onlyDigits(supplierRequest.cnpj()));
        supplier.setEnabled(true);

        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toResponse(savedSupplier);
    }

    private void validateEmailUniqueness(String email) {
        if (email != null && supplierRepository.findByEmail(email) != null) {
            throw new ResourceConflictException("Email already exists");
        }
    }

    private void validateCnpjUniqueness(String cnpj) {
        if (!ValidationCpfCnpj.validationCnpj(cnpj)) {
            throw new BusinessException("Invalid CNPJ");
        }

        String normalizedCnpj = ValidationCpfCnpj.onlyDigits(cnpj);
        if (supplierRepository.findByCnpj(normalizedCnpj) != null) {
            throw new ResourceConflictException("CNPJ already exists");
        }
    }
}
