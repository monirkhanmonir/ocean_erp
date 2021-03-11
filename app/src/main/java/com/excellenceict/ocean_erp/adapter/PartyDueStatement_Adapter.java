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

import com.excellenceict.ocean_erp.PartyDueStatement_Result_Entity;
import com.excellenceict.ocean_erp.R;

import java.text.DecimalFormat;
import java.util.List;

public class PartyDueStatement_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PartyDueStatement_Result_Entity> listItems;
    private Context context;


    public PartyDueStatement_Adapter(List<PartyDueStatement_Result_Entity> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new PartyDueStatementViewHolder(LayoutInflater.from(context).inflate(R.layout.party_due_statement_result_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PartyDueStatement_Result_Entity model = listItems.get(position);
        PartyDueStatementViewHolder viewHolder = (PartyDueStatementViewHolder) holder;

        viewHolder.header.setText(model.getSub_header_name());
        viewHolder.udNo.setText(model.getUd_no());
        viewHolder.analyzer.setText(model.getAnalyzer());
        viewHolder.roach.setText(CurrencyFormating(model.getRoche()));
        viewHolder.sysmex.setText(CurrencyFormating(model.getSysmex()));

        viewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private class PartyDueStatementViewHolder extends RecyclerView.ViewHolder {
        private TextView header, udNo, analyzer, roach, sysmex;
        private LinearLayout linearLayout;

        public PartyDueStatementViewHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.partyDueRes_HeaderName);
            udNo = itemView.findViewById(R.id.partyDueRes_UdNo);
            analyzer = itemView.findViewById(R.id.partyDueRes_Analyzer);
            roach = itemView.findViewById(R.id.partyDueRes_Roach);
            sysmex = itemView.findViewById(R.id.partyDueRes_Sysmex);

            linearLayout = itemView.findViewById(R.id.partyDueStatement);
        }

    }

    private static String CurrencyFormating(String number) {
        DecimalFormat format = new DecimalFormat("##,##,##0");
        return format.format(Double.parseDouble(number));
    }
}
