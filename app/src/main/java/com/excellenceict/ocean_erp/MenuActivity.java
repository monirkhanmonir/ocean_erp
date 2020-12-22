package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private Connection connection;
    private CardView j_card_hrm_id,j_card_inventory_id,j_card_sales_id,j_card_account_id,j_card_office_id,j_card_misc_id;
    TextView nameset,depertmentset;
    private ImageSlider imageSlider;
    private ImageView j_power_id;



   //<<<<<<<<<<<<<<<<<<............. start No back in this method...................>>>>>>>>>>>>>>>>>>>>>>
   private long backpressed;
    @Override
    public void onBackPressed() {

        if(backpressed + 001 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(getBaseContext(),"please Logout",Toast.LENGTH_SHORT).show();
        }
        backpressed = System.currentTimeMillis();
    }

                         //<<<<<<<<<<<<<<<<<<............. End Swithing No back in this method...................>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        nameset =findViewById(R.id.name_id);
        depertmentset =findViewById(R.id.designation_dept);

        imageSlider = findViewById(R.id.image_slider_menu);

        j_card_hrm_id = findViewById(R.id.card_hrm_id);
        j_card_inventory_id = findViewById(R.id.card_inventory_id);
        j_card_sales_id = findViewById(R.id.card_sales_id);
        j_card_account_id = findViewById(R.id.card_account_id);
      //  j_card_office_id = findViewById(R.id.card_office_id);
     //   j_card_misc_id = findViewById(R.id.card_misc_id);

        j_power_id = findViewById(R.id.power_id);



        //for image slider
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/1.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/2.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/3.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/4.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/5.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/6.jpg"));

        imageSlider.setImageList(slideModels,true);

        j_power_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setIcon(R.drawable.ic_power_color_button);
                builder.setTitle("Do you want to exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
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

                Intent intent = new Intent(MenuActivity.this,HRMActivity.class);
                //............>>>>>>>>>>>>>>>>.....Login value pass.................>>>>>
                Bundle b = getIntent().getExtras();
                String testsUserName = b.getString("userName");
                intent.putExtra("myName", testsUserName);

                String person_Name = b.getString("p_name");
                intent.putExtra("persong_name",person_Name );

                String designation_Dept = b.getString("designation");
                intent.putExtra("desig_dept",designation_Dept );
                //............>>>>>>>>>>>>>>>>.....Login value pass.................>>>>>
                startActivity(intent);

            }
        });



        j_card_inventory_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this,INVActivity.class);
                Bundle b = getIntent().getExtras();
                String testsUserName = b.getString("userName");
                intent.putExtra("myName", testsUserName);
                String person_Name = b.getString("p_name");
                intent.putExtra("persong_name",person_Name );

                String designation_Dept = b.getString("designation");
                intent.putExtra("desig_dept",designation_Dept );
//                        intent.putExtra("info","This is activity from card item index  "+finalI);
                startActivity(intent);

            }
        });


        j_card_sales_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Work in progress" , Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(),Test_Activity.class);
//                startActivity(intent);
            }
        });

        j_card_account_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,AccActivity.class);
                Bundle b = getIntent().getExtras();
                String testsUserName = b.getString("userName");
                intent.putExtra("myName", testsUserName);
                String person_Name = b.getString("p_name");
                intent.putExtra("persong_name",person_Name );

                String designation_Dept = b.getString("designation");
                intent.putExtra("desig_dept",designation_Dept );
//                        intent.putExtra("info","This is activity from card item index  "+finalI);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }


    private void getLoginValueShowHeader(){
//        >>>>>>>>>>>>>>>Start Get passing value login Activity To Menu Activity --<<<<<<<<
        Bundle b = getIntent().getExtras();
        String person_Name = b.getString("p_name");
        String designation_Dept = b.getString("designation");
        nameset.setText(person_Name);
        depertmentset.setText(designation_Dept);
//           >>>>>>>>>>>>>>>---End Get passing value login and set Textview --<<<<<<<<

    }
    private void set_userLast_LoginTime(){
        try {
            Bundle b = getIntent().getExtras();
            String userLoginValue = b.getString("userName");
//            Log.d("loginUser","=======user value======"+userLoginValue);

            connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();


            Log.d("connection", "============Menu====DB====Connect===========");
            if (connection != null) {

            }

            Statement stmt = connection.createStatement();

            String sql = "BEGIN PKG$SEC.prc$update_mobile_login ('"+userLoginValue+"'); END;";

            stmt.executeUpdate(sql);

            connection.close();

        } catch (Exception e) {

            Toast.makeText(MenuActivity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
