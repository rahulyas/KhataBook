package com.example.application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.EmpolyeeMangmentSystem.TaskActivity;
import com.example.application.EmpolyeeMangmentSystem.Task;
import com.example.application.R;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    private Context context;
    private ArrayList<Task> data;

    public TasksAdapter(Context context, ArrayList<Task> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public TasksAdapter.TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.tasks_layout,parent,false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.TasksViewHolder holder, int position) {
        final Task task = data.get(position);
        holder.titletask.setText(task.getTaskName());
        holder.disctask.setText(task.getTaskDetails());
        holder.datetask.setText(task.getTaskDate());
        //holder.deadlinetask.setText(task.getTaskDeadline());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titletask;
        TextView disctask;
        TextView datetask;
        TextView deadlinetask;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);
            titletask = itemView.findViewById(R.id.title_card);
            disctask = itemView.findViewById(R.id.disc_card);
            datetask = itemView.findViewById(R.id.date);
            deadlinetask= itemView.findViewById(R.id.deadline);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.v(" idfrom Ad", "" +  data.get(getAdapterPosition()).getId());
            Intent in = new Intent(context, TaskActivity.class);
            in.putExtra("data",data);
            Log.v("position", "" +  data.get(getAdapterPosition()));
            in.putExtra("position",  getAdapterPosition());
            in.putExtra("taskId", data.get(getAdapterPosition()).getId());
            context.startActivity(in);

        }
    }
}
