package br.app.gym_app.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import br.app.gym_app.domain.FirebaseDomain;
import br.app.gym_app.model.Exercise;
import br.app.gym_app.presenter.callback.ExerciseCallback;
import br.app.gym_app.presenter.interfaces.IExcercisieListPresenter;
import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.activitys.ExerciseActivity;
import br.app.gym_app.view.interfaces.IExercisieListView;

public class ExercisieListPresenter implements IExcercisieListPresenter {

    private Context context;
    private IExercisieListView view;
    private FirebaseDomain domain;
    private SharedPreferencesManager manager;

    public ExercisieListPresenter(Context context, IExercisieListView view) {
        this.context = context;
        this.view = view;
        domain = new FirebaseDomain(context);
        manager = new SharedPreferencesManager(context);
    }

    @Override
    public void getAllExercisies(ExerciseCallback callback) {
        domain.getFirebaseFireStore().collection("exercicios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Exercise> exerciseList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Processar documentos e adicionar à lista de exercícios
                        String id = document.getId();
                        Log.e("UKLTRQ", id);
                        String nome = document.getString("nome");
                        String observacoes = document.getString("observacoes");
                        String url = document.getString("url");
                        Exercise exercise = new Exercise(id, nome, url, observacoes);
                        exerciseList.add(exercise);
                    }
                    callback.onExercisesLoaded(exerciseList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.onError("Erro na busca das listas");
            }
        });
    }

    @Override
    public void updateExercise(String id, String nome, String observacoes, String url) {
        manager.getEditor().putBoolean("update", true);
        manager.getEditor().putString("id_upd", id);
        manager.getEditor().putString("nome_upd", nome);
        manager.getEditor().putString("observacoes", observacoes);
        manager.getEditor().putString("url_upd", url);
        manager.getEditor().apply();
        view.onRedirectionToExercisie();
    }

    @Override
    public void deleteExercisie(String id, String url) {
        domain.getFirebaseFireStore().collection("exercicios")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if(!url.isEmpty()){
                                deleteExercisePhoto(url);
                            }else{
                                view.onSuccess("Exercício excluído com sucesso!");
                            }
                        } else {
                            view.onError("Falha ao excluir o exercício!");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.onError("Falha ao excluir o exercício!");
                    }
                });
    }

    private void deleteExercisePhoto(String url) {
        StorageReference storageRef = domain.initializeStorageExerc(url);
        storageRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        view.onSuccess("Exercício excluído com sucesso!");
                        view.onRedirectionToExercisie();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.onError("Falha ao excluir a foto do exercício!");
                    }
                });
    }
}
