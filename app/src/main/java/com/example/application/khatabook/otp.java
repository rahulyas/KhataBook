package com.example.application.khatabook;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class otp extends AppCompatActivity {

    ImageView redirectback;
    TextView number1, number2, number3, number4, user_number, resend_timer;
    String otp, phone_number,msg;
    int randomNumber, Min = 1000, Max = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
//hooks
        redirectback = findViewById(R.id.redirectback);
        user_number = findViewById(R.id.user_number);
        resend_timer = findViewById(R.id.resend_timer);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
// ASking for Runtime Permission
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_SMS).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
// User Phone Number
        phone_number = getIntent().getStringExtra("User_number");
        user_number.setText("+91-"+phone_number.substring(2));
// For Otp
        generateOtp();
//Phone Number
        number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!number1.getText().toString().isEmpty()) {
                    number2.setFocusableInTouchMode(true);
                    number2.requestFocus();
                }
                if(!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()){
// Verify Otp
                    verify_otp();
                }
            }
        });
        number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number2.getText().toString().isEmpty()) {
                    number1.setFocusableInTouchMode(true);
                    number1.requestFocus();
                }
                else{
                    number3.setFocusableInTouchMode(true);
                    number3.requestFocus();
                }

                if(!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()){
                    verify_otp();
                }
            }
        });
        number3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number3.getText().toString().isEmpty()) {
                    number2.setFocusableInTouchMode(true);
                    number2.requestFocus();
                }
                else{
                    number4.setFocusableInTouchMode(true);
                    number4.requestFocus();
                }

                if(!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()){
                    verify_otp();
                }
            }
        });
        number4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number4.getText().toString().isEmpty()) {
                    number3.setFocusableInTouchMode(true);
                    number3.requestFocus();
                }
                if(!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()){
                    verify_otp();
                }
            }
        });
// Back Button ClickListener
        redirectback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent = new Intent(otp.this, registraction.class);
                startActivity(new_intent);
                finish();
            }
        });
// Resend the timmer
        resend_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resend_timer.setEnabled(false);
                resend_timer.setTextColor(getResources().getColor(R.color.grey));
                generateOtp();
                counter();

            }
        });
// for Timeer
        counter();
    }
//Method of OTP
    private void generateOtp() {
        Random random = new Random();
        randomNumber = random.nextInt((Max - Min) + 1) + Min;
        msg = "Your OTP is" + randomNumber;
        Toast.makeText(otp.this, "Otp is " + randomNumber, Toast.LENGTH_SHORT).show();
    }
 // Method for verify otp
 public void verify_otp() {
     otp = number1.getText().toString() + number2.getText().toString() + number3.getText().toString() + number4.getText().toString();
     if (randomNumber == Integer.valueOf(otp)) {
         Intent intent = new Intent(otp.this,register_passcode.class);
         intent.putExtra("User_number",phone_number);
         startActivity(intent);
         finish();
         Toast.makeText(getApplicationContext(), "Phone Number Verify", Toast.LENGTH_SHORT).show();
     } else {
         Toast.makeText(getApplicationContext(), "OTP is invalid", Toast.LENGTH_SHORT).show();
     }
 }

 //Api Part For OTP
// here we send the otp with the help of "SMS4India" website
    public static boolean sendOtp(String apiKey, String secretKey, String useType, String phone, String message, String senderId){
        String url = "https://www.sms4india.com";
        try{
            // construct data
            JSONObject urlParameters = new JSONObject();
            urlParameters.put("apikey",apiKey);
            urlParameters.put("secret",secretKey);
            urlParameters.put("usetype",useType);
            urlParameters.put("phone", phone);
            urlParameters.put("message", URLEncoder.encode(message,"UTF-8"));
            urlParameters.put("senderid", senderId);
            URL obj = new URL(url + "/api/v1/sendCampaign");
            // send data
            HttpURLConnection httpConnection = (HttpURLConnection) obj.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(urlParameters.toString().getBytes());
            // get the response
            BufferedReader bufferedReader = null;
            if (httpConnection.getResponseCode() == 200) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
            return true;
        }catch(Exception ex){
            return false;
        }

    }

//Counter Method
// CountDownTimer For OTP
public void counter(){

    new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long l) {
            resend_timer.setText("Resend OTP (" + l / 1000 + ")");
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onFinish() {

            resend_timer.setText("Resend OTP");
            resend_timer.setTextColor(getResources().getColor(R.color.colorAccent));
            resend_timer.setEnabled(true);

        }
    }.start();
}

}