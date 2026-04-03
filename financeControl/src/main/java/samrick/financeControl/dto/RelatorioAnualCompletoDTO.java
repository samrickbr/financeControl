package samrick.financeControl.dto;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioAnualCompletoDTO(
        List<RelatorioMensalDTO> meses,
        BigDecimal totalEntradasAno,
        BigDecimal totalSaidasAno,
        BigDecimal saldoFinalAno
) {
    public RelatorioAnualCompletoDTO(List<RelatorioMensalDTO> meses){
        this(
                meses,
                meses.stream().map(RelatorioMensalDTO::entradas).reduce(BigDecimal.ZERO, BigDecimal::add),
                meses.stream().map(RelatorioMensalDTO::saidas).reduce(BigDecimal.ZERO, BigDecimal::add),
                meses.stream().map(RelatorioMensalDTO::saldoMensal).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}
