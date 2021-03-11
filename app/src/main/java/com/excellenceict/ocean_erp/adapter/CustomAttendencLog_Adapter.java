package com.excellenceict.ocean_erp.adapter;

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

import com.excellenceict.ocean_erp.Model.AttendenceLog_Model;
import com.excellenceict.ocean_erp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAttendencLog_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<AttendenceLog_Model> attendenceList;
    private List<AttendenceLog_Model> attendenceFilterList;
    private Context context;
    private SelectedAttendenceLog selectedAttendenceLog;

    public CustomAttendencLog_Adapter(List<AttendenceLog_Model> attendenceList, SelectedAttendenceLog selectedAttendenceLog) {
        this.attendenceList = attendenceList;
        this.selectedAttendenceLog = selectedAttendenceLog;
        this.attendenceFilterList = attendenceList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new AttendenceLogViewHolder(LayoutInflater.from(context).inflate(R.layout.attendenceloglistview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        AttendenceLog_Model personAttendence = attendenceList.get(position);
        AttendenceLogViewHolder viewHolder = (AttendenceLogViewHolder) holder;


        viewHolder.personName.setText(personAttendence.getPersongName());
        viewHolder.empID.setText(personAttendence.getPersongID());
        viewHolder.designation.setText(personAttendence.getPersongDesignaton());
        viewHolder.depertment.setText(personAttendence.getPersonDepartment());
        viewHolder.loginTime.setText(personAttendence.getPersongLoginTime());
        String late_duration = personAttendence.getLateDuration();
        String early_duration = personAttendence.getEarlyDuration();
        String lateVal = "", earlyVal = "";

        if (late_duration != null && late_duration.trim().length() > 7) {
            lateVal = late_duration.substring(0, late_duration.length() - 3);
        } else {
            lateVal = personAttendence.getLateDuration();
        }
        if (early_duration != null && early_duration.length() > 7) {
            earlyVal = early_duration.substring(0, early_duration.length() - 3);
        } else {
            earlyVal = personAttendence.getEarlyDuration();
        }
        viewHolder.lateDuration.setText(lateVal);
        viewHolder.logoutTime.setText(personAttendence.getPersonLogoutTime());
        viewHolder.earlyDuration.setText(earlyVal);


        viewHolder.j_attendence_log_row_layout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        if (personAttendence.getWeekend().equals("N") && personAttendence.getHoliday().equals("N") && personAttendence.getLateLogin_Flag().equals("Y")) {
            viewHolder.loginTime.setTextColor(Color.RED);
        } else {
//                    loginTime.setTextColor(Color.GREEN);
            viewHolder.loginTime.setTextColor(Color.parseColor("#4D850D"));
        }
        if (personAttendence.getWeekend().equals("N") && personAttendence.getHoliday().equals("N") && personAttendence.getEarlyLogOut_Flag().equals("Y")) {
            viewHolder.logoutTime.setTextColor(Color.RED);
        } else {
//                    logoutTime.setTextColor(Color.GREEN);
            viewHolder.logoutTime.setTextColor(Color.parseColor("#4D850D"));
        }

    }

    @Override
    public int getItemCount() {
        return attendenceList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null | constraint.length() == 0) {
                    filterResults.count = attendenceFilterList.size();
                    filterResults.values = attendenceFilterList;

                } else {
                    String searchChr = constraint.toString().toLowerCase();
                    List<AttendenceLog_Model> resultData = new ArrayList<>();
                    for (AttendenceLog_Model attendenceModel : attendenceFilterList) {
                        if (attendenceModel.getPersongName().toLowerCase().contains(searchChr) | attendenceModel.getPersonDepartment().toLowerCase().contains(searchChr) | attendenceModel.getPersongDesignaton().toLowerCase().contains(searchChr) | attendenceModel.getPersongID().toLowerCase().contains(searchChr)) {
                            resultData.add(attendenceModel);
                        }
                    }

                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                attendenceList = (List<AttendenceLog_Model>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    private class AttendenceLogViewHolder extends RecyclerView.ViewHolder {

        private TextView personName, empID, designation, depertment, loginTime, logoutTime, earlyDuration, lateDuration;
        private LinearLayout j_attendence_log_row_layout;

        public AttendenceLogViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.personName_text);
            empID = itemView.findViewById(R.id.empId_text);
            designation = itemView.findViewById(R.id.designation_text);
            depertment = itemView.findViewById(R.id.department_text);
            loginTime = itemView.findViewById(R.id.login_text);
            lateDuration = itemView.findViewById(R.id.login_duration);
            logoutTime = itemView.findViewById(R.id.Logout_text);
            earlyDuration = itemView.findViewById(R.id.logout_duration);
            j_attendence_log_row_layout = itemView.findViewById(R.id.attendence_log_row_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedAttendenceLog.getSelectedAttendence(attendenceList.get(getAdapterPosition()));
                }
            });

        }
    }


    public interface SelectedAttendenceLog {
        void getSelectedAttendence(AttendenceLog_Model person_log);
    }

}
