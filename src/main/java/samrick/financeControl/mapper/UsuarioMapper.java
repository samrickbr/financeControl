package samrick.financeControl.mapper;

import org.springframework.stereotype.Component;
import samrick.financeControl.dto.UsuarioRequestDTO;
import samrick.financeControl.dto.UsuarioResponseDTO;
import samrick.financeControl.exceptions.RegraNegocioException;
import samrick.financeControl.infra.utils.CpfUtils;
import samrick.financeControl.model.Usuario;

import java.time.LocalDateTime;

@Component
public class UsuarioMapper {
    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getProfissao(),
                usuario.getDataUltimaAlteracao(),
                usuario.getUsuarioUltimaAlteracao(),
                usuario.getTiposVinculo(),
                usuario.getPerfil()
        );
    }

    public Usuario toEntity(UsuarioRequestDTO dto, Usuario usuarioLogado){
        //Validação de CPF
        if (!CpfUtils.isValido(dto.cpf())){
            throw new RegraNegocioException("O CPF informado é inválido.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setProfissao(dto.profissao());
        usuario.setCpf(dto.cpf());
        usuario.setTiposVinculo(dto.tiposVinculo());
        usuario.setPerfil(dto.perfil());
        if (usuarioLogado != null){
            usuario.setUsuarioUltimaAlteracao(usuarioLogado.getNome());
            usuario.setDataUltimaAlteracao(LocalDateTime.now());
        }
        return usuario;
    }
}
