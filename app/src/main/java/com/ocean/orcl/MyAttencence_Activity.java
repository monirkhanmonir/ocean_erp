package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ocean.orcl.adapter.CustomMyAttendenceAdapter;
import com.ocean.orcl.util.BusyDialog;
import com.ocean.orcl.util.Helper;
import com.ocean.orcl.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAttencence_Activity extends AppCompatActivity implements CustomMyAttendenceAdapter.SelectedMyAttendence {

    private Connection connection;
    private RecyclerView jmy_Attendence_listView;
    private CustomMyAttendenceAdapter adapter;
    private TextView textViewDateFrom,textViewDateTo;
    private ImageButton searchDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener2;

    private ArrayList<MyAttendence_Entity> myAttendenceItems;
    private Toolbar jMyAttendenceToolBarId;

    private String userInput, crntManthFstday, dateCurrent2;
    private Context context;
    private BusyDialog busyDialog;
    private Handler handler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attencence);
        jmy_Attendence_listView =findViewById(R.id.my_Attendence_listView);

        textViewDateFrom = findViewById(R.id.From_date1);
        textViewDateTo = findViewById(R.id.To_date2);
        searchDate =findViewById(R.id.search_btn);
        jMyAttendenceToolBarId = findViewById(R.id.myAttendenceToolBarId);
        handler = new Handler();
        context = MyAttencence_Activity.this;


        this.setSupportActionBar(jMyAttendenceToolBarId);

                    //<<<<<<<<<<<<<<<<<..........value pass Login to this Activity................>>>>>>>>>>>>>>>>>
                            Bundle b = getIntent().getExtras();
                             userInput = b.getString("userInputName");

                            Log.d("Your_Login_value","====================="+userInput.toUpperCase());


         dateCurrent2 = new SimpleDateFormat("MMM dd,yyyy",Locale.getDefault()).format(new Date());
        textViewDateTo.setText(dateCurrent2.toUpperCase());
        Log.d("CurrentDate","======TO======="+dateCurrent2);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date chosenDate = c.getTime();
        DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
         crntManthFstday = df_medium_us.format(chosenDate);
        textViewDateFrom.setText(crntManthFstday.toUpperCase());
        Log.d("currentMonthDay","=======From===MonthFirstDay======="+crntManthFstday);




        if(NetworkHelpers.isNetworkAvailable(context)){
            new MyAttendenceTask().execute();
        }else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }


        searchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(NetworkHelpers.isNetworkAvailable(context)){
                    new MyAttendenceDateTask().execute();
                }else {
                    Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dateSetFROM();
        dateSetTO();


    }

    public void dateSetFROM(){

        textViewDateFrom.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(MyAttencence_Activity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {
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
                        textViewDateFrom.setText(date.toUpperCase());

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }


    public void dateSetTO(){

        textViewDateTo.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(MyAttencence_Activity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {

                        year = selectedyear;
                        month = selectedmonth;
                        day = selectedday;

                        cal.setTimeInMillis(0);
                        cal.set(year, month, day, 0, 0, 0);
                        Date chosenDate = cal.getTime();

                        // Format the date using style medium and US locale
                        DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
                        String date = df_medium_us.format(chosenDate);
                        textViewDateTo.setText(date.toUpperCase());

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    @Override
    public void getSelectedMyAttendence(MyAttendence_Entity attendenceModel) {
        //Toast.makeText(this, "Date: "+attendenceModel.getDATEs(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MyAttendence_Details_Activity.class);
        intent.putExtra("login_value",userInput);
        intent.putExtra("date",attendenceModel.getDATEs());
        intent.putExtra("absent_reason",attendenceModel.getABSENT_REASON());
        intent.putExtra("late_login_rason",attendenceModel.getLATE_LOGIN_REASON());
        intent.putExtra("early_logout_rason",attendenceModel.getEARLY_LOGOUT_REASON());

        startActivity(intent);

    }


    private class MyAttendenceTask extends AsyncTask<Void,Void,Void>{

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
                connection = com.ocean.orcl.ODBC.Db.createConnection();
                Log.d("connection","================MyAttendence===DB==Connected===========");
                if(connection != null){
                    myAttendenceItems = new ArrayList<MyAttendence_Entity>();


                    Statement stmt=connection.createStatement();

                    String query = "select to_char(D_DATE,'MON DD,RRRR'), to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
                            "V_LATE_LOGIN_REASON,\n" +
                            "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
                            "V_EARLY_LOGOUT_REASON, V_ABSENT_REASON,\n" +
                            "decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                            "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                            "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG, \n" +
                            "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG \n" +
                            "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                            "where D_DATE between to_date('"+crntManthFstday.toUpperCase()+"','MON DD,RRRR') and to_date('"+dateCurrent2.toUpperCase()+"','MON DD,RRRR')\n" +
                            "and  N_PERSON_ID = (select N_PERSON_ID from sec_user where V_USER_NAME='"+userInput.toUpperCase()+"')\n" +
                            "order by D_DATE";

                    ResultSet rs=stmt.executeQuery(query);

                    while(rs.next()) {
                        myAttendenceItems.add(new MyAttendence_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)));

                        Log.d("weekend","====weeek==="+rs.getString(7));
                        Log.d("holiday","====holi==="+rs.getString(8));
                        Log.d("log","====log flag==="+rs.getString(9));
                        Log.d("logout","====logout flag==="+rs.getString(10));

                    }
                    busyDialog.dismis();
                }
                    busyDialog.dismis();

                connection.close();

            }
            catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            busyDialog.dismis();
            jmy_Attendence_listView.setLayoutManager(new LinearLayoutManager(MyAttencence_Activity.this));
            jmy_Attendence_listView.addItemDecoration(new DividerItemDecoration(MyAttencence_Activity.this,DividerItemDecoration.VERTICAL));
            adapter =new CustomMyAttendenceAdapter(myAttendenceItems, (CustomMyAttendenceAdapter.SelectedMyAttendence) MyAttencence_Activity.this);
            jmy_Attendence_listView.setAdapter(adapter);
        }
    }

    private class MyAttendenceDateTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                connection = com.ocean.orcl.ODBC.Db.createConnection();
                if(connection != null){

                    myAttendenceItems = new ArrayList<MyAttendence_Entity>();
                    String fromDate = textViewDateFrom.getText().toString();
                    String toDate = textViewDateTo.getText().toString();
                    Log.d("fromdate","=========="+fromDate);
                    Log.d("tomdate","=========="+toDate);


                    Statement stmt=connection.createStatement();
                    String query = "select to_char(D_DATE,'MON DD,RRRR'), to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
                            "V_LATE_LOGIN_REASON,\n" +
                            "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
                            "V_EARLY_LOGOUT_REASON, V_ABSENT_REASON,\n" +
                            "decode(N_WEEKEND_FLAG,0,'N','Y') V_WEEKEND_FLAG,\n" +
                            "decode(N_HOLIDAY_FLAG,0,'N','Y') V_HOLIDAY_FLAG, \n" +
                            "decode(N_LATE_LOGIN_FLAG,0,'N','Y') V_LATE_LOGIN_FLAG, \n" +
                            "decode(N_EARLY_LOGOUT_FLAG,0,'N','Y') V_EARLY_LOGOUT_FLAG \n" +
                            "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
                            "where D_DATE between to_date('"+fromDate.toUpperCase()+"','MON DD,RRRR') and to_date('"+toDate.toUpperCase()+"','MON DD,RRRR')\n" +
                            "and  N_PERSON_ID = (select N_PERSON_ID from sec_user where V_USER_NAME='"+userInput.toUpperCase()+"')\n" +
                            "order by D_DATE";

                    ResultSet rs=stmt.executeQuery(query);

                    while(rs.next()) {

                        myAttendenceItems.add(new MyAttendence_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)));
                    }
                    busyDialog.dismis();
                }

                busyDialog.dismis();
                connection.close();

            }

            catch (Exception e) {
                busyDialog.dismis();
                 e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            busyDialog.dismis();
            jmy_Attendence_listView.setLayoutManager(new LinearLayoutManager(context));
            jmy_Attendence_listView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
            adapter =new CustomMyAttendenceAdapter(myAttendenceItems, MyAttencence_Activity.this);
            jmy_Attendence_listView.setAdapter(adapter);
        }
    }

}


