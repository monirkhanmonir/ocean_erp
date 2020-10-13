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

public class ACC_ViewVoucher_Result_Adapter extends ArrayAdapter<ACC_View_Voucher_Result_Entity> {
    public ACC_ViewVoucher_Result_Adapter(Context context, ArrayList<ACC_View_Voucher_Result_Entity> item_name){
        super(context,0,item_name);

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
                    R.layout.acc_view_voucher_result_listview,parent,false);
        }
        TextView date = convertView.findViewById(R.id.voucherRes_date);
        TextView voucher = convertView.findViewById(R.id.voucherRes_voucher);
        TextView ref = convertView.findViewById(R.id.voucherRes_ref);
        TextView trType = convertView.findViewById(R.id.voucherRes_trType);
        TextView amount = convertView.findViewById(R.id.voucherRes_Amount);
        TextView note = convertView.findViewById(R.id.voucherRes_Note);
        ACC_View_Voucher_Result_Entity currentItems =getItem(position);
        if(currentItems !=null){
            date.setText(currentItems.getDate());
            voucher.setText(currentItems.getVoucher());
            ref.setText(currentItems.getRef());
            trType.setText(currentItems.getTr_type());
//            amount.setText(currentItems.getTotalVoucher_amount());
            amount.setText(CurrencyFormating(currentItems.getTotalVoucher_amount()));
            note.setText(currentItems.getNote());
        }

        return convertView;
    }
    private static String CurrencyFormating( String number){
        DecimalFormat format = new DecimalFormat("##,##,##0");
        return format.format(Double.parseDouble(number));
    }

}
