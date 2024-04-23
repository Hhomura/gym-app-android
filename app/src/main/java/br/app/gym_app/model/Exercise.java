package br.app.gym_app.model;

public class Exercise {
    private String id;
    private String nome;
    private String url;
    private String observacoes;

    public Exercise(String id, String nome, String url, String observacoes) {
        this.id = id;
        this.nome = nome;
        this.url = url;
        this.observacoes = observacoes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
