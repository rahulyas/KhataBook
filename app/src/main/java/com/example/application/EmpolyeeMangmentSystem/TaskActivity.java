package com.example.application.EmpolyeeMangmentSystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.Adapter.EmployeeAdapter;
import com.example.application.Database.EmployeesManagementDbHelper;
import com.example.application.R;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    EmployeesManagementDbHelper employeeDBHelper;
    TextView datetext,descriptiontext,deadlinetext,mEvaluation;
    TaskFragment tasksFragment = TaskFragment.newInstance();
    RatingBar mRatingBar;
    int position;
    ArrayList<Task> tasks;
    long taskID;
    EmployeeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        employeeDBHelper = new EmployeesManagementDbHelper(this);
        datetext = findViewById(R.id.taskdate);
        descriptiontext = findViewById(R.id.taskdesc);
        deadlinetext = findViewById(R.id.deadline);
        mRatingBar = findViewById(R.id.ratingBar_task);
        mEvaluation = findViewById(R.id.evaluation);
        Intent intent= getIntent();
        position = intent.getExtras().getInt("position");
        tasks = (ArrayList<Task>) getIntent().getSerializableExtra("data");
        taskID = getIntent().getExtras().getLong("taskId");
        setTitle(tasks.get(position).getTaskName());
        datetext.setText(tasks.get(position).getTaskDate());
        descriptiontext.setText(tasks.get(position).getTaskDetails());
      //  deadlinetext.setText(tasks.get(position).getTaskDeadline());
        if (tasks.get(position).isDone()) {
            mRatingBar.setRating(tasks.get(position).getEvaluation());
            mRatingBar.setVisibility(View.VISIBLE);
            mEvaluation.setVisibility(View.VISIBLE);
        }
        setEmployees();

    }

    private void setEmployees(){

        Cursor cursor = employeeDBHelper.getEmployeesOfTask(taskID);
        ListView employees = (ListView)findViewById(R.id.employees_list);
        EmployeeAdapter adapter = new EmployeeAdapter(this,cursor);
        employees.setAdapter(adapter);
        RelativeLayout emptyView = (RelativeLayout) findViewById(R.id.empty_employees);
        employees.setEmptyView(emptyView);
        employees.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(TaskActivity.this, EmployeeActivity.class);
                intent.putExtra("employeeId", id);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
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
            Intent intent = new Intent(TaskActivity.this, TaskCreation.class);
            intent.putExtra("task", tasks.get(position));
            intent.putExtra("task_id",tasks.get(position).getId());
            intent.putExtra("IsEdit", true);
            finish();
            startActivity(intent);
        }
        if (id == R.id.action_done) {
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    public void openDialog(){
        Evaluation evaluation = new Evaluation();
        evaluation.show(getSupportFragmentManager(),"EvaluationTaskDialog");
    }


    public void applyingRating(int rate) {
        Log.v("ID From Activity", "" + tasks.get(position).getId());
        boolean re = employeeDBHelper.updateTaskEvaluation(tasks.get(position).getId(),true,rate);
        Log.v("boolean", "" + re);
        tasks.get(position).setEvaluation(rate);
        tasks.get(position).setDone(true);
       // TaskFragment.newInstance().updateTasksList(tasks.get(position),(int)taskID);
        mRatingBar.setRating(rate);
        mRatingBar.setVisibility(View.VISIBLE);
        mEvaluation.setVisibility(View.VISIBLE);
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this task ?");
        builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                /*if (tasksFragment.deleteTaskFromList((int) tasks.get(position).getId())) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Can't close this department", Toast.LENGTH_LONG).show();
                }*/
                Toast.makeText(getApplicationContext(), "Can't close this department", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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