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

public class Bill_Results_CustomAdapter extends ArrayAdapter<Bill_Result_Entity> {
    public Bill_Results_CustomAdapter(Context context, ArrayList<Bill_Result_Entity> result){
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
        Bill_Result_Entity currentItems = (Bill_Result_Entity) getItem(position);
        if(currentItems !=null){
            invoice_no.setText(currentItems.getInvoide_No());
            invoice_date.setText(currentItems.getInvoice_Date());
//            invoice_amount.setText(currentItems.getInvoice_Amount());
            invoice_amount.setText(CurrencyFormating(currentItems.getInvoice_Amount()));
        }

        return convertView;
    }
    private static String CurrencyFormating( String number){
        DecimalFormat format = new DecimalFormat("##,##,##0");
        return format.format(Double.parseDouble(number));
    }
}
