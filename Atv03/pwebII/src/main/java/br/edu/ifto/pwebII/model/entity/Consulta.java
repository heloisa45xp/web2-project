package br.edu.ifto.pwebII.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity

public class Consulta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private LocalDate data;
    private double valor;
    private String observacao;

    public Consulta(Long id, LocalDate data, double valor, String observacao, String nome) {
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.observacao = observacao;
    }

    //associaçao aqui
    @ManyToOne
    @JoinColumn(name = "id_paciente") //coluna associativa
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Medico medico;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
