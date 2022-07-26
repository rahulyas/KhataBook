package com.example.application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.application.Contract.EmployeeContract;
import com.example.application.R;

import java.util.ArrayList;
import java.util.Set;

public class TaskCreationAdapter extends CursorAdapter {

    private final String TAG = "adapter";
    private Set<Long> employees;
    private ArrayList<Boolean> checkBoxState;
    private Set<Long> selectedEmp;

    public TaskCreationAdapter(Context context, Cursor c, Set<Long> employees, Set<Long> selectedEmp) {
        super(context, c, 0);
        this.employees = employees;
        this.selectedEmp = selectedEmp;
        checkBoxState = new ArrayList<>(c.getCount());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //TODO too much on main thread !!!!!!!!!

        View view = LayoutInflater.from(context).inflate(R.layout.task_creation_row, parent, false);
        //An object of view holder to hold the widgets data
        ListViewHolder holder = new ListViewHolder(view);
        holder.employeeName = view.findViewById(R.id.employee_name_text);
        holder.checkBox = view.findViewById(R.id.employee_check_box);
        view.setTag(holder);
        view.setTag(R.id.employee_name_text, holder.employeeName);
        view.setTag(R.id.employee_check_box, holder.checkBox);

        if (checkBoxState.isEmpty())
            //initialize the array with false as a value
            for (int i = 0; i < cursor.getCount(); i++) {
                checkBoxState.add(false);
            }

        return view;
    }

    //method to handle the list view
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        //retrieve the view holder object created in new view
        ListViewHolder holder = (ListViewHolder) view.getTag();

        //get the employee names from the cursor
        final String employeeName = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME));
        @SuppressLint("Range") final long ID = cursor.getLong(cursor.getColumnIndex(EmployeeContract.EmployeeEntry._ID));

        final int position = cursor.getPosition();

        //if the activity is in edit mode, set the checkboxes with the employees selected in the being edited task
        if (selectedEmp != null && selectedEmp.contains(ID)) {
            holder.checkBox.setChecked(true);
            holder.checkBox.setTag(position);
            checkBoxState.set((Integer) holder.checkBox.getTag(), true);
            employees.add(ID);

        }

        holder.employeeName.setText(employeeName);//set the text view with the employee names

        //handle the changing in the check box
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checkBoxState.set((Integer) buttonView.getTag(), buttonView.isChecked()); //set the state of the check box

                if (buttonView.isChecked()) {
                    employees.add(ID);
                    buttonView.setChecked(true);
                } else {
                    employees.remove(ID);
                    buttonView.setChecked(false);
                }
            }
        });
        //set tag in view holder to hold the position of the checkbox
        holder.checkBox.setTag(position);

        //get the state of the checkBox from the boolean array and set it to the checkBox in the holder
        holder.checkBox.setChecked(checkBoxState.get(position));
    }


    //custom implementation ViewHolder class
    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView employeeName;
        CheckBox checkBox;

        ListViewHolder(View itemView) {
            super(itemView);

            employeeName = itemView.findViewById(R.id.employee_name_text);
            checkBox = itemView.findViewById(R.id.employee_check_box);

        }

    }


}
