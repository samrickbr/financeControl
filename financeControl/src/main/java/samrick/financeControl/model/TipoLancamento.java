package samrick.financeControl.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import samrick.financeControl.exceptions.RegraNegocioException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TipoLancamento {
    RECEITA, DESPESA, PIX;

    public static String listarOpcoes() {
        return Stream.of(TipoLancamento.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    @JsonCreator
    public static TipoLancamento fromValue(String value){
        for (TipoLancamento tipo : TipoLancamento.values()){
            if (tipo.name().equalsIgnoreCase(value)){
                return tipo;
            }
        }
        throw new RegraNegocioException("Tipo inválido. Opções aceitas: " + listarOpcoes());
    }
}
