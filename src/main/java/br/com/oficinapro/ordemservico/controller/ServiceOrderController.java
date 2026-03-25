package br.com.oficinapro.ordemservico.controller;

import br.com.oficinapro.ordemservico.dto.request.ApproveServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.request.CancelServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.request.DeliverServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.request.FinishServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.request.ServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import br.com.oficinapro.ordemservico.service.ApproveServiceOrderService;
import br.com.oficinapro.ordemservico.service.CancelServiceOrderService;
import br.com.oficinapro.ordemservico.service.CreateServiceOrderService;
import br.com.oficinapro.ordemservico.service.DeliverServiceOrderService;
import br.com.oficinapro.ordemservico.service.FindAllServiceOrderService;
import br.com.oficinapro.ordemservico.service.FindByServiceOrderCodeService;
import br.com.oficinapro.ordemservico.service.FinishServiceOrderService;
import br.com.oficinapro.ordemservico.service.UpdateServiceOrderService;
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
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/service-orders")
public class ServiceOrderController {

    private final CreateServiceOrderService createServiceOrderService;
    private final UpdateServiceOrderService updateServiceOrderService;
    private final FindAllServiceOrderService findAllServiceOrderService;
    private final FindByServiceOrderCodeService findByServiceOrderCodeService;
    private final ApproveServiceOrderService approveServiceOrderService;
    private final FinishServiceOrderService finishServiceOrderService;
    private final DeliverServiceOrderService deliverServiceOrderService;
    private final CancelServiceOrderService cancelServiceOrderService;

    public ServiceOrderController(CreateServiceOrderService createServiceOrderService,
                                  UpdateServiceOrderService updateServiceOrderService,
                                  FindAllServiceOrderService findAllServiceOrderService,
                                  FindByServiceOrderCodeService findByServiceOrderCodeService,
                                  ApproveServiceOrderService approveServiceOrderService,
                                  FinishServiceOrderService finishServiceOrderService,
                                  DeliverServiceOrderService deliverServiceOrderService,
                                  CancelServiceOrderService cancelServiceOrderService) {
        this.createServiceOrderService = createServiceOrderService;
        this.updateServiceOrderService = updateServiceOrderService;
        this.findAllServiceOrderService = findAllServiceOrderService;
        this.findByServiceOrderCodeService = findByServiceOrderCodeService;
        this.approveServiceOrderService = approveServiceOrderService;
        this.finishServiceOrderService = finishServiceOrderService;
        this.deliverServiceOrderService = deliverServiceOrderService;
        this.cancelServiceOrderService = cancelServiceOrderService;
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
    @PatchMapping("/{code}/approve")
    public ResponseEntity<ServiceOrderResponse> approve(@PathVariable String code,
                                                        @Valid @RequestBody ApproveServiceOrderRequest request) {
        ServiceOrderResponse response = approveServiceOrderService.approve(code, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}/finish")
    public ResponseEntity<ServiceOrderResponse> finish(@PathVariable String code,
                                                       @Valid @RequestBody FinishServiceOrderRequest request) {
        ServiceOrderResponse response = finishServiceOrderService.finish(code, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}/deliver")
    public ResponseEntity<ServiceOrderResponse> deliver(@PathVariable String code,
                                                        @Valid @RequestBody DeliverServiceOrderRequest request) {
        ServiceOrderResponse response = deliverServiceOrderService.deliver(code, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}/cancel")
    public ResponseEntity<ServiceOrderResponse> cancel(@PathVariable String code,
                                                       @Valid @RequestBody CancelServiceOrderRequest request) {
        ServiceOrderResponse response = cancelServiceOrderService.cancel(code, request);
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
