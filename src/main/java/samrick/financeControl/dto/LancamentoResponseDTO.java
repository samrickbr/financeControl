package samrick.financeControl.dto;

import samrick.financeControl.model.Lancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LancamentoResponseDTO(
        Long id,
        String tipo,
        BigDecimal valor,
        LocalDate dataLancamento,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        String descricao,
        String categoria,
        String nomeUsuario,
        LocalDateTime dataUltimaAlteracao,
        String usuarioUltimaAlteracao
) {
    public LancamentoResponseDTO(Lancamento lancamento) {
        this(
                lancamento.getId(),
                String.valueOf(lancamento.getTipo()),
                lancamento.getValor(),
                lancamento.getDataLancamento(),
                lancamento.getDataVencimento(),
                lancamento.getDataPagamento(),
                lancamento.getDescricao(),
                lancamento.getCategoria(),
                lancamento.getUsuario().getNome(),
                lancamento.getDataUltimaAlteracao(),
                lancamento.getUsuarioUltimaAlteracao()
        );
    }
}
