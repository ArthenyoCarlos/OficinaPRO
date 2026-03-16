package br.com.oficinapro.auth.dto.user.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String fullName,
        String email,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enabled,
        List<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
