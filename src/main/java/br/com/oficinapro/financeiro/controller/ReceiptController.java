package br.com.oficinapro.financeiro.controller;

import br.com.oficinapro.financeiro.dto.request.ReceiptRequest;
import br.com.oficinapro.financeiro.dto.response.ReceiptResponse;
import br.com.oficinapro.financeiro.service.CreateReceiptService;
import br.com.oficinapro.financeiro.service.DeleteReceiptService;
import br.com.oficinapro.financeiro.service.FindAllReceiptService;
import br.com.oficinapro.financeiro.service.FindByReceiptIdService;
import br.com.oficinapro.financeiro.service.UpdateReceiptService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

    private final CreateReceiptService createReceiptService;
    private final UpdateReceiptService updateReceiptService;
    private final FindAllReceiptService findAllReceiptService;
    private final FindByReceiptIdService findByReceiptIdService;
    private final DeleteReceiptService deleteReceiptService;

    public ReceiptController(CreateReceiptService createReceiptService,
                             UpdateReceiptService updateReceiptService,
                             FindAllReceiptService findAllReceiptService,
                             FindByReceiptIdService findByReceiptIdService,
                             DeleteReceiptService deleteReceiptService) {
        this.createReceiptService = createReceiptService;
        this.updateReceiptService = updateReceiptService;
        this.findAllReceiptService = findAllReceiptService;
        this.findByReceiptIdService = findByReceiptIdService;
        this.deleteReceiptService = deleteReceiptService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ReceiptResponse> create(@Valid @RequestBody ReceiptRequest request) {
        ReceiptResponse response = createReceiptService.create(request);
        URI location = URI.create("/api/v1/receipts/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReceiptResponse> update(@PathVariable UUID id, @Valid @RequestBody ReceiptRequest request) {
        return ResponseEntity.ok(updateReceiptService.update(id, request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ReceiptResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(findAllReceiptService.findAll(pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReceiptResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(findByReceiptIdService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteReceiptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
