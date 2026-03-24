package br.com.oficinapro.cliente.controller;

import br.com.oficinapro.cliente.dto.request.ClientRequest;
import br.com.oficinapro.cliente.dto.response.ClientResponse;
import br.com.oficinapro.cliente.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final CreateClientService createClientService;
    private final UpdateClientService updateClientService;
    private final FindAllClientService findAllClientService;
    private final FindByClientCodeService findByClientCodeService;
    private final DeleteClientService deleteClientService;
    private final ActivateClientService activateClientService;

    public ClientController(CreateClientService createClientService,
                            UpdateClientService updateClientService,
                            FindAllClientService findAllClientService,
                            FindByClientCodeService findByClientCodeService,
                            DeleteClientService deleteClientService,
                            ActivateClientService activateClientService) {
        this.createClientService = createClientService;
        this.updateClientService = updateClientService;
        this.findAllClientService = findAllClientService;
        this.findByClientCodeService = findByClientCodeService;
        this.deleteClientService = deleteClientService;
        this.activateClientService = activateClientService;
    }

    // Create And Update

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest clientRequest) {
        ClientResponse response = createClientService.create(clientRequest);
        URI location = URI.create("/api/v1/client/" + response.code());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}")
    public ResponseEntity<ClientResponse> update(@PathVariable String code, @Valid @RequestBody ClientRequest clientRequest) {
        ClientResponse response = updateClientService.update(code, clientRequest);
        return ResponseEntity.ok(response);
    }

    // Find All

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ClientResponse>> findAll(
            @RequestParam(name = "enabled", defaultValue = "true") boolean enabled,
            Pageable pageable){
        Page<ClientResponse> response = findAllClientService.findAll(enabled, pageable);
        return ResponseEntity.ok(response);
    }

    // Find By Code

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<ClientResponse> findByCode(@PathVariable String code){
        ClientResponse response = findByClientCodeService.findByCode(code);
        return ResponseEntity.ok(response);
    }

    // Delete

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code){
        deleteClientService.delete(code);
        return ResponseEntity.noContent().build();
    }

    // Active

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}/active")
    public ResponseEntity<Void> activate(@PathVariable String code){
        activateClientService.activateUser(code);
        return ResponseEntity.noContent().build();
    }
}
