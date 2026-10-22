package br.edu.ifto.pwebII.model.entity;

import br.edu.ifto.pwebII.model.validation.ConsultaValida;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@ConsultaValida
public class Consulta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Informe a data e a hora da consulta.")
    private LocalDateTime data;

    @PositiveOrZero(message = "O valor da consulta nao pode ser negativo.")
    private double valor;

    @Size(max = 500, message = "A observacao deve ter no maximo {max} caracteres.")
    private String observacao;

    @NotNull(message = "Selecione o paciente da consulta.")
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @NotNull(message = "Selecione o medico da consulta.")
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    public Consulta() {
    }

    public Consulta(LocalDateTime data, double valor, String observacao, Paciente paciente, Medico medico) {
        this.data = data;
        this.valor = valor;
        this.observacao = observacao;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}