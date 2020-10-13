package com.ocean.orcl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HRMActivity extends AppCompatActivity {
    private Connection connection;
    private String userName;
    String database_AttendanceLog,database_Team,database_MyAttendance,database_MyAttendance_EmpInfo,database_BloodBank;
    String access_AttendanceLog,access_Team,access_MyAttendance,access_MyAttendance_EmpInfo,access_BloodBank;
    private ArrayList<HRM_AccessPermission_Entity> itemNameList;
    ListView listView;
    TextView name,designation, jHrm_textView;
//    ImageButton backButton;
    String mTitle[] = {"Attendance Log","My Team's Attendence" ,"My Attendance","Employee Information","Blood Bank"};

    int [] imageItem = {R.drawable.ic_attendence, R.drawable.ic_team_finger_print, R.drawable.ic_my_fingerprint, R.drawable.ic_employee_info,R.drawable.ic_blood_bank};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrm);
//        backButton = findViewById(R.id.back_btn);
        listView = findViewById(R.id.hrm_listView);
        name =findViewById(R.id.p_name);
        designation =findViewById(R.id.p_designation);
        jHrm_textView = findViewById(R.id.hrm_textView);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BroshkPlum-YzqJL.ttf");
        jHrm_textView.setTypeface(typeface);


        MyAdapter adapter = new MyAdapter(this,mTitle,imageItem);
//        MyAdapter adapter = new MyAdapter(this,mTitle, imageItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    if(database_AttendanceLog.equals("Attendance Log") && access_AttendanceLog.equals("0")){
                        Toast.makeText(HRMActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(HRMActivity.this,AttendenceLog_Activity.class);
                        startActivity(intent);
                    }

               }
              else if(position == 1){
                  if( database_Team.equals("My Team's Attendance") && access_Team.equals("0")){
                      Toast.makeText(HRMActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                  }else {
                      Intent intent = new Intent(HRMActivity.this,MyTeamsAttendence_Activity.class);

                      Bundle b = getIntent().getExtras();
                      String login_value = b.getString("myName");
                      intent.putExtra("loginValue", login_value);
                      startActivity(intent);
                  }

                }
              else if (position == 2 ){
                  if( database_MyAttendance.equals("My Attendance") && access_MyAttendance.equals("0")){
                      Toast.makeText(HRMActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();

                  }else {
                      Intent intent = new Intent(HRMActivity.this, MyAttencence_Activity.class);
                      Bundle b = getIntent().getExtras();
                      String bypassUserName = b.getString("myName");
                      intent.putExtra("userInputName", bypassUserName);
                      startActivity(intent);
                  }

                }
              else if(position == 3){
                   if( database_MyAttendance_EmpInfo.equals("Employee Information") && access_MyAttendance_EmpInfo.equals("0")){
                       Toast.makeText(HRMActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();

                   }else {
                       Intent intent = new Intent(HRMActivity.this,Emp_info_Activity.class);
                       startActivity(intent);
                   }

                }
              else if(position == 4 ){
                  if( database_BloodBank.equals("Blood Bank") && access_BloodBank.equals("0")){
                      Toast.makeText(HRMActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                  }else {
                      Intent intent = new Intent(HRMActivity.this,Blood_Bank_Activity.class);
                      startActivity(intent);
                  }

              }

            }
        });

        getLoginValueShowHeader2();
        OptionEnableDisable();

    }


    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String rtitle[];
        int rImg[];


         MyAdapter( Context context,String title[] ,int img[]) {
//           MyAdapter( Context context,String title[], int rImg[] ) {
            super(context, R.layout.hrmlistview,R.id.hrm_textView,title);
            this.context =context;
            this.rtitle =title;
            this.rImg =img;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.hrmlistview,parent,false);
            ImageView images = row.findViewById(R.id.hrmImg_icon);
            TextView mytitle =row.findViewById(R.id.hrm_textView);

            images.setImageResource(rImg[position]);
            mytitle.setText(rtitle[position]);


            return row;
        }
    }

    private void getLoginValueShowHeader2(){
//        <<<<<<<<<<<<<<<------ (Start getting login value by get Menu Activity To HRM Activity) -------->>>>>>>>>>>
        Bundle b = getIntent().getExtras();
        String person_Name = b.getString("persong_name");
        String persong_desig_dept = b.getString("desig_dept");

        name.setText(person_Name);
        designation.setText(persong_desig_dept);
//        <<<<<<<<<<<<<<<------ End getting login value -------->>>>>>>>>>>

    }

    private void OptionEnableDisable(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();

            if(connection != null){
                Bundle b = getIntent().getExtras();
                 userName = b.getString("myName");
                Log.d("login_valueeee","--------------"+userName);
                itemNameList = new ArrayList<HRM_AccessPermission_Entity>();

                Statement stmt=connection.createStatement();
                String query = "select a.N_OBJECT_ID position, a.V_OBJECT_NAME name,\n" +
                        "decode(a.N_ACTIVE_FLAG,0,0,decode(a.N_CAN_ACCESS,0,0,b.N_CAN_ACCESS)) enable_flag\n" +
                        "from SEC_OBJECT_MOBILE a, SEC_USER_OBJECT_PRIV_MOBILE b\n" +
                        "where a.N_OBJECT_ID=b.N_OBJECT_ID\n" +
                        "and b.N_USER_ID = (select N_USER_ID from sec_user where V_USER_NAME='"+userName.toUpperCase()+"')";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    itemNameList.add(new HRM_AccessPermission_Entity(rs.getString(1),rs.getString(2),rs.getString(3)));

                    Log.d("value1","======res====1==========="+rs.getString(1));
                    Log.d("value2","======res====2==========="+rs.getString(2));
                    Log.d("value3","======res====3==========="+rs.getString(3));
                    Log.d("value4","======res====++++=================");

                }
                database_AttendanceLog = itemNameList.get(0).getTitle_name();
                database_Team = itemNameList.get(1).getTitle_name();
                database_MyAttendance = itemNameList.get(2).getTitle_name();
                database_MyAttendance_EmpInfo =itemNameList.get(3).getTitle_name();
                database_BloodBank =itemNameList.get(4).getTitle_name();
                access_AttendanceLog = itemNameList.get(0).getAccess();
                access_Team = itemNameList.get(1).getAccess();
                access_MyAttendance = itemNameList.get(2).getAccess();
                access_MyAttendance_EmpInfo = itemNameList.get(3).getAccess();
                access_BloodBank = itemNameList.get(4).getAccess();

            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(HRMActivity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

}
