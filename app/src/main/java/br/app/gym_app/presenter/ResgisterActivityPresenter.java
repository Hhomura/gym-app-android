package br.app.gym_app.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import br.app.gym_app.domain.FirebaseDomain;
import br.app.gym_app.presenter.interfaces.IResgisterPresenter;
import br.app.gym_app.view.interfaces.IRegisterView;

public class ResgisterActivityPresenter implements IResgisterPresenter {

    private FirebaseDomain firebaseDomain;
    private IRegisterView view;
    private Context context;

    public ResgisterActivityPresenter(Context context, IRegisterView view){
        firebaseDomain = new FirebaseDomain(context);
        this.context = context;
        this.view = view;
    }

    public void createAccountFirebase(String email, String password, String name, Uri uri){
        firebaseDomain.getmAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {// Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseDomain.getmAuth().getCurrentUser();
                            firebaseDomain.setFirebaseUser(user);
                            String fileImgName = nameFileImage();
                            Map<String, Object> userObject = new LinkedHashMap<>();
                            userObject.put("name", name);
                            userObject.put("email", email);
                            userObject.put("url", uri != null? fileImgName : "");

                            if(uri != null)
                                createUser(userObject, email, fileImgName, uri);
                            else
                                createUser(userObject, email, "", null);

                        } else {
                            view.onError("Erro na criação de Usuário!");
                        }
                    }
                });

    }

    public void createUser(Map<String, Object> user, String email, String filename, Uri uri){

        firebaseDomain.getFirebaseFireStore().collection("users").document(email).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (uri != null) {
                            StorageReference reference = firebaseDomain.initializeStorageUser(filename);
                            reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    view.onSuccess("Usuário criado com sucesso!");
                                    Log.e("AA", reference.getName());
                                    view.redirectedLogin();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    view.onError("Falha na criação do usuário!");
                                }
                            });
                        }else{
                            view.onSuccess("Usuário criado com sucesso!");
                            view.redirectedLogin();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.onError("Erro na Criação!");
                    }
                });
    }

    public String nameFileImage(){
        return UUID.randomUUID().toString();
    }
}
