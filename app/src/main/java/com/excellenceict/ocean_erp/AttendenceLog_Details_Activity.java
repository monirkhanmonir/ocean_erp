package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.AttendenceLog_Details_C_Model;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.Helper;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class AttendenceLog_Details_Activity extends AppCompatActivity {
    private Connection connection;
    private ArrayList<AttendenceLog_Details_C_Model> myAttendenceDetiles_c;
    private ListView listView;
    private CustomAdapter_AttendenceLog_Details adapter;
    private BusyDialog busyDialog;
    private Handler handler;
    private Context context;
    private TextView late_duration, early_duration;
    private String id, date, early_logout_flag, weekends_flag,
            holidays_flag, late_login_flag, late_loginreason, absent_flag, absent_reason;
    private LinearLayout j_attendence_log_details_layout;
    private Toolbar toolbar;
    private String lateVal = "", earlyVal = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_log__details_ativity);
        listView = findViewById(R.id.attendenceDetails_listView);
        j_attendence_log_details_layout = findViewById(R.id.attendence_log_details_layout);
        late_duration = findViewById(R.id.att_log_lateDuration);
        early_duration = findViewById(R.id.att_log_earlyDuration);
        toolbar = findViewById(R.id.AttendenceDetailsToolBarId);

        // ....................For Query (A) ..............
        TextView personID = findViewById(R.id.aDetiles_id);
        TextView currentDate = findViewById(R.id.aDetiles_date);
        TextView personName = findViewById(R.id.aDetiles_Empname);
        TextView personDesignation = findViewById(R.id.aDetiles_designation);
        TextView personDepartment = findViewById(R.id.aDetiles_department);

        // ....................For Query (B) ..............
        TextView weekend_holyday = findViewById(R.id.aDetailes_weekend_holiday);
        // TextView holiday = findViewById(R.id.aDetailes_holiday);

        TextView inTime = findViewById(R.id.aDetailes_inTime);
        TextView Login_office_location = findViewById(R.id.aDetailes_login_Office_Location);
        TextView require_LoginTime = findViewById(R.id.aDetailes_requireLoginTime);

        TextView logout_Time = findViewById(R.id.aDetailes_officeLogOut);
        TextView logOut_office_Locaton = findViewById(R.id.aDetailes_logOutOffice_Location);
        TextView require_LogOutTime = findViewById(R.id.aDetailes_requireLogOutTime);
        TextView earlyLogoutReason = findViewById(R.id.aDetailes_earlyLogoutReason);

        // ....................For Query (C) ..............
        TextView time = findViewById(R.id.aDetails_time);
        TextView mode = findViewById(R.id.aDetails_mode);
        TextView location = findViewById(R.id.aDetails_location);

        handler = new Handler();
        context = AttendenceLog_Details_Activity.this;
        //  j_attendence_log_details_layout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));


        //control toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        id = intent.getStringExtra("person_id");
        date = intent.getStringExtra("current_date");
        String name = intent.getStringExtra("personName");
        String designation = intent.getStringExtra("personDesignation");
        String department = intent.getStringExtra("person_department");


        // ...................Query A ..............
        try {
            connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
            Log.d("DB", "==========AttencenceLog details===========Connect===========");
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select PKG$HRM.fnc$emp_name2(V_SALUTATION,V_FNAME, V_LNAME) V_EMP_NAME, V_DEPT_NAME, V_DESIG_NAME \n" +
                    " from BAS_PERSON\n" +
                    " where V_PERSON_NO = to_char('" + id + "')");


            while (rs.next()) {
                personName.setText(rs.getString(1));
                personDepartment.setText(rs.getString(2));
                personDesignation.setText(rs.getString(3));
            }

            personID.setText(id);
            currentDate.setText(date.toUpperCase());

            connection.close();
        } catch (Exception e) {

            Toast.makeText(AttendenceLog_Details_Activity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //......................For Query (B) ....................
        try {
            connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
            Log.d("connection", "=========(B)==========Connect===========");

            Statement stmt = connection.createStatement();

            String query = "select decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                    "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG,\n" +
                    "to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME,V_LOGIN_OFFICE_LOCATION,\n" +
                    "to_char(D_REQUIRED_LOGINTIME,'HH12:MI AM') V_REQUIRED_LOGIN_TIME,\n" +
                    "V_LATE_DURATION,\n" +
                    "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG,V_LATE_LOGIN_REASON,\n" +
                    "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,V_LOGOUT_OFFICE_LOCATION,\n" +
                    "to_char(D_REQUIRED_LOGOUTTIME,'HH12:MI AM') V_REQUIRED_LOGOUT_TIME,\n" +
                    "V_EARLY_DURATION,\n" +
                    "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG,V_EARLY_LOGOUT_REASON,\n" +
                    "N_ABSENT_FLAG, V_ABSENT_REASON\n" +
                    "from VW_HR_EMP_ATTENDANCE_SUMMARY \n" +
                    "where V_PERSON_NO = to_char('" + id + "')" +
                    "and D_DATE=to_date('" + date.toUpperCase() + "','MON DD,RRRR')";

            ResultSet rs = stmt.executeQuery(query);


            while (rs.next()) {

                weekend_holyday.setText("Weekend= " + rs.getString(1) + ", Holiday= " + rs.getString(2));

                weekends_flag = rs.getString(1);
                holidays_flag = rs.getString(2);
                inTime.setText(rs.getString(3));
                Login_office_location.setText(rs.getString(4));
                require_LoginTime.setText(rs.getString(5));

                String late_Duration = rs.getString(6);
                if (late_Duration != null && late_Duration.trim().length() > 7) {
                    lateVal = late_Duration.substring(0, late_Duration.length() - 3);
                } else {
                    lateVal = late_Duration;
                }
                late_duration.setText(lateVal);
                late_login_flag = rs.getString(7);
                late_loginreason = rs.getString(8);
                logout_Time.setText(rs.getString(9));
                logOut_office_Locaton.setText(rs.getString(10));
                require_LogOutTime.setText(rs.getString(11));

                String early_Duration = rs.getString(12);
                if (early_Duration != null && early_Duration.length() > 7) {
                    earlyVal = early_Duration.substring(0, early_Duration.length() - 3);
                } else {
                    earlyVal = rs.getString(12);
                }
                early_duration.setText(earlyVal);
                early_logout_flag = rs.getString(13);
                earlyLogoutReason.setText(rs.getString(14));
                absent_flag = rs.getString(15);
                absent_reason = rs.getString(16);

                Log.d("1", "==V_WEEKEND_FLAG===>" + rs.getString(1));
                Log.d("2", "==V_HOLIDAY_FLAG===>" + rs.getString(2));
                Log.d("3", "==V_LOGINTIME===>" + rs.getString(3));
                Log.d("4", "==V_LOGIN_OFFICE_LOCATION===>" + rs.getString(4));
                Log.d("5", "==V_REQUIRED_LOGIN_TIME===>" + rs.getString(5));
                Log.d("6", "==V_LATE_DURATION===>" + rs.getString(6));
                Log.d("7", "==V_LATE_LOGIN_FLAG===>" + rs.getString(7));
                Log.d("8", "==V_LATE_LOGIN_REASON===>" + rs.getString(8));
                Log.d("9", "==V_LOGOUTTIME===>" + rs.getString(9));
                Log.d("10", "==V_LOGOUT_OFFICE_LOCATION===>" + rs.getString(10));
                Log.d("11", "==V_REQUIRED_LOGOUT_TIME===>" + rs.getString(11));
                Log.d("12", "==V_EARLY_DURATION===>" + rs.getString(12));
                Log.d("13", "==V_EARLY_LOGOUT_FLAG===>" + rs.getString(13));
                Log.d("14", "==V_EARLY_LOGOUT_REASON===>" + rs.getString(14));
                Log.d("15", "==N_ABSENT_FLAG===>" + rs.getString(15));
                Log.d("16", "==V_ABSENT_REASON===>" + rs.getString(16));

                if (weekends_flag.equals("N") && holidays_flag.equals("N") && late_login_flag.equals("Y")) {
                    inTime.setTextColor(Color.RED);
                } else {
                    inTime.setTextColor(Color.parseColor("#4D850D"));
                }
                if (weekends_flag.equals("N") && holidays_flag.equals("N") && early_logout_flag.equals("Y")) {
                    logout_Time.setTextColor(Color.RED);
                } else {
                    logout_Time.setTextColor(Color.parseColor("#4D850D"));
                }

            }

            connection.close();
        } catch (Exception e) {

            Toast.makeText(AttendenceLog_Details_Activity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //......................For Query (C) ....................

        if (NetworkHelpers.isNetworkAvailable(context)) {
            new AttendenceLoginDetailsC().execute();
        } else {
            Toast.makeText(context, "Please check your internate connection", Toast.LENGTH_SHORT).show();
        }

        if (Login_office_location.equals(null) || Login_office_location.length() == 0 || Login_office_location.equals("")) {
            Login_office_location.setVisibility(View.GONE);
        } else {
            Login_office_location.setVisibility(View.VISIBLE);
        }
        if (earlyLogoutReason.equals(null) || earlyLogoutReason.length() == 0 || earlyLogoutReason.equals("")) {
            earlyLogoutReason.setVisibility(View.GONE);
        } else {
            earlyLogoutReason.setVisibility(View.VISIBLE);
        }

    }


    private class AttendenceLoginDetailsC extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "==========(C)===========Connect===========");
                if (connection != null) {
                    myAttendenceDetiles_c = new ArrayList<AttendenceLog_Details_C_Model>();

                }
                Statement stmt = connection.createStatement();

                // ....................Query C ..............

                ResultSet rs = stmt.executeQuery("select  V_TIME V_TIME1, to_char(V_TIME,'HH12:MI AM') V_TIME , V_MODE, V_OFFICE_LOCATION\n" +
                        "from VW_HR_EMP_ATTENDANCE\n" +
                        "where V_PERSON_NO = to_char(" + id + ") \n" +
                        "and D_DATE=to_date('" + date.toUpperCase() + "','MON DD,RRRR')\n" +
                        "order by V_TIME1 asc");

                while (rs.next()) {
                    myAttendenceDetiles_c.add(new AttendenceLog_Details_C_Model(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }

                busyDialog.dismis();

                connection.close();
            } catch (Exception e) {

                busyDialog.dismis();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new CustomAdapter_AttendenceLog_Details(context, myAttendenceDetiles_c);
            listView.setAdapter(adapter);
            Helper.getListViewSize(listView);
        }
    }

}