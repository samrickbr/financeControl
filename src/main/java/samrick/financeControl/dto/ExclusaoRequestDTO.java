package samrick.financeControl.dto;

import jakarta.validation.constraints.NotBlank;

public record ExclusaoRequestDTO (
        @NotBlank(message = "A justificcativa é obrigatória para excluir.")
        String justificativa
){
}
