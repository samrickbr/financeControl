package samrick.financeControl.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samrick.financeControl.dto.LancamentoRequestDTO;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.model.TipoLancamento;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.repository.LancamentoRepository;
import samrick.financeControl.repository.UsuarioRepository;

import java.util.List;

@Service
public class LancamentoService {
    @Autowired
    private LancamentoRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository; //injeção dos repositorys

    public Lancamento salvar(@Valid LancamentoRequestDTO dados) {
        Lancamento lancamento = new Lancamento();

        lancamento.setValor(dados.valor());
        lancamento.setDescricao(dados.descricao());
        lancamento.setDataVencimento(dados.dataVencimento());
        lancamento.setCategoria(dados.categoria());

        try {

            lancamento.setTipo(TipoLancamento.valueOf(dados.tipo().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e){
            throw  new RuntimeException("Tipo de lançamento inválido! Use: RECEITA ou DESPESA.");
        }

        // realizar a busca do usuário para retornar o nome de quem está salvando no Json
        Usuario usuarioCompleto = usuarioRepository.findById(dados.usuarioID()).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado"));

        // Vincular o usário completo ao lançamento
        lancamento.setUsuario(usuarioCompleto);


        //Lançamento precisa ter data de vencimento
        if (lancamento.getDataVencimento() == null) {
            throw new RuntimeException("Data de vencimento é obrigatória!");
        }
        //salvar o lancamento com todos os dados vinculados
        return repository.save(lancamento);
    }

    public List<Lancamento> listarTodos() {
        return repository.findAll();
    }

    public Lancamento buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
