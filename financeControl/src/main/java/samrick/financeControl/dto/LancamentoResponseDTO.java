package samrick.financeControl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoResponseDTO(
        Long id,

        String tipo,

        BigDecimal valor,

        LocalDate dataVencimento,

        LocalDate dataPagamento,

        String descricao,

        String categoria,

        Long idUsuario,

        String nomeUsuario
) {
}
