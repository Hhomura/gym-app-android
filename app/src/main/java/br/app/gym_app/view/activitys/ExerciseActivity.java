package br.app.gym_app.view.activitys;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import br.app.gym_app.model.Exercise;
import br.app.gym_app.presenter.ExerciseActivityPresenter;
import br.app.gym_app.utils.SharedPreferencesManager;
import br.app.gym_app.view.R;
import br.app.gym_app.view.interfaces.IExerciseView;

public class ExerciseActivity extends AppCompatActivity implements IExerciseView {

    private Button btnCreate, btnShowList;
    private ImageView btn_back, imgExercise;
    private EditText edt_name, edt_observation;
    private ExerciseActivityPresenter mPresenter;
    private SharedPreferencesManager manager;
    //private RecyclerView recyclerView;
    boolean update;
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
        edt_name = findViewById(R.id.edt_training_description);
        edt_observation = findViewById(R.id.edt_observation_exercise);
        mPresenter = new ExerciseActivityPresenter(getApplicationContext(), this);
        btnShowList = findViewById(R.id.btn_show_list_exercisie);
        manager = new SharedPreferencesManager(getApplicationContext());
        update = manager.getPreferences().getBoolean("update", false);
        if(update){
            btnShowList.setEnabled(false);
            btnShowList.setVisibility(View.GONE);
            btnCreate.setText("Atualizar");
            edt_name.setText(manager.getPreferences().getString("nome_upd", ""));
            edt_observation.setText((manager.getPreferences().getString("observacoes", "")));
            showImg(imgExercise, manager.getPreferences().getString("url_upd", ""));
        }
    }

    public void listeners(){
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update){
                    Log.e("Opa", "Opas");
                    mPresenter.updateExercise(manager.getPreferences().getString("id_upd", ""),
                            edt_name.getText().toString(), edt_observation.getText().toString(), imgUri);
                }else{
                    mPresenter.createExercise(edt_name.getText().toString(),
                            edt_observation.getText().toString(), imgUri);
                }
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
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ExercisieListActivity.class);
                startActivity(i);
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

    public void showImg(ImageView view, String filename){
        StorageReference reference = FirebaseStorage.getInstance().getReference("exerc/"+filename);
        Log.e("References", reference.getPath());
        try{
            File localFile = File.createTempFile("tempFile", ".jpg");
            reference.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    view.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Erro na Renderização do Update", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext().getApplicationContext(), "Erro na procura da imagem no update", Toast.LENGTH_SHORT).show();
            Log.e("Error", e.toString());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (update) {
            manager.getEditor().putBoolean("update", false);
            manager.getEditor().putString("id_upd", "");
            manager.getEditor().putString("nome_upd", "");
            manager.getEditor().putString("observacoes", "");
            manager.getEditor().putString("url_upd", "");
            manager.getEditor().apply();
            finish();
        }
    }
}