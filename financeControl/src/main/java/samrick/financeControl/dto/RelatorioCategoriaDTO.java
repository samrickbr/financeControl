package samrick.financeControl.dto;

import java.math.BigDecimal;

public record RelatorioCategoriaDTO(
        String categoria,
        BigDecimal total
){
}
