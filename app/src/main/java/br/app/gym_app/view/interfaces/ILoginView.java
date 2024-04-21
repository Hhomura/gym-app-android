package br.app.gym_app.view.interfaces;

public interface ILoginView {
    void onSuccess(String msg);
    void onError(String msg);
    void redirectedMainActivity();
}
