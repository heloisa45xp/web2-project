package br.edu.ifto.pwebII.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@DiscriminatorValue("PJ")
public class PessoaJuridica extends Pessoa {

    @NotBlank(message = "Informe a razao social.")
    @Size(max = 150, message = "A razao social deve ter no maximo {max} caracteres.")
    private String razaoSocial;

    @NotBlank(message = "Informe o CNPJ.")
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$",
            message = "Informe um CNPJ valido, por exemplo 00.000.000/0000-00.")
    private String cnpj;

    public PessoaJuridica() {
    }

    public PessoaJuridica(String razaoSocial, String cnpj, String email, String telefone) {
        super(email, telefone);
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}