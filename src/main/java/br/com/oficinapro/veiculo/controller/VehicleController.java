package br.com.oficinapro.veiculo.controller;

import br.com.oficinapro.veiculo.dto.request.VehicleRequest;
import br.com.oficinapro.veiculo.dto.response.VehicleResponse;
import br.com.oficinapro.veiculo.service.ActivateVehicleService;
import br.com.oficinapro.veiculo.service.CreateVehicleService;
import br.com.oficinapro.veiculo.service.DeleteVehicleService;
import br.com.oficinapro.veiculo.service.FindAllVehicleService;
import br.com.oficinapro.veiculo.service.FindByVehicleCodeService;
import br.com.oficinapro.veiculo.service.UpdateVehicleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final CreateVehicleService createVehicleService;
    private final UpdateVehicleService updateVehicleService;
    private final FindAllVehicleService findAllVehicleService;
    private final FindByVehicleCodeService findByVehicleCodeService;
    private final DeleteVehicleService deleteVehicleService;
    private final ActivateVehicleService activateVehicleService;

    public VehicleController(CreateVehicleService createVehicleService,
                             UpdateVehicleService updateVehicleService,
                             FindAllVehicleService findAllVehicleService,
                             FindByVehicleCodeService findByVehicleCodeService,
                             DeleteVehicleService deleteVehicleService,
                             ActivateVehicleService activateVehicleService) {
        this.createVehicleService = createVehicleService;
        this.updateVehicleService = updateVehicleService;
        this.findAllVehicleService = findAllVehicleService;
        this.findByVehicleCodeService = findByVehicleCodeService;
        this.deleteVehicleService = deleteVehicleService;
        this.activateVehicleService = activateVehicleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest vehicleRequest) {
        VehicleResponse response = createVehicleService.create(vehicleRequest);
        URI location = URI.create("/api/v1/vehicles/" + response.code());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}")
    public ResponseEntity<VehicleResponse> update(@PathVariable String code, @Valid @RequestBody VehicleRequest vehicleRequest) {
        VehicleResponse response = updateVehicleService.update(code, vehicleRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<VehicleResponse>> findAll(
            @RequestParam(name = "enabled", defaultValue = "true") boolean enabled,
            Pageable pageable) {
        Page<VehicleResponse> response = findAllVehicleService.findAll(enabled, pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<VehicleResponse> findByCode(@PathVariable String code) {
        VehicleResponse response = findByVehicleCodeService.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        deleteVehicleService.delete(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}/active")
    public ResponseEntity<Void> activate(@PathVariable String code) {
        activateVehicleService.activate(code);
        return ResponseEntity.noContent().build();
    }
}
