package br.app.gym_app.view.interfaces;

public interface IRegisterView {

    void onSuccess(String msg);
    void onError(String msg);
    void redirectedLogin();
}
