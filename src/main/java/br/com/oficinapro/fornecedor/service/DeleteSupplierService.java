package br.com.oficinapro.fornecedor.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.fornecedor.domain.Supplier;
import br.com.oficinapro.fornecedor.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteSupplierService {

    private final SupplierRepository supplierRepository;

    public DeleteSupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(String code) {
        Supplier supplier = supplierRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        if (!supplier.isEnabled()) {
            throw new BusinessException("Supplier already disabled");
        }

        supplier.setEnabled(false);
        supplierRepository.save(supplier);
    }
}
