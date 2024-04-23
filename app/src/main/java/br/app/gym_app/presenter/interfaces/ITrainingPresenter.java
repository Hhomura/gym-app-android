package br.app.gym_app.presenter.interfaces;

import br.app.gym_app.presenter.callback.ExerciseCallback;

public interface ITrainingPresenter {
    void getAllExercisies(ExerciseCallback callback);
}
