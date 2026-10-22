package br.edu.ifto.pwebII.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricao customizada de classe aplicada a {@code Consulta}.
 *
 * <p>As restricoes de campo(@NotNull em paciente e medico) verificam se as
 * referencias foram preenchidas, porem nao verificam se o registro
 * selecionado no formulario realmente existe. Esta restricao de classe
 * completa essa lacuna conferindo, em conjunto, que a consulta possui um
 * paciente e um medico efetivamente selecionados.</p>
 *
 * <p>Uso:</p>
 * <pre>
 * &#64;ConsultaValida
 * public class Consulta { ... }
 * </pre>
 *
 * <p>As violacoes sao reportadas nos nos de propriedade "paciente" e
 * "medico", permitindo que cada mensagem apareca junto ao campo
 * correspondente do formulario.</p>
 */
@Documented
@Constraint(validatedBy = ConsultaValidaValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsultaValida {

    String message() default "Selecione o paciente e o medico da consulta.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
