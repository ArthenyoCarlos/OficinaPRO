package br.com.oficinapro.categoriaProduto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductCategoryRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 120, message = "Name must be between 3 and 120 characters")
        String name,

        @Size(max = 1000, message = "Description must be less than 1000 characters")
        String description
) {
}
