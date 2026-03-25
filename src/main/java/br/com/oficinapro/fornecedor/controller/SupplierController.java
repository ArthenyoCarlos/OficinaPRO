package br.com.oficinapro.fornecedor.controller;

import br.com.oficinapro.fornecedor.dto.request.SupplierRequest;
import br.com.oficinapro.fornecedor.dto.response.SupplierResponse;
import br.com.oficinapro.fornecedor.service.ActivateSupplierService;
import br.com.oficinapro.fornecedor.service.CreateSupplierService;
import br.com.oficinapro.fornecedor.service.DeleteSupplierService;
import br.com.oficinapro.fornecedor.service.FindAllSupplierService;
import br.com.oficinapro.fornecedor.service.FindBySupplierCodeService;
import br.com.oficinapro.fornecedor.service.UpdateSupplierService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final CreateSupplierService createSupplierService;
    private final UpdateSupplierService updateSupplierService;
    private final FindAllSupplierService findAllSupplierService;
    private final FindBySupplierCodeService findBySupplierCodeService;
    private final DeleteSupplierService deleteSupplierService;
    private final ActivateSupplierService activateSupplierService;

    public SupplierController(CreateSupplierService createSupplierService,
                              UpdateSupplierService updateSupplierService,
                              FindAllSupplierService findAllSupplierService,
                              FindBySupplierCodeService findBySupplierCodeService,
                              DeleteSupplierService deleteSupplierService,
                              ActivateSupplierService activateSupplierService) {
        this.createSupplierService = createSupplierService;
        this.updateSupplierService = updateSupplierService;
        this.findAllSupplierService = findAllSupplierService;
        this.findBySupplierCodeService = findBySupplierCodeService;
        this.deleteSupplierService = deleteSupplierService;
        this.activateSupplierService = activateSupplierService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<SupplierResponse> create(@Valid @RequestBody SupplierRequest supplierRequest) {
        SupplierResponse response = createSupplierService.create(supplierRequest);
        URI location = URI.create("/api/v1/suppliers/" + response.code());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}")
    public ResponseEntity<SupplierResponse> update(@PathVariable String code, @Valid @RequestBody SupplierRequest supplierRequest) {
        SupplierResponse response = updateSupplierService.update(code, supplierRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<SupplierResponse>> findAll(
            @RequestParam(name = "enabled", defaultValue = "true") boolean enabled,
            Pageable pageable) {
        Page<SupplierResponse> response = findAllSupplierService.findAll(enabled, pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<SupplierResponse> findByCode(@PathVariable String code) {
        SupplierResponse response = findBySupplierCodeService.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        deleteSupplierService.delete(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}/active")
    public ResponseEntity<Void> activate(@PathVariable String code) {
        activateSupplierService.activate(code);
        return ResponseEntity.noContent().build();
    }
}
