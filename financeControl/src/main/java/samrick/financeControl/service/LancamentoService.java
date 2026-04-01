package samrick.financeControl.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samrick.financeControl.audit.LogAuditoriaService;
import samrick.financeControl.dto.LancamentoRequestDTO;
import samrick.financeControl.dto.LancamentoResponseDTO;
import samrick.financeControl.dto.LancamentoUpdateDTO;
import samrick.financeControl.exceptions.RecursoNaoEncontradoException;
import samrick.financeControl.exceptions.RegraNegocioException;
import samrick.financeControl.mapper.LancamentoMapper;
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
    @Autowired
    private LancamentoMapper mapper;
    @Autowired
    private LogAuditoriaService logService;

    public LancamentoResponseDTO salvar(@Valid LancamentoRequestDTO dados) {
        Usuario usuario = usuarioRepository.findById(dados.usuarioID())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", dados.usuarioID()));
        Lancamento novo = mapper.toEntity(dados, usuario);
        novo.setUsuario(usuario);
        Lancamento salvo = repository.save(novo);

        logService.registrarLog(
                "Lançamento",
                salvo.getId(),
                "CADASTRAR",
                null,
                "Novo lançamento registrado no sistema",
                "Rick Admin"
        );
        return mapper.toDTO(salvo);
    }

    public List<LancamentoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public Lancamento buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<LancamentoResponseDTO> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional
    public LancamentoResponseDTO atualizar(Long id, @Valid LancamentoUpdateDTO dto) {
        Lancamento lancamento = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento", id));

        logService.registrarLog(
                "USUARIO",
                id,
                "ALTERAR",
                lancamento,
                dto.justificativa(),
                "Sistema (RICK)"
        );
        //atualizar os dados:
        lancamento.setValor(dto.valor());
        lancamento.setDescricao(dto.descricao());
        lancamento.setDataVencimento(dto.dataVencimento());
        lancamento.setCategoria(dto.categoria());

        try {

            lancamento.setTipo(TipoLancamento.valueOf(dto.tipo().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Tipo de lançamento inválido! Opções permitidas: "
                    + TipoLancamento.listarOpcoes());
        }

        // realizar a busca do usuário para retornar o nome de quem está salvando no Json
        Usuario usuarioCompleto = usuarioRepository.findById(dto.usuarioID()).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado"));

        // Vincular o usário completo ao lançamento
        lancamento.setUsuario(usuarioCompleto);


        //Lançamento precisa ter data de vencimento
        if (lancamento.getDataVencimento() == null) {
            throw new RuntimeException("Data de vencimento é obrigatória!");
        }
        //salvar o lancamento com todos os dados vinculados
        return mapper.toDTO(repository.save(lancamento));
    }

    @Transactional
    public void excluir(Long id, String justificativa) {
        Lancamento lancamento = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento", id));
        logService.registrarLog(
                "USUÁRIO",
                id,
                "EXCLUIR",
                lancamento,
                justificativa,
                "Rick Admin"
        );
        repository.delete(lancamento);
    }
}
