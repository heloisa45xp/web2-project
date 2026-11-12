package br.edu.ifto.pwebII.model.entity;

import br.edu.ifto.pwebII.model.validation.Edicao;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Informe o identificador do registro.", groups = Edicao.class)
    private Long id;

    @NotBlank(message = "Informe o e-mail.")
    @Email(message = "Informe um e-mail valido.")
    @Size(max = 100, message = "O e-mail deve ter no maximo {max} caracteres.")
    private String email;

    @NotBlank(message = "Informe o telefone.")
    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$",
            message = "Informe um telefone valido com DDD, por exemplo (63) 99999-9999.")
    @Size(max = 20, message = "O telefone deve ter no maximo {max} caracteres.")
    private String telefone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario = new Usuario();

    public Pessoa() {
    }

    public Pessoa(String email, String telefone) {
        this.email = email;
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void criarUsuario(String login, String senhaCodificada, Perfil... perfis) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsuario(login);
        novoUsuario.setSenha(senhaCodificada);
        if (perfis != null) {
            for (Perfil p : perfis) {
                novoUsuario.adicionarPerfil(p);
            }
        }
        this.usuario = novoUsuario;
    }
}