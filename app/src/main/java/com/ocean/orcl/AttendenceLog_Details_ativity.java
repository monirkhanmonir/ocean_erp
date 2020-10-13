package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class AttendenceLog_Details_ativity extends AppCompatActivity {
    private Connection connection;
    ArrayList<AttendenceLog_Details_A_Entity> myAttendenceDetiles;
    ArrayList<AttendenceLog_Details_B_Entity> myAttendenceDetiles_b;
    ArrayList<AttendenceLog_Details_C_Entity> myAttendenceDetiles_c;
    ListView listView;
    CustomAdapter_AttendenceLog_Details adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_log__details_ativity);
        listView = findViewById(R.id.attendenceDetails_listView);

        // ....................For Query (A) ..............
        TextView personID = findViewById(R.id.aDetiles_id);
        TextView currentDate = findViewById(R.id.aDetiles_date);
        TextView personName = findViewById(R.id.aDetiles_Empname);
        TextView personDesignation = findViewById(R.id.aDetiles_designation);
        TextView personDepartment = findViewById(R.id.aDetiles_department);

        // ....................For Query (B) ..............
        TextView weekend = findViewById(R.id.aDetailes_weekend);
        TextView holiday = findViewById(R.id.aDetailes_holiday);

        TextView inTime = findViewById(R.id.aDetailes_inTime);
        TextView Login_office_location = findViewById(R.id.aDetailes_login_Office_Location);
        TextView require_LoginTime = findViewById(R.id.aDetailes_requireLoginTime);

        TextView office_LogoutTime = findViewById(R.id.aDetailes_officeLogOut);
        TextView logOut_Locaton = findViewById(R.id.aDetailes_logOutOffice_Location);
        TextView require_LogOutTime = findViewById(R.id.aDetailes_requireLogOutTime);
        TextView earlyLogoutReason = findViewById(R.id.aDetailes_earlyLogoutReason);

        // ....................For Query (C) ..............
        TextView time = findViewById(R.id.aDetails_time);
        TextView mode = findViewById(R.id.aDetails_mode);
        TextView location = findViewById(R.id.aDetails_location);


        Intent intent = getIntent();
        String id = intent.getStringExtra("person_id");
        String date = intent.getStringExtra("current_date");
        String name = intent.getStringExtra("personName");
        String designation = intent.getStringExtra("personDesignation");
        String department = intent.getStringExtra("person_department");
