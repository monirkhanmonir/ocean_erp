package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.Statement;

public class Profile_NameUpdate_Activity extends AppCompatActivity {
    private BusyDialog mBusyDialog;
    private Connection connection;
    private int userID;
    private TextInputEditText salutation, first_Name, last_Name, nick_Name;
    private Button saveBtn;
    private String salutation_data, firstName_data, lastName_data,
            nickName_data, status_data, religion_data, mobileNo_data,
            emergencyContact_data, email_data, birth_data, bloodGroup_data, presentAddress_data;
    private Handler handler;
    private String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_name_update_activity);

        SharedPreferences sharedPreferences = getSharedPreferences(Login_Activity.globalPreference, MODE_PRIVATE);
        userID = sharedPreferences.getInt("userId", 500);
        handler = new Handler();

        salutation = findViewById(R.id.prof_changeSalutation);
        first_Name = findViewById(R.id.prof_changeFname);
        last_Name = findViewById(R.id.prof_changeLname);
        nick_Name = findViewById(R.id.prof_changeNickname);
        saveBtn = findViewById(R.id.userSaveBtn);

        Intent intent = getIntent();
        salutation_data = intent.getStringExtra("salutation");
        firstName_data = intent.getStringExtra("fName");
        lastName_data = intent.getStringExtra("lName");
        nickName_data = intent.getStringExtra("nName");
        status_data = intent.getStringExtra("status");
        religion_data = intent.getStringExtra("religion");
        mobileNo_data = intent.getStringExtra("mobile");
        emergencyContact_data = intent.getStringExtra("emgContact");
        email_data = intent.getStringExtra("pEmail");
        presentAddress_data = intent.getStringExtra("pAddress");
        birth_data = intent.getStringExtra("pBirth");
        bloodGroup_data = intent.getStringExtra("bloodG");

        salutation.setText(salutation_data);
        first_Name.setText(firstName_data);
        last_Name.setText(lastName_data);
        nick_Name.setText(nickName_data);
        Log.d("NNNNNNN", "=========" + nick_Name);
        Log.d("MMMMMM", "=========" + email_data);
        Log.d("RRRRRR", "=========" + religion_data);

        //controll toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.prof_nameChange_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        getSupportActionBar().setTitle("Current Stock");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkHelpers.isNetworkAvailable(Profile_NameUpdate_Activity.this)) {
                    new UpdataNameTask().execute();
                    startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
                    finish();
                } else {
                    Toast.makeText(Profile_NameUpdate_Activity.this, "Please Check Your Internate.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class UpdataNameTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mBusyDialog = new BusyDialog(Profile_NameUpdate_Activity.this);
            mBusyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String address2 ="N/A";
                String address3 ="N/A";
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();

                Statement stmt = connection.createStatement();
                Log.d("DB","======Connected========");
                Log.d("0","=======>"+userID);
                Log.d("1","=======>"+salutation.getText().toString());
                Log.d("2","=======>"+first_Name.getText().toString());
                Log.d("3","=======>"+last_Name.getText().toString());
                Log.d("4","=======>"+nick_Name.getText().toString());
                Log.d("5","=======>"+status_data);
                Log.d("6","=======>"+bloodGroup_data);
                Log.d("7","=======>"+religion_data);
                Log.d("8","=======>"+birth_data);
                Log.d("9","=======>"+mobileNo_data);
                Log.d("10","=======>"+emergencyContact_data);
                Log.d("11","=======>"+email_data);
                Log.d("12","=======>"+presentAddress_data);
                Log.d("13","=======>"+address2);
                Log.d("14","=======>"+address2);



                String sql = "Update BAS_PERSON\n" +
                        "set V_SALUTATION=upper('"+salutation.getText().toString()+"'), \n" +
                        "V_FNAME=upper('"+first_Name.getText().toString()+"'), \n" +
                        "V_LNAME=upper('"+last_Name.getText().toString()+"'), \n" +
                        "V_NICK_NAME=upper('"+nick_Name.getText().toString()+"'), \n" +
                        "V_M_STATUS=upper('"+status_data+"'), \n" +
                        "V_BLOOD_GRP=upper('"+bloodGroup_data+"'), \n" +
                        "V_RELIGION=upper('"+religion_data+"'), \n" +
//                        "V_PLACE_OF_BIRTH=upper('"+birth_data+"'), \n" +
                        "V_PHONE_MOBILE=upper('"+mobileNo_data+"'),\n" +
                        "V_PHONE_HOME=upper('"+emergencyContact_data+"'), \n" +
                        "V_EMAIL_PERSONAL=lower('"+email_data+"') \n" +
//                        "V_PR_ADDR1=upper('"+presentAddress_data+"'), \n" +
//                        "V_PR_ADDR2=upper('"+address2+"'), \n" +
//                        "V_PR_ADDR3=upper('"+address3+"')\n" +
                        "where n_person_id = '"+userID+"'";

                stmt.executeUpdate(sql);
                connection.commit();
                mBusyDialog.dismis();
                connection.close();
            } catch (Exception e) {

                mBusyDialog.dismis();
                e.printStackTrace();
                e.getMessage();
            }
            return null;
        }
    }
}