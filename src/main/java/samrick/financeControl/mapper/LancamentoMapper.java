package samrick.financeControl.mapper;

import org.springframework.stereotype.Component;
import samrick.financeControl.dto.LancamentoRequestDTO;
import samrick.financeControl.dto.LancamentoResponseDTO;
import samrick.financeControl.exceptions.RegraNegocioException;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.model.TipoLancamento;
import samrick.financeControl.model.Usuario;

@Component
public class LancamentoMapper {

    public LancamentoResponseDTO toDTO(Lancamento lancamento) {
        return new LancamentoResponseDTO(lancamento);
    }

    public Lancamento toEntity(LancamentoRequestDTO dto, Usuario usuario) {
        Lancamento lancamento = new Lancamento();
        lancamento.setTipo(dto.tipo());
        lancamento.setValor(dto.valor());
        lancamento.setDataLancamento(dto.dataLancamento());
        lancamento.setDataVencimento(dto.dataVencimento());
        lancamento.setDataPagamento(dto.dataPagamento());
        lancamento.setDescricao(dto.descricao());
        lancamento.setCategoria(dto.categoria());
        lancamento.setUsuario(usuario);
        return lancamento;
    }
}
