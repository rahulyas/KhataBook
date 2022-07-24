package com.example.application.khatabook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.Database.DatabaseHelper;
import com.example.application.R;

public class loginkhatabook extends AppCompatActivity {

    TextView redirectlink, phone_number ,error_msg;
    ImageView redirectpasscode;
    Intent intent;
    String database_passcode,database_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginkhatabook);

        // hooks

        redirectlink = findViewById(R.id.redirectlink);
        redirectpasscode = findViewById(R.id.redirectpasscode);
        phone_number = findViewById(R.id.phone_number);
        error_msg = findViewById(R.id.error_msg);

        // ClickListener

        redirectlink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(loginkhatabook.this, registraction.class);
                startActivity(intent);
                finish();
            }
        });

        // Here we Check user exist in the database or not .
        //if user exist goto Activity1 / if not goto registraction
        redirectpasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_no = phone_number.getText().toString();
                if (!phone_no.isEmpty() && phone_no.length() == 10 && phone_no.matches("^[0-9]{10}")) {
                    final String complete_phone_no = "+91" + phone_no;
                    DatabaseHelper myDB = new DatabaseHelper(loginkhatabook.this);
                    Cursor cursor = myDB.check_usernumber_exist(complete_phone_no,1);
                    if(cursor.getCount() == 0){
                        Toast.makeText(loginkhatabook.this, "No such user exist", Toast.LENGTH_SHORT).show();
                        intent = new Intent(loginkhatabook.this,registraction.class);
                    }
                    else{
                        while (cursor.moveToNext()){
                            database_id = cursor.getString(0);
                            database_passcode = cursor.getString(3);
                        }
                        intent = new Intent(loginkhatabook.this,passcode.class);
                        intent.putExtra("Id",database_id);
                        finish();
                        intent.putExtra("Passcode",database_passcode);
                    }
                    intent.putExtra("User_number",complete_phone_no);
                    startActivity(intent);
                    finish();
                }
                else{
                    phone_number.requestFocus();
                    if(phone_no.isEmpty()){
                        error_msg.setText("Mobile Number is required");
                    }
                    else if(phone_no.length() != 10){
                        error_msg.setText("Mobile Number is not valid");
                    }else{
                        phone_number.setError("Require only 10 digit");
                    }
                }
            }
        });
    }
}