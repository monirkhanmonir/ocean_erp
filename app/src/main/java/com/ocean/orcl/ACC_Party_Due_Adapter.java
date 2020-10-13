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

public class ACC_Party_Due_Adapter extends ArrayAdapter<ACC_partyDueStatement_Entity> {
    private int mSelectedIndex = -1;
    public ACC_Party_Due_Adapter(Context context, ArrayList<ACC_partyDueStatement_Entity> customer){
        super(context,0,customer);

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
                    R.layout.acc_view_voucher_custom_spinner,parent,false);
        }
        TextView textView = convertView.findViewById(R.id.view_voucher_text);
        ACC_partyDueStatement_Entity currentItems =getItem(position);
        if(currentItems !=null){
            textView.setText(currentItems.getSub_head_name());
        }

        return convertView;
    }
}
