package com.excellenceict.ocean_erp;

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
    EditText callText,test_editText;
    Button callBtn,test_btn;
    private static final int PERMISSION_READ_STATE =123;
    String strPhoneType;
    private  String checkNumber="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        callBtn = findViewById(R.id.call_btn);
        test_btn=findViewById(R.id.testBtn_id);
        test_editText = findViewById(R.id.test_id);
        callText =findViewById(R.id.call_text);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
//        start();
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test_initList();
            }
        });

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
    }


private void Test_initList(){

    try {

        connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
        Log.d("connection","================Test DB==Connected===========");
        if(connection != null){
//            groupNameList = new ArrayList<>();

            Statement stmt=connection.createStatement();
            String query = "select pkg$sec.fnc$check_otp_log ('"+test_editText.getText().toString()+"') a from dual";

            ResultSet rs=stmt.executeQuery(query);

            while(rs.next()) {
                    Log.d("value1","==========1========="+rs.getString(1));
                    checkNumber =rs.getString(1);
                    if (checkNumber.equals("0")){
                        UsernumberInsert();
                    }else {
                        Toast.makeText(this, "already get your number", Toast.LENGTH_SHORT).show();
                    }
            }
        }


        connection.close();

    }
    catch (Exception e) {

        Toast.makeText(Test_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }


}
private void UsernumberInsert(){
    try {
        connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
        if (connection != null) {
        }
        Statement stmt = connection.createStatement();
        String sql = "begin pkg$sec.prc$insert_otp_log ('"+test_editText.getText().toString()+"'); end;";
        stmt.executeUpdate(sql);
        Log.d("success","======Success Insert number========");
        connection.commit();
        connection.close();
    } catch (Exception e) {

        e.printStackTrace();
    }
}

}
