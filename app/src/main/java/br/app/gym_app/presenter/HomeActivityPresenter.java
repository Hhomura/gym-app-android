package br.app.gym_app.presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import br.app.gym_app.domain.FirebaseDomain;
import br.app.gym_app.presenter.interfaces.IHomePresenter;
import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.R;
import br.app.gym_app.view.interfaces.IHomeView;

public class HomeActivityPresenter implements IHomePresenter {

    private Context context;
    private IHomeView view;
    private FirebaseDomain domain;
    private SharedPreferencesManager manager;

    public HomeActivityPresenter(Context context, IHomeView view) {
        this.context = context;
        this.view = view;
        domain = new FirebaseDomain(context);
        domain.checkConnection();
        domain.setFirebaseUser(domain.getmAuth().getCurrentUser());
        manager = new SharedPreferencesManager(context);
    }

    @Override
    public void logOut() {
        domain.getmAuth().signOut();
        view.onSuccess(context.getString(R.string.msg_logout));
        view.redirectionLogin();
    }

    @Override
    public void deleteUser() {
        domain.getmUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                domain.getFirebaseFireStore().collection("users")
                        .document(manager.getPreferences().getString("email", "")).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                StorageReference reference = FirebaseStorage.getInstance().
                                        getReference("users/"+manager.getPreferences().getString("url", ""));
                                reference.delete();

                                view.onSuccess("Usuário deletado com sucesso");
                                view.redirectionLogin();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                view.onError("Erro ao deletar");
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.onError("Erro no processo de deletar");
            }
        });
    }
}
