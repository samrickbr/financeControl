package samrick.financeControl.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import samrick.financeControl.exceptions.RegraNegocioException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TipoVinculo {
    FUNCIONARIO,
    CLIENTE,
    FORNECEDOR,
    OUTROS;

    public static String listarOpcoesTipo() {
        return Stream.of(TipoVinculo.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    @JsonCreator
    public static TipoVinculo fromValue(String value){
        for (TipoVinculo tipo : TipoVinculo.values()){
            if (tipo.name().toUpperCase().trim().equalsIgnoreCase(value)){
                return tipo;
            }
        }
        throw new RegraNegocioException("Tipo inválido. Opções aceitas: " + listarOpcoesTipo());
    }
}
