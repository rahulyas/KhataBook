package com.example.application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.Contract.EmployeeContract;
import com.example.application.R;

import java.io.File;

public class EmployeeAdapter extends CursorAdapter {
    public EmployeeAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.employee_item, parent, false);
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(cursor.getString(cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME)));
        TextView job = (TextView)view.findViewById(R.id.post);
        String post = cursor.getString(cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_JOB)).trim();

        if(post.isEmpty() || post == null){
            job.setText("POST");
        }else{
            job.setText(post);
        }

        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        //saving img path in database as string
        //in case no img was chosen or chosen img is deleted , employee's img is set to default one "unknown"
        String path = cursor.getString(cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_PHOTO));
        if(!TextUtils.isEmpty(path) && (new File(path)).exists()){
            icon.setImageBitmap(BitmapFactory.decodeFile(path));
        }else{
            icon.setImageResource(R.drawable.unknown);
        }
    }
}
