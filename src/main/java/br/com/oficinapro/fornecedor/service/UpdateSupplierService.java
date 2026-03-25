package br.com.oficinapro.fornecedor.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceConflictException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.common.util.ValidationCpfCnpj;
import br.com.oficinapro.fornecedor.domain.Supplier;
import br.com.oficinapro.fornecedor.dto.request.SupplierRequest;
import br.com.oficinapro.fornecedor.dto.response.SupplierResponse;
import br.com.oficinapro.fornecedor.mapper.SupplierMapper;
import br.com.oficinapro.fornecedor.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateSupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public UpdateSupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Transactional
    public SupplierResponse update(String code, SupplierRequest supplierRequest) {
        Supplier supplier = supplierRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + code));

        updateCnpj(supplier, supplierRequest.cnpj());
        updateEmail(supplier, supplierRequest.email());
        supplierMapper.updateEntity(supplierRequest, supplier);

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updatedSupplier);
    }

    private void updateCnpj(Supplier supplier, String cnpj) {
        if (cnpj == null || cnpj.equals(supplier.getCnpj())) {
            return;
        }

        if (!ValidationCpfCnpj.validationCnpj(cnpj)) {
            throw new BusinessException("Invalid CNPJ");
        }

        String normalizedCnpj = ValidationCpfCnpj.onlyDigits(cnpj);
        Supplier existingSupplier = supplierRepository.findByCnpj(normalizedCnpj);
        if (existingSupplier != null && !existingSupplier.getId().equals(supplier.getId())) {
            throw new ResourceConflictException("CNPJ already exists");
        }

        supplier.setCnpj(normalizedCnpj);
    }

    private void updateEmail(Supplier supplier, String email) {
        if (email == null || email.equals(supplier.getEmail())) {
            return;
        }

        Supplier existingSupplier = supplierRepository.findByEmail(email);
        if (existingSupplier != null && !existingSupplier.getId().equals(supplier.getId())) {
            throw new ResourceConflictException("Email already exists");
        }

        supplier.setEmail(email);
    }
}
