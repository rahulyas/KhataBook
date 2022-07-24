package com.example.application.EmpolyeeMangmentSystem;

import com.example.application.Database.EmployeesManagementDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class NewCommand extends TaskCreationCommand {

    private EmployeesManagementDbHelper employeeDBHelper;
    private static final String TAG="new";

    public NewCommand(EmployeesManagementDbHelper employeeDBHelper) {
        this.employeeDBHelper = employeeDBHelper;
    }

    @Override
    public Set<Long> execute() {
        //Any extra functionality can be added to the new task

        return null;
    }

    @Override
    boolean saveData(String task_name, int task_evaluation, String task_description, String task_deadline, ArrayList<Long> emplyee_id) {
        return false;
    }
/*
    @Override
    public boolean saveData(String task_name, int task_evaluation, String task_description, String task_deadline, ArrayList<Long> emplyee_id) {
        //call the db helper's method to add a new task
        Calendar calendar = Calendar.getInstance();
        boolean notDone = false;
        Task task = new Task();
        task.setEvaluation(task_evaluation);
        task.setDone(notDone);
        task.setEmployees_id(emplyee_id);
        task.setTaskDeadLine(task_deadline);
        task.setTaskDetails(task_description);
        task.setTaskName(task_name);
        task.setTaskDate(calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"
                +calendar.get(Calendar.YEAR));
        return TaskFragment.newInstance().addTaskToView(task);

    }
*/
}

