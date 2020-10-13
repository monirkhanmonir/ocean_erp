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

public class Sales_Report_DetailsResult_Customadapter extends ArrayAdapter<SalesReport_DetailsResult_Entity> {
    public Sales_Report_DetailsResult_Customadapter(Context context, ArrayList<SalesReport_DetailsResult_Entity> result){
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
        SalesReport_DetailsResult_Entity currentItems = (SalesReport_DetailsResult_Entity) getItem(position);
        if(currentItems !=null){
            invoice_no.setText(currentItems.getSale_qty());
            invoice_date.setText(currentItems.getMu_name());
//            invoice_amount.setText(currentItems.getSales_amount());
            invoice_amount.setText(CurrencyFormating(currentItems.getSales_amount()));


        }

        return convertView;
    }
    private static String CurrencyFormating( String number){
        DecimalFormat format = new DecimalFormat("##,##,##0");
        return format.format(Double.parseDouble(number));
    }
}
