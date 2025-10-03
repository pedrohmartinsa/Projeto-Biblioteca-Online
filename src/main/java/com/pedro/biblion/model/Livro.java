package com.pedro.biblion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O título não pode ser vazio.")
    private String titulo;

    @NotEmpty(message = "O autor não pode ser vazio.")
    private String autor;

    @NotNull(message = "O ano de publicação é obrigatório.")
    private Integer anoPublicacao;

    @Enumerated(EnumType.STRING)
    private StatusLivro status;

    // Construtores
    public Livro() {
        this.status = StatusLivro.DISPONIVEL; // Status padrão ao criar
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public StatusLivro getStatus() { return status; }
    public void setStatus(StatusLivro status) { this.status = status; }
}