package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

    private String userInput;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attencence);
        jmy_Attendence_listView =findViewById(R.id.my_Attendence_listView);

        textViewDateFrom = findViewById(R.id.From_date1);
        textViewDateTo = findViewById(R.id.To_date2);
        searchDate =findViewById(R.id.search_btn);
        jMyAttendenceToolBarId = findViewById(R.id.myAttendenceToolBarId);

        this.setSupportActionBar(jMyAttendenceToolBarId);


                    //<<<<<<<<<<<<<<<<<..........value pass Login to this Activity................>>>>>>>>>>>>>>>>>
                            Bundle b = getIntent().getExtras();
                             userInput = b.getString("userInputName");

                            Log.d("Your_Login_value","====================="+userInput.toUpperCase());

                   //<<<<<<<<<<<<<<<<<..........End value pass Login to this Activity................>>>>>>>>>>>>>>>>>

        //<<<<<<<<<<<--------------start Two Filled default set Textview in Current Date ------------->>>>>>>>>>>>>>>>>>>>>>
//        String dateCurrent = new SimpleDateFormat("MMM dd,yyyy",Locale.getDefault()).format(new Date());
//        textViewDateFrom.setText(dateCurrent.toUpperCase());
//        Log.d("CurrentDate","======FROM======="+dateCurrent);

        String dateCurrent2 = new SimpleDateFormat("MMM dd,yyyy",Locale.getDefault()).format(new Date());
        textViewDateTo.setText(dateCurrent2.toUpperCase());
        Log.d("CurrentDate","======TO======="+dateCurrent2);
        //<<<<<<<<<<<--------------End Two Filled default set Textview in Current Date ------------->>>>>>>>>>>>>>>>>>>>>>
        //<<<<<<<<<<<<................. Strt For current month first Date...........>>>>>>>>>>>>>>>>>>>

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date chosenDate = c.getTime();
        DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String crntManthFstday = df_medium_us.format(chosenDate);
        textViewDateFrom.setText(crntManthFstday.toUpperCase());
        Log.d("currentMonthDay","=======From===MonthFirstDay======="+crntManthFstday);


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

//                String query = "select to_char(D_DATE,'MON DD,RRRR'),\n" +
//                        "to_char(D_LOGINTIME,'HH12:MI AM') V_LOGINTIME, \n" +
//                        "V_LATE_LOGIN_REASON,\n" +
//                        "to_char(D_LOGOUTTIME,'HH12:MI AM') V_LOGOUTTIME,\n" +
//                        " V_EARLY_LOGOUT_REASON, V_ABSENT_REASON\n" +
//                        "from VW_HR_EMP_ATTENDANCE_SUMMARY\n" +
//                        "where N_PERSON_ID = (select N_PERSON_ID from sec_user where V_USER_NAME='"+userInput.toUpperCase()+"')\n" +
//                        "order by D_DATE";
                ResultSet rs=stmt.executeQuery(query);

            while(rs.next()) {
                myAttendenceItems.add(new MyAttendence_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)));

                Log.d("weekend","====weeek==="+rs.getString(7));
                Log.d("holiday","====holi==="+rs.getString(8));
                Log.d("log","====log flag==="+rs.getString(9));
                Log.d("logout","====logout flag==="+rs.getString(10));

            }
            }

            jmy_Attendence_listView.setLayoutManager(new LinearLayoutManager(MyAttencence_Activity.this));
            jmy_Attendence_listView.addItemDecoration(new DividerItemDecoration(MyAttencence_Activity.this,DividerItemDecoration.VERTICAL));
            adapter =new CustomMyAttendenceAdapter(myAttendenceItems, (CustomMyAttendenceAdapter.SelectedMyAttendence) MyAttencence_Activity.this);
            jmy_Attendence_listView.setAdapter(adapter);
            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(MyAttencence_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        searchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    }

                    jmy_Attendence_listView.setLayoutManager(new LinearLayoutManager(MyAttencence_Activity.this));
                    jmy_Attendence_listView.addItemDecoration(new DividerItemDecoration(MyAttencence_Activity.this,DividerItemDecoration.VERTICAL));
                    adapter =new CustomMyAttendenceAdapter(myAttendenceItems, (CustomMyAttendenceAdapter.SelectedMyAttendence) MyAttencence_Activity.this);
                    jmy_Attendence_listView.setAdapter(adapter);

                    connection.close();

                }


                catch (Exception e) {

                    Toast.makeText(MyAttencence_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
        dateSetFROM();
        dateSetTO();


    }



//    public void dateSet(){
//        textViewDateFrom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar =Calendar.getInstance();
//                int year =calendar.get(Calendar.YEAR);
//                int month =calendar.get(Calendar.MONTH);
//                int day =calendar.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog dialog =new DatePickerDialog(MyAttencence_Activity.this,R.style.Widget_AppCompat_Light_ActionBar_Solid
//                        ,mDateSetListener,year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
//                dialog.show();
//            }
//        });
//        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(0);
//                cal.set(year, month, dayOfMonth, 0, 0, 0);
//                Date chosenDate = cal.getTime();
//
//                // Format the date using style medium and US locale
//                DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
//                String date = df_medium_us.format(chosenDate);
//                textViewDateFrom.setText(date.toUpperCase());
//
//
//            }
//        };
//    }

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

//    public void dateSet2(){
//        textViewDateTo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar =Calendar.getInstance();
//
//                int year =calendar.get(Calendar.YEAR);
//                int month =calendar.get(Calendar.MONTH);
//                int day =calendar.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog dialog =new DatePickerDialog(MyAttencence_Activity.this,R.style.Widget_AppCompat_Light_ActionBar_Solid
//                        ,mDateSetListener2,year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
//                dialog.show();
//            }
//        });
//        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(0);
//                cal.set(year, month, dayOfMonth, 0, 0, 0);
//                Date chosenDate = cal.getTime();
//
//                // Format the date using style medium and US locale
//                DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
//                String date = df_medium_us.format(chosenDate);
//                textViewDateTo.setText(date.toUpperCase());
//                Log.d("toDate","=======chosen======"+date);
//
//
//            }
//        };
//    }
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
}


