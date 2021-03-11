package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.excellenceict.ocean_erp.Model.ChartOfAccountFirstView_model;
import com.excellenceict.ocean_erp.adapter.Chart_Of_Account_firstView_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ChartOfAccounts_Activity extends AppCompatActivity implements Chart_Of_Account_firstView_Adapter.SelectedChart_FirstViewItems {
    private Toolbar toolbar;
    private BusyDialog busyDialog;
    private Context context;
    private Connection connection;
    private ArrayList<ChartOfAccountFirstView_model> model;
    private RecyclerView recyclerView;
    private Chart_Of_Account_firstView_Adapter adapter;
    private String headCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_of_accounts_activity);
        context = ChartOfAccounts_Activity.this;
        toolbar = findViewById(R.id.chartOfAccount_ToolBarId);
        recyclerView = findViewById(R.id.chartOfAccountFirstview_recyclerView);

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

        new ViewTask().execute();
    }

    @Override
    public void getSelectedItems(ChartOfAccountFirstView_model fmodel) {
//        Toast.makeText(context, "item" + fmodel.getHeadCode(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, Chart_of_account_details_Dynamic_Activity.class);
        intent.putExtra("headCode", fmodel.getHeadCode());
        startActivity(intent);

    }

    private class ViewTask extends AsyncTask<Void, Void, Void> {
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
                    String query = "select distinct V_HEAD_CODE\n" +
                            "From ACC_HEAD\n" +
                            "where V_HEAD_CODE is not null\n" +
                            "order by V_HEAD_CODE";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        model.add(new ChartOfAccountFirstView_model(rs.getString(1)));
                        Log.d("value1", "======chart==H=1===========" + rs.getString(1));


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
            recyclerView.setLayoutManager(new LinearLayoutManager(ChartOfAccounts_Activity.this));
            recyclerView.addItemDecoration(new DividerItemDecoration(ChartOfAccounts_Activity.this, DividerItemDecoration.VERTICAL));
            adapter = new Chart_Of_Account_firstView_Adapter(model, (Chart_Of_Account_firstView_Adapter.SelectedChart_FirstViewItems) ChartOfAccounts_Activity.this);
            recyclerView.setAdapter(adapter);

            busyDialog.dismis();
            super.onPostExecute(aVoid);
        }
    }

}