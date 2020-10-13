package com.ocean.orcl;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Billinvoice_Group_Adapter extends ArrayAdapter<Billinvoice_Group_Entity> {

    public Billinvoice_Group_Adapter(Context context, ArrayList<Billinvoice_Group_Entity> group_name){
        super(context,0,group_name);

    }
    private int mSelectedIndex = -1;




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
                    R.layout.bill_group_spinner,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.bill_group_text);
        Billinvoice_Group_Entity currentItems =getItem(position);
        if(currentItems !=null){
            textView.setText(currentItems.getItemGroup_Name());
        }
        if (position == mSelectedIndex) {
            textView.setTextColor(Color.parseColor("#059DE2"));

        } else {
            textView.setTextColor(Color.parseColor("#000080"));
        }



        return convertView;
    }

}
