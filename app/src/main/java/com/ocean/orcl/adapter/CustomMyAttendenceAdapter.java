package com.ocean.orcl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ocean.orcl.MyAttendence_Entity;
import com.ocean.orcl.R;

import java.util.List;

public class CustomMyAttendenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<MyAttendence_Entity> myAttendenceList;
    private List<MyAttendence_Entity> myAttendenceFilterList;
    private Context context;
    private SelectedMyAttendence selectedAttendence;

    public CustomMyAttendenceAdapter(List<MyAttendence_Entity> myAttendenceList, SelectedMyAttendence selectedAttendence) {
        this.myAttendenceList = myAttendenceList;
        this.myAttendenceFilterList = myAttendenceList;
        this.selectedAttendence = selectedAttendence;
    }

    @Override
    public Filter getFilter() {

//        Filter filter = new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults filterResults = new FilterResults();
//                if (constraint==null | constraint.length()==0){
//                    filterResults.count = myAttendenceFilterList.size();
//                    filterResults.values = myAttendenceFilterList;
//
//                }else {
//                    String searchChr = constraint.toString().toLowerCase();
//                    List<MyAttendence_Entity> resultData = new ArrayList<>();
//                    for (MyAttendence_Entity attendenceModel: myAttendenceFilterList){
//                        if(attendenceModel.getPKG_DESCRIPTION().toLowerCase().contains(searchChr)| PackageModel.getSERVICE_NAME().toLowerCase().contains(searchChr)){
//                            resultData.add(PackageModel);
//                        }
//                    }
//
//                    filterResults.count = resultData.size();
//                    filterResults.values = resultData;
//                }
//
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//
//                packageList = (List<PackageModel>) results.values;
//                notifyDataSetChanged();
//            }
//        };
//        return filter;

        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new MyAttendenceViewHolder(LayoutInflater.from(context).inflate(R.layout.myattendence_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyAttendence_Entity attendenceModel = myAttendenceList.get(position);
        MyAttendenceViewHolder  viewHolder =(MyAttendenceViewHolder) holder;

        viewHolder.jMyAttendence_date.setText(attendenceModel.getDATEs());
        viewHolder.jMyAttendence_login_time.setText(attendenceModel.getLOGINTIME());
        viewHolder.jMyAttendence_logout_time.setText(attendenceModel.getLOGOUTTIME());


        viewHolder.jMyAttendence_let_login_Reason.setText(" :"+attendenceModel.getLATE_LOGIN_REASON());
        viewHolder.jMyAttendence_early_logout_reason.setText(" :"+attendenceModel.getEARLY_LOGOUT_REASON());
        viewHolder.jMyAttendence_absence_reason.setText(" :"+attendenceModel.getABSENT_REASON());

        viewHolder.j_my_Attendence_row_layout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));


        if(attendenceModel.getLATE_LOGIN_REASON()==null)
        {
            viewHolder.jMyAttendence_let_login_Reason_layout.setVisibility(View.GONE);
        } else{
            viewHolder.jMyAttendence_let_login_Reason_layout.setVisibility(View.VISIBLE);
        }
        if(attendenceModel.getEARLY_LOGOUT_REASON() == null)
        {
            viewHolder.jMyAttendence_logout_reson_layout.setVisibility(View.GONE);
        } else {
            viewHolder.jMyAttendence_logout_reson_layout.setVisibility(View.VISIBLE);
        }
        if(attendenceModel.getABSENT_REASON() ==null)
        {
            viewHolder.jMyAttendence_absence_layout.setVisibility(View.GONE);
        } else {
            viewHolder.jMyAttendence_absence_layout.setVisibility(View.VISIBLE);
        }
        if(attendenceModel.getWeekend().equals("N")  && attendenceModel.getHoliday().equals("N") && attendenceModel.getLateLogin_Flag().equals("Y")){
            viewHolder.jMyAttendence_login_time.setTextColor(Color.RED);
        }else {
//                    loginTime.setTextColor(Color.GREEN);
            viewHolder.jMyAttendence_login_time.setTextColor(Color.parseColor("#4D850D"));
        }if(attendenceModel.getWeekend().equals("N")  && attendenceModel.getHoliday().equals("N") && attendenceModel.getEarlyLogOut_Flag().equals("Y")){
            viewHolder.jMyAttendence_logout_time.setTextColor(Color.RED);
        }else {
//                    logoutTime.setTextColor(Color.GREEN);
            viewHolder.jMyAttendence_logout_time.setTextColor(Color.parseColor("#4D850D"));
        }

    }

    @Override
    public int getItemCount() {
        return myAttendenceList.size();
    }

    private class MyAttendenceViewHolder extends RecyclerView.ViewHolder{
        private TextView jMyAttendence_date, jMyAttendence_login_time, jMyAttendence_logout_time,
                jMyAttendence_let_login_Reason, jMyAttendence_early_logout_reason,jMyAttendence_absence_reason;
        private LinearLayout jMyAttendence_let_login_Reason_layout, jMyAttendence_logout_reson_layout,
                jMyAttendence_absence_layout, j_my_Attendence_row_layout;

        public MyAttendenceViewHolder(@NonNull View itemView) {
            super(itemView);
            jMyAttendence_date = itemView.findViewById(R.id.myAttendence_date);
            jMyAttendence_login_time = itemView.findViewById(R.id.myAttendence_login_time);
            jMyAttendence_logout_time = itemView.findViewById(R.id.myAttendence_logout_time);
            jMyAttendence_let_login_Reason = itemView.findViewById(R.id.myAttendence_let_login_Reason);
            jMyAttendence_early_logout_reason = itemView.findViewById(R.id.myAttendence_early_logout_reason);
            jMyAttendence_absence_reason = itemView.findViewById(R.id.myAttendence_absence_reason);

            jMyAttendence_let_login_Reason_layout = itemView.findViewById(R.id.myAttendence_let_login_Reason_layout);
            jMyAttendence_logout_reson_layout = itemView.findViewById(R.id.myAttendence_early_logout_layout);
            jMyAttendence_absence_layout = itemView.findViewById(R.id.myAttendence_absence_layout);
            j_my_Attendence_row_layout = itemView.findViewById(R.id.my_Attendence_row_layout);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedAttendence.getSelectedMyAttendence(myAttendenceList.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface SelectedMyAttendence{
        void getSelectedMyAttendence( MyAttendence_Entity attendenceModel);
    }

}
