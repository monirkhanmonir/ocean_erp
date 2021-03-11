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

import com.excellenceict.ocean_erp.Model.ChartOfAccounts_Final_Model;
import com.excellenceict.ocean_erp.Model.Leave_Log_Model;
import com.excellenceict.ocean_erp.R;

import java.util.ArrayList;
import java.util.List;


public class Leave_Log_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<Leave_Log_Model> listItems;
    private Context context;
    private SelecetedItems selectedItems;
    private List<Leave_Log_Model> dataFilter;
    int  previousExpandedPosition = -1;
    int mExpandedPosition =-1;

    public Leave_Log_Adapter(List<Leave_Log_Model> listItems, SelecetedItems selectedItems) {
        this.listItems = listItems;
        this.dataFilter = listItems;
        this.selectedItems =selectedItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new LeaveLogViewHolder(LayoutInflater.from(context).inflate(R.layout.leave_log_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Leave_Log_Model model = listItems.get(position);
        LeaveLogViewHolder viewHolder = (LeaveLogViewHolder) holder;

        viewHolder.id.setText(model.getId());
        viewHolder.name.setText(model.getNameF()+" "+model.getNameL());
        viewHolder.desigantion.setText(model.getDesignation());
        viewHolder.department.setText(model.getDepartment());

//        final boolean isExpanded = position == mExpandedPosition;
//        viewHolder.test.setVisibility(isExpanded?View.VISIBLE:View.GONE);
//        viewHolder.itemView.setActivated(isExpanded);
//
//        if (isExpanded)
//            previousExpandedPosition = position;
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mExpandedPosition = isExpanded ? -1:position;
//                notifyItemChanged(previousExpandedPosition);
//                notifyItemChanged(position);
//            }
//        });



        viewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null | constraint.length() == 0) {
                    filterResults.count = dataFilter.size();
                    filterResults.values = dataFilter;

                } else {
                    String searchChr = constraint.toString().toLowerCase();
                    List<Leave_Log_Model> resultData = new ArrayList<>();
                    for (Leave_Log_Model leaveModel : dataFilter) {
//                        String name = charAccountModel.getSubHeaderCode()+" "+charAccountModel.getSubHeaderName();
                        String name1 = leaveModel.getNameF() + " " + leaveModel.getNameL();
//                        name.toLowerCase().contains(searchChr)
//                        charAccountModel.getSubHeaderCode().toLowerCase().contains(searchChr) | charAccountModel.getSubHeaderName().toLowerCase().contains(searchChr)
                        if (name1.toLowerCase().contains(searchChr) | leaveModel.getDesignation().toLowerCase().contains(searchChr) |
                                leaveModel.getId().toLowerCase().contains(searchChr)) {
                            resultData.add(leaveModel);
                        }
                    }

                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                 listItems= (List<Leave_Log_Model>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
    private class LeaveLogViewHolder extends RecyclerView.ViewHolder {
        private TextView id,name, desigantion, department,test;
        private LinearLayout linearLayout;

        public LeaveLogViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.leave_userID);

            name = itemView.findViewById(R.id.leave_name);
            desigantion = itemView.findViewById(R.id.leave_designation);
            department = itemView.findViewById(R.id.leave_department);
//            test = itemView.findViewById(R.id.testing);

            linearLayout = itemView.findViewById(R.id.leaveLog_row_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItems.selectItem(getAdapterPosition(),listItems.get(getAdapterPosition()));

                }
            });
        }

    }
    public interface SelecetedItems{
        void selectItem(int positon,Leave_Log_Model model);
    }
}
