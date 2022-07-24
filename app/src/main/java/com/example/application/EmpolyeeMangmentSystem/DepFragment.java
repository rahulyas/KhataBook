package com.example.application.EmpolyeeMangmentSystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.Adapter.MyDepartmentRecyclerViewAdapter;
import com.example.application.Contract.DepartmentContract;
import com.example.application.Database.EmployeesManagementDbHelper;
import com.example.application.Model.DepartmentItem;
import com.example.application.R;

import java.util.ArrayList;
import java.util.List;

public class DepFragment extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private EmployeesManagementDbHelper mDataBase;
    private Cursor cursor;
    private Context context;
    private List<DepartmentItem> mValues;
    private MyDepartmentRecyclerViewAdapter mAdapter;
    private static RecyclerView recyclerView;

    public DepFragment() {
        // Required empty public constructor
    }

    public static DepFragment newInstance(int columnCount) {
        DepFragment fragment = new DepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mDataBase = new EmployeesManagementDbHelper(getContext());
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_department_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mValues = new ArrayList<>();
            cursor = mDataBase.getAllDepartments();
            if (cursor.moveToFirst()){
                do{
                    String name,description;
                    Long id = Long.parseLong(cursor.getString(cursor.getColumnIndex(DepartmentContract.DepartmentEntry._ID)));
                    name = cursor.getString(cursor.getColumnIndex(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME));
                    description = cursor.getString(cursor.getColumnIndex(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION));
                    DepartmentItem dataProvider = new  DepartmentItem (id,name,description);
                    mValues.add(dataProvider);
                }while (cursor.moveToNext());
            }
            cursor.close();
            mAdapter = new MyDepartmentRecyclerViewAdapter(mValues, mListener);
            recyclerView.setAdapter(mAdapter);
            recyclerView.invalidate();
        }
        return view;
    }


    @SuppressLint("Range")
    public void updateDepartmentList(EmployeesManagementDbHelper mDataBase){
        mValues = new ArrayList<>();
        cursor =  mDataBase.getAllDepartments();
        if (cursor.moveToFirst()) {
            do {
                String  name, description;
                Long id ;
                id = Long.parseLong(cursor.getString(cursor.getColumnIndex(DepartmentContract.DepartmentEntry._ID)));
                name = cursor.getString(cursor.getColumnIndex(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME));
                description = cursor.getString(cursor.getColumnIndex(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION));
                DepartmentItem dataProvider = new DepartmentItem(id, name, description);
                mValues.add(dataProvider);
            } while (cursor.moveToNext());
        }
        if (mAdapter == null) {
            mAdapter = new MyDepartmentRecyclerViewAdapter(mValues,mListener);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        cursor.close();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DepartmentItem item);
    }

}