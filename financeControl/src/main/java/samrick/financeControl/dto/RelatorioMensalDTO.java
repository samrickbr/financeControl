package samrick.financeControl.dto;

import java.math.BigDecimal;

public record RelatorioMensalDTO(
        int mes,
        BigDecimal entradas,
        BigDecimal saidas,
        BigDecimal saldoMensal
) {
}
