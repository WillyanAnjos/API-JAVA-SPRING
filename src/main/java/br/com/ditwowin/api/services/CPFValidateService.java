package br.com.ditwowin.api.services;

public class CPFValidateService {
    public static boolean validateCPF(String cpf) {
            if (cpf == null || cpf.length() != 11 || cpf.matches("[^0-9]+")) {
                return false;
            }

            char[] digits = cpf.toCharArray();
            int firstDigitCheck = 0;
            int secondDigitCheck = 0;

            for (int i = 0; i < 9; i++) {
                firstDigitCheck += (digits[i] - '0') * (10 - i);
                secondDigitCheck += (digits[i] - '0') * (11 - i);
            }

            firstDigitCheck %= 11;
            firstDigitCheck = (firstDigitCheck < 2) ? 0 : 11 - firstDigitCheck;

            if (firstDigitCheck != (digits[9] - '0')) {
                return false;
            }

            secondDigitCheck += firstDigitCheck * 2;
            secondDigitCheck %= 11;
            secondDigitCheck = (secondDigitCheck < 2) ? 0 : 11 - secondDigitCheck;

            if (secondDigitCheck != (digits[10] - '0')) {
                return false;
            }

            return true;
        }
}
