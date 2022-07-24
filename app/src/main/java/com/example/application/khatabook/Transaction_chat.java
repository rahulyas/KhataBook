package com.example.application.khatabook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.Adapter.ChatAdapter;
import com.example.application.Database.DatabaseHelper;
import com.example.application.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Transaction_chat extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout share_layout;
    ImageView sidebar_icon;
    Intent intent;
    String Friend_id, user_id;
    ArrayList<String> transaction_amount, transaction_time, transaction_remarks, transaction_sender_id, transaction_id;
    Cursor cursor;
    RecyclerView transaction_chat_recycle;
    ChatAdapter chatAdapter;
    MaterialCardView transaction_debit, transaction_credit;
    TextView transaction_date, save_debit, transaction_name, transaction_balance, save_credit, friend_name, error_msg_transaction_amount, error_msg_transaction_date, error_msg_transaction_name;
    String transaction_date_text, transaction_name_text, transaction_balance_text, Friend_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_chat);

        //hooks

        sidebar_icon = findViewById(R.id.sidebar_icon);
        transaction_chat_recycle = findViewById(R.id.transaction_chat_recycle);
        transaction_debit = findViewById(R.id.transaction_debit);
        transaction_credit = findViewById(R.id.transaction_credit);
        friend_name = findViewById(R.id.friend_name);
        share_layout = findViewById(R.id.share_layout);

//SharedPreferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

        user_id = sharedPreferences.getString("Id", "");
        intent = getIntent();
        Friend_id = intent.getStringExtra("Friend_id");
//Database Helper
        DatabaseHelper myDB = new DatabaseHelper(this);
        Cursor cursor = myDB.get_user_details(Friend_id);
//while loop
        while (cursor.moveToNext()) {
            Friend_name = cursor.getString(1);
        }
        friend_name.setText(Friend_name);
//ArrayList
        transaction_sender_id = new ArrayList<>();
        transaction_amount = new ArrayList<>();
        transaction_remarks = new ArrayList<>();
        transaction_time = new ArrayList<>();
        transaction_id = new ArrayList<>();
//ClickListener
        transaction_debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//BottomSheet Dialog transaction
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Transaction_chat.this);
                bottomSheetDialog.setContentView(R.layout.transaction_chat_bottom_sheet_debit_dialog);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
//hooks
                transaction_balance = bottomSheetDialog.findViewById(R.id.transaction_amount);
                transaction_name = bottomSheetDialog.findViewById(R.id.transaction_name);
                transaction_date = bottomSheetDialog.findViewById(R.id.transaction_date);
                error_msg_transaction_amount = bottomSheetDialog.findViewById(R.id.error_msg_transaction_amount);
                error_msg_transaction_date = bottomSheetDialog.findViewById(R.id.error_msg_transaction_date);
                error_msg_transaction_name = bottomSheetDialog.findViewById(R.id.error_msg_transaction_name);
                save_debit = bottomSheetDialog.findViewById(R.id.save_debit);
//Click Listener
                transaction_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Transaction_chat.this,
                                Transaction_chat.this,
                                Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });
// Transaction Debit Save Part
                save_debit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transaction_balance_text = transaction_balance.getText().toString();
                        transaction_name_text = transaction_name.getText().toString();
                        transaction_date_text = transaction_date.getText().toString();

                        int count1 = 1, count2 = 1, count3 = 1;

                        error_msg_transaction_name.setVisibility(View.GONE);
                        error_msg_transaction_amount.setVisibility(View.GONE);
                        error_msg_transaction_date.setVisibility(View.GONE);
// now we make logic using if loop
//here we check the empty field of name
                        if (transaction_name_text.isEmpty()){
                            count1 = 0;
                            error_msg_transaction_name.setVisibility(View.VISIBLE);
                            error_msg_transaction_name.setText("This is required");
                        }
//here we check the empty field of amount
                        if (transaction_balance_text.isEmpty()) {
                            count2 = 0;
                            error_msg_transaction_amount.setVisibility(View.VISIBLE);
                            error_msg_transaction_amount.setText("This is required");
                        }
 //here we check the empty field of date
                        if (transaction_date_text.isEmpty()) {
                            count3 = 0;
                            error_msg_transaction_date.setVisibility(View.VISIBLE);
                            error_msg_transaction_date.setText("This is required");
                        }
// from here database helper logic start
                        if (count1 == 1 && count2 == 1 && count3 == 1) {
                            DatabaseHelper myDB = new DatabaseHelper(getApplicationContext());
                            String tost_message = null;
                            if (myDB.storeNewDebitTransaction(user_id, Friend_id, transaction_balance_text, transaction_name_text, transaction_date_text)) {
                                tost_message = "Transaction Added";
                            } else {
                                tost_message = "Something went wrong";
                            }
// ArrayList
                            transaction_sender_id = new ArrayList<>();
                            transaction_amount = new ArrayList<>();
                            transaction_remarks = new ArrayList<>();
                            transaction_time = new ArrayList<>();
                            transaction_id = new ArrayList<>();
// Here we Fetch the Transaction

                            Fetch_Transaction(user_id, Friend_id);
// chatAdapter
                            chatAdapter = new ChatAdapter(getApplicationContext(), user_id, transaction_sender_id, transaction_amount, transaction_remarks, transaction_time, transaction_id);
                            transaction_chat_recycle.setAdapter(chatAdapter);
                            transaction_chat_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            transaction_chat_recycle.smoothScrollToPosition(transaction_amount.size());
                            Toast.makeText(Transaction_chat.this, tost_message, Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.hide();
                        }

                    }
                });
                bottomSheetDialog.show();
            }
        });
