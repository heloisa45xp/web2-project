package br.edu.ifto.pwebII.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PACIENTE")
public class Paciente extends PessoaFisica {

    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas = new ArrayList<>();

    public Paciente() {
    }

    public Paciente(String nome, String cpf, String email, String telefone) {
        super(nome, cpf, email, telefone);
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public String dados() {
        return "Paciente: " + getNome() + " - Tel: " + getTelefone();
    }
}