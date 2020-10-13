package com.ocean.orcl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter_AttendenceLog_Details extends BaseAdapter {

    Context context;
    private ArrayList<AttendenceLog_Details_C_Entity> queryC_time;
    private ArrayList<AttendenceLog_Details_C_Entity>queryC_mode;
    private ArrayList<AttendenceLog_Details_C_Entity>queryC_location;

    public CustomAdapter_AttendenceLog_Details(Context context, ArrayList<AttendenceLog_Details_C_Entity> allValue) {
        this.context = context;
        this.queryC_time = allValue;
        this.queryC_mode = allValue;
        this.queryC_location = allValue;
    }

    @Override
    public int getCount() {
        return queryC_time.size();
    }

    @Override
    public Object getItem(int position) {
        return queryC_time.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.attendence_details_listview, parent, false);

        TextView time = row.findViewById(R.id.aDetails_time);
        TextView mode = row.findViewById(R.id.aDetails_mode);
        TextView location = row.findViewById(R.id.aDetails_location);

        time.setText(queryC_time.get(position).time);
        mode.setText(queryC_mode.get(position).mode);
        location.setText(queryC_location.get(position).office_Location);

        return row;
    }
}
