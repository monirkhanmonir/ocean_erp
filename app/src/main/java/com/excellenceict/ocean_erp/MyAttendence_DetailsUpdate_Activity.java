package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.Statement;

public class MyAttendence_DetailsUpdate_Activity extends AppCompatActivity {
    private TextView date, weekend, holiday, loginTime, dueLoginTime, logoutTime, dueLogOutTime, dept, absenceFlag, reason_text_absence;


    private TextInputEditText lateLogin_reason, earlyLogout_reason, absent_reason;
    private LinearLayout j_absentReasonUpdateLayout;

    String dates, userInput, st_lateLogin_reason, st_earlyLogout_reason, st_absent_reason;
    private Button update_btn;
    private Connection connection;
    private Context context;
    private Handler handler;
    private BusyDialog busyDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_attendence_details_update_activity);
        date = findViewById(R.id.myAttendance_updateDate);
        lateLogin_reason = findViewById(R.id.updateLateLoginReason);
        earlyLogout_reason = findViewById(R.id.updateEarlyLogOutReason);
        absent_reason = findViewById(R.id.updateAbsentReason);
        weekend = findViewById(R.id.update_weekend);
        holiday = findViewById(R.id.update_holiday);
        loginTime = findViewById(R.id.updateInTime);
        dueLoginTime = findViewById(R.id.updateLoginDueTime);
        logoutTime = findViewById(R.id.updateOutTime);
        dueLogOutTime = findViewById(R.id.updateLogOutDueTime);
        dept = findViewById(R.id.updateDept);
        toolbar = findViewById(R.id.myTeamAttendenceReasonUpdateToolBarId);


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


        context = MyAttendence_DetailsUpdate_Activity.this;
        handler = new Handler();

        update_btn = findViewById(R.id.update_Btn);
        j_absentReasonUpdateLayout = findViewById(R.id.absentReasonUpdateLayout);

        final Intent intent = getIntent();

        userInput = intent.getStringExtra("userInput");
        dates = intent.getStringExtra("date");
        String weekends = intent.getStringExtra("weekend");
        String holidays = intent.getStringExtra("holiday");
        String login_flag = intent.getStringExtra("late_login_flag");
        String logOut_flag = intent.getStringExtra("early_logout_flag");
        String absence_flag = intent.getStringExtra("absence");
        Log.d("absence_flag", "======absence_flag=====" + absence_flag);

        String login_time = intent.getStringExtra("login_time");
        String loginDept = intent.getStringExtra("dept");
        String due_login_time = intent.getStringExtra("due_loginTime");
        String logout_time = intent.getStringExtra("logout_time");
        String due_logout_time = intent.getStringExtra("due_logout_time");
//        Log.d("value","==========put=========login time="+login_time+" due login="+due_login_time+" logout time="+logout_time+" due logout="+due_logout_time);

        String lateLoginReason = intent.getStringExtra("late_loginReason");
        String ealryLogoutReason = intent.getStringExtra("early_logoutreaason");
        String absentReason = intent.getStringExtra("absent_reason");
//        Log.d("test_value","=======put value====="+dates +" weekend ="+weekend+" login flag="+login_flag+" logout flag="+logOut_flag);

        date.setText(dates);
        weekend.setText(weekends);
        holiday.setText(holidays);
        loginTime.setText(login_time);
        dueLoginTime.setText(due_login_time);
        logoutTime.setText(logout_time);
        logoutTime.setText(logout_time);
        dueLogOutTime.setText(due_logout_time);
        lateLogin_reason.setText(lateLoginReason);
        earlyLogout_reason.setText(ealryLogoutReason);
        absent_reason.setText(absentReason);
        dept.setText(loginDept);


        if (weekends.equals("N") && holidays.equals("N") && login_flag.equals("Y")) {
            loginTime.setTextColor(Color.RED);
        } else {
            loginTime.setTextColor(Color.parseColor("#4D850D"));
        }
        if (weekends.equals("N") && holidays.equals("N") && logOut_flag.equals("Y")) {
            logoutTime.setTextColor(Color.RED);
        } else {
            logoutTime.setTextColor(Color.parseColor("#4D850D"));
        }

        if (absence_flag.equals("0")) {
            j_absentReasonUpdateLayout.setVisibility(View.GONE);

        } else {
            j_absentReasonUpdateLayout.setVisibility(View.VISIBLE);
        }
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_lateLogin_reason = "Late Login Reason: " + lateLogin_reason.getText();
                st_earlyLogout_reason = "Early LogOut Reason: " + earlyLogout_reason.getText();
                st_absent_reason = "Absence Reason: " + absent_reason.getText();

                alertDialog();

            }
        });

    }

    private void alertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        TextView textView = new TextView(this);
        textView.setText("Are you sure to commit changes?");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(Color.parseColor("#196A98"));
        textView.setTextColor(Color.WHITE);
        dialog.setCustomTitle(textView);


        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        if (NetworkHelpers.isNetworkAvailable(context)) {
                            new MyAttendenceDetailsUpdatetask().execute();
                        }
                    }
                });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();

            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        Button buttonOK = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button buttonCancle = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonCancle.setTextColor(ContextCompat.getColor(this, R.color.red));
        buttonOK.setTextColor(ContextCompat.getColor(this, R.color.green));
    }


    private class MyAttendenceDetailsUpdatetask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                if (connection != null) {
                }

                Statement stmt = connection.createStatement();
                String sql = "update hrm_attendance_mst\n" +
                        "set V_LATE_LOGIN_REASON = '" + lateLogin_reason.getText() + "',\n" +
                        "V_EARLY_LOGOUT_REASON='" + earlyLogout_reason.getText() + "',\n" +
                        "V_ABSENT_REASON='" + absent_reason.getText() + "'\n" +
                        "where N_EMP_ID = (select N_PERSON_ID from sec_user u where V_USER_NAME='" + userInput.toUpperCase() + "') \n" +
                        "and D_DATE=to_date('" + dates.toUpperCase() + "','MON DD,RRRR')";

                stmt.executeUpdate(sql);
                connection.commit();
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
            //finish only previous activity
            startActivity(getIntent());
            Intent intent = new Intent(getApplicationContext(), MyAttendence_Details_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("login_value", userInput);
            intent.putExtra("date", dates);
            startActivity(intent);
        }
    }


}
