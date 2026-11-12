package br.edu.ifto.pwebII.model.jdbc.repository;

import br.edu.ifto.pwebII.model.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioUsuario {

    @PersistenceContext
    private EntityManager editm;

    public void save(Usuario usuario) {
        editm.persist(usuario);
    }

    public Usuario usuario(Long id) {
        return editm.find(Usuario.class, id);
    }

    public List<Usuario> usuarios() {
        Query query = editm.createQuery("from Usuario");
        return query.getResultList();
    }

    public Usuario buscarPorUsuario(String usuario) {
        Query query = editm.createQuery("select distinct u from Usuario u left join fetch u.perfis where u.usuario = :usuario");
        query.setParameter("usuario", usuario);

        List<Usuario> resultado = query.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    public boolean existeUsuario(String usuario, Long idIgnorado) {
        String jpql = "select count(u) from Usuario u where u.usuario = :usuario";

        if (idIgnorado != null) {
            jpql += " and u.id <> :id";
        }

        Query query = editm.createQuery(jpql);
        query.setParameter("usuario", usuario);

        if (idIgnorado != null) {
            query.setParameter("id", idIgnorado);
        }

        return ((Number) query.getSingleResult()).longValue() > 0;
    }

    public void remove(Long id) {
        Usuario usuario = editm.find(Usuario.class, id);
        if (usuario != null) {
            editm.remove(usuario);
        }
    }
}
