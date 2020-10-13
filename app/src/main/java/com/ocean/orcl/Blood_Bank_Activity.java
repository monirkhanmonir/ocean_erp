package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ocean.orcl.adapter.CustomBloodBankAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Blood_Bank_Activity extends AppCompatActivity implements CustomBloodBankAdapter.SelectedBloodDonner {
    private Connection connection;
    private static final int REQUEST_CALL =1;
    private ArrayList<Blood_Bank_Entity> bloodBankItems, bloodBankFilterItems;
    CustomBloodBankAdapter adapter;
    //ListView listView;
    private RecyclerView jbloodBank_recyclerView;
    private SearchView searchBlood;
   // private Spinner spinner;

    Button jblood_group_button;

    private Toolbar jmyBloodBankToolBarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        jbloodBank_recyclerView =findViewById(R.id.bloodBank_recyclerView);
        jmyBloodBankToolBarId = findViewById(R.id.myBloodBankToolBarId);
//        callBtn = findViewById(R.id.call_btn);
//        callText =findViewById(R.id.call_text);
        searchBlood = findViewById(R.id.search_blood);
        jblood_group_button = findViewById(R.id.blood_group_btn);

        this.setSupportActionBar(jmyBloodBankToolBarId);

        searchBlood.setActivated(true);
        searchBlood.setQueryHint("Search blood...");
        searchBlood.onActionViewExpanded();
        searchBlood.setIconified(false);
        searchBlood.clearFocus();

        //for rechycler view
        jbloodBank_recyclerView.setLayoutManager(new LinearLayoutManager(Blood_Bank_Activity.this));
        jbloodBank_recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));



        // spinner = findViewById(R.id.spinner_bloodBank);



        final List<String>catagories = new ArrayList<>();
        catagories.add("All Group");
        catagories.add("A+");
        catagories.add("A-");
        catagories.add("B+");
        catagories.add("B-");
        catagories.add("AB+");
        catagories.add("AB-");
        catagories.add("O+");
        catagories.add("O-");
        catagories.add("N/A");
//        ArrayAdapter<String> dataAdapter;
//        dataAdapter = new ArrayAdapter<>(Blood_Bank_Activity.this,android.R.layout.simple_spinner_item,catagories);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinner.setAdapter(dataAdapter);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        defaultQueryRun();


        jblood_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Blood_Bank_Activity.this);
                builder.setTitle("Select Group");
                builder.setIcon(R.drawable.ic_blood_bank);
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                        Blood_Bank_Activity.this,
                        android.R.layout.simple_list_item_single_choice,
                        catagories
                );


                builder.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       onSearchBlood(catagories.get(which));
                       jblood_group_button.setText(catagories.get(which));
                       // Toast.makeText(Blood_Bank_Activity.this, "Selected: "+catagories.get(which), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });





//callBtn.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//       makeCall();
//    }
//});


        searchBlood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>When {selected} for ALL Blood Group and DropDown menu set Query>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public void defaultQueryRun(){
        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "=============BloodBnk_DB========Connect===========");
            if (connection != null) {
                bloodBankItems = new ArrayList<Blood_Bank_Entity>();

            }

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select V_FNAME, V_LNAME, V_DEPT_NAME, V_DESIG_NAME, V_PHONE_MOBILE,V_BLOOD_GRP\n" +
                    "from BAS_PERSON\n" +
                    "where N_ACTIVE_FLAG=1\n" +
                    "and N_PERSON_TYPE=0\n" +
                    "and V_PERSON_NO<>'ADMINISTRATOR'\n" +
                    "order by v_fname");


            while (rs.next()) {

                bloodBankItems.add(new Blood_Bank_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));


            }

             adapter = new CustomBloodBankAdapter(bloodBankItems, (CustomBloodBankAdapter.SelectedBloodDonner) Blood_Bank_Activity.this);
            jbloodBank_recyclerView.setAdapter(adapter);

            connection.close();


        }catch (Exception e) {

            Toast.makeText(Blood_Bank_Activity.this, "" + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void getSelectedBloodDonner(Blood_Bank_Entity bloodDonnerModel) {
        Toast.makeText(this,"Donner name:"+ bloodDonnerModel.getF_name(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCallBloodDonner(String donner_number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+donner_number));
        startActivity(intent);

    }
//public void makeCall(){
//    String number = callText.getText().toString();
//    if(number.trim().length() >0){
//        if(ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(Blood_Bank_Activity.this,
//                    new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
//        }else {
//            String dail = "tel:"+number;
//            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dail)));
//        }
//    }else {
//        Toast.makeText(getApplicationContext(),"Enter Phone Number",Toast.LENGTH_SHORT).show();
//
//    }
//
//
//}
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == REQUEST_CALL){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                makeCall();
//            }else {
//                Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


    private void onSearchBlood(String group){
        if(group.equals("All Group")){
            adapter = new CustomBloodBankAdapter(bloodBankItems, (CustomBloodBankAdapter.SelectedBloodDonner) Blood_Bank_Activity.this);
            jbloodBank_recyclerView.setAdapter(adapter);
        }else {
            bloodBankFilterItems = new ArrayList<>();
            for (Blood_Bank_Entity blood_donner: bloodBankItems){
               if(blood_donner.getBlood_Grp().equals(group)){
                   bloodBankFilterItems.add(blood_donner);
               }
            }
            adapter = new CustomBloodBankAdapter(bloodBankFilterItems, (CustomBloodBankAdapter.SelectedBloodDonner) Blood_Bank_Activity.this);
            jbloodBank_recyclerView.setAdapter(adapter);
        }
    }


}
