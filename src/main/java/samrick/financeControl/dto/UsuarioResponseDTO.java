package samrick.financeControl.dto;

import samrick.financeControl.model.PerfilUsuario;
import samrick.financeControl.model.TipoVinculo;

import java.time.LocalDateTime;
import java.util.List;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        String profissao,
        LocalDateTime dataUltimaAlteracao,
        String usuarioUltimaAlteracao,
        List<TipoVinculo> tiposVinculo,
        PerfilUsuario perfil
)
{}
