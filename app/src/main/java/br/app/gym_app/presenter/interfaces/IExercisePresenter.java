package br.app.gym_app.presenter.interfaces;

import android.net.Uri;

import java.util.List;

import br.app.gym_app.model.Exercise;
import br.app.gym_app.presenter.callback.ExerciseCallback;

public interface IExercisePresenter {
    void createExercise(String name, String observation, Uri uri);
    void updateExercise(String name, String observation, Uri uri);
    void getAllExercisies(ExerciseCallback callback);
}
