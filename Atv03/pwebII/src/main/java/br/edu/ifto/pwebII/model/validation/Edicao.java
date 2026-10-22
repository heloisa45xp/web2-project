package br.edu.ifto.pwebII.model.validation;

/**
 * Grupo de validacao aplicado nas operacoes de <strong>edicao</strong>.
 *
 * <p>As restricoes que valem para o cadastro (e para a edicao) pertencem ao
 * grupo {@code Default}. As restricoes exclusivas da edicao sao declaradas
 * com {@code groups = Edicao.class} e so sao verificadas quando o grupo e
 * solicitado, por exemplo:</p>
 *
 * <pre>
 * &#64;Validated({Default.class, Edicao.class})
 * &#64;ModelAttribute("consulta") Consulta consulta,
 * BindingResult result
 * </pre>
 *
 * <p>Exemplo de uso: na edicao o identificador do registro e obrigatorio,
 * pois a operacao atualiza um registro ja existente no banco de dados.</p>
 */
public interface Edicao {
}
