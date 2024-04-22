package br.app.gym_app.view.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.app.gym_app.view.R;

public class ExercisieViewHolder extends RecyclerView.ViewHolder {

    public TextView name, observation;
    public ImageView imgExercisie;

    public ExercisieViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.item_name_exercisie);
        observation = itemView.findViewById(R.id.item_observation_exercisie);
        imgExercisie = itemView.findViewById(R.id.item_img_exercisie);
    }
}
