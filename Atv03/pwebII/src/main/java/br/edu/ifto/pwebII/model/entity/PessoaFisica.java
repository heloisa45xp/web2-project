package br.edu.ifto.pwebII.model.entity;

import br.edu.ifto.pwebII.model.validation.Cpf;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@DiscriminatorValue("PF")
public class PessoaFisica extends Pessoa {

    @NotBlank(message = "Informe o nome.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre {min} e {max} caracteres.")
    private String nome;

    @NotBlank(message = "Informe o CPF.")
    @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
            message = "Informe um CPF valido, por exemplo 000.000.000-00.")
    @Cpf(message = "O CPF informado possui digitos verificadores invalidos.")
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