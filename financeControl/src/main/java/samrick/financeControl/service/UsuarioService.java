package samrick.financeControl.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import samrick.financeControl.audit.LogAuditoriaService;
import samrick.financeControl.dto.UsuarioRequestDTO;
import samrick.financeControl.dto.UsuarioResponseDTO;
import samrick.financeControl.dto.UsuarioUpdateDTO;
import samrick.financeControl.exceptions.EmailJaCadastradoException;
import samrick.financeControl.exceptions.EntidadeEmUsoException;
import samrick.financeControl.exceptions.RecursoNaoEncontradoException;
import samrick.financeControl.exceptions.RegraNegocioException;
import samrick.financeControl.mapper.UsuarioMapper;
import samrick.financeControl.model.Perfilusuario;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.repository.LancamentoRepository;
import samrick.financeControl.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private LogAuditoriaService logService;
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private UsuarioMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    LancamentoRepository lancamentoRepository;

    public UsuarioResponseDTO salvar(@Valid UsuarioRequestDTO dados, Usuario usuarioLogado) {
        // 1- Validação de email duplicado
        if (repository.existsByEmail(dados.email())) {
            throw new EmailJaCadastradoException();
        }
        // 2. DEFINIÇÃO DO RESPONSÁVEL (Para a auditoria)
        String responsavel = (usuarioLogado != null) ? usuarioLogado.getNome() : "Auto-cadastro";
        // 3. MAPEAMENTO E PREPARAÇÃO
        Usuario novoUsuario = mapper.toEntity(dados, usuarioLogado);
        novoUsuario.setSenha(passwordEncoder.encode(dados.senha()));
        // Segurança: T odo cadastro novo é COMUM, a menos que um ADMIN mude depois
        novoUsuario.setPerfil(Perfilusuario.COMUM);
        novoUsuario.setAtivo(true);
        // 4. PERSISTÊNCIA
        Usuario salvo = repository.save(novoUsuario);
        // 5. AUDITORIA (Usando o 'responsavel' que definimos no passo 2)
        logService.registrarLog(
                "USUARIO",
                salvo.getId(),
                "CADASTRO",
                null,
                "Novo Registro no Sistema",
                responsavel
        );
        return mapper.toDTO(salvo);
    }

    //Listar todos os usuários:
    public List<UsuarioResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    //Buscar usuário por ID:
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("usuário", id));
        return mapper.toDTO(usuario);
    }

    //Buscar usuário por nome:
    public List<UsuarioResponseDTO> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    //Atualizar usuario por ID
    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO dados, Usuario usuarioLogado) {
        Usuario usuarioAntigo = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        //Validação de segurança:
        boolean ehDono = usuarioAntigo.getId().equals(usuarioLogado.getId());
        boolean ehAdmin = usuarioLogado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!ehDono && !ehAdmin) {
            throw new RegraNegocioException("Você não tem permissão para alterar este lançamento!");
        }
        logService.registrarLog(
                "USUARIO",
                id,
                "ALTERAR",
                usuarioAntigo,
                dados.justificativa(),
                usuarioLogado.getNome()
        );
        usuarioAntigo.setNome(dados.nome());
        usuarioAntigo.setEmail(dados.email());
        if (dados.senha() != null && !dados.senha().isBlank()) {
            usuarioAntigo.setSenha(passwordEncoder.encode(dados.senha()));
        }
        usuarioAntigo.setCpf(dados.cpf());
        usuarioAntigo.setProfissao(dados.profissao());
        usuarioAntigo.setDataUltimaAlteracao(LocalDateTime.now());
        usuarioAntigo.setUsuarioUltimaAlteracao(usuarioAntigo.getNome());
        return mapper.toDTO(repository.save(usuarioAntigo));
    }

    //Excluir usuário por ID
    @Transactional
    public void excluir(Long id, String justificativa, Usuario usuarioLogado) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        if (lancamentoRepository.existsByUsuarioId(id)) {
            throw new EntidadeEmUsoException("Não é possivel excluir um usuário que " +
                    "possui lançamentos cadastrados!");
        }
        //validação de segurança
        boolean ehDono = usuario.getId().equals(usuarioLogado.getId());
        boolean ehAdmin = usuarioLogado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!ehDono && !ehAdmin) {
            throw new RegraNegocioException("Você não tem permissão para excluir este lançamento!");
        }
        logService.registrarLog(
                "USUARIO",
                id,
                "EXCLUIR",
                usuario,
                justificativa,
                usuarioLogado.getNome()
        );
        repository.delete(usuario);
    }
}