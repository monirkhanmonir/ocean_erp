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

public class Chalan_Report_SummaryResult_Customadapter extends ArrayAdapter<Chalan_Report_Summary_Entity> {
    public Chalan_Report_SummaryResult_Customadapter(Context context, ArrayList<Chalan_Report_Summary_Entity> result){
        super(context,0,result);

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
                    R.layout.bill_result_listview,parent,false);
        }
        TextView invoice_no = convertView.findViewById(R.id.invoiceNO_text);
        TextView invoice_date = convertView.findViewById(R.id.invoiceDate_text);
        TextView invoice_amount = convertView.findViewById(R.id.amount_text);
        Chalan_Report_Summary_Entity currentItems = (Chalan_Report_Summary_Entity) getItem(position);
        if(currentItems !=null){
            invoice_no.setText(currentItems.getChalanSell_qty());
            invoice_date.setText(currentItems.getChalanMu_name());
//            invoice_amount.setText(currentItems.getSales_amount());
            invoice_amount.setText(CurrencyFormating(currentItems.getChalan_amount()));


        }

        return convertView;
    }
    private static String CurrencyFormating( String number){
        DecimalFormat format = new DecimalFormat("##,##,##0");
        return format.format(Double.parseDouble(number));
    }
}

