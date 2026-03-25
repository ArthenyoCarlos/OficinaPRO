package br.com.oficinapro.veiculo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record VehicleRequest(

        @NotBlank(message = "Plate is required")
        @Size(min = 7, max = 10, message = "Plate must be between 7 and 10 characters")
        @Pattern(
                regexp = "^[A-Za-z0-9-]+$",
                message = "Plate must contain only letters, numbers, and hyphen"
        )
        String plate,

        @Pattern(
                regexp = "^$|^[A-HJ-NPR-Z0-9]{17}$",
                message = "Chassis must have 17 valid characters"
        )
        String chassis,

        @Pattern(
                regexp = "^$|^\\d{9,11}$",
                message = "Renavam must contain between 9 and 11 digits"
        )
        String renavam,

        @Size(max = 100, message = "Brand must be less than 100 characters")
        String brand,

        @Size(max = 100, message = "Model must be less than 100 characters")
        String model,

        @Min(value = 1900, message = "Manufacture year must be greater than or equal to 1900")
        @Max(value = 2100, message = "Manufacture year must be less than or equal to 2100")
        Integer manufactureYear,

        @Min(value = 1900, message = "Model year must be greater than or equal to 1900")
        @Max(value = 2100, message = "Model year must be less than or equal to 2100")
        Integer modelYear,

        @Size(max = 50, message = "Color must be less than 50 characters")
        String color,

        @Size(max = 30, message = "Fuel type must be less than 30 characters")
        String fuelType,

        @PositiveOrZero(message = "Current mileage must be zero or greater")
        Long currentMileage,

        @Size(max = 500, message = "Notes must be less than 500 characters")
        String notes
) {
}
