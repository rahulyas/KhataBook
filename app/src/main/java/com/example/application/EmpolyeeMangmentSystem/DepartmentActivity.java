package com.example.application.EmpolyeeMangmentSystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.Adapter.EmployeeAdapter;
import com.example.application.Adapter.TaskAdapter;
import com.example.application.Contract.DepartmentContract;
import com.example.application.Database.EmployeesManagementDbHelper;
import com.example.application.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DepartmentActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private final int EMP_REQUEST = 1;
    private EmployeesManagementDbHelper helper;
    private TextView description;
    private ListView employees;
    private EmployeeAdapter adapterEmp;
    private long departmentId;
    private String depName;
    private DepFragment depFragment = DepFragment.newInstance(0);
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department2);

        helper = new EmployeesManagementDbHelper(this);
        setDepatementParameter();
        setEmployeeList();

        scrollView=findViewById(R.id.scroll);

        employees.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(DepartmentActivity.this, EmployeeActivity.class);
                intent.putExtra("employeeId", id);
                startActivityForResult(intent, EMP_REQUEST);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DepartmentActivity.this, Employee.class);
                intent.putExtra("departmentId", departmentId);
                startActivityForResult(intent, EMP_REQUEST);
            }
        });

        displayTaskList();

    }

    @SuppressLint("Range")
    private void setDepatementParameter() {
        description =  findViewById(R.id.description);
        Intent intent = getIntent();
        departmentId = intent.getExtras().getLong("departmentId");
        //setting name,description of department
        Cursor cursorDep = helper.getDepartment(departmentId);
        if (cursorDep.moveToFirst()) {
            description.setText(cursorDep.getString(cursorDep.getColumnIndex(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION)));
            depName = cursorDep.getString(cursorDep.getColumnIndex(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME));
            setTitle(depName);
        }
        cursorDep.close();
    }

    private void setEmployeeList() {
        //setting list of employees in this department
        employees = findViewById(R.id.employees_list);
        Cursor cursorEmp = helper.getEmployessOfDepartment(departmentId);
        adapterEmp = new EmployeeAdapter(this, cursorEmp);
        employees.setAdapter(adapterEmp);
        RelativeLayout emptyView = (RelativeLayout) findViewById(R.id.empty_employees);
        employees.setEmptyView(emptyView);
    }

    private void displayTaskList() {
        Cursor cursorTask = helper.getTasksOfDepartment(departmentId);
        ListView tasksList = findViewById(R.id.tasks_list);
        TaskAdapter adapterTask = new TaskAdapter(this, cursorTask);
        tasksList.setAdapter(adapterTask);

        RelativeLayout emptyTasks = (RelativeLayout) findViewById(R.id.empty_tasks_dep);
        tasksList.setEmptyView(emptyTasks);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMP_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Cursor cursor = helper.getEmployessOfDepartment(departmentId);
                adapterEmp.swapCursor(cursor);
                adapterEmp.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_department, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            showDeleteConfirmationDialog();
        }
        if (id == R.id.action_update) {
            Intent intent = new Intent(DepartmentActivity.this, Department.class);
            intent.putExtra("depatmentID", departmentId);
            intent.putExtra("IsEdit", true);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this department ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (helper.deleteDepartment(departmentId)){
                    depFragment.updateDepartmentList(helper);
                    finish();
                }else
                    Toast.makeText(getApplicationContext(), "Can\\'t fire this employee", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}