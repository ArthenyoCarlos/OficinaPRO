package br.com.oficinapro.fornecedor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SupplierRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 120, message = "Name must be between 3 and 120 characters")
        String name,

        @NotBlank(message = "CNPJ is required")
        @Pattern(
                regexp = "(^\\d{14}$)|(^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$)",
                message = "CNPJ must be in a valid format"
        )
        String cnpj,

        @Size(max = 20, message = "Phone must be less than 20 characters")
        String phone,

        @Email(message = "Invalid email format")
        @Size(max = 120, message = "Email must be less than 120 characters")
        String email,

        @Size(max = 120, message = "Contact must be less than 120 characters")
        String contact,

        @Size(max = 1000, message = "Observation must be less than 1000 characters")
        String observation
) {
}
