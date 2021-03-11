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

import com.excellenceict.ocean_erp.Model.AttendenceLog_Model;
import com.excellenceict.ocean_erp.Model.ChartOfAccounts_Final_Model;
import com.excellenceict.ocean_erp.Model.Ledger_position_FinalModel;
import com.excellenceict.ocean_erp.R;

import java.text.DecimalFormat;
import java.util.List;

public class Ledger_position_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Ledger_position_FinalModel> listItems;
    private SelectedItems selectedItems;
    private List<ChartOfAccounts_Final_Model> dataFilter;

    public Ledger_position_Adapter(List<Ledger_position_FinalModel> listItems) {
        this.listItems = listItems;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new LedgerViewHolder(LayoutInflater.from(context).inflate(R.layout.ledger_position_lisview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ledger_position_FinalModel items = listItems.get(position);
        LedgerViewHolder viewHolder = (LedgerViewHolder) holder;

        viewHolder.head.setText(items.getHead());
        viewHolder.subHeadName.setText(items.getSubHead());

        String type = items.getType();
        if (type == null) {
            viewHolder.type.setVisibility(View.GONE);
        }
        viewHolder.type.setText("("+items.getType()+")");
        viewHolder.balance.setText(CurrencyFormating(items.getBalance()));

        viewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private class LedgerViewHolder extends RecyclerView.ViewHolder {
        private TextView head, subHeadName, balance, type;
        private LinearLayout linearLayout;

        public LedgerViewHolder(@NonNull View itemView) {
            super(itemView);
//            headeCode = itemView.findViewById(R.id.chart_HeadCodeT);
            head = itemView.findViewById(R.id.leadger_Head);
            subHeadName = itemView.findViewById(R.id.leadger_subHead);
            balance = itemView.findViewById(R.id.leadger_balance);
            type = itemView.findViewById(R.id.leadger_type);

            linearLayout = itemView.findViewById(R.id.ledger_DetailsView);
        }


    }

    public interface SelectedItems {
        void getSelectedAttendence(AttendenceLog_Model person_log);
    }

    private static String CurrencyFormating(String number) {
        DecimalFormat format = new DecimalFormat("##,##,##0");
        return format.format(Double.parseDouble(number));
    }

}
