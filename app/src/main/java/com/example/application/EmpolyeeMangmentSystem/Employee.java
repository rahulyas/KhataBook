package com.example.application.EmpolyeeMangmentSystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.application.Database.EmployeesManagementDbHelper;
import com.example.application.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.regex.Pattern;

public class Employee extends AppCompatActivity {
    DatePickerDialog picker;
    Button submit;
    TextView date_select;
    EditText employee_name , employee_email, employee_phone, employee_job ; //to be read from input fields
    private static final String name_regex = "^([A-Za-z]{3,40})([ \\t][A-Za-z]{3,40})*$";
    private static final String email_regex = "^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$";
    private static final String phone_regex = "^[0-9]{7,}$";
    private  static  final  String birth_regex = "^[0-9]{1,2}-[0-9]{1,2}-[0-9]{4}$";

    EmployeesManagementDbHelper emdb ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        emdb = new EmployeesManagementDbHelper(this);
//Intent
        Intent intent = getIntent();
        final long departmentId = intent.getExtras().getLong("departmentId");
// Hooks


        date_select = findViewById(R.id.editBirth);
        submit=findViewById(R.id.submitButton);
        employee_name=findViewById(R.id.editName);
        employee_email=findViewById(R.id.editEmail);
        employee_phone=findViewById(R.id.editPhone);
        employee_job=findViewById(R.id.editJob);

// Set On ClickListener
        //Date
        date_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();

                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(Employee.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_select.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();


            }
        });


        //Submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean addEmployee = emdb.addEmployee(employee_name.getText().toString(),
                        date_select.getText().toString(),
                        departmentId, employee_job.getText().toString(),
                        employee_email.getText().toString(),
                        employee_phone.getText().toString(),null);
                        Snackbar.make(v, "CURRENT EMPLOYEE ENTERED SUCCESSFULLY.", Snackbar.LENGTH_LONG).setAction("", null).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
               /* if (matchFields()!= 5)
                    Snackbar.make(v, "CHECK ENTERED VALUES AND TRY AGAIN.", Snackbar.LENGTH_LONG).setAction("", null).show();
                else{
                    if (emdb.addEmployee(employee_name.getText().toString(),
                            date_select.getText().toString(),
                            departmentId, employee_job.getText().toString(),
                            employee_email.getText().toString(),
                            employee_phone.getText().toString(), null))
                    {
                        Snackbar.make(v, "CURRENT EMPLOYEE ENTERED SUCCESSFULLY.", Snackbar.LENGTH_LONG).setAction("", null).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                }*/

            }
        });


    }
//For Match Fields
 /*   private int matchFields() {
        int number_of_matches=0;
        if(Pattern.matches(name_regex,employee_name.getText().toString()))
            number_of_matches++;
        if(Pattern.matches(email_regex,employee_email.getText().toString()))
            number_of_matches++;
        if(Pattern.matches(phone_regex,employee_phone.getText().toString()))
            number_of_matches++;
        if(Pattern.matches(birth_regex,date_select.getText().toString()))
            number_of_matches++;
        if(employee_job.getText().toString()!=null)
            number_of_matches++;
        Log.i("opps ---", "done bro :)");
        return number_of_matches;
    }*/
}