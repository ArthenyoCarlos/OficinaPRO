package br.com.oficinapro.cliente.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 120, message = "Name must be between 3 and 120 characters")
        String name,

        @NotBlank(message = "CPF/CNPJ is required")
        @Pattern(
                regexp = "(^\\d{11}$)|(^\\d{14}$)|(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)|(^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$)",
                message = "CPF/CNPJ must be in a valid format"
        )
        String cpfCnpj,

        @Email(message = "Invalid email format")
        @Size(max = 120, message = "Email must be less than 120 characters")
        String email,

        @Size(max = 20, message = "Phone must be less than 20 characters")
        String phone,

        @Size(max = 255, message = "Address must be less than 255 characters")
        String address,

        @Size(max = 100, message = "City must be less than 100 characters")
        String city,

        @Size(min = 2, max = 2, message = "State must have 2 characters")
        String state,

        @Pattern(
                regexp = "(^\\d{8}$)|(^\\d{5}-\\d{3}$)",
                message = "Zip code must be in a valid format"
        )
        String zipCode,

        @Size(max = 500, message = "Observation must be less than 500 characters")
        String observation
) {
}
