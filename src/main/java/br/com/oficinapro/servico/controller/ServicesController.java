package br.com.oficinapro.servico.controller;

import br.com.oficinapro.servico.dto.request.ServicesRequest;
import br.com.oficinapro.servico.dto.response.ServicesResponse;
import br.com.oficinapro.servico.service.ActivateServicesService;
import br.com.oficinapro.servico.service.CreateServicesService;
import br.com.oficinapro.servico.service.DeleteServicesService;
import br.com.oficinapro.servico.service.FindAllServicesService;
import br.com.oficinapro.servico.service.FindByServicesCodeService;
import br.com.oficinapro.servico.service.UpdateServicesService;
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
@RequestMapping("/api/v1/services")
public class ServicesController {

    private final CreateServicesService createServicesService;
    private final UpdateServicesService updateServicesService;
    private final FindAllServicesService findAllServicesService;
    private final FindByServicesCodeService findByServicesCodeService;
    private final DeleteServicesService deleteServicesService;
    private final ActivateServicesService activateServicesService;

    public ServicesController(CreateServicesService createServicesService,
                              UpdateServicesService updateServicesService,
                              FindAllServicesService findAllServicesService,
                              FindByServicesCodeService findByServicesCodeService,
                              DeleteServicesService deleteServicesService,
                              ActivateServicesService activateServicesService) {
        this.createServicesService = createServicesService;
        this.updateServicesService = updateServicesService;
        this.findAllServicesService = findAllServicesService;
        this.findByServicesCodeService = findByServicesCodeService;
        this.deleteServicesService = deleteServicesService;
        this.activateServicesService = activateServicesService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ServicesResponse> create(@Valid @RequestBody ServicesRequest servicesRequest) {
        ServicesResponse response = createServicesService.create(servicesRequest);
        URI location = URI.create("/api/v1/services/" + response.code());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}")
    public ResponseEntity<ServicesResponse> update(@PathVariable String code, @Valid @RequestBody ServicesRequest servicesRequest) {
        ServicesResponse response = updateServicesService.update(code, servicesRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ServicesResponse>> findAll(
            @RequestParam(name = "enabled", defaultValue = "true") boolean enabled,
            Pageable pageable) {
        Page<ServicesResponse> response = findAllServicesService.findAll(enabled, pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<ServicesResponse> findByCode(@PathVariable String code) {
        ServicesResponse response = findByServicesCodeService.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        deleteServicesService.delete(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}/active")
    public ResponseEntity<Void> activate(@PathVariable String code) {
        activateServicesService.activate(code);
        return ResponseEntity.noContent().build();
    }
}
