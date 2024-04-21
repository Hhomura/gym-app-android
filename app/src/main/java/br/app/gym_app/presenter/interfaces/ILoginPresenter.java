package br.app.gym_app.presenter.interfaces;

import br.app.gym_app.model.User;

public interface ILoginPresenter {

    void signIn(String email, String senha);
    User getUser(String email);
}
