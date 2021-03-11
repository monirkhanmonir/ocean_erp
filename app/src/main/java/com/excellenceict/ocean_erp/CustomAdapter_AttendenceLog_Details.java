package com.excellenceict.ocean_erp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.excellenceict.ocean_erp.Model.AttendenceLog_Details_C_Model;

import java.util.ArrayList;

public class CustomAdapter_AttendenceLog_Details extends BaseAdapter {

    private Context context;
    private ArrayList<AttendenceLog_Details_C_Model> listItems;

    public CustomAdapter_AttendenceLog_Details(Context context, ArrayList<AttendenceLog_Details_C_Model> allValue) {
        this.context = context;
        this.listItems = allValue;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.attendence_details_listview, parent, false);
        AttendenceLog_Details_C_Model item = listItems.get(position);

        TextView time = row.findViewById(R.id.aDetails_time);
        TextView mode = row.findViewById(R.id.aDetails_mode);
        TextView location = row.findViewById(R.id.aDetails_location);

        time.setText(item.getTime());
        mode.setText(item.getMode());
        location.setText(item.getOffice_Location());

        return row;
    }
}
