package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.adapter.HRM_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HRM_Activity extends AppCompatActivity implements HRM_Adapter.SelectedItems {
    private Connection connection;
    private String userName;
    String database_AttendanceLog, database_Team, database_MyAttendance, database_MyAttendance_EmpInfo, database_BloodBank;
    String access_AttendanceLog, access_Team, access_MyAttendance, access_MyAttendance_EmpInfo, access_BloodBank;
    private ArrayList<HRM_AccessPermission_Entity> itemNameList;
    private ListView listView;
    private RecyclerView recyclerView;
    TextView name, designation, jHrm_textView;
    private BusyDialog busyDialog;
    private Context context;
    private HRM_Adapter adapter;
    //    ImageButton backButton;
    String mTitle[] = {"Attendance Log", "Leave Log", "My Attendance", "My Team's Attendence",
            "My Leave", "Employee Information", "Blood Bank"};

    int[] imageItem = {R.drawable.ic_attendence, R.drawable.ic_leave, R.drawable.ic_team_finger_print,
            R.drawable.ic_my_fingerprint, R.drawable.ic_myleave, R.drawable.ic_employee_info, R.drawable.ic_blood_bank};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hrm_activity);
        recyclerView = findViewById(R.id.hrm_listView);
        name = findViewById(R.id.p_name);
        designation = findViewById(R.id.p_designation);
        jHrm_textView = findViewById(R.id.hrm_text);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BroshkPlum-YzqJL.ttf");
        jHrm_textView.setTypeface(typeface);
        context = HRM_Activity.this;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        adapter = new HRM_Adapter(mTitle, imageItem, (HRM_Adapter.SelectedItems) context);
        recyclerView.setAdapter(adapter);

        if (NetworkHelpers.isNetworkAvailable(context)) {
            new OptionEnableDisable().execute();
            getLoginValueShowHeader2();
        } else {
            Toast.makeText(context, "Please check your internate connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void getLoginValueShowHeader2() {
//        <<<<<<<<<<<<<<<------ (Start getting login value by get Menu Activity To HRM Activity) -------->>>>>>>>>>>
        Bundle b = getIntent().getExtras();
        String person_Name = b.getString("persong_name");
        String persong_desig_dept = b.getString("desig_dept");

        name.setText(person_Name);
        designation.setText(persong_desig_dept);
    }


    @Override
    public void getSelectedItems(int position) {
//        Toast.makeText(context, "Select position: " + position, Toast.LENGTH_SHORT).show();
        if (position == 0) {
            if (database_AttendanceLog.equals("Attendance L.g") && access_AttendanceLog.equals("0")) {
                Toast.makeText(HRM_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(HRM_Activity.this, AttendenceLog_Activity.class);
                startActivity(intent);
            }

        } else if (position == 1) {

            Intent intent = new Intent(getApplicationContext(), Leave_Log_Activity.class);

            Bundle b = getIntent().getExtras();
            String login_value = b.getString("myName");
            intent.putExtra("loginValue", login_value);
            startActivity(intent);
        } else if (position == 2) {
            if (database_MyAttendance.equals("My Attendance") && access_MyAttendance.equals("0")) {
                Toast.makeText(HRM_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(HRM_Activity.this, MyAttencence_Activity.class);
                Bundle b = getIntent().getExtras();
                String bypassUserName = b.getString("myName");
                intent.putExtra("userInputName", bypassUserName);
                startActivity(intent);
            }

        } else if (position == 3) {
            if (database_Team.equals("My Team's Attendance") && access_Team.equals("0")) {
                Toast.makeText(HRM_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(HRM_Activity.this, MyTeamsAttendance_Activity.class);

                Bundle b = getIntent().getExtras();
                String login_value = b.getString("myName");
                intent.putExtra("loginValue", login_value);
                startActivity(intent);
            }

        } else if (position == 4) {

            Intent intent = new Intent(HRM_Activity.this, My_Leave_Activity.class);

            Bundle b = getIntent().getExtras();
            String login_value = b.getString("myName");
            intent.putExtra("loginValue", login_value);
            startActivity(intent);

        } else if (position == 5) {
            if (database_MyAttendance_EmpInfo.equals("Employee Information") && access_MyAttendance_EmpInfo.equals("0")) {
                Toast.makeText(HRM_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(HRM_Activity.this, EmpInfo_Activity.class);
                startActivity(intent);
            }

        } else if (position == 6) {
            if (database_BloodBank.equals("Blood Bank") && access_BloodBank.equals("0")) {
                Toast.makeText(HRM_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(HRM_Activity.this, Blood_Bank_Activity.class);
                startActivity(intent);
            }
        }
    }

    private class OptionEnableDisable extends AsyncTask<Void, Void, Void> {

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
                    Bundle b = getIntent().getExtras();
                    userName = b.getString("myName");
                    Log.d("login_valueeee", "--------------" + userName);
                    itemNameList = new ArrayList<HRM_AccessPermission_Entity>();

                    Statement stmt = connection.createStatement();
                    String query = "select a.N_OBJECT_ID position, a.V_OBJECT_NAME name,\n" +
                            "decode(a.N_ACTIVE_FLAG,0,0,decode(a.N_CAN_ACCESS,0,0,b.N_CAN_ACCESS)) enable_flag\n" +
                            "from SEC_OBJECT_MOBILE a, SEC_USER_OBJECT_PRIV_MOBILE b\n" +
                            "where a.N_OBJECT_ID=b.N_OBJECT_ID\n" +
                            "and b.N_USER_ID = (select N_USER_ID from sec_user where V_USER_NAME='" + userName.toUpperCase() + "')";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        itemNameList.add(new HRM_AccessPermission_Entity(rs.getString(1), rs.getString(2), rs.getString(3)));
                    }
                    database_AttendanceLog = itemNameList.get(0).getTitle_name();
                    database_Team = itemNameList.get(1).getTitle_name();
                    database_MyAttendance = itemNameList.get(2).getTitle_name();
                    database_MyAttendance_EmpInfo = itemNameList.get(3).getTitle_name();
                    database_BloodBank = itemNameList.get(4).getTitle_name();
                    access_AttendanceLog = itemNameList.get(0).getAccess();
                    access_Team = itemNameList.get(1).getAccess();
                    access_MyAttendance = itemNameList.get(2).getAccess();
                    access_MyAttendance_EmpInfo = itemNameList.get(3).getAccess();
                    access_BloodBank = itemNameList.get(4).getAccess();

                    busyDialog.dismis();

                }

                busyDialog.dismis();
                connection.close();

            } catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }

            busyDialog.dismis();
            return null;
        }
    }

}
