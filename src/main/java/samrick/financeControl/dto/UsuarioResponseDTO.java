package samrick.financeControl.dto;

import samrick.financeControl.model.TipoVinculo;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String profissao,
        LocalDateTime dataUltimaAlteracao,
        String usuarioUltimaAlteracao,
        TipoVinculo tipoVinculo
)
{}
