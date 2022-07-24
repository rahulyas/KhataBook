package com.example.application.EmpolyeeMangmentSystem;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseArray;

import com.example.application.Adapter.TaskCreationAdapter;
import com.example.application.Database.EmployeesManagementDbHelper;

import java.util.Set;

public class TaskCreationAdapterPool {

    public final String TAG = "pool";
    private SparseArray<TaskCreationAdapter> pool;
    private EmployeesManagementDbHelper dbHelper;
    private Context context;
    private Set<Long> employees;
    private Set<Long> selectedEmp;
    TaskCreationAdapterPool(EmployeesManagementDbHelper dbHelper, Context context, Set<Long> employees,Set<Long> selectedEmp) {
        this.dbHelper = dbHelper;
        this.context = context;
        this.employees = employees;
        this.selectedEmp=selectedEmp;
        pool = new SparseArray<>();
    }

    TaskCreationAdapter getAdapter(final int depID) {

        //if the adapter is created before then return it otherwise create a new one and return it
        if (pool.get(depID) == null) {
            Cursor cursor = dbHelper.getEmployessOfDepartment(depID);
            pool.append(depID, new TaskCreationAdapter(context, cursor, employees, selectedEmp));

        }
        return pool.get(depID);
    }



}
