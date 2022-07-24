package com.example.application.EmpolyeeMangmentSystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.widget.EditText;

import com.example.application.Contract.EmployeeContract;
import com.example.application.Contract.TaskContract;
import com.example.application.Database.EmployeesManagementDbHelper;
import com.example.application.R;
import com.example.application.EmpolyeeMangmentSystem.Task;
import com.example.application.EmpolyeeMangmentSystem.TaskFragment;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
public class EditCommand extends TaskCreationCommand {
    private final String TAG = "edit";
    private Activity activity;
    private EmployeesManagementDbHelper employeeDBHelper;
    private long task_id;
    private Task task;

    public EditCommand(Activity activity, EmployeesManagementDbHelper employeeDBHelper, long task_id, Task task) {
        this.activity = activity;
        this.employeeDBHelper = employeeDBHelper;
        this.task_id = task_id;
        this.task = task;
    }

    @SuppressLint("Range")
    @Override
    public Set<Long> execute() {

        //get the refrences to all the edit texts
        EditText taskName = activity.findViewById(R.id.task_name_edit);
        EditText taskDescription = activity.findViewById(R.id.department_description_edit_text);
        EditText taskDeadline = activity.findViewById(R.id.task_deadline_edit);

        //get a cursor for the task's data for its id
        Cursor textCursor = employeeDBHelper.getSpecifiTaskCursor(task_id);

        textCursor.moveToNext();
        //set the edit texts with the data from the cursor
        taskName.setText(textCursor.getString(textCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME)));
        taskDeadline.setText(textCursor.getString(textCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE)));
        taskDescription.setText(textCursor.getString(textCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION)));

        textCursor.close();

        //get a cursor for the employees of a specific task based on the id
        Cursor cursor = employeeDBHelper.getEmployeesOfTask(task_id);
        cursor.moveToNext();

        Set<Long> selectedEmp = new TreeSet<>();
        //add the employees ids to a set and return it
        while (!cursor.isAfterLast()) {
            selectedEmp.add(cursor.getLong(
                    cursor.getColumnIndex
                            (EmployeeContract.TABLE_NAME + EmployeeContract.EmployeeEntry._ID)));
            cursor.moveToNext();
        }
        cursor.close();
        return selectedEmp;
    }

    @Override
    boolean saveData(String task_name, int task_evaluation, String task_description, String task_deadline, ArrayList<Long> emplyee_id) {
        return false;
    }
/*
    @Override
    public boolean saveData(String task_name, int task_evaluation, String task_description, String task_deadline, ArrayList<Long> employees_ids) {
          task.setTaskName(task_name);
          task.setTaskDeadLine(task_deadline);
          task.setEmployees_id(employees_ids);
        return TaskFragment.newInstance().updateTasksList(task,(int)task_id);// this is should be updated
    }
*/
}
