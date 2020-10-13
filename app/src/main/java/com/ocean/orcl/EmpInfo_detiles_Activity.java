package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EmpInfo_detiles_Activity extends AppCompatActivity {
    private Connection connection;
    ArrayList<Emp_detiles_Entity> empInfoDetiles;
    private TextView idNo,name,detp_desig,reports,mbl_personal,mbl_emergency,email_personal,email_office,address,joinDate,pabx,bloodGrp;
    private Button callBtn,smsBtn;
    private static final int REQUEST_CALL =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_info_detiles);

        callBtn=findViewById(R.id.call_btn_empDelailes);
        smsBtn =findViewById(R.id.sms_btn_empDelailes);

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


        Intent intent = getIntent();
        String id = intent.getStringExtra("person_number");
        String itemsTest = intent.getStringExtra("Another");
        Log.d("person_Number","========per details id========"+id);
        Log.d("itemsTest","========222========"+itemsTest);

//        personNumber.setText(id);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "==========empInfo_detilesDB===========Connect===========");
            if (connection != null) {
                empInfoDetiles = new ArrayList<Emp_detiles_Entity>();

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

                empInfoDetiles.add(new Emp_detiles_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12)));

                idNo.setText(rs.getString(1));
                name.setText(rs.getString(2));
                detp_desig.setText(rs.getString(3));
                reports.setText(rs.getString(4));
                mbl_personal.setText(rs.getString(5));
                mbl_emergency.setText(rs.getString(6));
                email_personal.setText(rs.getString(7));
                email_office.setText(rs.getString(8));
                address.setText(rs.getString(9));
                joinDate.setText(rs.getString(10));
                pabx.setText(rs.getString(11));
                bloodGrp.setText(rs.getString(12));


            }

            connection.close();
        }catch (Exception e) {

            Toast.makeText(EmpInfo_detiles_Activity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                makeCall();
                String number = mbl_personal.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);

            }
        });
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Going sms text...",Toast.LENGTH_SHORT).show();

                String number = mbl_personal.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+number));
                startActivity(intent);

            }
        });
    }

//    // ................ direct calling system ...................
//    public void makeCall(){
//        String number = mbl_personal.getText().toString();
//        if(number.trim().length() >0){
//            if(ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(EmpInfo_detiles_Activity.this,
//                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
//            }else {
//                String dail = "tel:"+number;
//                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
//            }
//        }else {
//            Toast.makeText(getApplicationContext(),"Enter Phone Number",Toast.LENGTH_SHORT).show();
//
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == REQUEST_CALL){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                makeCall();
//            }else {
//                Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
//    // ................End direct calling system ...................
}
