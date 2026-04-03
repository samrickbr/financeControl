package samrick.financeControl.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import samrick.financeControl.exceptions.RegraNegocioException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Perfilusuario {
    ADMIN, FINANCEIRO, COMUM;

    public static String listarOpcoesPerfil() {
        return Stream.of(Perfilusuario.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    @JsonCreator
    public static Perfilusuario fromValue(String value){
        for (Perfilusuario perfil : Perfilusuario.values()){
            if (perfil.name().toUpperCase().trim().equalsIgnoreCase(value)){
                return perfil;
            }
        }
        throw new RegraNegocioException("Perfil inválido. Opções aceitas: " + listarOpcoesPerfil());
    }
}
