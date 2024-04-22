package br.app.gym_app.presenter.callback;

import java.util.List;

import br.app.gym_app.model.Exercise;

public interface ExerciseCallback {
    void onExercisesLoaded(List<Exercise> exercises);
}
