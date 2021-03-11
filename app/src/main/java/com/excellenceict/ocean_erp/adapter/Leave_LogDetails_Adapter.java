package com.excellenceict.ocean_erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excellenceict.ocean_erp.Model.Leave_LogDetails_Model;
import com.excellenceict.ocean_erp.Model.Leave_Log_Model;
import com.excellenceict.ocean_erp.Model.MyLeave_Model;
import com.excellenceict.ocean_erp.R;

import java.util.List;

public class Leave_LogDetails_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Leave_LogDetails_Model> listItems;
    private Context context;

    public Leave_LogDetails_Adapter(List<Leave_LogDetails_Model> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new LeaveLogDetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.myleave_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Leave_LogDetails_Model model = listItems.get(position);
        LeaveLogDetailsViewHolder viewHolder = (LeaveLogDetailsViewHolder) holder;

        viewHolder.type.setText(model.getType());
        viewHolder.allowed.setText(model.getAllowed());
        viewHolder.count.setText(model.getCount());
        viewHolder.balance.setText(model.getBalance());

        viewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    private class LeaveLogDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView id,name, desigantion, department, type,allowed,count,balance;
        private LinearLayout linearLayout;

        public LeaveLogDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.myleaveType);
            allowed = itemView.findViewById(R.id.myleave_allowed);
            count = itemView.findViewById(R.id.myleaveCount_text);
            balance = itemView.findViewById(R.id.myleave_balance);

            linearLayout = itemView.findViewById(R.id.myleaveLog_row_layout);

        }

    }
}
