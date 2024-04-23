package br.app.gym_app.view.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.app.gym_app.model.Exercise;
import br.app.gym_app.presenter.ExercisieListPresenter;
import br.app.gym_app.presenter.callback.ExerciseCallback;
import br.app.gym_app.view.R;
import br.app.gym_app.view.adapters.ExercisieListAdapter;
import br.app.gym_app.view.adapters.interfaces.IOnItemClickListener;
import br.app.gym_app.view.interfaces.IExercisieListView;

public class ExercisieListActivity extends AppCompatActivity implements IExercisieListView {

    private ExercisieListPresenter mPresenter;
    private RecyclerView recyclerView;
    private List<Exercise> exercisesCopy = new ArrayList<>();
    private ExercisieListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisie_list);

        init();
    }

    public void init(){
        mPresenter = new ExercisieListPresenter(getApplicationContext(), this);
        recyclerView = findViewById(R.id.rv_exercisie_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.getAllExercisies(new ExerciseCallback() {
            @Override
            public void onExercisesLoaded(List<Exercise> exercises) {
                // Fazer uma cópia da lista de exercícios
                exercisesCopy = exercises;
                adapter = new ExercisieListAdapter(getApplicationContext(), exercises);
                recyclerView.setAdapter(adapter);
                listeners();
            }
        });
    }

    public void listeners(){
        adapter.onItemClickListener(new IOnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                Exercise clickedExercise = exercisesCopy.get(position);
                Log.e("Nome", clickedExercise.getNome());
            }

            @Override
            public void onItemLongClick(int position) {
                Exercise clickedExercise = exercisesCopy.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExercisieListActivity.this);
                String[] options = {"Atualizar", "Deletar"};

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            mPresenter.updateExercise(clickedExercise.getId(),
                                    clickedExercise.getNome(), clickedExercise.getObservacoes(),
                                    clickedExercise.getUrl());
                        }
                        if( i == 1){
                            mPresenter.deleteExercisie(clickedExercise.getId(), clickedExercise.getUrl());
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
                Log.e("Nome", clickedExercise.getNome() + "Longo");
            }
        });
    }
    @Override
    public void onSuccess(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRedirectionToExercisie() {
        Intent i = new Intent(getApplicationContext(), ExerciseActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}