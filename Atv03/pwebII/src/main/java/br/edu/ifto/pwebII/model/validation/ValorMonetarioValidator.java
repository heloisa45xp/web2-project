package br.edu.ifto.pwebII.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Validador da restricao {@link ValorMonetario}.
 *
 * <p>A conversao do {@code double} para {@link BigDecimal} e feita com
 * {@code BigDecimal.valueOf}, que usa a representacao textual do numero e
 * portanto evita os desvios de arredondamento da conversao binaria (por
 * exemplo, 0.29 * 100 resulta em 28.999999999999996 em double).</p>
 *
 * <p>Cada regra violada gera uma mensagem propria, permitindo informar ao
 * usuario se o problema e o numero de casas decimais ou o valor maximo.</p>
 */
public class ValorMonetarioValidator implements ConstraintValidator<ValorMonetario, Double> {

    private static final int CASAS_DECIMAIS = 2;
    private static final BigDecimal VALOR_MAXIMO = new BigDecimal("1000000.00");

    @Override
    public boolean isValid(Double valor, ConstraintValidatorContext context) {
        if (valor == null) {
            return true;
        }

        BigDecimal decimal = BigDecimal.valueOf(valor);

        if (decimal.stripTrailingZeros().scale() > CASAS_DECIMAIS) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("O valor deve ter no maximo 2 casas decimais.")
                    .addConstraintViolation();
            return false;
        }

        if (decimal.compareTo(VALOR_MAXIMO) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("O valor da consulta nao pode ultrapassar R$ 1.000.000,00.")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
