package com.excellenceict.ocean_erp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.excellenceict.ocean_erp.Model.AttendenceLog_Model;
import com.excellenceict.ocean_erp.ODBC.Db;
import com.excellenceict.ocean_erp.adapter.CustomAttendencLog_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttendenceLog_Activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CustomAttendencLog_Adapter.SelectedAttendenceLog {
    private Connection connection;

    private ArrayList<AttendenceLog_Model> attendenceLogItems;
    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener2;

    private CustomAttendencLog_Adapter attendenceLogAdapter;
    private RecyclerView jAttendenceLogRecyclerView;

    private SearchView search;
    private SwipeRefreshLayout swipeRefreshL;
    private TextView from_dateFill;
    private BusyDialog busyDialog;
    private Context context;
    private String dateCurrent2, lateVal, earlyVal;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_log_activity);

        search = findViewById(R.id.search);
        swipeRefreshL = findViewById(R.id.swipeRefresh);
        jAttendenceLogRecyclerView = findViewById(R.id.attendenceLogRecyclerView);
        from_dateFill = findViewById(R.id.fromDate_Alog);
        toolbar = findViewById(R.id.attendenceLogToolBarId);
        context = AttendenceLog_Activity.this;

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

        //-------------- Filled default set Textview in Current Date ------------
        dateCurrent2 = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        from_dateFill.setText(dateCurrent2.toUpperCase());
        Log.d("CurrentDate", "======Default=======" + dateCurrent2);

        if (NetworkHelpers.isNetworkAvailable(context)) {
            new AttendenceLogTask().execute();
        } else {
            Toast.makeText(context, "Please check your internate connection", Toast.LENGTH_SHORT).show();
        }

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                attendenceLogItems.clear();
                if (attendenceLogItems.contains(query)) {
                    attendenceLogAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(AttendenceLog_Activity.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                AttendenceLog_Activity.this.attendenceLogAdapter.getFilter().filter(newText);

                return false;
            }
        });

        swipeRefreshL.setOnRefreshListener(this);
        dateSet();
    }

    @Override
    public void onRefresh() {
        attendenceLogAdapter = new CustomAttendencLog_Adapter(attendenceLogItems, AttendenceLog_Activity.this);
        jAttendenceLogRecyclerView.setAdapter(attendenceLogAdapter);
        attendenceLogAdapter.notifyDataSetChanged();
        swipeRefreshL.setRefreshing(false);

    }

    public void dateSet() {

        from_dateFill.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int year = 0, month = 0, day = 0;

            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker = new DatePickerDialog(AttendenceLog_Activity.this, new DatePickerDialog.OnDateSetListener() {
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
                        from_dateFill.setText(date.toUpperCase());
                        dateCurrent2 = from_dateFill.getText().toString();
                        if (NetworkHelpers.isNetworkAvailable(context)) {
                            new AttendenceDateLogTask().execute();
                        }

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    @Override
    public void getSelectedAttendence(AttendenceLog_Model person_log) {
        Intent intent = new Intent(getApplicationContext(), AttendenceLog_Details_Activity.class);

        intent.putExtra("person_id", person_log.getPersongID());
        final String dateChange = from_dateFill.getText().toString();
        intent.putExtra("current_date", dateChange);
        intent.putExtra("personName", person_log.getPersongName());
        intent.putExtra("personDesignation", person_log.getPersongDesignaton());
        intent.putExtra("person_department", person_log.getPersonDepartment());
        startActivity(intent);
    }

    private class AttendenceLogTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("DB", "==================Connect===========");
                if (connection != null) {
                    attendenceLogItems = new ArrayList<AttendenceLog_Model>();

                }

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_EMP_NAME, V_DESIGNATION, V_DEPARTMENT, \n" +
                        "to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
                        "V_LATE_DURATION," +
                        "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
                        "V_EARLY_DURATION," + //8
                        "decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                        "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                        "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG, \n" +
                        "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG \n" +
                        "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                        "where D_DATE = to_date('" + dateCurrent2.toUpperCase() + "','MON DD,RRRR')\n" +
                        "order by V_EMP_NAME asc");

                while (rs.next()) {

                    attendenceLogItems.add(new AttendenceLog_Model(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                            rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                            rs.getString(10), rs.getString(11), rs.getString(12)));


                    Log.d("1", "===V_PERSON_NO===>" + rs.getString(1));
                    Log.d("2", "===V_EMP_NAME===>" + rs.getString(2));
                    Log.d("3", "===V_DESIGNATION===>" + rs.getString(3));
                    Log.d("4", "===V_DEPARTMENT===>" + rs.getString(4));
                    Log.d("5", "===V_LOGINTIME===>" + rs.getString(5));
                    Log.d("6", "==V_LATE_DURATION====>" + rs.getString(6));
                    Log.d("7", "====V_LOGOUTTIME==>" + rs.getString(7));
                    Log.d("8", "====V_EARLY_DURATION==>" + rs.getString(8));
                    Log.d("9", "====V_WEEKEND_FLAG==>" + rs.getString(9));
                    Log.d("10", "====V_HOLIDAY_FLAG==>" + rs.getString(10));
                    Log.d("11", "====V_LATE_LOGIN_FLAG==>" + rs.getString(11));
                    Log.d("12", "====V_EARLY_LOGOUT_FLAG==>" + rs.getString(12));


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
            jAttendenceLogRecyclerView.setLayoutManager(new LinearLayoutManager(AttendenceLog_Activity.this));
            jAttendenceLogRecyclerView.addItemDecoration(new DividerItemDecoration(AttendenceLog_Activity.this, DividerItemDecoration.VERTICAL));
            attendenceLogAdapter = new CustomAttendencLog_Adapter(attendenceLogItems, AttendenceLog_Activity.this);
            jAttendenceLogRecyclerView.setAdapter(attendenceLogAdapter);

            search.setActivated(true);
            search.setQueryHint("Search Here...");
            search.onActionViewExpanded();
            search.setIconified(false);
            search.clearFocus();

        }
    }

    private class AttendenceDateLogTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = Db.createConnection();
                Log.d("connection", "==================Calader Query====Connect===========");

                if (connection != null) {
                    attendenceLogItems = new ArrayList<AttendenceLog_Model>();

                }

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_EMP_NAME, V_DESIGNATION, V_DEPARTMENT, \n" +
                        "to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
                        "V_LATE_DURATION," +
                        "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
                        "V_EARLY_DURATION," +
                        "decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                        "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                        "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG, \n" +
                        "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG \n" +
                        "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                        "where D_DATE = to_date('" + dateCurrent2.toUpperCase() + "','MON DD,RRRR')\n" +
                        "order by V_EMP_NAME asc");

                while (rs.next()) {

                    attendenceLogItems.add(new AttendenceLog_Model(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                            rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                            rs.getString(10), rs.getString(11), rs.getString(12)));

                    Log.d("1", "===V_PERSON_NO===>" + rs.getString(1));
                    Log.d("2", "===V_EMP_NAME===>" + rs.getString(2));
                    Log.d("3", "===V_DESIGNATION===>" + rs.getString(3));
                    Log.d("4", "===V_DEPARTMENT===>" + rs.getString(4));
                    Log.d("5", "===V_LOGINTIME===>" + rs.getString(5));
                    Log.d("6", "==V_LATE_DURATION====>" + rs.getString(6));
                    Log.d("7", "====V_LOGOUTTIME==>" + rs.getString(7));
                    Log.d("8", "====V_EARLY_DURATION==>" + rs.getString(8));
                    Log.d("9", "====V_WEEKEND_FLAG==>" + rs.getString(9));
                    Log.d("10", "====V_HOLIDAY_FLAG==>" + rs.getString(10));
                    Log.d("11", "====V_LATE_LOGIN_FLAG==>" + rs.getString(11));
                    Log.d("12", "====V_EARLY_LOGOUT_FLAG==>" + rs.getString(12));
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
            jAttendenceLogRecyclerView.setLayoutManager(new LinearLayoutManager(AttendenceLog_Activity.this));
            jAttendenceLogRecyclerView.addItemDecoration(new DividerItemDecoration(AttendenceLog_Activity.this, DividerItemDecoration.VERTICAL));
            attendenceLogAdapter = new CustomAttendencLog_Adapter(attendenceLogItems, AttendenceLog_Activity.this);
            jAttendenceLogRecyclerView.setAdapter(attendenceLogAdapter);
        }
    }
}


