package br.com.oficinapro.auth.controller;

import br.com.oficinapro.auth.dto.user.request.UserRequestDTO;
import br.com.oficinapro.auth.dto.user.response.UserResponseDTO;
import br.com.oficinapro.auth.service.user.CreateUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final CreateUserService createUserService;

    public UserController(CreateUserService createUserService) {
        this.createUserService = createUserService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO response = createUserService.create(userRequestDTO);
        URI location = URI.create("/api/v1/user/" + response.id());
        return ResponseEntity.created(location).body(response);
    }
}
