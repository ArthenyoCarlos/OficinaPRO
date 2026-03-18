package br.com.oficinapro.auth.controller;

import br.com.oficinapro.auth.dto.user.request.UserRequestDTO;
import br.com.oficinapro.auth.dto.user.response.UserResponseDTO;
import br.com.oficinapro.auth.service.user.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final CreateUserService createUserService;
    private final UpdateUserService updateUserService;
    private final FindAllUserService findAllUserService;
    private final FindByUserCodeService findByUserCodeService;
    private final DeleteUserService deleteUserService;
    private final ActivateUserService activateUserService;

    public UserController(
            CreateUserService createUserService,
            UpdateUserService updateUserService,
            FindAllUserService findAllUserService,
            FindByUserCodeService findByUserCodeService,
            DeleteUserService deleteUserService,
            ActivateUserService activateUserService
    ) {
        this.createUserService = createUserService;
        this.updateUserService = updateUserService;
        this.findAllUserService = findAllUserService;
        this.findByUserCodeService = findByUserCodeService;
        this.deleteUserService = deleteUserService;
        this.activateUserService = activateUserService;
    }

    // Create And Update

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO response = createUserService.create(userRequestDTO);
        URI location = URI.create("/api/v1/user/" + response.code());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable String code, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO response = updateUserService.update(code, userRequestDTO);
        return ResponseEntity.ok(response);
    }

    // Find All

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> findAll(
            @RequestParam(name = "enabled", defaultValue = "true") boolean enabled,
            Pageable pageable){
        Page<UserResponseDTO> response = findAllUserService.findAll(enabled, pageable);
        return ResponseEntity.ok(response);
    }

    // Find By Code

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<UserResponseDTO> findByCode(@PathVariable String code){
        UserResponseDTO response = findByUserCodeService.findByCode(code);
        return ResponseEntity.ok(response);
    }

    // Delete

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code){
        deleteUserService.delete(code);
        return ResponseEntity.noContent().build();
    }

    // Active

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}/active")
    public ResponseEntity<Void> activate(@PathVariable String code){
        activateUserService.activateUser(code);
        return ResponseEntity.noContent().build();
    }
}
