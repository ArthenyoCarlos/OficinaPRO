package br.com.oficinapro.ordemservico.controller;

import br.com.oficinapro.ordemservico.dto.request.ServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import br.com.oficinapro.ordemservico.service.CreateServiceOrderService;
import br.com.oficinapro.ordemservico.service.FindAllServiceOrderService;
import br.com.oficinapro.ordemservico.service.FindByServiceOrderCodeService;
import br.com.oficinapro.ordemservico.service.UpdateServiceOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/service-orders")
public class ServiceOrderController {

    private final CreateServiceOrderService createServiceOrderService;
    private final UpdateServiceOrderService updateServiceOrderService;
    private final FindAllServiceOrderService findAllServiceOrderService;
    private final FindByServiceOrderCodeService findByServiceOrderCodeService;

    public ServiceOrderController(CreateServiceOrderService createServiceOrderService,
                                  UpdateServiceOrderService updateServiceOrderService,
                                  FindAllServiceOrderService findAllServiceOrderService,
                                  FindByServiceOrderCodeService findByServiceOrderCodeService) {
        this.createServiceOrderService = createServiceOrderService;
        this.updateServiceOrderService = updateServiceOrderService;
        this.findAllServiceOrderService = findAllServiceOrderService;
        this.findByServiceOrderCodeService = findByServiceOrderCodeService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceOrderResponse> create(@Valid @RequestBody ServiceOrderRequest request) {
        ServiceOrderResponse response = createServiceOrderService.create(request);
        URI location = URI.create("/api/v1/service-orders/" + response.code());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}")
    public ResponseEntity<ServiceOrderResponse> update(@PathVariable String code, @Valid @RequestBody ServiceOrderRequest request) {
        ServiceOrderResponse response = updateServiceOrderService.update(code, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ServiceOrderResponse>> findAll(Pageable pageable) {
        Page<ServiceOrderResponse> response = findAllServiceOrderService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<ServiceOrderResponse> findByCode(@PathVariable String code) {
        ServiceOrderResponse response = findByServiceOrderCodeService.findByCode(code);
        return ResponseEntity.ok(response);
    }
}