//        String position = intent.getStringExtra("position");
//        Log.d("posationTest","==========list_positon=========="+position);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // ....................Query A ..............
        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "==========AttencenceLog details===========Connect===========");
            if (connection != null) {
                myAttendenceDetiles = new ArrayList<AttendenceLog_Details_A_Entity>();

            }
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select PKG$HRM.fnc$emp_name2(V_SALUTATION,V_FNAME, V_LNAME) V_EMP_NAME, V_DEPT_NAME, V_DESIG_NAME \n" +
                    " from BAS_PERSON\n" +
                    " where V_PERSON_NO = to_char('" + id + "')");


            while (rs.next()) {
                Log.d("result", "========rs result=========" + rs.getString(1));
                Log.d("valueOfQuery", "==========row count2=========" + rs.getRow());

//                myAttendenceDetiles.add(new AttendenceLog_Details_A_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
                Log.d("name", "======name==A======" + rs.getString(1));
                Log.d("designation", "======designation===A===" + rs.getString(2));
                Log.d("department", "======department==A===" + rs.getString(3));

                personName.setText(rs.getString(1));
                personDepartment.setText(rs.getString(2));
//                personDepartment.setText(department);
                personDesignation.setText(rs.getString(3));
            }

            personID.setText(id);
            currentDate.setText(date.toUpperCase());

            connection.close();
        } catch (Exception e) {

            Toast.makeText(AttendenceLog_Details_ativity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //......................For Query (B) ....................
        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "==========(B)===========Connect===========");
            if (connection != null) {
                myAttendenceDetiles_b = new ArrayList<AttendenceLog_Details_B_Entity>();

            }
            Statement stmt = connection.createStatement();

            // ....................Query B ..............
            ResultSet rs = stmt.executeQuery("select decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG, \n" +
                    "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                    "to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME,V_LOGIN_OFFICE_LOCATION,V_REQUIRED_LOGIN_TIME,decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG,V_LATE_LOGIN_REASON,\n" +
                    "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,V_LOGOUT_OFFICE_LOCATION,V_REQUIRED_LOGOUT_TIME, decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG,V_EARLY_LOGOUT_REASON,\n" +
                    "N_ABSENT_FLAG, V_ABSENT_REASON\n" +
                    "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                    "where V_PERSON_NO = to_char(" + id + ") \n" +
                    "and D_DATE=to_date('" + date.toUpperCase() + "','MON DD,RRRR')");


            while (rs.next()) {
//                myAttendenceDetiles.add(new AttendenceLog_Details_A_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));

                weekend.setText(rs.getString(1));
                holiday.setText(rs.getString(2));

                //...............2nd line......
                inTime.setText(rs.getString(3));
                Login_office_location.setText(rs.getString(4));
                require_LoginTime.setText(rs.getString(5));

                //...............1st line......
                office_LogoutTime.setText(rs.getString(8));
                logOut_Locaton.setText(rs.getString(9));
                require_LogOutTime.setText(rs.getString(10));
                earlyLogoutReason.setText(rs.getString(12));
//
//                Log.d("1","======1+Weekend===="+rs.getString(1));
//                Log.d("2","======2+Holiday===="+rs.getString(2));
//                Log.d("3","======3===="+rs.getString(3));
//                Log.d("4","======4===="+rs.getString(4));
//                Log.d("5","======5===="+rs.getString(5));
//                Log.d("6","======6+Late loginFlag===="+rs.getString(6));
//                Log.d("7","======7===="+rs.getString(7));
//                Log.d("8","======8===="+rs.getString(8));
//                Log.d("9","======9===="+rs.getString(9));
//                Log.d("10","======10===="+rs.getString(10));
//                Log.d("11","======11+early logout flag===="+rs.getString(11));
//                Log.d("12","======12===="+rs.getString(12));

//                 .............For Login and Logout Time Color................
                String weekends = rs.getString(1);
                String holidays = rs.getString(2);
                String late_login_flag = rs.getString(6);
                String early_logout_flag =rs.getString(11);
                Log.d("colorTest","=======inTime======"+weekends+","+holidays+","+late_login_flag);
                Log.d("colorTest2","=======logOut======"+weekends+","+holidays+","+early_logout_flag);

                if(weekends.equals("N") && holidays.equals("N") && late_login_flag.equals("Y")){
                    inTime.setTextColor(Color.RED);
                }else {
                    inTime.setTextColor(Color.parseColor("#4D850D"));
                }if(weekends.equals("N") && holidays.equals("N") && early_logout_flag.equals("Y")){
                    office_LogoutTime.setTextColor(Color.RED);
                }else {
                    office_LogoutTime.setTextColor(Color.parseColor("#4D850D"));
                }

            }

            connection.close();
        } catch (Exception e) {

            Toast.makeText(AttendenceLog_Details_ativity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //......................For Query (C) ....................
        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "==========(C)===========Connect===========");
            if (connection != null) {
                myAttendenceDetiles_c = new ArrayList<AttendenceLog_Details_C_Entity>();

            }
            Statement stmt = connection.createStatement();

            // ....................Query C ..............

            ResultSet rs = stmt.executeQuery("select  V_TIME V_TIME1, to_char(V_TIME,'HH12:MI AM') V_TIME , V_MODE, V_OFFICE_LOCATION\n" +
                    "from VW_HR_EMP_ATTENDANCE\n" +
                    "where V_PERSON_NO = to_char(" + id + ") \n" +
                    "and D_DATE=to_date('" + date.toUpperCase() + "','MON DD,RRRR')\n" +
                    "order by V_TIME1 asc");

            while (rs.next()) {
                myAttendenceDetiles_c.add(new AttendenceLog_Details_C_Entity(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4)));
                Log.d("Col", "========column======" + rs.getRow());
                Log.d("time1", "========c======" + rs.getString(1));
                Log.d("mode2", "=========c===" + rs.getString(2));
                Log.d("location3", "========c===" + rs.getString(3));
                Log.d("location3", "========c===" + rs.getString(4));
                Log.d("location3", "==========c===================");


            }
            adapter = new CustomAdapter_AttendenceLog_Details(this, myAttendenceDetiles_c);
            listView.setAdapter(adapter);


            connection.close();
        } catch (Exception e) {

            Toast.makeText(AttendenceLog_Details_ativity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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

//        if( personDate.equals(null) || personDate.length()==0 || personDate.equals(""))
//        {
//            personDate.setVisibility(View.GONE);
//        } else {
//            personDate.setVisibility(View.VISIBLE);
//        }
//        if( personTime.equals(null) || personTime.length()==0 || personTime.equals(""))
//        {
//            personTime.setVisibility(View.GONE);
//        } else {
//            personTime.setVisibility(View.VISIBLE);
//        }if( personLocation.equals(null) || personLocation.length()==0 || personLocation.equals(""))
//        {
//            personLocation.setVisibility(View.GONE);
//        } else {
//            personLocation.setVisibility(View.VISIBLE);
//        }

    }


}
