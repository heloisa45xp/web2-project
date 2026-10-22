package br.edu.ifto.pwebII.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador da restricao {@link Cpf}.
 *
 * <p>Remove a pontuacao do CPF, confere se restam 11 digitos e calcula os dois
 * digitos verificadores:</p>
 * <ul>
 *     <li>o primeiro digito e obtido com os pesos de 10 a 2 sobre os 9 primeiros
 *     digitos; o resultado e o resto de (soma * 10) por 11, sendo 0 quando o
 *     resto e 10;</li>
 *     <li>o segundo digito usa os pesos de 11 a 2 sobre os 10 primeiros
 *     digitos, com o mesmo tratamento do resto.</li>
 * </ul>
 *
 * <p>Valores nulos ou vazios sao consideredos validos, pois a obrigatoriedade
 * do campo e definida separadamente pela restricao {@code @NotBlank}.</p>
 */
public class CpfValidator implements ConstraintValidator<Cpf, String> {

    private static final int TAMANHO_CPF = 11;

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isBlank()) {
            return true;
        }

        String digitos = cpf.replaceAll("\\D", "");

        if (digitos.length() != TAMANHO_CPF) {
            return false;
        }

        return confereDigitoVerificador(digitos, 9) && confereDigitoVerificador(digitos, 10);
    }

    /**
     * Confere o digito verificador que ocupa a posicao informada.
     *
     * @param digitos  os 11 digitos do CPF
     * @param posicao  indice do digito a conferir (9 para o primeiro, 10 para o segundo)
     * @return true quando o digito da posicao corresponde ao calculo
     */
    private boolean confereDigitoVerificador(String digitos, int posicao) {
        int soma = 0;

        for (int i = 0; i < posicao; i++) {
            soma += Character.getNumericValue(digitos.charAt(i)) * (posicao + 1 - i);
        }

        int resto = (soma * 10) % 11;
        int digitoCalculado = resto == 10 ? 0 : resto;

        return digitoCalculado == Character.getNumericValue(digitos.charAt(posicao));
    }
}
