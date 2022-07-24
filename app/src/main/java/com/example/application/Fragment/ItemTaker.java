package com.example.application.Fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.application.Model.Items;
import com.example.application.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemTaker extends AppCompatActivity {
    EditText editTitle,editnotes,editrate;
    ImageView imageView_save;
    Items notes;
    boolean isOldNote= false;
    //String rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_taker);

        editTitle = findViewById(R.id.editTitle);
        editnotes = findViewById(R.id.editnotes);
        editrate = findViewById(R.id.editrate);
        imageView_save = findViewById(R.id.imageView_save);


        notes = new Items();
        try{
            notes = (Items) getIntent().getSerializableExtra("old_note");
            editTitle.setText(notes.getTitle());
            editnotes.setText(notes.getNotes());
            editrate.setText(notes.getRate());
            isOldNote=true;

        }catch (Exception e){
            e.printStackTrace();
        }

        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString();
                String description = editnotes.getText().toString();
                String rate =editrate.getText().toString();

                if (description.isEmpty()){
                    Toast.makeText(ItemTaker.this,"Please add some notes!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                if(!isOldNote){
                    notes =new Items();
                }


                notes.setTitle(title);
                notes.setDate(formatter.format(date));
                notes.setNotes(description);
                notes.setRate(rate);

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}