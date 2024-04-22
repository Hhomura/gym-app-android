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
import br.app.gym_app.view.interfaces.IExerciseView;

public class ExerciseActivityPresenter implements IExercisePresenter {

    private Context context;
    private IExerciseView view;
    private FirebaseDomain domain;
    List<Exercise> list = new ArrayList<>();

    public ExerciseActivityPresenter(Context context, IExerciseView view) {
        this.context = context;
        this.view = view;
        domain = new FirebaseDomain(context);
    }

    @Override
    public void createExercise(String name, String observation, Uri uri) {
        String urlImg = idRandom();
        Exercise exercise = new Exercise(idRandomNumber(), name, urlImg, observation);

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
    public void updateExercise(String name, String observation, Uri uri) {

    }

    @Override
    public void getAllExercisies(ExerciseCallback callback) {
        List<Exercise> exerciseList = new ArrayList<>();
        domain.getFirebaseFireStore().collection("exercicios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Exercise> exerciseList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Processar documentos e adicionar à lista de exercícios
                        String id = document.getId();
                        long idInt = document.getLong("id");
                        String nome = document.getString("nome");
                        String observacoes = document.getString("observacoes");
                        String url = document.getString("url");
                        Exercise exercise = new Exercise((int) idInt, nome, url, observacoes);
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

    public String idRandom(){
        return UUID.randomUUID().toString();
    }

    public int idRandomNumber(){return new Random().nextInt(Integer.MAX_VALUE);}


}
