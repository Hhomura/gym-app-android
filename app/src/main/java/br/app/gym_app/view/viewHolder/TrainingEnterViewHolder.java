package br.app.gym_app.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.app.gym_app.view.R;

public class TrainingEnterViewHolder extends RecyclerView.ViewHolder {
    public TextView name, obs;

    public TrainingEnterViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nome_enter_tr);
        obs = itemView.findViewById(R.id.obs_enter_tr);
    }
}
