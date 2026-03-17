package br.com.oficinapro.auth.controller;

import br.com.oficinapro.auth.dto.user.request.UserRequestDTO;
import br.com.oficinapro.auth.dto.user.response.UserResponseDTO;
import br.com.oficinapro.auth.service.user.CreateUserService;
import br.com.oficinapro.auth.service.user.FindAllUserService;
import br.com.oficinapro.auth.service.user.UpdateUserService;
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

    public UserController(
            CreateUserService createUserService,
            UpdateUserService updateUserService,
            FindAllUserService findAllUserService
    ) {
        this.createUserService = createUserService;
        this.updateUserService = updateUserService;
        this.findAllUserService = findAllUserService;
    }

    // Create And Update

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO response = createUserService.create(userRequestDTO);
        URI location = URI.create("/api/v1/user/" + response.id());
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
}
