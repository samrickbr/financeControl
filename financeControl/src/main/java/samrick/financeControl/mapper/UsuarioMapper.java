package samrick.financeControl.mapper;

import org.springframework.stereotype.Component;
import samrick.financeControl.dto.LancamentoRequestDTO;
import samrick.financeControl.dto.UsuarioRequestDTO;
import samrick.financeControl.dto.UsuarioResponseDTO;
import samrick.financeControl.exceptions.RegraNegocioException;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.model.TipoLancamento;
import samrick.financeControl.model.Usuario;

import java.time.LocalDateTime;

@Component
public class UsuarioMapper {
    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getProfissao(),
                usuario.getDataUltimaAlteracao(),
                usuario.getUsuarioUltimaAlteracao()
        );
    }

    public Usuario toEntity(UsuarioRequestDTO dto, Usuario usuarioLogado){
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setProfissao(dto.profissao());
        usuario.setCpf(dto.cpf());

        if (usuarioLogado != null){
            usuario.setUsuarioUltimaAlteracao(usuarioLogado.getNome());
            usuario.setDataUltimaAlteracao(LocalDateTime.now());
        }

        return usuario;
    }
}
