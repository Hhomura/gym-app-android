package br.app.gym_app.presenter.interfaces;

import android.net.Uri;

public interface IResgisterPresenter {

    void createAccountFirebase(String email, String senha, String name, Uri uri);

}
