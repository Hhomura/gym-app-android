package br.app.gym_app.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.app.gym_app.model.Exercise;
import br.app.gym_app.view.R;
import br.app.gym_app.view.viewHolder.TrainingEnterViewHolder;

public class TrainingEnterAdapter extends RecyclerView.Adapter<TrainingEnterViewHolder> {
    Context context;
    public List<Exercise> exerciseList;

    public TrainingEnterAdapter(Context context, List<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public TrainingEnterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrainingEnterViewHolder((LayoutInflater.from(context).inflate(R.layout.item_training_enter, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingEnterViewHolder holder, int position) {
        holder.name.setText(exerciseList.get(position).getNome());
        holder.obs.setText(exerciseList.get(position).getObservacoes());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
