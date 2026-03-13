package br.com.oficinapro.auth.dto.security;

public record AccountCredentialDTO(
        String username,
        String password
) {
}
