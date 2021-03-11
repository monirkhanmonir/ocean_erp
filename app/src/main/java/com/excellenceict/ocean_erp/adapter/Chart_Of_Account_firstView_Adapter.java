package com.excellenceict.ocean_erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excellenceict.ocean_erp.Model.ChartOfAccountFirstView_model;
import com.excellenceict.ocean_erp.R;

import java.util.List;

public class Chart_Of_Account_firstView_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChartOfAccountFirstView_model> chartModel;
    private SelectedChart_FirstViewItems selectedChartItems;

    public Chart_Of_Account_firstView_Adapter(List<ChartOfAccountFirstView_model> chartModel, SelectedChart_FirstViewItems selectedChartItems) {
        this.chartModel = chartModel;
        this.selectedChartItems = selectedChartItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ChartOfAccountsFirstViewHolder(LayoutInflater.from(context).inflate(R.layout.chart_accunts_firstview_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChartOfAccountFirstView_model model = chartModel.get(position);
       ChartOfAccountsFirstViewHolder viewHolder = (ChartOfAccountsFirstViewHolder) holder;
        viewHolder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

        viewHolder.headeCode.setText(model.getHeadCode());

    }

    @Override
    public int getItemCount() {
        return chartModel.size();
    }
    private class ChartOfAccountsFirstViewHolder extends RecyclerView.ViewHolder {
        private TextView headeCode;
        ImageView headCodeIcon;
        private RelativeLayout relativeLayout;


        public ChartOfAccountsFirstViewHolder(@NonNull View itemView) {
            super(itemView);
            headeCode = itemView.findViewById(R.id.chart_headCoteT);
            headCodeIcon = itemView.findViewById(R.id.chart_headCoteIcon);
            relativeLayout =itemView.findViewById(R.id.chart_row_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedChartItems.getSelectedItems(chartModel.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface SelectedChart_FirstViewItems {
        void getSelectedItems(ChartOfAccountFirstView_model fmodel);
//        void getSelectHeadCode(String headCode);
    }
}
