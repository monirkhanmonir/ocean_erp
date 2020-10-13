package com.ocean.orcl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ACC_PartyDueResult_Adapter extends ArrayAdapter<Acc_PartyDueStatement_Result_Entity> {
    public ACC_PartyDueResult_Adapter(Context context, ArrayList<Acc_PartyDueStatement_Result_Entity> item_name) {
        super(context, 0, item_name);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private  View initView(int position, View convertView,ViewGroup parent){
        if(convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.acc_party_due_statement_result_listview,parent,false);
        }
        TextView header = convertView.findViewById(R.id.partyDueRes_HeaderName);
        TextView udNo = convertView.findViewById(R.id.partyDueRes_UdNo);
        TextView analyzer = convertView.findViewById(R.id.partyDueRes_Analyzer);
        TextView roach = convertView.findViewById(R.id.partyDueRes_Roach);
        TextView sysmex = convertView.findViewById(R.id.partyDueRes_Sysmex);

        Acc_PartyDueStatement_Result_Entity currentItems =getItem(position);
        if(currentItems !=null){
            header.setText(currentItems.getSub_header_name());
            udNo.setText(currentItems.getUd_no());
            analyzer.setText(CurrencyFormating(currentItems.getAnalyzer()));
//            roach.setText(currentItems.getRoche());
            roach.setText(CurrencyFormating(currentItems.getRoche()));
            sysmex.setText(CurrencyFormating(currentItems.getSysmex()));

        }

        return convertView;
    }
    private static String CurrencyFormating( String number){
        DecimalFormat format = new DecimalFormat("##,##,##0");
        return format.format(Double.parseDouble(number));
    }
}
