package br.edu.ifto.pwebII.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PF")
public class PessoaFisica extends Pessoa {

    private String nome;
    private String cpf;

    public PessoaFisica() {
    }

    public PessoaFisica(String nome, String cpf, String email, String telefone) {
        super(email, telefone);
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}