package br.edu.ifto.pwebII.model.jdbc.repository;

import br.edu.ifto.pwebII.model.entity.Consulta;
import br.edu.ifto.pwebII.model.entity.Paciente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class RepositorioConsulta {

    @PersistenceContext
    private EntityManager editm;

    public void save(Consulta consulta) {
        editm.persist(consulta);
    }

    public Consulta consulta(Long id) {
        return editm.find(Consulta.class, id);
    }

    public List<Consulta> consultas() {
        Query query = editm.createQuery("from Consulta");
        return query.getResultList();
    }

    public List<Consulta> consultasPorPaciente(Long pacienteId) {
        Query query = editm.createQuery("select c from Consulta c where c.paciente.id = :pacienteId");
        query.setParameter("pacienteId", pacienteId);
        return query.getResultList();
    }

    public List<Consulta> consultasPorMedico(Long medicoId) {
        Query query = editm.createQuery("select c from Consulta c where c.medico.id = :medicoId");
        query.setParameter("medicoId", medicoId);
        return query.getResultList();
    }

    public void remove(Long id) {
        Consulta c = editm.find(Consulta.class, id);
        if (c != null) {
            editm.remove(c);
        }
    }

    public void update(Consulta consulta) {
        editm.merge(consulta);
    }
}
