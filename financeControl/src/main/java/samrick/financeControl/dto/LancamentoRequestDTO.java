package samrick.financeControl.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequestDTO(

        @NotNull
        String tipo,
        @Positive
        BigDecimal valor,
        @NotNull
        LocalDate dataVencimento,
        @NotBlank @Size(min = 5, message = "O campo deve ter no mínimo 5 caracteres")
        String descricao,
        @NotNull @Size(min = 5, message = "O campo deve ter no mínimo 5 caracteres")
        String categoria,
        @NotNull
        Long usuarioID
) {
}
