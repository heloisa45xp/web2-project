package br.edu.ifto.pwebII.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MEDICO")
public class Medico extends PessoaFisica {

    private String crm;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Consulta> consultas = new ArrayList<>();

    public Medico() {
    }

    public Medico(String nome, String cpf, String email, String telefone, String crm) {
        super(nome, cpf, email, telefone);
        this.crm = crm;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public String dados() {
        return "Médico: " + getNome() + " - CRM: " + crm;
    }
}