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

public class SalesChalan_Results_CustomAdapter extends ArrayAdapter<SalesChalan_Result_Entity> {
    public SalesChalan_Results_CustomAdapter(Context context, ArrayList<SalesChalan_Result_Entity> result){
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
                    R.layout.sales_chalan_listview,parent,false);
        }
        TextView chalan_no = convertView.findViewById(R.id.chalanNO_text);
        TextView chalan_date = convertView.findViewById(R.id.chalanDate_text);
        TextView chalan_amount = convertView.findViewById(R.id.chalan_amount_text);
        SalesChalan_Result_Entity currentItems = (SalesChalan_Result_Entity) getItem(position);
        if(currentItems !=null){
            chalan_no.setText(currentItems.getChalan_No());
            chalan_date.setText(currentItems.getChalan_Date());
//            chalan_amount.setText(currentItems.getChalan_Amount());
            chalan_amount.setText(CurrencyFormating(currentItems.getChalan_Amount()));

        }

        return convertView;
    }
    private static String CurrencyFormating( String number){
        DecimalFormat format = new DecimalFormat("###,###,##0");
        return format.format(Double.parseDouble(number));
    }
}
