package br.app.gym_app.presenter.interfaces;

import br.app.gym_app.presenter.callback.ExerciseCallback;

public interface IExcercisieListPresenter {
    void getAllExercisies(ExerciseCallback callback);
    void updateExercise(String id, String nome, String observacoes, String url);
    void deleteExercisie(String id, String url);
}
