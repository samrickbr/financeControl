package samrick.financeControl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import samrick.financeControl.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequestDTO(

        @NotNull(message = "O tipo é obrigatório!")
        TipoLancamento tipo,
        @Positive
        BigDecimal valor,
        @NotNull
        LocalDate dataLancamento,
        @NotNull(message = "A data de vencimento é obrigatória!")
        LocalDate dataVencimento,

        LocalDate dataPagamento,

        @NotBlank @Size(min = 5, message = "O campo deve ter no mínimo 5 caracteres")
        String descricao,
        @NotNull @Size(min = 5, message = "O campo deve ter no mínimo 5 caracteres")
        String categoria
) {
}
