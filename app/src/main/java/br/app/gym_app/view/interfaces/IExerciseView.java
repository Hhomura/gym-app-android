package br.app.gym_app.view.interfaces;

import java.util.List;

import br.app.gym_app.model.Exercise;

public interface IExerciseView {
    void onSucess(String msg);
    void onError(String msg);
    void onProgressBar();
    void onRedirectionLogin();
    void initializeRecyclerView(List<Exercise> exerciseList);
}
