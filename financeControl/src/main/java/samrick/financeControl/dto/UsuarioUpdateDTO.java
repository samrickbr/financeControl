package samrick.financeControl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
        @NotNull @Size(min = 5, message = "O campo deve ter no mínimo 5 caracteres")
        String nome,
        @NotBlank(message = "O email não pode estar em blanco!")
        @Email(message = "O email deve ser válido!")
        String email,
        @NotNull
        @Size(min = 5, message = "O campo deve ter no mínimo 5 caracteres")
        String senha,
        @NotNull @Size(min = 11, message = "O campo deve ter 11 caracteres", max = 11)
        String cpf,
        @NotBlank @Size(min = 5, message = "O campo deve ter no mínimo 5 caracteres")
        String profissao,

        @NotBlank(message = "A justificativa é obrigatória para realizar alterações.")
        String justificativa
) {
}
