package br.com.oficinapro.auth.dto.user.response;

import br.com.oficinapro.auth.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String code,
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
    public UserResponseDTO(User entity) {
        this(
                entity.getId(),
                entity.getCode(),
                entity.getUsername(),
                entity.getFullName(),
                entity.getEmail(),
                entity.isAccountNonExpired(),
                entity.isAccountNonLocked(),
                entity.isCredentialsNonExpired(),
                entity.isEnabled(),
                entity.getPermissions().stream().map(x -> x.getName()).toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
