package com.excellenceict.ocean_erp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Menu_Activity extends AppCompatActivity {

    private Connection connection;
    private CardView j_card_hrm_id, j_card_inventory_id, j_card_sales_id, j_card_account_id, j_card_office_id, j_card_misc_id;
    TextView nameset, depertmentset;
    private ImageSlider imageSlider;
    private ImageView j_power_id;
    private String heltCall;

    private String user, full_name, des_dep; //  Data Store in sharepreference................


    //............. back button Handle..................
    private long backpressed;

    @Override
    public void onBackPressed() {

        if (backpressed + 001 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast mToast;
            mToast = Toast.makeText(getApplicationContext(), "Please Logout", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
        backpressed = System.currentTimeMillis();
    }

    //<<<<<<<<<<<<<<<<<<............. back button stop...................>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        //store user,fullname,designation and depertment in share preference.......
        SharedPreferences sharedPreferences = getSharedPreferences(Login_Activity.globalPreference, MODE_PRIVATE);
        user = sharedPreferences.getString("userName", "user name found");
        full_name = sharedPreferences.getString("p_name", "user Full Name found");
        des_dep = sharedPreferences.getString("designation", "user des found");
        heltCall = sharedPreferences.getString("helpCall", "help Number found");
//        Log.d("globalDATA", "==================" + user + " === " + full_name + "=====" + des_dep);

        nameset = findViewById(R.id.name_id);
        depertmentset = findViewById(R.id.designation_dept);

        imageSlider = findViewById(R.id.image_slider_menu);

        j_card_hrm_id = findViewById(R.id.card_hrm_id);
        j_card_inventory_id = findViewById(R.id.card_inventory_id);
        j_card_sales_id = findViewById(R.id.card_sales_id);
        j_card_account_id = findViewById(R.id.card_account_id);
        //  j_card_office_id = findViewById(R.id.card_office_id);
        //   j_card_misc_id = findViewById(R.id.card_misc_id);

        j_power_id = findViewById(R.id.power_id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);

        //for image slider
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/1.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/2.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/3.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/4.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/5.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/6.jpg"));

        imageSlider.setImageList(slideModels, true);

        j_power_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Menu_Activity.this);
                builder.setIcon(R.drawable.ic_power_color_button);
                builder.setTitle("Do you want to exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Menu_Activity.this, Login_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.show();
            }
        });


        j_card_hrm_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Menu_Activity.this, HRM_Activity.class);

                intent.putExtra("myName", user);
                intent.putExtra("persong_name", full_name);
                intent.putExtra("desig_dept", des_dep);
                startActivity(intent);

            }
        });


        j_card_inventory_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Menu_Activity.this, INV_Activity.class);

                intent.putExtra("myName", user);
                intent.putExtra("persong_name", full_name);
                intent.putExtra("desig_dept", des_dep);
                startActivity(intent);

            }
        });


        j_card_sales_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "Work in progress", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(),Test_Activity.class);
//                startActivity(intent);
            }
        });

        j_card_account_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, Acc_Activity.class);

                intent.putExtra("myName", user);
                intent.putExtra("persong_name", full_name);
                intent.putExtra("desig_dept", des_dep);
                startActivity(intent);

            }
        });

//        j_card_office_id.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MenuActivity.this, "Work in progress" , Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        j_card_misc_id.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MenuActivity.this, "Work in progress" , Toast.LENGTH_SHORT).show();
//            }
//        });


        //Set Event


        getLoginValueShowHeader();


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        set_userLast_LoginTime();

        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        bottomNavigationView.setSelectedItemId(R.id.emg_call);
        bottomNavigationView.getMenu().getItem(0).setChecked(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.emg_call:
                        makeCall();
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.passwordChange:
                        startActivity(new Intent(getApplicationContext(), ChangePassWord_Activity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(), InfoBtn_Activity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call_help:
//                startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
                makeCall();
                return true;

            case R.id.pass_change:
                startActivity(new Intent(getApplicationContext(), ChangePassWord_Activity.class));
                return true;

            case R.id.logout_menu:
                startActivity(new Intent(getApplicationContext(), Login_Activity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + heltCall));
        startActivity(intent);
    }

    private void getLoginValueShowHeader() {
//        >>>>>>>>>>>>>>>Start Get passing value login Activity To Menu Activity --<<<<<<<<
//        Bundle b = getIntent().getExtras();
        String person_Name = full_name;
        String designation_Dept = des_dep;
        nameset.setText(person_Name);
        depertmentset.setText(designation_Dept);
//           >>>>>>>>>>>>>>>---End Get passing value login and set Textview --<<<<<<<<

    }

    private void set_userLast_LoginTime() {
        try {
//            Bundle b = getIntent().getExtras();
            String userLoginValue = user;
//            Log.d("loginUser","=======user value======"+userLoginValue);

            connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();


            Log.d("connection", "============Menu====DB====Connect===========");
            if (connection != null) {

            }

            Statement stmt = connection.createStatement();

            String sql = "BEGIN PKG$SEC.prc$update_mobile_login ('" + userLoginValue + "'); END;";

            stmt.executeUpdate(sql);

            connection.close();

        } catch (Exception e) {

            Toast.makeText(Menu_Activity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


}
