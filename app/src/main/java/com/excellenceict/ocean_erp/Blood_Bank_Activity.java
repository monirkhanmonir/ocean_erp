package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.Blood_Bank_Model;
import com.excellenceict.ocean_erp.adapter.CustomBloodBankAdapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Blood_Bank_Activity extends AppCompatActivity implements CustomBloodBankAdapter.SelectedBloodDonner {
    private Connection connection;
    private static final int REQUEST_CALL =1;
    private ArrayList<Blood_Bank_Model> bloodBankItems, bloodBankFilterItems;
    private CustomBloodBankAdapter adapter;
    private RecyclerView jbloodBank_recyclerView;
    private SearchView searchBlood;
    private BusyDialog busyDialog;
    private Context context;
    private Handler handler;
    private Toolbar toolbar;

    Button jblood_group_button;

    private Toolbar jmyBloodBankToolBarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_bank_activity);
        jbloodBank_recyclerView =findViewById(R.id.bloodBank_recyclerView);
        jmyBloodBankToolBarId = findViewById(R.id.myBloodBankToolBarId);
        searchBlood = findViewById(R.id.search_blood);
        jblood_group_button = findViewById(R.id.blood_group_btn);
        toolbar = findViewById(R.id.myBloodBankToolBarId);

        context = Blood_Bank_Activity.this;
        handler = new Handler();

        //control toolbar
        this.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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


        if(NetworkHelpers.isNetworkAvailable(context)){
            new Blood_Bank_Task().execute();
        }else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }


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


    @Override
    public void getSelectedBloodDonner(Blood_Bank_Model bloodDonnerModel) {
        Toast.makeText(this,"Donner name:"+ bloodDonnerModel.getF_name(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCallBloodDonner(String donner_number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+donner_number));
        startActivity(intent);

    }


    private void onSearchBlood(String group){
        if(group.equals("All Group")){
            adapter = new CustomBloodBankAdapter(bloodBankItems, (CustomBloodBankAdapter.SelectedBloodDonner) Blood_Bank_Activity.this);
            jbloodBank_recyclerView.setAdapter(adapter);
        }else {
            bloodBankFilterItems = new ArrayList<>();
            for (Blood_Bank_Model blood_donner: bloodBankItems){
               if(blood_donner.getBlood_Grp().equals(group)){
                   bloodBankFilterItems.add(blood_donner);
               }
            }
            adapter = new CustomBloodBankAdapter(bloodBankFilterItems, (CustomBloodBankAdapter.SelectedBloodDonner) Blood_Bank_Activity.this);
            jbloodBank_recyclerView.setAdapter(adapter);
        }
    }



    private class Blood_Bank_Task extends AsyncTask<Void, Void,Void>{

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "=============BloodBnk_DB========Connect===========");
                if (connection != null) {
                    bloodBankItems = new ArrayList<Blood_Bank_Model>();

                }

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery("select V_FNAME, V_LNAME, V_DEPT_NAME, V_DESIG_NAME, V_PHONE_MOBILE,V_BLOOD_GRP\n" +
                        "from BAS_PERSON\n" +
                        "where N_ACTIVE_FLAG=1\n" +
                        "and N_PERSON_TYPE=0\n" +
                        "and V_PERSON_NO<>'ADMINISTRATOR'\n" +
                        "order by v_fname");


                while (rs.next()) {

                    bloodBankItems.add(new Blood_Bank_Model(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
                }
                busyDialog.dismis();
                connection.close();


            }catch (Exception e) {

               busyDialog.dismis();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            busyDialog.dismis();

            adapter = new CustomBloodBankAdapter(bloodBankItems, (CustomBloodBankAdapter.SelectedBloodDonner) Blood_Bank_Activity.this);
            jbloodBank_recyclerView.setAdapter(adapter);
        }
    }


}
