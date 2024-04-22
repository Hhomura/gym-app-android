package br.app.gym_app.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import br.app.gym_app.model.Exercise;
import br.app.gym_app.view.R;
import br.app.gym_app.view.viewHolder.ExercisieViewHolder;

public class ExercisieAdapter extends RecyclerView.Adapter<ExercisieViewHolder> {

    Context context;
    List<Exercise> exerciseList;

    public ExercisieAdapter(Context context, List<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExercisieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExercisieViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exercisie_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisieViewHolder holder, int position) {
        Log.e("AAA", "AAA");
        holder.name.setText(exerciseList.get(position).getNome());
        holder.observation.setText(exerciseList.get(position).getObservacoes());
        showImg(holder.imgExercisie, exerciseList.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
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
                    Toast.makeText(context.getApplicationContext(), "Erro na Renderização do Adapter", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(context.getApplicationContext(), "Erro na procura da imagem no Adapter", Toast.LENGTH_SHORT).show();
            Log.e("Error", e.toString());
        }
    }
}
