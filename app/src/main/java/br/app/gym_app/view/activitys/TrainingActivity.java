package br.app.gym_app.view.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.app.gym_app.model.Exercise;
import br.app.gym_app.presenter.TrainingActivityPresenter;
import br.app.gym_app.presenter.callback.ExerciseCallback;
import br.app.gym_app.view.R;
import br.app.gym_app.view.adapters.TrainingEnterAdapter;
import br.app.gym_app.view.interfaces.ITrainingActivityView;

public class TrainingActivity extends AppCompatActivity implements ITrainingActivityView {

    private TrainingActivityPresenter mPresenter;
    private EditText edt_nome_treino, edt_description, edt_dt_treino;
    private Spinner spinner;
    private RecyclerView rv_training_view;
    private Button btn_add_training, btn_show_training;
    private TrainingEnterAdapter adapter;
    List<String> listExer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        init();
        listeners();
    }

    public void init(){
        mPresenter = new TrainingActivityPresenter(getApplicationContext(), this);
        edt_nome_treino = findViewById(R.id.edt_name_training);
        edt_description = findViewById(R.id.edt_training_description);
        edt_dt_treino = findViewById(R.id.edt_training_date);
        spinner = findViewById(R.id.spinner_training);
        rv_training_view = findViewById(R.id.rv_add_training);
        btn_add_training = findViewById(R.id.btn_add_training);
        btn_show_training = findViewById(R.id.btn_show_trainings);
        adapter = new TrainingEnterAdapter(getApplicationContext(), new ArrayList<>());
        listExer = new ArrayList<>();

            mPresenter.getAllExercisies(new ExerciseCallback() {
                @Override
                public void onExercisesLoaded(List<Exercise> exercises) {

                    for(int i = 0; i < exercises.size(); i++){
                        listExer.add(exercises.get(i).getNome());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, listExer);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Exercise ex = new Exercise(exercises.get(i).getId(), exercises.get(i).getNome(), exercises.get(i).getUrl(), exercises.get(i).getObservacoes());
                            
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            });
    }

    public void listeners(){
        btn_show_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TrainingListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onError(String msg) {

    }
}