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
import samrick.financeControl.model.Usuario;
import samrick.financeControl.repository.LancamentoRepository;
import samrick.financeControl.repository.UsuarioRepository;

import java.time.LocalDateTime;
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

    @Transactional
    public LancamentoResponseDTO salvar(@Valid LancamentoRequestDTO dados, Usuario usuarioLogado) {
        Lancamento novo = mapper.toEntity(dados, usuarioLogado);
        novo.setUsuario(usuarioLogado);

        Lancamento salvo = repository.save(novo);

        logService.registrarLog(
                "Lançamento",
                salvo.getId(),
                "CADASTRAR",
                null,
                "Novo lançamento de " + salvo.getTipo() + " registrado no sistema",
                usuarioLogado.getNome()
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
    public LancamentoResponseDTO atualizar(Long id, @Valid LancamentoUpdateDTO dto,
                                           Usuario usuarioLogado) {
        //Buscar o Lançamento original
        Lancamento lancamento = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento", id));

        //Validação de segurança:
        boolean ehDono = lancamento.getUsuario().getId().equals(usuarioLogado.getId());
        boolean ehAdmin = usuarioLogado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!ehDono && !ehAdmin) {
            throw new RegraNegocioException("Você não tem permissão para alterar este lançamento!");
        }
        //Auditoria
        logService.registrarLog(
                "LANCAMENTO",
                id,
                "ALTERAR",
                lancamento,
                dto.justificativa(),
                usuarioLogado.getNome()
        );

        //atualizar os dados:
        lancamento.setValor(dto.valor());
        lancamento.setDescricao(dto.descricao());
        lancamento.setDataVencimento(dto.dataVencimento());
        lancamento.setDataPagamento(dto.dataPagamento());
        lancamento.setCategoria(dto.categoria());
        lancamento.setTipo(dto.tipo());
        lancamento.setUsuarioUltimaAlteracao((usuarioLogado.getNome()));
        lancamento.setDataUltimaAlteracao(LocalDateTime.now());

        //salvar o lancamento com todos os dados vinculados
        return mapper.toDTO(repository.save(lancamento));
    }

    @Transactional
    public void excluir(Long id, String justificativa, Usuario usuarioLogado) {
        Lancamento lancamento = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento", id));

        boolean ehDono = lancamento.getUsuario().getId().equals(usuarioLogado.getId());
        boolean ehAdmin = usuarioLogado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!ehDono && !ehAdmin) {
            throw new RegraNegocioException("Você não tem permissão para alterar este lançamento!");
        }

        logService.registrarLog(
                "LANCAMENTO",
                id,
                "EXCLUIR",
                lancamento,
                justificativa,
                usuarioLogado.getNome()
        );
        repository.delete(lancamento);
    }
}
