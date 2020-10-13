package com.ocean.orcl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test_Activity extends AppCompatActivity {
    private Connection connection;
    private static final int REQUEST_CALL =1;
    EditText callText;
    Button callBtn;
    private static final int PERMISSION_READ_STATE =123;
    String strPhoneType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        callBtn = findViewById(R.id.call_btn);
        callText =findViewById(R.id.call_text);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
//        start();

        Test_initList();

    }

    public void makeCall(){
        String number = callText.getText().toString();
        if(number.trim().length() >0){
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Test_Activity.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else {
                String dail = "tel:"+number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
            }
        }else {
            Toast.makeText(getApplicationContext(),"Enter Phone Number",Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makeCall();
            }else {
                Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_SHORT).show();
            }
        }
//        ---------for phone number Ime sim serial number --------------
//        switch (requestCode){
//            case PERMISSION_READ_STATE:
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    start();
//                }else {
//                    Toast.makeText(getApplicationContext(),"You don't have permission ",Toast.LENGTH_SHORT).show();
//                }
//        }
    }

//---------for phone number Ime sim serial number --------------
//    private void start(){
//        int permissionCheck = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_PHONE_STATE);
//        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_READ_STATE);
//        }else {
//            TelephonyManager telephonyManager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String getSimSerialNumber = telephonyManager.getSimSerialNumber();
//            String getSimNumber =telephonyManager.getLine1Number();
//            String telNumber = telephonyManager.getLine1Number();
//            String telNumber2 = telephonyManager.getDeviceId();
//            String telNumbe3 = telephonyManager.getDeviceSoftwareVersion();
//            if (telNumber != null)
//                Toast.makeText(this, "Phone number: " + telNumber+"\n"+" Version "+telNumbe3+" Ime nmber"+telNumber2,
//                        Toast.LENGTH_LONG).show();
//            Log.d("number ","=============number"+getSimNumber+"========"+"emi= "+telNumber2);
//
//        }
//    }
private void Test_initList(){

    try {

        connection = com.ocean.orcl.ODBC.Db.createConnection();
        Log.d("connection","================Test DB==Connected===========");
        if(connection != null){
//            groupNameList = new ArrayList<>();

            Statement stmt=connection.createStatement();
            String query = "select V_SALUTATION, V_FNAME, V_LNAME, V_DEGREE, V_DOCTOR_SPECIALITY, N_OPD_1ST_FEE, N_OPD_2ND_FEE, B_PHOTO_IMAGE \n"+
                    "from VW_DOCTOR \n" +
                    "where V_ORGANIZATION_NAME = 'Sono Diagnostic Centre Ltd.'\n" +
                    "and V_BRANCH_NAME = 'Sono Tower - 2' \n" +
                    "order by V_DOCTOR_SPECIALITY, V_SALUTATION, V_FNAME, V_LNAME, V_DEGREE";

            ResultSet rs=stmt.executeQuery(query);

            while(rs.next()) {
//                groupNameList.add(new Billinvoice_Group_Entity(rs.getString(1),rs.getString(2)));
                    Log.d("value1","==========1========="+rs.getString(1));
                    Log.d("value2","========2==========="+rs.getString(2));
                    Log.d("value3","========3==========="+rs.getString(3));
                    Log.d("value4","========4==========="+rs.getString(4));
                    Log.d("value5","========5==========="+rs.getString(5));
                    Log.d("value6","========6==========="+rs.getString(6));
                    Log.d("value7","========7==========="+rs.getString(7));




            }
//            group_adapter =new Billinvoice_Group_Adapter(getApplication(),groupNameList);
//            group_spinner.setAdapter(group_adapter);
        }


        connection.close();

    }
    catch (Exception e) {

        Toast.makeText(Test_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }


}

}
