package br.com.oficinapro.fornecedor.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.fornecedor.domain.Supplier;
import br.com.oficinapro.fornecedor.dto.response.SupplierResponse;
import br.com.oficinapro.fornecedor.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindBySupplierCodeService {

    private final SupplierRepository supplierRepository;

    public FindBySupplierCodeService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional(readOnly = true)
    public SupplierResponse findByCode(String code) {
        Supplier supplier = supplierRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + code));

        return new SupplierResponse(supplier);
    }
}
