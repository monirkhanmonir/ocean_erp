package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ocean.orcl.ODBC.Db;

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

public class MyTeamsAttendence_Activity extends AppCompatActivity {
//public class MyTeamsAttendence_Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Connection connection;
    private CustomAdapter_MyTeamsAttendence adapter;
    private List<MyTeams_Entity> teamsList;
    private TextView fromDate,toDate;
    private RecyclerView recyclerView;

    private Toolbar jMyTeamAttendenceToolBarId;

    SearchView search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams_attendence);
        toDate = findViewById(R.id.toDate_teams);
        search = findViewById(R.id.searchTeams);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
         recyclerView = findViewById(R.id.recycler_view);


         jMyTeamAttendenceToolBarId = findViewById(R.id.myTeamAttendenceToolBarId);
         this.setSupportActionBar(jMyTeamAttendenceToolBarId);


        //<<<<<<<<<<<<<<<<<..........value pass Login to this Activity................>>>>>>>>>>>>>>>>>
        Bundle b = getIntent().getExtras();
        String userInput = b.getString("loginValue");
        Log.d("Your_Login_value","=====teams======login value=========="+userInput.toUpperCase());
        //<<<<<<<<<<<<<<<<<..........End value pass Login to this Activity................>>>>>>>>>>>>>>>>>

        String dateCurrent2 = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        toDate.setText(dateCurrent2.toUpperCase());
        String toDates = dateCurrent2.toUpperCase();

        Log.d("CurrentDate","===Treams===TO======="+toDates);
        //<<<<<<<<<<<--------------End Two Filled default set Textview in Current Date ------------->>>>>>>>>>>>>>>>>>>>>>
        //<<<<<<<<<<<<................. Strt For current month first Date...........>>>>>>>>>>>>>>>>>>>

//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.DAY_OF_MONTH, 1);
//        Date chosenDate = c.getTime();
//        DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
//        String crntManthFstday = df_medium_us.format(chosenDate);
//        fromDate.setText(crntManthFstday.toUpperCase());
//        String fromdates = fromDate.getText().toString();
//        Log.d("currentMonthDay","===Treams====From===MonthFirstDay======="+fromdates);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();


            Log.d("connection", "============TeamsDB=========Connect===========");
            if (connection != null) {

                teamsList = new ArrayList<MyTeams_Entity>();
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
                    "where D_DATE between to_date('"+toDates+"','MON DD,RRRR') and to_date('"+toDates+"','MON DD,RRRR')\n" +
                    "and N_PERSON_ID in (select p.N_PERSON_ID from sec_user u, bas_person p where u.N_PERSON_ID=p.N_REPORTING_TO and u.V_USER_NAME='"+userInput.toUpperCase()+"')\n" +
                    "order by D_DATE");

            while (rs.next()) {

//                myTimesItems.add(new MyTeamsEntity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
                teamsList.add(new MyTeams_Entity(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13)));
//                Log.d("res","======"+rs.getString(10)+"\n"+"========11=="+rs.getString(11));

            }

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            adapter = new CustomAdapter_MyTeamsAttendence(this, teamsList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
//            recyclerView.setIsRecyclable(false);
//
            search.setActivated(true);
            search.setQueryHint("Search name.");
            search.onActionViewExpanded();
            search.setIconified(false);
            search.clearFocus();


            connection.close();

        } catch (Exception e) {

            Toast.makeText(MyTeamsAttendence_Activity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
        List<MyTeams_Entity> newList = new ArrayList<>();
        for(MyTeams_Entity lists : teamsList){
            if(lists.getEmp_name().toLowerCase().contains(userInput)){
                newList.add((MyTeams_Entity)lists);

            }
        }
        adapter.filterList((ArrayList<MyTeams_Entity>) newList);


                return false;
            }
        });
        dateSet();

    }


    public void dateSet(){

        toDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(MyTeamsAttendence_Activity.this, new DatePickerDialog.OnDateSetListener()
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
                        toDate.setText(date.toUpperCase());

                        try {
                            connection = Db.createConnection();

                            final String toSelectDate = toDate.getText().toString();
//                            Log.d("fromdate","=====picker Formate====="+toSelectDate);

                            //<<<<<<<<<<<<<<<<<..........value pass Login to this Activity................>>>>>>>>>>>>>>>>>
                            Bundle b = getIntent().getExtras();
                            String userInput = b.getString("loginValue");
                            Log.d("Your_Login_value","=====teams======login value=========="+userInput.toUpperCase());
                            //<<<<<<<<<<<<<<<<<..........End value pass Login to this Activity................>>>>>>>>>>>>>>>>>

                            if (connection != null) {
                                teamsList = new ArrayList<MyTeams_Entity>();

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
                                    "where D_DATE between to_date('"+toSelectDate+"','MON DD,RRRR') and to_date('"+toSelectDate+"','MON DD,RRRR')\n" +
                                    "and N_PERSON_ID in (select p.N_PERSON_ID from sec_user u, bas_person p where u.N_PERSON_ID=p.N_REPORTING_TO and u.V_USER_NAME='"+userInput.toUpperCase()+"')\n" +
                                    "order by D_DATE");

                            while (rs.next()) {
                                teamsList.add(new MyTeams_Entity(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13)));

                            }
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyTeamsAttendence_Activity.this);
                            adapter = new CustomAdapter_MyTeamsAttendence(MyTeamsAttendence_Activity.this, teamsList);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);

                            connection.close();


                        }catch (Exception e) {

                            Toast.makeText(MyTeamsAttendence_Activity.this, "" + e,
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }


}



