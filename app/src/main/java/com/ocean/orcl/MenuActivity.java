package com.ocean.orcl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
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
    GridLayout mainGrid;
    TextView nameset,depertmentset;
    private ImageSlider imageSlider;



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

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        //for image slider
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/1.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/2.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/3.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/4.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/5.jpg"));
        slideModels.add(new SlideModel("https://excellenceict.com/Hospimate_online/banner/visitor/landingpage/6.jpg"));

        imageSlider.setImageList(slideModels,true);



        //Set Event
        setSingleEvent(mainGrid);

        getLoginValueShowHeader();


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        set_userLast_LoginTime();

    }
   


    private void setSingleEvent(GridLayout mainGrid) {

        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI == 0){
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

                    } else if(finalI == 1){
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

                    } else if(finalI == 2){
                        Toast.makeText(MenuActivity.this, "Work in progress" , Toast.LENGTH_SHORT).show();

                    } else if(finalI == 3){
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

                    }else if(finalI == 4){
                        Toast.makeText(MenuActivity.this, "Work in progress" , Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MenuActivity.this,Test_Activity.class);
////                        intent.putExtra("info","This is activity from card item index  "+finalI);
//                        startActivity(intent);

                    }else if(finalI == 5){
                        Toast.makeText(MenuActivity.this, "Work in progress" , Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MenuActivity.this, "Can't Find Item" , Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                         Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
            return true;
                default: return super.onOptionsItemSelected(item);
        }

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

            connection = com.ocean.orcl.ODBC.Db.createConnection();


            Log.d("connection", "============Menu====DB====Connect===========");
            if (connection != null) {

            }

            Statement stmt = connection.createStatement();

            String sql = "BEGIN PKG$SEC.prc$update_mobile_login ('"+userLoginValue+"'); END;";

            stmt.executeUpdate(sql);
//            ResultSet rs = stmt.executeQuery(sql);
//
//            while (rs.next()) {
//                Log.d("test","======= Query success ======");
//
//
//            }

            connection.close();

        } catch (Exception e) {

            Toast.makeText(MenuActivity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
