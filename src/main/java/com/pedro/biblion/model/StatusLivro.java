package com.pedro.biblion.model;

public enum StatusLivro {
    DISPONIVEL("Disponível"),
    EMPRESTADO("Emprestado");

    private final String descricao;

    StatusLivro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
