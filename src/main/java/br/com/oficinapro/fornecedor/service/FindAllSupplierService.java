package br.com.oficinapro.fornecedor.service;

import br.com.oficinapro.fornecedor.domain.Supplier;
import br.com.oficinapro.fornecedor.dto.response.SupplierResponse;
import br.com.oficinapro.fornecedor.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllSupplierService {

    private final SupplierRepository supplierRepository;

    public FindAllSupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional(readOnly = true)
    public Page<SupplierResponse> findAll(boolean enabled, Pageable pageable) {
        Page<Supplier> suppliers = supplierRepository.findAll(enabled, pageable);
        return suppliers.map(SupplierResponse::new);
    }
}
