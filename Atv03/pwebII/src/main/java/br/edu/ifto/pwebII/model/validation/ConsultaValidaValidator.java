package br.edu.ifto.pwebII.model.validation;

import br.edu.ifto.pwebII.model.entity.Consulta;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador da restricao de classe {@link ConsultaValida}.
 *
 * <p>No formulario de consulta, os campos de selecao enviam apenas o
 * identificador do paciente e do medico. Quando o usuario nao escolhe
 * nenhuma opcao, o Spring MVC cria as entidades associadas com o id nulo,
 * e nao a ausencia do proprio objeto. Por isso a verificacao e feita sobre
 * o identificador da referencia e nao apenas sobre a referencia.</p>
 *
 * <p>Cada falha e reportada em um campo especifico (no de propriedade), para
 * que a mensagem seja exibida junto ao seletor correspondente no
 * formulario.</p>
 */
public class ConsultaValidaValidator implements ConstraintValidator<ConsultaValida, Consulta> {

    @Override
    public boolean isValid(Consulta consulta, ConstraintValidatorContext context) {
        if (consulta == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        boolean valida = true;

        if (consulta.getPaciente() != null && consulta.getPaciente().getId() == null) {
            context.buildConstraintViolationWithTemplate("Selecione o paciente da consulta.")
                    .addPropertyNode("paciente")
                    .addConstraintViolation();
            valida = false;
        }

        if (consulta.getMedico() != null && consulta.getMedico().getId() == null) {
            context.buildConstraintViolationWithTemplate("Selecione o medico da consulta.")
                    .addPropertyNode("medico")
                    .addConstraintViolation();
            valida = false;
        }

        return valida;
    }
}
