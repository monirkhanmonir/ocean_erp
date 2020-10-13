package com.ocean.orcl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Current_Stock_UN_No_Adapter extends ArrayAdapter {
    public Current_Stock_UN_No_Adapter(Context context, ArrayList<CurrentStock_UD_No_Entity_D> unNo){
        super(context,0,unNo);

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
                    R.layout.currentstock_un_no_spinner,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.un_no_text);
        CurrentStock_UD_No_Entity_D currentItems = (CurrentStock_UD_No_Entity_D) getItem(position);
        if(currentItems !=null){
            textView.setText(currentItems.getUd_no1());
        }

        return convertView;
    }
}
