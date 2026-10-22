package br.edu.ifto.pwebII.model.jdbc.repository;

import br.edu.ifto.pwebII.model.entity.Medico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioMedico {
    @PersistenceContext
    private EntityManager editm;

  public void save(Medico medico){
      editm.persist(medico);
  }

  public Medico medico(Long id) {
      return editm.find(Medico.class, id);
  }

  public List<Medico> medicos() {
      Query query = editm.createQuery("from Medico");
      return query.getResultList();
  }

  public void remove(Long id) {
      Medico m = editm.find(Medico.class, id);
      if (m != null) {
          editm.remove(m);
      }
  }

  public void update(Medico medico) {
      editm.merge(medico);
  }

  /**
   * Verifica se ja existe um medico cadastrado com o CRM informado.
   *
   * @param crm         CRM procurado
   * @param idIgnorado  identificador a desconsiderar na busca (usado na edicao
   *                    para nao encontrar o proprio registro)
   * @return true quando existe outro medico com o mesmo CRM
   */
  public boolean existeCrm(String crm, Long idIgnorado) {
      String jpql = "select count(m) from Medico m where m.crm = :crm";

      if (idIgnorado != null) {
          jpql += " and m.id <> :id";
      }

      Query query = editm.createQuery(jpql);
      query.setParameter("crm", crm);

      if (idIgnorado != null) {
          query.setParameter("id", idIgnorado);
      }

      return ((Number) query.getSingleResult()).longValue() > 0;
  }

}

