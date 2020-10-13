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

public class Current_Stock_GroupName_Adapter extends ArrayAdapter<CurrentStock_Group_Entity_B> {
    private int mSelectedIndex = -1;

   public Current_Stock_GroupName_Adapter(Context context, ArrayList<CurrentStock_Group_Entity_B> group_name){
       super(context,0,group_name);

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
                R.layout.currentstock_groupname_spinner,parent,false);
    }
        TextView textView = convertView.findViewById(R.id.group_text);
        CurrentStock_Group_Entity_B currentItems =getItem(position);
        if(currentItems !=null){
            textView.setText(currentItems.getItem_groupName());
        }
        if (position == mSelectedIndex) {
            textView.setTextColor(Color.parseColor("#059DE2"));

        } else {
            textView.setTextColor(Color.parseColor("#000080"));
        }

        return convertView;
    }
}
