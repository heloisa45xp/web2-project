package br.edu.ifto.pwebII.model.jdbc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio com as consultas sobre a tabela de pessoas.
 *
 * <p>Existe para centralizar as regras de unicidade do sistema. O CPF e o
 * e-mail identificam uma pessoa, e nao um tipo especifico de pessoa: um
 * paciente e um medico nao podem compartilhar o mesmo CPF, por exemplo.
 * Como a hierarquia usa a estrategia SINGLE_TABLE, todas as pessoas estao
 * na tabela {@code pessoa}, e a consulta sobre a entidade raiz {@code Pessoa}
 * alcanca todos os registros.</p>
 */
@Repository
public class RepositorioPessoa {

    @PersistenceContext
    private EntityManager editm;

    /**
     * Verifica se ja existe uma pessoa cadastrada com o CPF informado.
     *
     * @param cpf         CPF procurado
     * @param idIgnorado  identificador a desconsiderar na busca (usado na edicao
     *                    para nao encontrar o proprio registro)
     * @return true quando existe outro registro com o mesmo CPF
     */
    public boolean existeCpf(String cpf, Long idIgnorado) {
        String jpql = "select count(p) from Pessoa p where p.cpf = :cpf";

        if (idIgnorado != null) {
            jpql += " and p.id <> :id";
        }

        Query query = editm.createQuery(jpql);
        query.setParameter("cpf", cpf);

        if (idIgnorado != null) {
            query.setParameter("id", idIgnorado);
        }

        return ((Number) query.getSingleResult()).longValue() > 0;
    }

    /**
     * Verifica se ja existe uma pessoa cadastrada com o e-mail informado.
     *
     * @param email       e-mail procurado
     * @param idIgnorado  identificador a desconsiderar na busca (usado na edicao
     *                    para nao encontrar o proprio registro)
     * @return true quando existe outro registro com o mesmo e-mail
     */
    public boolean existeEmail(String email, Long idIgnorado) {
        String jpql = "select count(p) from Pessoa p where p.email = :email";

        if (idIgnorado != null) {
            jpql += " and p.id <> :id";
        }

        Query query = editm.createQuery(jpql);
        query.setParameter("email", email);

        if (idIgnorado != null) {
            query.setParameter("id", idIgnorado);
        }

        return ((Number) query.getSingleResult()).longValue() > 0;
    }
}
