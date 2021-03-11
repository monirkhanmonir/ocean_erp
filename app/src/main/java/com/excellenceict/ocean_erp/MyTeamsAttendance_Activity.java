package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.MyTeamsAttendance_Model;
import com.excellenceict.ocean_erp.ODBC.Db;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.Helper;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyTeamsAttendance_Activity extends AppCompatActivity {
    //public class MyTeamsAttendence_Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Connection connection;
    private CustomAdapter_MyTeamsAttendence adapter;
    private List<MyTeamsAttendance_Model> teamsList;
    private TextView fromDate, toDate;
    private RecyclerView recyclerView;

    private Toolbar toolbar;
    private BusyDialog busyDialog;
    private Context context;
    private Helper helper;
    private String toDates, userInput;

    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_teams_attendence_activity);
        toDate = findViewById(R.id.toDate_teams);
        search = findViewById(R.id.searchTeams);
        recyclerView = findViewById(R.id.recycler_view);

        helper = new Helper();
        context = MyTeamsAttendance_Activity.this;

        //contraol toolbar
        toolbar = findViewById(R.id.myTeamAttendenceToolBarId);
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


        //<<<<<<<<<<<<<<<<<..........value pass Login to this Activity................>>>>>>>>>>>>>>>>>
        Bundle b = getIntent().getExtras();
        userInput = b.getString("loginValue");
        Log.d("Your_Login_value", "=====teams======login value==========" + userInput.toUpperCase());
        //<<<<<<<<<<<<<<<<<..........End value pass Login to this Activity................>>>>>>>>>>>>>>>>>

        String dateCurrent2 = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        toDate.setText(dateCurrent2.toUpperCase());
        toDates = dateCurrent2.toUpperCase();

        Log.d("CurrentDate", "===Treams===TO=======" + toDates);


        if (NetworkHelpers.isNetworkAvailable(context)) {
            new MyTeamsAttendenceTask().execute();
        } else {
            Toast.makeText(context, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
        }


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//                adapter.getFilter().filter(newText.toString());
                String userInput = newText.toLowerCase();
                List<MyTeamsAttendance_Model> newList = new ArrayList<>();
                for (MyTeamsAttendance_Model lists : teamsList) {
                    if (lists.getEmp_name().toLowerCase().contains(userInput)) {
                        newList.add((MyTeamsAttendance_Model) lists);

                    }
                }
                adapter.filterList((ArrayList<MyTeamsAttendance_Model>) newList);


                return false;
            }
        });
        dateSet();

    }


    public void dateSet() {

        toDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int year = 0, month = 0, day = 0;

            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker = new DatePickerDialog(MyTeamsAttendance_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
//
                        year = selectedyear;
                        month = selectedmonth;
                        day = selectedday;

                        cal.setTimeInMillis(0);
                        cal.set(year, month, day, 0, 0, 0);
                        Date chosenDate = cal.getTime();

                        // Format the date using style medium and US locale
                        DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
                        String date = df_medium_us.format(chosenDate);
                        toDate.setText(date.toUpperCase());

                        if (NetworkHelpers.isNetworkAvailable(context)) {
                            new MyTeamsAttendenceDateTask().execute();
                        } else {
                            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                        }


                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    private class MyTeamsAttendenceTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();

                Log.d("connection", "============TeamsDB=========Connect===========");
                if (connection != null) {

                    teamsList = new ArrayList<MyTeamsAttendance_Model>();
                }

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_EMP_NAME, V_DESIGNATION, V_DEPARTMENT, to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
                        "V_LATE_LOGIN_REASON,\n" +
                        "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
                        "V_EARLY_LOGOUT_REASON, V_ABSENT_REASON,\n" +
                        "decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                        "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                        "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG, \n" +
                        "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG \n" +
                        "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                        "where D_DATE between to_date('" + toDates + "','MON DD,RRRR') and to_date('" + toDates + "','MON DD,RRRR')\n" +
                        "and N_PERSON_ID in (select p.N_PERSON_ID from sec_user u, bas_person p where u.N_PERSON_ID=p.N_REPORTING_TO and u.V_USER_NAME='" + userInput.toUpperCase() + "')\n" +
                        "order by D_DATE");

                while (rs.next()) {

                    teamsList.add(new MyTeamsAttendance_Model(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13)));

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
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            adapter = new CustomAdapter_MyTeamsAttendence(context, teamsList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            search.setActivated(true);
            search.setQueryHint("Search name.");
            search.onActionViewExpanded();
            search.setIconified(false);
            search.clearFocus();
        }
    }

    private class MyTeamsAttendenceDateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                connection = Db.createConnection();


                final String toSelectDate = toDate.getText().toString();
                Bundle b = getIntent().getExtras();
                String userInput = b.getString("loginValue");
                Log.d("Your_Login_value", "=====teams======login value==========" + userInput.toUpperCase());

                if (connection != null) {
                    teamsList = new ArrayList<MyTeamsAttendance_Model>();

                }

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_EMP_NAME, V_DESIGNATION, V_DEPARTMENT, to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
                        "V_LATE_LOGIN_REASON,\n" +
                        "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
                        "V_EARLY_LOGOUT_REASON, V_ABSENT_REASON,\n" +
                        "decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                        "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                        "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG, \n" +
                        "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG \n" +
                        "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                        "where D_DATE between to_date('" + toSelectDate + "','MON DD,RRRR') and to_date('" + toSelectDate + "','MON DD,RRRR')\n" +
                        "and N_PERSON_ID in (select p.N_PERSON_ID from sec_user u, bas_person p where u.N_PERSON_ID=p.N_REPORTING_TO and u.V_USER_NAME='" + userInput.toUpperCase() + "')\n" +
                        "order by D_DATE");

                while (rs.next()) {
                    teamsList.add(new MyTeamsAttendance_Model(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13)));

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
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            adapter = new CustomAdapter_MyTeamsAttendence(context, teamsList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }
    }

}



