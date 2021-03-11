package com.excellenceict.ocean_erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excellenceict.ocean_erp.Model.Leave_Log_Model;
import com.excellenceict.ocean_erp.Model.MyLeave_Model;
import com.excellenceict.ocean_erp.R;

import java.util.ArrayList;
import java.util.List;

public class My_Leave_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyLeave_Model> listItems;
    private Context context;
    //    private SelectedChartItems selectedChartItems;
    private List<MyLeave_Model> dataFilter;

    public My_Leave_Adapter(List<MyLeave_Model> listItems) {
        this.listItems = listItems;
        this.dataFilter = listItems;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyLeaveViewHolder(LayoutInflater.from(context).inflate(R.layout.myleave_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyLeave_Model model = listItems.get(position);
        MyLeaveViewHolder viewHolder = (MyLeaveViewHolder) holder;

        viewHolder.type.setText(model.getLeaveType());
        viewHolder.allowed.setText(model.getLeaveAllowed());
        viewHolder.count.setText(model.getLeaveCount());
        viewHolder.balance.setText(model.getLeaveBalance());

        viewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private class MyLeaveViewHolder extends RecyclerView.ViewHolder {
        private TextView id,name, desigantion, department, type,allowed,count,balance;
        private LinearLayout linearLayout;

        public MyLeaveViewHolder(@NonNull View itemView) {
            super(itemView);
//            id = itemView.findViewById(R.id.userID_text);

            type = itemView.findViewById(R.id.myleaveType);
            allowed = itemView.findViewById(R.id.myleave_allowed);
            count = itemView.findViewById(R.id.myleaveCount_text);
            balance = itemView.findViewById(R.id.myleave_balance);

            linearLayout = itemView.findViewById(R.id.myleaveLog_row_layout);
        }

    }
}
