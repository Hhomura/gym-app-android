package br.app.gym_app.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Training {
    private int id;
    private String nome;
    private String descricao;
    private Timestamp timestamp;
    private List<Exercise> exerciseList;

    public Training(int id, String nome, String descricao, Timestamp timestamp, List<Exercise> exerciseList) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.timestamp = timestamp;
        this.exerciseList = exerciseList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }
}
