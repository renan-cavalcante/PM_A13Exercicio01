package com.example.pm_a13exercicio01.model;

public class Livro extends  Exemplar{
    private String ISBN;
    private int edicao;

    public Livro(){
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getEdicao() {
        return edicao;
    }

    public void setEdicao(int edicao) {
        this.edicao = edicao;
    }
}
