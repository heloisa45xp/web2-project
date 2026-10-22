package br.edu.ifto.pwebII.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricao customizada para valores monetarios de consulta.
 *
 * <p>O atributo {@code Consulta.valor} e do tipo {@code double}. Nesse tipo,
 * as restricoes decimais da especificacao (@DecimalMin, @DecimalMax e
 * @Digits) nao sao suportadas e lancam {@code UnexpectedTypeException} durante
 * a validacao. Esta restricao cobre as duas regras que faltam:</p>
 * <ul>
 *     <li>o valor deve ter no maximo duas casas decimais;</li>
 *     <li>o valor nao pode ultrapassar R$ 1.000.000,00.</li>
 * </ul>
 *
 * <p>Uso:</p>
 * <pre>
 * &#64;ValorMonetario(message = "Informe um valor valido para a consulta.")
 * private double valor;
 * </pre>
 */
@Documented
@Constraint(validatedBy = ValorMonetarioValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValorMonetario {

    String message() default "Informe um valor valido para a consulta.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
