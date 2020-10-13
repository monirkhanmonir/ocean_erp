package com.ocean.orcl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    //private EditText userName;
    //private EditText password;
    private TextInputEditText userName, password;
    private TextView showText;
    Button showBtn;
    String userInputPassword;
    private String name,pass_Name;
    private String passDes_Dep;
    private Handler handler;

    private ImageSlider imageSlider;


    private Connection connection;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.login_btn);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        imageSlider = findViewById(R.id.image_slider_reg);
        handler = new Handler();

        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/1.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/2.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/3.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/4.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/5.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/6.jpg"));

        imageSlider.setImageList(slideModels,true);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                 name = userName.getText().toString();
                String psd = password.getText().toString();

                if(!name.isEmpty()){
                    if(!psd.isEmpty()){

                        Log.d("UserName","==================="+name);
                        Log.d("Password","==================="+psd);


                        try {
                            connection =com.ocean.orcl.ODBC.Db.createConnection();
//                    if(connection == null){
//                        Toast.makeText(LoginActivity.this,"please check your Connection",Toast.LENGTH_SHORT).show();
//                    }
                            Log.d("connectionDB","===================connected==================");
//                    Toast.makeText(LoginActivity.this, "Connected",
//                            Toast.LENGTH_SHORT).show();
                            Statement stmt=connection.createStatement();

                            String query ="SELECT count(1), upper('"+name+"') user_name, pkg$hrm.fnc$emp_name2 (p.V_SALUTATION,p.V_FNAME,p.V_LNAME) emp_name, UPPER (v_desig_name) || ', ' || UPPER (v_dept_name) desig_dept FROM SEC_USER u, bas_person p WHERE u.N_PERSON_ID = p.N_PERSON_ID and u.V_USER_NAME=upper('"+name+"') AND u.V_PASSWORD=PKG$SEC.FNC$ENCRYPT('"+psd+"') group by p.V_SALUTATION,p.V_FNAME,p.V_LNAME,V_DESIG_NAME,V_DEPT_NAME";

                            ResultSet rs=stmt.executeQuery(query);




                            while (rs.next()){


                                pass_Name = rs.getString(3);
                                passDes_Dep = rs.getString(4);
                                userInputPassword = rs.getString(1);


                                Log.d("name","====1======="+rs.getString(1));
                                Log.d("pass","====2======="+rs.getString(2));
                                Log.d("p_name","====3======="+rs.getString(3));
                                Log.d("designation","====4======="+rs.getString(4));

                            }
                            securityLogin();

                            connection.close();
                        }
                        catch (Exception e) {

                            Toast.makeText(LoginActivity.this, "Check Your Connection "+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

//                    e.printStackTrace();
                        }



                    }else {
                        Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Please enter user name", Toast.LENGTH_SHORT).show();
                }

            }
        });
        showUsernameText();
//        blankMessage();

    }

    public void showUsernameText(){
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("userNameKey")){
            String userN = sharedPreferences.getString("userNameKey","data not found");
            userName.setText(userN);
        }
    }
    private void securityLogin(){
        if(userInputPassword.equals("1")){
            Log.d("login","=========Login====Success==============");
            Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
            //............>>>>>>>>>>>>>>>>.....value pass.................>>>>>
            Intent intent =new Intent(LoginActivity.this,MenuActivity.class);
            intent.putExtra("userName", name);
            intent.putExtra("p_name", pass_Name);
            intent.putExtra("designation", passDes_Dep);
            //............>>>>>>>>>>>>>>>>.....value pass.................>>>>>
            startActivity(intent);
        }if(userInputPassword.equals("0")){
            Toast.makeText(getApplicationContext(),"User name or password is not Currect.",Toast.LENGTH_SHORT).show();
        }
//        else {
//
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),"User name or password is not Currect.",Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
////            password.setError("Enter PassWord");
//        }

        storeLoginUserName();

    }
    private void blankMessage(){

        if (userName.getText().toString().length() <= 0 || userName.getText().equals(null)) {
            Log.d("nill_name","----------null pass------------");
            userName.setError("Enter User Name");
        }
        if (password.getText().toString().length() <= 0 || password.getText().equals(null)) {
            Log.d("nill_pas","----------null pass------------");
            password.setError("Enter PassWord");
        }
        if (password.getText().toString().length() <= 0 || password.getText().equals(null)) {
            Log.d("nill_pas","----------null pass------------");
            password.setError("Enter PassWord");
        }
    }
    private  void  storeLoginUserName(){
        String usernameStore = userName.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userNameKey",usernameStore.toUpperCase());
        editor.commit();
//                            Toast.makeText(getApplicationContext(),"Data stored",Toast.LENGTH_SHORT).show();


    }

    }

