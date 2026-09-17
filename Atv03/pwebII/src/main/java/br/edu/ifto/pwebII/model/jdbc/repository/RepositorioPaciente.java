package br.edu.ifto.pwebII.model.jdbc.repository;

import br.edu.ifto.pwebII.model.entity.Paciente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioPaciente {

    @PersistenceContext
    private EntityManager editm;

    public void save(Paciente paciente){
        editm.persist(paciente);
    }

    public Paciente paciente(Long id){
        return editm.find(Paciente.class, id);

    }

    public List<Paciente> pacientes(){
        Query query = editm.createQuery("from Paciente");
        return query.getResultList();
    }

    public void remove(Long id){
        Paciente p = editm.find(Paciente.class, id);
        if (p != null) {
            editm.remove(p);
        }
    }

    public void update(Paciente paciente){
        editm.merge(paciente);
    }









}

