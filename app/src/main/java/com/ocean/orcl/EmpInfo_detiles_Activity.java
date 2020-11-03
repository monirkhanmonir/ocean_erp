package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ocean.orcl.util.BusyDialog;
import com.ocean.orcl.util.Helper;
import com.ocean.orcl.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EmpInfo_detiles_Activity extends AppCompatActivity {
    private Connection connection;
    Emp_detiles_Entity empInfoDetiles;
    private TextView idNo,name,detp_desig,reports,mbl_personal,mbl_emergency,email_personal,email_office,address,joinDate,pabx,bloodGrp;
    private static final int REQUEST_CALL =1;
    private BusyDialog busyDialog;
    private Context context;
    private Handler handler;
    private String id;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_info_detiles);

        idNo = findViewById(R.id.personDetiles_number);
        name = findViewById(R.id.emp_name_detiles);
        detp_desig = findViewById(R.id.dept_desig_detiles);
        reports = findViewById(R.id.reports_detiles);
        mbl_personal = findViewById(R.id.mobile_no_deiles);
        mbl_emergency = findViewById(R.id.emergency_mobileNO_detiles);
        email_personal = findViewById(R.id.email_pers_detiles);
        email_office = findViewById(R.id.email_office_detiles);
        address = findViewById(R.id.address_detiles);
        joinDate = findViewById(R.id.joinDate_detiles);
        pabx = findViewById(R.id.pabx_detiles);
        bloodGrp = findViewById(R.id.blood_Grp_detiles);
        toolbar = findViewById(R.id.employeeInfoDetailsToolBarId);

        context = EmpInfo_detiles_Activity.this;
        handler = new Handler();

        //control toolbar
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


        Intent intent = getIntent();
         id = intent.getStringExtra("person_number");
        String itemsTest = intent.getStringExtra("Another");
        Log.d("person_Number","========per details id========"+id);
        Log.d("itemsTest","========222========"+itemsTest);

//        personNumber.setText(id);

        if(NetworkHelpers.isNetworkAvailable(context)){
            new EmployeeInfoDetailsTask().execute();
        }else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }

        mbl_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+empInfoDetiles.getPhone_mobile()));
                startActivity(intent);
            }
        });

        mbl_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+empInfoDetiles.getPhone_home()));
                startActivity(intent);
            }
        });


   }

   private class EmployeeInfoDetailsTask extends AsyncTask<Void,Void,Void>{

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
               Log.d("connection", "==========empInfo_detilesDB===========Connect===========");
               if (connection != null) {


               }
               Statement stmt = connection.createStatement();

               ResultSet rs = stmt.executeQuery("select V_PERSON_NO,\n" +
                       "pkg$hrm.fnc$emp_name2 (p.V_SALUTATION,p.V_FNAME,p.V_LNAME) emp_name, \n" +
                       "UPPER (v_desig_name) || ', ' || UPPER (v_dept_name) desig_dept,\n" +
                       "(select pkg$hrm.fnc$emp_name2 (a.V_SALUTATION,a.V_FNAME,a.V_LNAME)||' ('|| \n" +
                       "UPPER (a.v_desig_name) || ', ' || UPPER (a.v_dept_name)||')'\n" +
                       "from BAS_PERSON a where a.N_PERSON_ID=p.N_REPORTING_TO) reports_to,\n" +
                       "V_PHONE_MOBILE,V_PHONE_HOME,\n" +
                       "V_EMAIL_PERSONAL, V_EMAIL_OFFICIAL,\n" +
                       "V_PR_ADDR1,D_JOIN_DATE, V_PABX_EXT,\n" +
                       "V_BLOOD_GRP\n" +
                       "from BAS_PERSON p\n" +
                       "where N_PERSON_TYPE=0\n" +
                       "and V_PERSON_NO='"+id+"'");

               while (rs.next()) {

                   empInfoDetiles = new Emp_detiles_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12));


               }

              busyDialog.dismis();
               connection.close();
           }catch (Exception e) {

               busyDialog.dismis();
               e.printStackTrace();
           }

           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {

           busyDialog.dismis();

           idNo.setText(empInfoDetiles.getPerson_id());
           name.setText(empInfoDetiles.getEmp_name());
           detp_desig.setText(empInfoDetiles.getDesig_dept());
           reports.setText(empInfoDetiles.getReporters_to());
           mbl_personal.setText(empInfoDetiles.getPhone_mobile());
           mbl_emergency.setText(empInfoDetiles.getPhone_home());
           email_personal.setText(empInfoDetiles.getEmail_personal());
           email_office.setText(empInfoDetiles.email_official);
           address.setText(empInfoDetiles.getAddress1());
           joinDate.setText(empInfoDetiles.getJoin_date());
           pabx.setText(empInfoDetiles.getPabx());
           bloodGrp.setText(empInfoDetiles.getBlood_grp());
       }
   }




}
