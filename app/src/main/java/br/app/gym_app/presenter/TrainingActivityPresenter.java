package br.app.gym_app.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.app.gym_app.domain.FirebaseDomain;
import br.app.gym_app.model.Exercise;
import br.app.gym_app.model.Training;
import br.app.gym_app.presenter.callback.ExerciseCallback;
import br.app.gym_app.presenter.interfaces.ITrainingPresenter;
import br.app.gym_app.view.interfaces.ITrainingActivityView;

public class TrainingActivityPresenter implements ITrainingPresenter {

    private Context context;
    ITrainingActivityView view;
    private FirebaseDomain domain;
    List<Exercise> list = new ArrayList<>();

    public TrainingActivityPresenter(Context context, ITrainingActivityView view) {
        this.context = context;
        this.view = view;
        domain = new FirebaseDomain(context);
    }

    @Override
    public void getAllExercisies(ExerciseCallback callback) {
        domain.getFirebaseFireStore().collection("exercicios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Exercise> exerciseList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
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

    public void addExercisie(Exercise exercise){
        list.add(exercise);
        Toast.makeText(context, list.size()+"", Toast.LENGTH_SHORT).show();
    }

    public void createTraining(String name, String descricao, Timestamp data){
        Training training = new Training(idRandom(), name, descricao, data, list);
        domain.getFirebaseFireStore().collection("training").document().set(training)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        view.onSuccess("Cadastrado");
                        view.onRedirectionHome();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.onError("Erro");
                    }
                });
    }
    public String idRandom(){
        return UUID.randomUUID().toString();
    }
}
