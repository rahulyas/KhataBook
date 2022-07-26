package com.example.application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.EmpolyeeMangmentSystem.DepFragment;
import com.example.application.EmpolyeeMangmentSystem.DepartmentActivity;
import com.example.application.Model.DepartmentItem;
import com.example.application.R;

import java.util.List;

public class MyDepartmentRecyclerViewAdapter extends RecyclerView.Adapter<MyDepartmentRecyclerViewAdapter.ViewHolder> {
    private final List<DepartmentItem> mValues;
    private final DepFragment.OnListFragmentInteractionListener mListener;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public MyDepartmentRecyclerViewAdapter(List<DepartmentItem> items, DepFragment.OnListFragmentInteractionListener mListener) {
        this.mValues = items;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyDepartmentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDepartmentRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).name);
        holder.mdescriptionView.setText(mValues.get(position).details);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
                Context context = v.getContext();
                Intent intent = new Intent(context, DepartmentActivity.class);
                intent.putExtra("departmentId",mValues.get(position).id); // set id to Department activity
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mName;
        public final TextView mdescriptionView;
        public DepartmentItem mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mName = (TextView) itemView.findViewById(R.id.department_name);
            mdescriptionView = (TextView) itemView.findViewById(R.id.department_description);        }

        @Override
        public String toString() {
            return super.toString() + " '" + mdescriptionView.getText() + "'";
        }

    }
}
