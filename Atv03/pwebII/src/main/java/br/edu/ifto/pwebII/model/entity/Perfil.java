package br.edu.ifto.pwebII.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Perfil implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String nome;

    public Perfil() {
    }

    public Perfil(String nome) {
        this.nome = nome;
    }

    @Override
    public String getAuthority() {
        if (nome == null) return null;
        return nome.startsWith("ROLE_") ? nome : "ROLE_" + nome;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perfil perfil = (Perfil) o;
        return Objects.equals(nome, perfil.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }

    @Override
    public String toString() {
        return nome;
    }
}
