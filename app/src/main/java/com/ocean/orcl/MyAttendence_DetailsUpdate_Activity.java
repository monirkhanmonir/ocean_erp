package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Statement;

public class MyAttendence_DetailsUpdate_Activity extends AppCompatActivity {
    private TextView date,weekend,holiday,loginTime,dueLoginTime,logoutTime,dueLogOutTime,dept,absenceFlag,reason_text_absence;
    private EditText lateLogin_reason,earlyLogout_reason,absent_reason;
    String dates,userInput;
    private Button update_btn;
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendence__details_update);
        reason_text_absence =findViewById(R.id.updateReasonAbcence);
        date =findViewById(R.id.myAttendance_updateDate);
        lateLogin_reason =findViewById(R.id.updateLateLoginReason);
        earlyLogout_reason =findViewById(R.id.updateEarlyLogOutReason);
        absent_reason =findViewById(R.id.updateAbsentReason);
        weekend =findViewById(R.id.update_weekend);
        holiday = findViewById(R.id.update_holiday);
        loginTime =findViewById(R.id.updateInTime);
        dueLoginTime =findViewById(R.id.updateLoginDueTime);
        logoutTime =findViewById(R.id.updateOutTime);
        dueLogOutTime =findViewById(R.id.updateLogOutDueTime);
        dept =findViewById(R.id.updateDept);
        absenceFlag =findViewById(R.id.update_absence);
        update_btn =findViewById(R.id.update_Btn);

        final Intent intent = getIntent();

        userInput = intent.getStringExtra("userInput");
        dates = intent.getStringExtra("date");
        String weekends = intent.getStringExtra("weekend");
        String holidays = intent.getStringExtra("holiday");
        String login_flag = intent.getStringExtra("late_login_flag");
        String logOut_flag = intent.getStringExtra("early_logout_flag");
        String absence_flag = intent.getStringExtra("absence");
        Log.d("absence_flag","======absence_flag====="+absence_flag);

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


        if(weekends.equals("N") && holidays.equals("N") && login_flag.equals("Y")){
            loginTime.setTextColor(Color.RED);
        }else {
            loginTime.setTextColor(Color.GREEN);
        }if(weekends.equals("N") && holidays.equals("N") && logOut_flag.equals("Y")){
            logoutTime.setTextColor(Color.RED);
        }else {
            logoutTime.setTextColor(Color.GREEN);
        }if(absence_flag.equals("0")){
            absenceFlag.setText("NO");
        }else {
            absenceFlag.setText("Yes");
        }if (absence_flag.equals("0")) {
            absent_reason.setVisibility(View.GONE);
            reason_text_absence.setVisibility(View.GONE);
        } else {
            absent_reason.setVisibility(View.VISIBLE);
            reason_text_absence.setVisibility(View.VISIBLE);
        }
update_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        alertDialog();

    }
});

    }
    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
//        dialog.setMessage("Late Login Reason: "+lateLogin_reason.getText());
//        dialog.setMessage("Early LogOut Reason: "+earlyLogout_reason.getText());
//        dialog.setMessage("Absence Reason: "+absent_reason.getText());
        String alert1 = "Late Login Reason: " + lateLogin_reason.getText();
        String alert2 = "Early LogOut Reason: " + earlyLogout_reason.getText();
        String alert3 = "Absence Reason: " + absent_reason.getText();
//        dialog.setMessage(alert1 +"\n"+ alert2 +"\n"+ alert3);

        TextView textView = new TextView(this);
        textView.setText("Are you sure to commit changes?");
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(Color.LTGRAY);
        textView.setTextColor(Color.BLUE);
        dialog.setCustomTitle(textView);


//        dialog.setTitle("are you want to update data?");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
//                        Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                        try {
                            connection = com.ocean.orcl.ODBC.Db.createConnection();
//            Log.d("connection", "==========MyAttencence==Details===Update===========Connect===========");
//            Log.d("loginValue","==========update======="+userInput);
//            Log.d("earlyLogout_reason","==========update======="+earlyLogout_reason.getText());
//            Log.d("lateLogin_reason","==========update======="+lateLogin_reason.getText());
//            Log.d("absent_reason","==========update======="+absent_reason.getText());
//            Log.d("date","==========update======="+dates.toUpperCase());
                            if (connection != null) {
//                myAttendenceDetiles = new ArrayList<AttendenceLog_Details_A_Entity>();

                            }
                            Statement stmt = connection.createStatement();
                            String sql = "update hrm_attendance_mst\n" +
                                    "set V_LATE_LOGIN_REASON = '"+lateLogin_reason.getText()+"',\n" +
                                    "V_EARLY_LOGOUT_REASON='"+earlyLogout_reason.getText()+"',\n" +
                                    "V_ABSENT_REASON='"+absent_reason.getText()+"'\n"+
                                    "where V_PERSON_NO = (select V_PERSON_NO from sec_user u, bas_person p where u.N_PERSON_ID=p.N_PERSON_ID and V_USER_NAME='"+userInput.toUpperCase()+"') \n" +
                                    "and D_DATE=to_date('"+dates.toUpperCase() + "','MON DD,RRRR')";

                            stmt.executeUpdate(sql);
                            connection.commit();
                            Toast.makeText(getApplicationContext(),"Data Update success",Toast.LENGTH_SHORT).show();

                            connection.close();
                        } catch (Exception e) {

                            Toast.makeText(MyAttendence_DetailsUpdate_Activity.this, "" + e,
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(),MyAttendence_Details_Activity.class);
                        intent.putExtra("login_value",userInput);
                        intent.putExtra("date",dates);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"cancel",Toast.LENGTH_LONG).show();

            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
        Button buttonOK = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button buttonCancle = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonCancle.setTextColor(ContextCompat.getColor(this, R.color.red));
        buttonOK.setTextColor(ContextCompat.getColor(this, R.color.green));
    }
}
