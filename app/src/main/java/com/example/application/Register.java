package com.example.application;

import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


public class Register extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailBox, passwordBox, nameBox;
    TextView loginBtn ;
    AppCompatButton signupBtn;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        emailBox = findViewById(R.id.emailBox);
        nameBox = findViewById(R.id.namebox);
        passwordBox = findViewById(R.id.passwordBox);

        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailBox.getText().toString();
                final String pass = passwordBox.getText().toString();
                final String name = nameBox.getText().toString();

                if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {

                    ///something goes wrong: all field must be filled
                    ///we need to display a error message
                    showMessage("Please fill all fields");
                    signupBtn.setVisibility(View.VISIBLE);

                }else {

                /*final User user = new User();
                user.setEmail(email);
                user.setPass(pass);
                user.setName(name);

*/
                    CreateUserAccount(email, name, pass);
                }

      /*          final User user = new User();
                user.setEmail(email);
                user.setPass(pass);
                user.setName(name);

*/
            }
        });




    }
    private void CreateUserAccount(String email, final String name, String pass) {
        //this method create user account with specific email password
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                         /*   database.collection("Users")
                                    .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(Register.this, Login.class));
                                }
                            });*/

                            startActivity(new Intent(Register.this, Login.class));
                            //user account create succesful
                            showMessage("Account created");
                            //after creating account now we update user information like name picture
                            //updateUserInfo(name ,pickedImaUri,firebaseAuth.getCurrentUser());

                        }
                        else
                        {
                            //account creation failed
                            showMessage("Account creation failed" + task.getException());
                            signupBtn.setVisibility(View.VISIBLE);

                        }

                    }
                });

    }

    // Simple message to toast message

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();



    }
}