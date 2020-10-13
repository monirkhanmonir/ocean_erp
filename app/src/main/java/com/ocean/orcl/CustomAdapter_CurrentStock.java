package com.ocean.orcl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter_CurrentStock extends BaseAdapter {
    Context context;
    private ArrayList<CurrentStock_Entity_F> lotNo;
    private ArrayList<CurrentStock_Entity_F> expiryDate;
    private ArrayList<CurrentStock_Entity_F> qty_test;
    private LayoutInflater inflater;
    public CustomAdapter_CurrentStock(Context context, ArrayList<CurrentStock_Entity_F> allItems) {
        this.context =context;
        this.lotNo=allItems;
        this.expiryDate=allItems;
        this.qty_test=allItems;


    }


    @Override
    public int getCount() {
        return lotNo.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView =  inflater.inflate(R.layout.currentstock_qty_listview,parent,false);
        TextView lot = convertView.findViewById(R.id.lotNo_text);
        TextView date = convertView.findViewById(R.id.expiry_text);
        TextView qty = convertView.findViewById(R.id.qty_text);
        lot.setText(lotNo.get(position).getLot_no());
        date.setText(expiryDate.get(position).getExp_date());
        qty.setText(qty_test.get(position).getQty());

        return convertView;
    }
}
