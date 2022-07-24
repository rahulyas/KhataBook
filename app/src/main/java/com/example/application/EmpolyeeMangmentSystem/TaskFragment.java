package com.example.application.EmpolyeeMangmentSystem;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.Adapter.TasksAdapter;
import com.example.application.Contract.TaskContract;
import com.example.application.Database.EmployeesManagementDbHelper;
import com.example.application.R;

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    private static TaskFragment fragment;
    private EmployeesManagementDbHelper employeeDBHelper;
    private ArrayList<Task> mValues;
    private TasksAdapter mAdapter;

    @SuppressLint("ValidFragment")
    public static TaskFragment newInstance() {
        if (fragment == null) {
            fragment = new TaskFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeDBHelper = new EmployeesManagementDbHelper(this.getContext());
        Cursor cursor = employeeDBHelper.getAllTasksCursor();
        mValues = new ArrayList<>();
        setmValues(cursor);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TasksAdapter(getActivity(), mValues);
        recyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("Range")
    private void setmValues(Cursor cursor) {
        mValues = new ArrayList<Task>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Task task = new Task();
                task.setId(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry._ID)));
                task.setTaskName(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME)));
                task.setTaskDetails(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION)));
                task.setTaskDeadLine(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE)));
                task.setTaskDate(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_DATE)));
                task.setEvaluation(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_EVALUATION)));
                if (cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_COMPLETED)) == 0) {
                   task.setDone(false);
                } else {
                   task.setDone(true);
                }
                ArrayList<Long> ids = new ArrayList<>();
                //set employees ids
                Cursor employees = employeeDBHelper.getEmployeesOfTask(cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry._ID)));
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    do {
                        ids.add(cursor.getLong(0));
                    } while (cursor.moveToNext());
                }
                employees.close();
                task.setEmployees_id(ids);
                mValues.add(task);
                cursor.moveToNext();
            }


        }
        cursor.close();

    }

        public boolean updateTasksList(com.google.android.gms.tasks.Task updatedTask, int id) {
        for (int i = 0; i < mValues.size(); i++) {
           if (mValues.get(i).getId() == id) {
                mValues.remove(i);
            //    mValues.add(i, updatedTask);
                break;
            }
        }
        mAdapter.notifyItemChanged(mValues.indexOf(updatedTask), updatedTask);
        return employeeDBHelper.updateTask(updatedTask);
    }

    public boolean deleteTaskFromList(int id) {
        boolean remove = employeeDBHelper.deleteTask(id);
        for (int i = 0; i < mValues.size(); i++) {
            if (mValues.get(i).getId() == id) {
                mValues.remove(i);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
        return remove;
    }

 /*  public boolean addTaskToView( Task task) {
       Long id  = employeeDBHelper.addTask(task);
        task.setId(id.toString());
        mValues.add(task);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        return id > 0;

    }*/
}