package br.app.gym_app.presenter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import br.app.gym_app.domain.FirebaseDomain;
import br.app.gym_app.model.User;
import br.app.gym_app.presenter.interfaces.ILoginPresenter;
import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.interfaces.ILoginView;

public class LoginActivityPresenter implements ILoginPresenter {

    private ILoginView view;
    private Context context;
    private FirebaseDomain firebaseDomain;
    private User user;
    private SharedPreferencesManager preferencesManager;

    public LoginActivityPresenter(ILoginView view, Context context) {
        this.view = view;
        this.context = context;
        firebaseDomain = new FirebaseDomain(context);
        preferencesManager = new SharedPreferencesManager(context);
    }

    @Override
    public void signIn(String email, String password) {
        firebaseDomain.getmAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseDomain.getmAuth().getCurrentUser();
                            firebaseDomain.setFirebaseUser(user);
                            getUser(email);
                            view.redirectedMainActivity();
                        } else {
                            view.onError("Erro no Login " + task.getException());
                        }
                    }
                });
    }

    @Override
    public User getUser(String email) {
        DocumentReference docRef = firebaseDomain.getFirebaseFireStore().collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String url = document.getString("url");
                        user = new User(name, email, url);
                        preferencesManager.getEditor().putString("name", user.getName());
                        preferencesManager.getEditor().putString("email", user.getEmail());
                        preferencesManager.getEditor().putString("url", user.getUrl());
                        preferencesManager.getEditor().apply();
                    } else {
                        Log.e(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return null;
    }

}
