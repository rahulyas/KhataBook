package com.example.application.EmpolyeeMangmentSystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.Contract.DepartmentContract;
import com.example.application.Database.EmployeesManagementDbHelper;
import com.example.application.Model.DepartmentItem;
import com.example.application.R;
import com.google.android.material.snackbar.Snackbar;

public class Department extends AppCompatActivity {

    private EditText description, nameOfDepartment;
    private EmployeesManagementDbHelper emdb ;
    private DepFragment depFragment = DepFragment.newInstance(0);
    private Button save;
    private Intent intent;
    private long departmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emdb = new EmployeesManagementDbHelper(this);

        intent = getIntent();
        boolean IsEditable = intent.getExtras().getBoolean("IsEdit");

//Hooks
        description= findViewById(R.id.department_description);
        nameOfDepartment= findViewById(R.id.department_name);
        save= findViewById(R.id.save);
//Logic

        if (IsEditable) {
            updateAction();
        } else {
            AddNewDepartemnt();
        }


    }

    // Add New Departement
    private void AddNewDepartemnt() {
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                  //  boolean result =   emdb.addDepartment( nameOfDepartment.getText().toString(),description.getText().toString());
                    boolean addDepartment = emdb.addDepartment(nameOfDepartment.getText().toString(), description.getText().toString());
                    actionSave(addDepartment, v, false);

            }
        });
    }
//UpDate Action
    @SuppressLint("Range")
    private void updateAction() {
        departmentId = intent.getExtras().getLong("depatmentID");
        Cursor cursorDep = emdb.getDepartment(departmentId);
      //  Log.v("Dep cre cur" , ""+departmentId);
        if (cursorDep.moveToFirst()) {
            description.setText(cursorDep.getString(cursorDep.getColumnIndex(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION)));
            nameOfDepartment.setText(cursorDep.getString(cursorDep.getColumnIndex(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME)));
        }
        cursorDep.close();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((nameOfDepartment.getText().toString()).matches("^\\w+( \\w+)*$")) || !((description.getText().toString()).matches("^[\\s\\S]{2,200}$"))) {
                    Snackbar.make(v, "SOME OR ALL INPUTS ARE INVALID. PLEASE ENTER VALID VALUES.", Snackbar.LENGTH_LONG).setAction("", null).show();
                } else {
                    boolean correct = emdb.updateDepartment(new DepartmentItem(departmentId,nameOfDepartment.getText().toString(),description.getText().toString()));
                    actionSave(correct, v, true);

                }

            }
        });

    }



    //Save
    public void actionSave(boolean addDepartment,View v, boolean isEdit) {
        if(addDepartment){
            if(!isEdit)
                Snackbar.make(v, "ENTERED SUCCESSFULLY", Snackbar.LENGTH_LONG).setAction("", null).show();
            else
                Snackbar.make(v, "UPDATED SUCCESSFULLY", Snackbar.LENGTH_LONG).setAction("", null).show();
            description.setText("",TextView.BufferType.EDITABLE);
            nameOfDepartment.setText("",TextView.BufferType.EDITABLE);
            depFragment.updateDepartmentList(emdb);
            Intent intent2 = new Intent(getBaseContext(), DepartmentActivity.class);
            intent2.putExtra("departmentId", departmentId);
            this.finish();
            startActivity(intent2);
        }
        else
            Snackbar.make(v, "FAILED TO ENTER CURRENT DEPARTMENT. TRY AGAIN LATER.", Snackbar.LENGTH_LONG).setAction("", null).show();
    }

}