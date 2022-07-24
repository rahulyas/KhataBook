package com.example.application.khatabook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.Database.DatabaseHelper;
import com.example.application.R;

public class registraction extends AppCompatActivity {

    TextView redirectlink, phone_number, error_msg;
    ImageView redirectotp;
    String database_passcode, database_id;
    Intent intent;
    ProgressBar progressBar;
    static final int REQUEST_CODE = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraction);
//hooks
        redirectlink = findViewById(R.id.redirectlink);
        redirectotp = findViewById(R.id.redirectotp);
        phone_number = findViewById(R.id.phone_number);
        error_msg = findViewById(R.id.error_msg);
        progressBar = findViewById(R.id.progressBar);
// Strict Mode
/* Strict mode is a developer tool which detectx things you might be doing by accident and brings them to your attention so you can fix them*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
// Click Listener
        redirectlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registraction.this, loginkhatabook.class);
                startActivity(intent);
                finish();
            }
        });
//OTP
        redirectotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String phone_no = phone_number.getText().toString();
                if (!phone_no.isEmpty() && phone_no.length() == 10 && phone_no.matches("^[0-9]{10}")) {
                    phone_number.setError(null);
                    phone_no = "+91" + phone_no;
                    DatabaseHelper myDB = new DatabaseHelper(registraction.this);
                    Cursor cursor = myDB.check_usernumber_exist(phone_no, 1);
                    if (cursor.getCount() == 0) {
                        intent = new Intent(registraction.this, otp.class);
                    } else {
                        while (cursor.moveToNext()) {
                            database_id = cursor.getString(0);
                            database_passcode = cursor.getString(3);
                        }
                        Toast.makeText(registraction.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                        intent = new Intent(registraction.this, passcode.class);
                        intent.putExtra("Id", database_id);
                        intent.putExtra("Passcode", database_passcode);
                    }
                    intent.putExtra("User_number", phone_no);
                    startActivity(intent);
                    finish();
                } else {
                    phone_number.requestFocus();
                    if (phone_no.isEmpty()) {
                        phone_number.setError("Mobile Number is required");
                    } else if (phone_no.length() != 10) {
                        phone_number.setError("Mobile Number is not valid");
                    }else{
                        phone_number.setError("Require only 10 digit");
                    }
                }


            }

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}