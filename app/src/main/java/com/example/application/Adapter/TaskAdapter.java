package com.example.application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.application.EmpolyeeMangmentSystem.EmployeeActivity;
import com.example.application.R;

public class TaskAdapter extends CursorAdapter {

    public TaskAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task, parent, false);
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView)view.findViewById(R.id.taskName);
        name.setText(cursor.getString(cursor.getColumnIndex("TaskName")));
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar_task);
        ratingBar.setRating(cursor.getInt(cursor.getColumnIndex("Evaluation")));

    }
}
