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
import samrick.financeControl.mapper.UsuarioMapper;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.repository.LancamentoRepository;
import samrick.financeControl.repository.UsuarioRepository;

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

    public UsuarioResponseDTO salvar(@Valid UsuarioRequestDTO dados) {
        //validação de email duplicado
        if (repository.existsByEmail(dados.email())) {
            throw new EmailJaCadastradoException();
        }
        Usuario novoUsuario = mapper.toEntity(dados);
        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        novoUsuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = repository.save(novoUsuario);

        logService.registrarLog(
                "USUARIO",
                usuarioSalvo.getId(),
                "CADASTRAR",
                null,
                "Novo usuário registrado no sistema",
                "Sistema/Auto"
        );
        return mapper.toDTO(usuarioSalvo);
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
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO dados) {
        Usuario usuarioAntigo = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));

        logService.registrarLog(
                "USUARIO",
                id,
                "ALTERAR",
                usuarioAntigo,
                dados.justificativa(),
                "Sistema (RICK)"
        );

        usuarioAntigo.setNome(dados.nome());
        usuarioAntigo.setProfissao(dados.profissao());

        if (dados.senha() != null && !dados.senha().isBlank()) {
            usuarioAntigo.setSenha(passwordEncoder.encode(dados.senha()));
        }

        return mapper.toDTO(repository.save(usuarioAntigo));
    }

    //Excluir usuário por ID
    @Transactional
    public void excluir(Long id, String justificativa) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        if (lancamentoRepository.existsByUsuarioId(id)) {
            throw new EntidadeEmUsoException("Não é possivel excluir um usuário que " +
                    "possui lançamentos cadastrados!");
        }
        logService.registrarLog(
                "USUARIO",
                id,
                "EXCLUIR",
                usuario,
                justificativa,
                "Rick Admin"
        );
        repository.delete(usuario);
    }
}