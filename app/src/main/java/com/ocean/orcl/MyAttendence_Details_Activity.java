package com.ocean.orcl;

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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ocean.orcl.util.BusyDialog;
import com.ocean.orcl.util.Helper;
import com.ocean.orcl.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MyAttendence_Details_Activity extends AppCompatActivity {
    private Connection connection;
    private TextView lateLogin_reason, absent_reason;
    private Button editBtn;
    private ListView listView;
    private CustomAdapter_AttendenceLog_Details adapter;
    private ArrayList<AttendenceLog_Details_A_Entity> myAttendenceDetiles;
    private ArrayList<AttendenceLog_Details_B_Entity> myAttendenceDetiles_b;
    private ArrayList<AttendenceLog_Details_C_Entity> myAttendenceDetiles_c;
    private String late_login_flag;
    private String early_logout_flag, absence_flag, userInput, dates;
    private LinearLayout j_my_Att_Detailes_lateLoginReason_layout, j_my_Att_Detailes_earlyLogoutReason_layout,
             j_my_Att_Detailes_absentReason_layout;

    private BusyDialog busyDialog;
    private Context context;
    private Handler handler;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendence__details);
        listView = findViewById(R.id.myAttDetails_listView);
        editBtn = findViewById(R.id.edit_Btn);
        context = getApplicationContext();
        handler = new Handler();
        toolbar = findViewById(R.id.myTeamAttendenceDetailsToolBarId);

        // ....................For Query (A) ..............
        TextView personID = findViewById(R.id.myAttendanceDetails_id);
        final TextView date = findViewById(R.id.myAttendanceDetails_date);
        TextView personName = findViewById(R.id.myAttendanceDetails_name);
        TextView personDesignation = findViewById(R.id.myAttendanceDetails_designation);
        TextView personDepartment = findViewById(R.id.myAttendanceDetails_dept);

        j_my_Att_Detailes_lateLoginReason_layout = findViewById(R.id.my_Att_Detailes_lateLoginReason_layout);
        j_my_Att_Detailes_earlyLogoutReason_layout = findViewById(R.id.my_Att_Detailes_earlyLogoutReason_layout);
        j_my_Att_Detailes_absentReason_layout = findViewById(R.id.my_Att_Detailes_absentReason_layout);

        // ....................For Query (B) ..............
        final TextView weekend = findViewById(R.id.myAttendanceDetails_weekend);
        final TextView holiday = findViewById(R.id.myAttendanceDetails_holiday);


        final TextView inTime = findViewById(R.id.my_Att_Detailes_inTime);
        final TextView Login_office_location = findViewById(R.id.my_Att_Detailes_login_Office_Location);
        final TextView require_LoginTime = findViewById(R.id.my_Att_Detailes_requireLoginTime);

        final TextView office_LogoutTime = findViewById(R.id.my_Att_Detailes_officeLogOut);
        TextView logOut_Locaton = findViewById(R.id.my_Att_Detailes_logOutOffice_Location);
        final TextView require_LogOutTime = findViewById(R.id.my_Att_Detailes_requireLogOutTime);
        final TextView earlyLogoutReason = findViewById(R.id.my_Att_Detailes_earlyLogoutReason);
        lateLogin_reason = findViewById(R.id.my_Att_Detailes_lateLoginReason);
        absent_reason = findViewById(R.id.my_Att_Detailes_absentReason);


        //controll toolbar
        this.setSupportActionBar(toolbar);
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



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Intent intent = getIntent();
        userInput = intent.getStringExtra("login_value");
        dates = intent.getStringExtra("date");
        Log.d("myAttendence", "=======My Attndnc==LV======" + userInput.toUpperCase());

        date.setText(dates);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyAttendence_DetailsUpdate_Activity.class);
                intent.putExtra("date", dates);
                intent.putExtra("weekend", weekend.getText());
                intent.putExtra("holiday", holiday.getText());
                intent.putExtra("late_login_flag", late_login_flag);
                intent.putExtra("early_logout_flag", early_logout_flag);
                intent.putExtra("absent_reason", absent_reason.getText());
                intent.putExtra("late_loginReason", lateLogin_reason.getText());
                intent.putExtra("early_logoutreaason", earlyLogoutReason.getText());
                intent.putExtra("login_time", inTime.getText());
                intent.putExtra("due_loginTime", require_LoginTime.getText());
                intent.putExtra("logout_time", office_LogoutTime.getText());
                intent.putExtra("due_logout_time", require_LogOutTime.getText());
                intent.putExtra("dept", Login_office_location.getText());
                intent.putExtra("absence", absence_flag);
                intent.putExtra("userInput", userInput);
                startActivity(intent);
            }
        });

        // ....................Query A ..............
        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "==========MyAttencence==Details===A===========Connect===========");
            if (connection != null) {
                myAttendenceDetiles = new ArrayList<AttendenceLog_Details_A_Entity>();

            }
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select PKG$HRM.fnc$emp_name2(V_SALUTATION,V_FNAME, V_LNAME) V_EMP_NAME, V_DEPT_NAME, V_DESIG_NAME,V_PERSON_NO \n" +
                    " from BAS_PERSON\n" +
                    " where  N_PERSON_ID = (select N_PERSON_ID from sec_user where V_USER_NAME='" + userInput.toUpperCase() + "')");

            while (rs.next()) {

                personName.setText(rs.getString(1));
                personDepartment.setText(rs.getString(2));
                personDesignation.setText(rs.getString(3));
                personID.setText(rs.getString(4));
            }


            connection.close();
        } catch (Exception e) {

            Toast.makeText(MyAttendence_Details_Activity.this, "" + e,
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
                    "where  N_PERSON_ID = (select N_PERSON_ID from sec_user where V_USER_NAME='" + userInput.toUpperCase() + "') \n" +
                    "and D_DATE=to_date('" + dates.toUpperCase() + "','MON DD,RRRR')");


            while (rs.next()) {
//                myAttendenceDetiles.add(new AttendenceLog_Details_A_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));

                weekend.setText(rs.getString(1));
                holiday.setText(rs.getString(2));

                //...............2nd line......
                inTime.setText(rs.getString(3));
                Login_office_location.setText(rs.getString(4));
                require_LoginTime.setText(rs.getString(5));
                lateLogin_reason.setText(rs.getString(7));
                absent_reason.setText(rs.getString(14));

                //...............1st line......
                office_LogoutTime.setText(rs.getString(8));
                logOut_Locaton.setText(rs.getString(9));
                require_LogOutTime.setText(rs.getString(10));
                earlyLogoutReason.setText(rs.getString(12));
                absence_flag = rs.getString(13);

                String weekends = rs.getString(1);
                String holidays = rs.getString(2);
                late_login_flag = rs.getString(6);
                early_logout_flag = rs.getString(11);

//                 .............For Login and Logout Time Color................
                if (weekends.equals("N") && holidays.equals("N") && late_login_flag.equals("Y")) {
                    inTime.setTextColor(Color.RED);
                } else {
                    inTime.setTextColor(Color.parseColor("#4D850D"));
                }
                if (weekends.equals("N") && holidays.equals("N") && early_logout_flag.equals("Y")) {
                    office_LogoutTime.setTextColor(Color.RED);
                } else {
                    office_LogoutTime.setTextColor(Color.parseColor("#4D850D"));
                }

            }

            connection.close();
        } catch (Exception e) {

            Toast.makeText(MyAttendence_Details_Activity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //......................For Query (C) ....................

        if (NetworkHelpers.isNetworkAvailable(context)) {
            new MyAttendenceDetailstask().execute();
        } else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }


        if (Login_office_location.equals(null) || Login_office_location.length() == 0 || Login_office_location.equals("")) {
            Login_office_location.setVisibility(View.GONE);
        } else {
            Login_office_location.setVisibility(View.VISIBLE);
        }

        if (earlyLogoutReason.equals(null) || earlyLogoutReason.length() == 0 || earlyLogoutReason.equals("")) {
            j_my_Att_Detailes_earlyLogoutReason_layout.setVisibility(View.GONE);
        } else {
            j_my_Att_Detailes_earlyLogoutReason_layout.setVisibility(View.VISIBLE);
        }

        if (lateLogin_reason.getText().equals(null) || lateLogin_reason.getText().length() == 0 || lateLogin_reason.getText().equals("")) {
            j_my_Att_Detailes_lateLoginReason_layout.setVisibility(View.GONE);
        } else {
            j_my_Att_Detailes_lateLoginReason_layout.setVisibility(View.VISIBLE);
        }
        if (absent_reason.getText().equals(null) || absent_reason.getText().length() == 0 || absent_reason.getText().equals("")) {
            j_my_Att_Detailes_absentReason_layout.setVisibility(View.GONE);
        } else {
            j_my_Att_Detailes_absentReason_layout.setVisibility(View.VISIBLE);
        }

    }


    private class MyAttendenceDetailstask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

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
                        "where N_PERSON_ID = (select N_PERSON_ID from sec_user where V_USER_NAME = '" + userInput.toUpperCase() + "')\n " +
                        "and D_DATE=to_date('" + dates.toUpperCase() + "','MON DD,RRRR')\n" +
                        "order by V_TIME1 asc");

                while (rs.next()) {
                    myAttendenceDetiles_c.add(new AttendenceLog_Details_C_Entity(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

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
            busyDialog.dismis();
            adapter = new CustomAdapter_AttendenceLog_Details(context, myAttendenceDetiles_c);
            listView.setAdapter(adapter);
            Helper.getListViewSize(listView);
        }
    }
}
