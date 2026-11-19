package br.edu.ifto.pwebII.model.jdbc.repository;

import br.edu.ifto.pwebII.model.entity.Secretario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioSecretario {

    @PersistenceContext
    private EntityManager em;

    public void save(Secretario secretario) {
        em.persist(secretario);
    }

    public Secretario secretario(Long id) {
        return em.find(Secretario.class, id);
    }

    public List<Secretario> secretarios() {
        Query query = em.createQuery("from Secretario");
        return query.getResultList();
    }

    public void remove(Long id) {
        Secretario s = em.find(Secretario.class, id);
        if (s != null) {
            em.remove(s);
        }
    }

    public void update(Secretario secretario) {
        Secretario managed = em.find(Secretario.class, secretario.getId());
        if (managed == null) {
            return;
        }

        managed.setNome(secretario.getNome());
        managed.setCpf(secretario.getCpf());
        managed.setEmail(secretario.getEmail());
        managed.setTelefone(secretario.getTelefone());

        if (secretario.getUsuario() != null) {
            if (managed.getUsuario() == null) {
                managed.setUsuario(new br.edu.ifto.pwebII.model.entity.Usuario());
            }
            if (secretario.getUsuario().getUsuario() != null && !secretario.getUsuario().getUsuario().isBlank()) {
                managed.getUsuario().setUsuario(secretario.getUsuario().getUsuario());
            }
            if (secretario.getUsuario().getSenha() != null && !secretario.getUsuario().getSenha().isBlank()) {
                managed.getUsuario().setSenha(secretario.getUsuario().getSenha());
            }
        }
    }
}
