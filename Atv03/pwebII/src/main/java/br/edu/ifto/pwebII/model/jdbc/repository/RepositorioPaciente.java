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

    /**
     * Atualiza os dados do paciente.
     *
     * <p>A atualizacao e feita sobre a entidade gerenciada, e nao com
     * {@code merge} do objeto detached. O {@code merge} copia a lista de
     * consultas (vazia, pois o formulario nao a envia) para a entidade
     * gerenciada e, como a collection possui {@code orphanRemoval}, o
     * Hibernate remove as consultas do paciente como orfas.</p>
     *
     * @param paciente dados do paciente a atualizar
     */
    public void update(Paciente paciente){
        Paciente managed = editm.find(Paciente.class, paciente.getId());

        if (managed == null) {
            return;
        }

        managed.setNome(paciente.getNome());
        managed.setCpf(paciente.getCpf());
        managed.setEmail(paciente.getEmail());
        managed.setTelefone(paciente.getTelefone());
    }









}

