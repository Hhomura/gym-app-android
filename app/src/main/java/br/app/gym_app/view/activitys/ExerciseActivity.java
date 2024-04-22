package br.app.gym_app.view.activitys;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.app.gym_app.model.Exercise;
import br.app.gym_app.presenter.ExerciseActivityPresenter;
import br.app.gym_app.presenter.callback.ExerciseCallback;
import br.app.gym_app.view.R;
import br.app.gym_app.view.adapters.ExercisieAdapter;
import br.app.gym_app.view.interfaces.IExerciseView;

public class ExerciseActivity extends AppCompatActivity implements IExerciseView {

    private Button btnCreate;
    private ImageView btn_back, imgExercise;
    private EditText edt_name, edt_observation;
    private ExerciseActivityPresenter mPresenter;
    private RecyclerView recyclerView;
    Uri imgUri;
    ActivityResultLauncher<String> getContentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        init();
        listeners();
    }

    public void init(){
        btnCreate = findViewById(R.id.btn_create_exercise);
        btn_back = findViewById(R.id.imb_back_exercise);
        imgExercise = findViewById(R.id.img_exercise);
        edt_name = findViewById(R.id.edt_exercise_name);
        edt_observation = findViewById(R.id.edt_observation_exercise);
        mPresenter = new ExerciseActivityPresenter(getApplicationContext(), this);
        recyclerView = findViewById(R.id.rv_exercisie_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.getAllExercisies(new ExerciseCallback() {
            @Override
            public void onExercisesLoaded(List<Exercise> exercises) {
                recyclerView.setAdapter(new ExercisieAdapter(getApplicationContext(), exercises));
            }
        });
        /*List<Exercise> exerciseList = Arrays.asList(
                new Exercise(0, "Exercício 1", "00000", "Observações do exercício 1"),
                new Exercise(0, "Exercício 1", "00000", "Observações do exercício 1"),
                new Exercise(0, "Exercício 1", "00000", "Observações do exercício 1"),
                new Exercise(0, "Exercício 1", "00000", "Observações do exercício 1"));
         */
    }

    public void listeners(){
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.createExercise(edt_name.getText().toString(),
                        edt_observation.getText().toString(), imgUri);
            }
        });

        imgExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentLauncher.launch("image/*");
            }
        });
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imgUri = result;
                            imgExercise.setImageURI(imgUri);
                        }
                    }
                });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imgUri = data.getData();
            imgExercise.setImageURI(imgUri);
        }
    }

    @Override
    public void onSucess(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressBar() {

    }

    @Override
    public void onRedirectionLogin() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void initializeRecyclerView(List<Exercise> exerciseList) {

    }
}