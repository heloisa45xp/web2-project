package br.edu.ifto.pwebII.model.validation;

/**
 * Grupo de validacao aplicado nas operacoes de <strong>cadastro</strong>.
 *
 * <p>As restricoes que valem para o cadastro (e para a edicao) pertencem ao
 * grupo {@code Default}. As restricoes exclusivas do cadastro sao declaradas
 * com {@code groups = Cadastro.class} e so sao verificadas quando o grupo e
 * solicitado, por exemplo:</p>
 *
 * <pre>
 * &#64;Validated({Default.class, Cadastro.class})
 * &#64;ModelAttribute("consulta") Consulta consulta,
 * BindingResult result
 * </pre>
 *
 * <p>Exemplo de uso: a data de uma consulta nao pode estar no passado no
 * momento em que ela e agendada, mas precisa ser aceita na edicao de uma
 * consulta antiga, cuja data ja passou.</p>
 */
public interface Cadastro {
}
