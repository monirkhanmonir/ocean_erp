package com.ocean.orcl;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Current_Stock_ItemName_Adapter extends ArrayAdapter<CurrentStock_ItemName_Entity_C> {
    private int mSelectedIndex = -1;
    public Current_Stock_ItemName_Adapter(Context context, ArrayList<CurrentStock_ItemName_Entity_C> item_name){
        super(context,0,item_name);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        mSelectedIndex =  position;
        notifyDataSetChanged();
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private  View initView(int position, View convertView,ViewGroup parent){
        if(convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.currentstock_item_name_spinner,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.itemName_text);
        CurrentStock_ItemName_Entity_C currentItems =getItem(position);
        if(currentItems !=null){
            textView.setText(currentItems.getItem_Name());
        }
        if (position == mSelectedIndex) {
            textView.setTextColor(Color.parseColor("#059DE2"));

        } else {
            textView.setTextColor(Color.parseColor("#000080"));
        }

        return convertView;
    }
}
