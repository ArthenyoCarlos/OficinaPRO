package br.com.oficinapro.cliente.controller;

import br.com.oficinapro.cliente.dto.request.ClientRequest;
import br.com.oficinapro.cliente.dto.response.ClientResponse;
import br.com.oficinapro.cliente.service.CreateClientService;
import br.com.oficinapro.cliente.service.UpdateClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final CreateClientService createClientService;
    private final UpdateClientService updateClientService;

    public ClientController(CreateClientService createClientService, UpdateClientService updateClientService) {
        this.createClientService = createClientService;
        this.updateClientService = updateClientService;
    }

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
}
