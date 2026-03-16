package br.com.oficinapro.auth.dto.user.request;

import java.util.List;

public record UserRequestDTO(
        String username,
        String fullName,
        String email,
        String password,
        List<String> roles
) {
}
