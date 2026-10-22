package br.edu.ifto.pwebII.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricao customizada que verifica a validade do CPF, incluindo os
 * digitos verificadores.
 *
 * <p>O {@code @Pattern} garante apenas o <em>formato</em> do CPF (11 digitos).
 * Esta restricao complementa o formato conferindo o calculo dos dois digitos
 * verificadores, exatamente como a Receita Federal faz. Um CPF como
 * "000.000.000-00" tem o formato correto, porem nao passa no calculo.</p>
 *
 * <p>Uso:</p>
 * <pre>
 * &#64;Cpf(message = "Informe um CPF valido.")
 * private String cpf;
 * </pre>
 *
 * <p>A anotacao pode ser declarada tambem em metodos, parametros e tipos,
 * seguindo o padrao das restricoes da especificacao Jakarta Validation.</p>
 */
@Documented
@Constraint(validatedBy = CpfValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cpf {

    String message() default "Informe um CPF valido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
