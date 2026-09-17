package br.edu.ifto.pwebII.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Medico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String crm;

    public Medico(Long id, String nome, String crm) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
    }

    @OneToMany(mappedBy = "medico")
    private List<Consulta> consultas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String dados() {
        return "Médico: " + nome + " - CRM: " + crm;
    }
}
