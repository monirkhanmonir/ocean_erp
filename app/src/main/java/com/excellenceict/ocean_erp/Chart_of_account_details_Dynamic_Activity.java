package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.ChartOfAccounts_Final_Model;
import com.excellenceict.ocean_erp.adapter.Chart_Of_Account_Details_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Chart_of_account_details_Dynamic_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private BusyDialog busyDialog;
    private Context context;
    private Connection connection;
    private ArrayList<ChartOfAccounts_Final_Model> model;
    private RecyclerView recyclerView;
    private Chart_Of_Account_Details_Adapter adapter;
    private String headCode;
    private TextView toolHeaderText;
    private SearchView searchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_of_account_details_activity);
        context = Chart_of_account_details_Dynamic_Activity.this;
        toolbar = findViewById(R.id.chartOfAccountDetails_ToolBarId);
        recyclerView = findViewById(R.id.chartOfAccountDetails_recyclerView);
        toolHeaderText =findViewById(R.id.toobarHeaer);
        searchData =findViewById(R.id.chartDetails_search);

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
        Bundle bundle = getIntent().getExtras();
        headCode = bundle.getString("headCode");
        toolHeaderText.setText(headCode);

        if (NetworkHelpers.isNetworkAvailable(context)){
            new ClickViewTask().execute();
        }else {
            Toast.makeText(context, "Please check your internate connection", Toast.LENGTH_SHORT).show();
        }

        searchData.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                Log.d("searchT","=====>"+newText);

                return false;
            }
        });

    }

    private class ClickViewTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "==================ConnectedDB===========");
                if (connection != null) {
                    model = new ArrayList<>();
                    Statement stmt = connection.createStatement();
                    String query = "select V_SUB_HEAD_CODE, V_SUB_HEAD_NAME, V_DESCRIPTION\n" +
                            "From ACC_HEAD\n" +
                            "where V_HEAD_CODE = '"+headCode+"'\n" +
                            "order by V_SUB_HEAD_CODE";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        model.add(new ChartOfAccounts_Final_Model(rs.getString(1),rs.getString(2),rs.getString(3)));
                        Log.d("value1", "======chart=====1===========" + rs.getString(1));
                        Log.d("value2", "======chart=====2===========" + rs.getString(2));
                        Log.d("value2", "======chart=====3===========" + rs.getString(3));


                    }
                }
                busyDialog.dismis();
                connection.close();

            } catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            recyclerView.setLayoutManager(new LinearLayoutManager(Chart_of_account_details_Dynamic_Activity.this));
            recyclerView.addItemDecoration(new DividerItemDecoration(Chart_of_account_details_Dynamic_Activity.this, DividerItemDecoration.VERTICAL));
            adapter = new Chart_Of_Account_Details_Adapter(model);
            recyclerView.setAdapter(adapter);

            searchData.setActivated(true);
            searchData.setQueryHint("Search Here...");
            searchData.onActionViewExpanded();
            searchData.setIconified(false);
            searchData.clearFocus();

            busyDialog.dismis();
        }
    }
}