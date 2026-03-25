package br.com.oficinapro.estoque.controller;

import br.com.oficinapro.estoque.dto.request.StockMovementRequest;
import br.com.oficinapro.estoque.dto.response.StockMovementResponse;
import br.com.oficinapro.estoque.service.CreateStockMovementService;
import br.com.oficinapro.estoque.service.DeleteStockMovementService;
import br.com.oficinapro.estoque.service.FindAllStockMovementService;
import br.com.oficinapro.estoque.service.FindByStockMovementIdService;
import br.com.oficinapro.estoque.service.UpdateStockMovementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock-movements")
public class StockMovementController {

    private final CreateStockMovementService createStockMovementService;
    private final UpdateStockMovementService updateStockMovementService;
    private final FindAllStockMovementService findAllStockMovementService;
    private final FindByStockMovementIdService findByStockMovementIdService;
    private final DeleteStockMovementService deleteStockMovementService;

    public StockMovementController(CreateStockMovementService createStockMovementService,
                                   UpdateStockMovementService updateStockMovementService,
                                   FindAllStockMovementService findAllStockMovementService,
                                   FindByStockMovementIdService findByStockMovementIdService,
                                   DeleteStockMovementService deleteStockMovementService) {
        this.createStockMovementService = createStockMovementService;
        this.updateStockMovementService = updateStockMovementService;
        this.findAllStockMovementService = findAllStockMovementService;
        this.findByStockMovementIdService = findByStockMovementIdService;
        this.deleteStockMovementService = deleteStockMovementService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<StockMovementResponse> create(@Valid @RequestBody StockMovementRequest request) {
        StockMovementResponse response = createStockMovementService.create(request);
        URI location = URI.create("/api/v1/stock-movements/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<StockMovementResponse> update(@PathVariable UUID id, @Valid @RequestBody StockMovementRequest request) {
        return ResponseEntity.ok(updateStockMovementService.update(id, request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<StockMovementResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(findAllStockMovementService.findAll(pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(findByStockMovementIdService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteStockMovementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
