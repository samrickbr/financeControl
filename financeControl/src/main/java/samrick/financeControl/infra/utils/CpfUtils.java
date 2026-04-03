package samrick.financeControl.infra.utils;

public class CpfUtils {

    public static boolean isValido(String cpf) {
        if (cpf == null) return false;

        // Remove qualquer caractere que não seja número
        cpf = cpf.replaceAll("\\D", "");

        // CPFs devem ter 11 dígitos e não podem ser sequências repetidas (111.111...)
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Cálculo do 1º dígito verificador
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            int resto = 11 - (soma % 11);
            char digito10 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

            // Cálculo do 2º dígito verificador
            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            resto = 11 - (soma % 11);
            char digito11 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

            return (digito10 == cpf.charAt(9)) && (digito11 == cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }
}