// Transaction Credit Part
        transaction_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//BottomSheet Dialog
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Transaction_chat.this);
                bottomSheetDialog.setContentView(R.layout.transaction_chat_bottom_sheet_credit_dialog);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
//hooks
                transaction_balance = bottomSheetDialog.findViewById(R.id.transaction_amount);
                transaction_name = bottomSheetDialog.findViewById(R.id.transaction_name);
                transaction_date = bottomSheetDialog.findViewById(R.id.transaction_date);
                error_msg_transaction_amount = bottomSheetDialog.findViewById(R.id.error_msg_transaction_amount);
                error_msg_transaction_date = bottomSheetDialog.findViewById(R.id.error_msg_transaction_date);
                error_msg_transaction_name = bottomSheetDialog.findViewById(R.id.error_msg_transaction_name);
                save_credit = bottomSheetDialog.findViewById(R.id.save_credit);
//ClickListener
                transaction_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Transaction_chat.this,
                                Transaction_chat.this,
                                Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });
                save_credit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transaction_balance_text = transaction_balance.getText().toString();
                        transaction_name_text = transaction_name.getText().toString();
                        transaction_date_text = transaction_date.getText().toString();

                        int count1 = 1, count2 = 1, count3 = 1;

                        error_msg_transaction_name.setText("");
                        error_msg_transaction_amount.setText("");
                        error_msg_transaction_date.setText("");

                        if (transaction_name_text.isEmpty()) {
                            count1 = 0;
                            error_msg_transaction_name.setText("This is required");
                        }
                        if (transaction_balance_text.isEmpty()) {
                            count2 = 0;
                            error_msg_transaction_amount.setText("This is required");
                        }
                        if (transaction_date_text.isEmpty()) {
                            count3 = 0;
                            error_msg_transaction_date.setText("This is required");
                        }

                        if (count1 == 1 && count2 == 1 && count3 == 1) {
                            DatabaseHelper myDB = new DatabaseHelper(getApplicationContext());
                            String tost_message = null;
                            if (myDB.storeNewCreditTransaction(user_id, Friend_id, transaction_balance_text, transaction_name_text, transaction_date_text)) {
                                tost_message = "Transaction Added";
                            } else {
                                tost_message = "Something went wrong";
                            }
                            transaction_sender_id = new ArrayList<>();
                            transaction_amount = new ArrayList<>();
                            transaction_remarks = new ArrayList<>();
                            transaction_time = new ArrayList<>();
                            transaction_id = new ArrayList<>();
                            Fetch_Transaction(user_id, Friend_id);
                            chatAdapter = new ChatAdapter(getApplicationContext(), user_id, transaction_sender_id, transaction_amount, transaction_remarks, transaction_time, transaction_id);
                            transaction_chat_recycle.setAdapter(chatAdapter);
                            transaction_chat_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            transaction_chat_recycle.smoothScrollToPosition(transaction_amount.size());
                            Toast.makeText(Transaction_chat.this, tost_message, Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.hide();
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });
/* Fetchinf Transaction*/
        Fetch_Transaction(user_id, Friend_id);
        chatAdapter = new ChatAdapter(getApplicationContext(), user_id, transaction_sender_id, transaction_amount, transaction_remarks, transaction_time, transaction_id);
        transaction_chat_recycle.setAdapter(chatAdapter);
        transaction_chat_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        transaction_chat_recycle.smoothScrollToPosition(transaction_amount.size());

    }

    private void Fetch_Transaction(String user_id, String friend_id) {
        Date oneWayTripDate = null;
        DatabaseHelper myDB = new DatabaseHelper(this);
        cursor = myDB.user_friend_transaction(user_id, friend_id);
        while (cursor.moveToNext()) {
            transaction_sender_id.add(cursor.getString(0));
            transaction_amount.add(cursor.getString(1));
            transaction_remarks.add(cursor.getString(2));
            String date = cursor.getString(3);
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy hh:mm a");
            try {
                oneWayTripDate = input.parse(date);  // parse input
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transaction_time.add(output.format(oneWayTripDate));
            transaction_id.add(cursor.getString(4));
        }
    }

    @Override
    public void onDateSet (DatePicker view,int year, int month, int day){
        String month_name = new DateFormatSymbols().getMonths()[month];
        String temp_date = String.valueOf(day) + " - " + month_name.substring(0, 3) + " - " + String.valueOf(year);
        transaction_date.setText(temp_date);

    }


}