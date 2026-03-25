package br.com.oficinapro.fornecedor.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.fornecedor.domain.Supplier;
import br.com.oficinapro.fornecedor.repository.SupplierRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivateSupplierService {

    private final SupplierRepository supplierRepository;

    public ActivateSupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public void activate(String code) {
        Supplier supplier = supplierRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + code));

        if (supplier.isEnabled()) {
            throw new BusinessException("Supplier already enabled");
        }

        supplier.setEnabled(true);
        supplierRepository.save(supplier);
    }
}
