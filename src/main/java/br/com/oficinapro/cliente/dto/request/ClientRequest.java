package br.com.oficinapro.cliente.dto.request;

public record ClientRequest(
        String name,
        String cpfCnpj,
        String email,
        String phone,
        String address,
        String city,
        String state,
        String zipCode,
        String observation
) {
}
