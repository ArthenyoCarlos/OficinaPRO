package br.com.oficinapro.common.util;

public class ValidationCpfCnpj {

    public static String onlyDigits(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        return value.replaceAll("\\D", "");
    }

    public static boolean validationCpfCnpj(String cpfCnpj) {
        cpfCnpj = onlyDigits(cpfCnpj);

        if (cpfCnpj.isBlank()) {
            return false;
        }
        return cpfCnpj.matches("^[0-9]{11}$") || cpfCnpj.matches("^[0-9]{14}$");
    }

    public static boolean validationCpf(String cpf) {
        cpf = onlyDigits(cpf);

        if (cpf.isBlank()) {
            return false;
        }
        return cpf.matches("^[0-9]{11}$");
    }

    public static boolean validationCnpj(String cnpj) {
        cnpj = onlyDigits(cnpj);

        if (cnpj.isBlank()) {
            return false;
        }
        return cnpj.matches("^[0-9]{14}$");
    }
}
