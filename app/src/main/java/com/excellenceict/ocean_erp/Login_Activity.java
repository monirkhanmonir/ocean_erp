package com.excellenceict.ocean_erp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Login_Activity extends AppCompatActivity {
    private Button loginBtn;

    private TextInputEditText userName, password;
    String userInputPassword = "0";
    private String name, pass_Name;
    private String passDes_Dep,fullName;
    private Handler handler;
    private int userID;
    private Context context;

    private ImageSlider imageSlider;
    BusyDialog mBusyDialog;

    FloatingActionButton floatingCallBtn;
    private static final int REQUEST_CALL = 1;
    private String EMERGEMCY_NUMBER;


    private Connection connection;
    //    PreFerence_datapass preFerence_datapass;
    public static String globalPreference = "localData";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //globally set share preference
        editor = getSharedPreferences(globalPreference, Context.MODE_PRIVATE).edit();

        loginBtn = findViewById(R.id.login_btn);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        floatingCallBtn = findViewById(R.id.floatingBtn);

        imageSlider = findViewById(R.id.image_slider_reg);
        handler = new Handler();
        context = Login_Activity.this;

        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/1.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/2.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/3.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/4.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/5.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/6.jpg"));

        imageSlider.setImageList(slideModels, true);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                name = userName.getText().toString();
                pass_Name = password.getText().toString();
                Log.d("loginRes","========"+name+"====="+pass_Name);

                if (!name.isEmpty()) {
                    if (!pass_Name.isEmpty()) {

                        if (NetworkHelpers.isNetworkAvailable(Login_Activity.this)) {
                            new LoginTask().execute();
                            storeLoginUserName();
                        } else {
                            Toast.makeText(Login_Activity.this, "Please Check Your Internate.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Login_Activity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login_Activity.this, "Please enter user name", Toast.LENGTH_SHORT).show();
                }

            }
        });
        floatingCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
        showUsernameText();
        new EmergencyTask().execute();

    }



    public void showUsernameText() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("userNameKey")) {
            String userN = sharedPreferences.getString("userNameKey", "data not found");
            userName.setText(userN);
        }
    }

    private void storeLoginUserName() {
        String usernameStore = userName.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userNameKey", usernameStore.toUpperCase());
        editor.commit();

    }

    private class LoginTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            mBusyDialog = new BusyDialog(Login_Activity.this);
            mBusyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                if(connection == null){
                    Log.d("Connection_Checked","===connection null==>>>>");
                }
                Log.d("DB", "===================connected==================");
                Statement stmt = connection.createStatement();
                String query = "SELECT count(1), upper('" + name + "') user_name, pkg$hrm.fnc$emp_name2 (p.V_SALUTATION,p.V_FNAME,p.V_LNAME) emp_name, UPPER (v_desig_name) || ', ' || UPPER (v_dept_name) desig_dept, p.N_PERSON_ID FROM SEC_USER u, bas_person p WHERE u.N_PERSON_ID = p.N_PERSON_ID and u.V_USER_NAME=upper('" + name + "') AND u.V_PASSWORD=PKG$SEC.FNC$ENCRYPT('" + pass_Name + "') group by p.V_SALUTATION,p.V_FNAME,p.V_LNAME,V_DESIG_NAME,V_DEPT_NAME,p.N_PERSON_ID";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    fullName = rs.getString(3);
                    passDes_Dep = rs.getString(4);
                    userInputPassword = rs.getString(1);
                    userID = rs.getInt(5);

                    if (userInputPassword.equals("1")) {
                        mBusyDialog.dismis();
                        Log.d("login", "=========Login====Success==============");

                        Intent intent = new Intent(Login_Activity.this, Menu_Activity.class);

                        editor.putString("passwordKew",pass_Name );
                        editor.putString("userName", name);
                        editor.putString("p_name", fullName);
                        editor.putString("designation", passDes_Dep);
                        editor.putString("helpCall", EMERGEMCY_NUMBER);
                        editor.putInt("userId", userID);
                        editor.commit();
//                            Log.d("values","====dddd== +"+pass_Name + passDes_Dep);
                        startActivity(intent);
                    } else {
                        mBusyDialog.dismis();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "User name or password is not Currect.", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }


                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "User name or password is not Correct.", Toast.LENGTH_SHORT).show();
                            mBusyDialog.dismis();
                        }
                    });
                }


                connection.close();
            } catch (Exception e) {
                mBusyDialog.dismis();
                e.printStackTrace();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login_Activity.this, "Check Your Connection ",
                                Toast.LENGTH_SHORT).show();
                    }
                });


            }

            return null;
        }
    }

    private class EmergencyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mBusyDialog = new BusyDialog(Login_Activity.this);
            mBusyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();

                Statement stmt = connection.createStatement();
                String query = "select pkg$bas.fnc$bas_control_par('EMERGENCY_NO') from dual";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    EMERGEMCY_NUMBER = rs.getString(1);
                    Log.d("querynu_Number", "number==========" + rs.getString(1));
                    mBusyDialog.dismis();

                } else {
                    mBusyDialog.dismis();
                }

            } catch (Exception e) {
                mBusyDialog.dismis();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login_Activity.this, "Check Your Connection ",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }
    }

    public void makeCall() {


//        String number = "01856922061";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + EMERGEMCY_NUMBER));
        startActivity(intent);

        if (EMERGEMCY_NUMBER.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Login_Activity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dail = "tel:" + EMERGEMCY_NUMBER;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dail)));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Phone Number Found", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
