package samrick.financeControl.dto;

import java.math.BigDecimal;

public record RelatorioFinanceiroDTO(
        BigDecimal totalEntradas,
        BigDecimal totalSaidas,
        BigDecimal saldoFinal
) {
}
