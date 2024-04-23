package br.app.gym_app.presenter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import br.app.gym_app.domain.FirebaseDomain;
import br.app.gym_app.model.Exercise;
import br.app.gym_app.presenter.callback.ExerciseCallback;
import br.app.gym_app.presenter.interfaces.IExercisePresenter;
import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.interfaces.IExerciseView;

public class ExerciseActivityPresenter implements IExercisePresenter {

    private Context context;
    private IExerciseView view;
    private FirebaseDomain domain;
    private SharedPreferencesManager manager;
    List<Exercise> list = new ArrayList<>();

    public ExerciseActivityPresenter(Context context, IExerciseView view) {
        this.context = context;
        this.view = view;
        domain = new FirebaseDomain(context);
        manager = new SharedPreferencesManager(context);
    }

    @Override
    public void createExercise(String name, String observation, Uri uri) {
        String urlImg = idRandom();
        Exercise exercise;
        exercise = new Exercise(idRandom(), name, uri != null? urlImg: "", observation);

        domain.getFirebaseFireStore().collection("exercicios")
                .document().set(exercise).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(uri != null){
                            StorageReference reference = domain.initializeStorageExerc(urlImg);
                            reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    view.onSucess("Criado");
                                    view.onRedirectionLogin();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    view.onError("Falha na criação do usuário!");
                                }
                            });
                        }else{
                            Toast.makeText(context, "Criado", Toast.LENGTH_SHORT).show();
                            view.onRedirectionLogin();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void updateExercise(String id, String name, String observation, Uri uri) {
        Exercise exercise = new Exercise(idRandom(), name, manager.getPreferences().getString("url_upd", ""), observation);
        domain.getFirebaseFireStore().collection("exercicios")
                .document(id).set(exercise).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(uri != null){
                            StorageReference reference = domain.initializeStorageExerc(manager.getPreferences().getString("url_upd", ""));
                            reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    view.onSucess("Atualizado");
                                    manager.getEditor().putBoolean("update", false);
                                    manager.getEditor().putString("id_upd", "");
                                    manager.getEditor().putString("nome_upd", "");
                                    manager.getEditor().putString("observacoes", "");
                                    manager.getEditor().putString("url_upd", "");
                                    manager.getEditor().apply();
                                    view.onRedirectionLogin();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    view.onError("Falha na Atualização");
                                }
                            });
                        }else{
                            view.onSucess("Atualizado");
                            manager.getEditor().putBoolean("update", false);
                            manager.getEditor().putString("id_upd", "");
                            manager.getEditor().putString("nome_upd", "");
                            manager.getEditor().putString("observacoes", "");
                            manager.getEditor().putString("url_upd", "");
                            manager.getEditor().apply();
                            view.onRedirectionLogin();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public String idRandom(){
        return UUID.randomUUID().toString();
    }

    public int idRandomNumber(){return new Random().nextInt(Integer.MAX_VALUE);}


}
