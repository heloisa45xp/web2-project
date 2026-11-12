package br.edu.ifto.pwebII.model.jdbc.repository;

import br.edu.ifto.pwebII.model.entity.Perfil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioPerfil {

    @PersistenceContext
    private EntityManager editm;

    public void save(Perfil perfil) {
        editm.persist(perfil);
    }

    public List<Perfil> perfis() {
        Query query = editm.createQuery("from Perfil");
        return query.getResultList();
    }

    public Perfil buscarPorNome(String nome) {
        Query query = editm.createQuery("select p from Perfil p where p.nome = :nome");
        query.setParameter("nome", nome);

        List<Perfil> resultado = query.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    public Perfil buscarOuCriar(String nome) {
        Perfil perfil = buscarPorNome(nome);

        if (perfil == null) {
            perfil = new Perfil(nome);
            editm.persist(perfil);
        }

        return perfil;
    }
}
