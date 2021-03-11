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

import com.excellenceict.ocean_erp.Model.AttendenceLog_Model;
import com.excellenceict.ocean_erp.Model.ChartOfAccounts_Final_Model;
import com.excellenceict.ocean_erp.R;

import java.util.ArrayList;
import java.util.List;

public class Chart_Of_Account_Details_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context context;
    private List<ChartOfAccounts_Final_Model> chartModel;
    private SelectedChartItems selectedChartItems;
    private List<ChartOfAccounts_Final_Model> dataFilter;

    public Chart_Of_Account_Details_Adapter(List<ChartOfAccounts_Final_Model> model) {
        this.chartModel = model;
        this.dataFilter = model;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ChartOfAccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.chart_of_account_details_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChartOfAccounts_Final_Model model = chartModel.get(position);
        ChartOfAccountsViewHolder viewHolder = (ChartOfAccountsViewHolder) holder;

//        viewHolder.headeCode.setText(model.getHeaderCode());
        viewHolder.subHeadCode.setText(model.getSubHeaderCode());
        viewHolder.subHeadName.setText(model.getSubHeaderName());
        viewHolder.description.setText(model.getDescription());

        viewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return chartModel.size();
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
                    List<ChartOfAccounts_Final_Model> resultData = new ArrayList<>();
                    for (ChartOfAccounts_Final_Model charAccountModel : dataFilter) {
//                        String name = charAccountModel.getSubHeaderCode()+" "+charAccountModel.getSubHeaderName();
                        String name1 = charAccountModel.getSubHeaderName() + " " + charAccountModel.getDescription();
//                        name.toLowerCase().contains(searchChr)
//                        charAccountModel.getSubHeaderCode().toLowerCase().contains(searchChr) | charAccountModel.getSubHeaderName().toLowerCase().contains(searchChr)
                        if (name1.toLowerCase().contains(searchChr) | charAccountModel.getSubHeaderCode().toLowerCase().contains(searchChr)) {
                            resultData.add(charAccountModel);
                        }
                    }

                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                chartModel = (List<ChartOfAccounts_Final_Model>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    private class ChartOfAccountsViewHolder extends RecyclerView.ViewHolder {
        private TextView headeCode, subHeadCode, subHeadName, description;
        private LinearLayout linearLayout;

        public ChartOfAccountsViewHolder(@NonNull View itemView) {
            super(itemView);
//            headeCode = itemView.findViewById(R.id.chart_HeadCodeT);
            subHeadCode = itemView.findViewById(R.id.chart_SubheadCodeT);
            subHeadName = itemView.findViewById(R.id.chart_HeadNameT);
            description = itemView.findViewById(R.id.chart_DescriptionT);

            linearLayout = itemView.findViewById(R.id.chartDetailsView);
        }

    }

    public interface SelectedChartItems {
        void getSelectedAttendence(AttendenceLog_Model person_log);
    }
}
