package br.edu.ifto.pwebII.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SECRETARIO")
public class Secretario extends PessoaFisica {

    public Secretario() {
    }

    public Secretario(String nome, String cpf, String email, String telefone) {
        super(nome, cpf, email, telefone);
    }

    public String dados() {
        return "Secretário(a): " + getNome() + " - Tel: " + getTelefone();
    }
}
