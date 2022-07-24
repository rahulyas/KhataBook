package com.example.application.EmpolyeeMangmentSystem;

import com.example.application.Database.EmployeesManagementDbHelper;

import java.util.ArrayList;
import java.util.Set;

public abstract class TaskCreationCommand {
    abstract Set<Long> execute();
    abstract boolean saveData(String task_name, int task_evaluation, String task_description, String task_deadline, ArrayList<Long> emplyee_id);

}
