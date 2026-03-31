package samrick.financeControl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoResponseDTO(
        Long id,

        String tipo,

        BigDecimal valor,

        LocalDate dataVencimento,

        String descricao,

        String categoria,

        String nomeUsuario
) {
}
