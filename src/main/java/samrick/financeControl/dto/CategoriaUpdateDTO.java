package samrick.financeControl.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaUpdateDTO(
        @NotBlank String categoria,
        @NotBlank String justificativa
) {}
