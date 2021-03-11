package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.adapter.INV_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class INV_Activity extends AppCompatActivity implements INV_Adapter.SelectedItems {
    private Connection connection;
    private String userName;
    private RecyclerView recyclerView;
    private TextView name, designation, invH_textView;
    private String database_currentStock, database_Bill_Invoice, database_SalesChalan,
            database_LoanChalan, database_expiry, database_salesReport_Summary, database_salesReport_Details,
            database_chalanReport_Summary, database_chalanReport_Details;
    private String access_currentStock, access_Bill_Invoice, access_SalesChalan,
            access_LoanChalan, access_Expiry, access_SalesReport_Summary, access_SalesReport_Details,
            access_chalanReport_Summary, access_chalanReport_Details;
    private ArrayList<HRM_AccessPermission_Entity> itemNameList;
    private INV_Adapter adapter;

    String invTitle[] = {"Current Stock", "Bill/Invoice", "Sales Chalan",
            "Loan Chalan", "Expiry list", "Sales Report(Summary)", "Sales Report(Details)",
            "Chalan Report(Summary)", "Chalan Report(Details)"};

    int[] invimageItem = {R.drawable.ic_stock_inventory, R.drawable.ic_bill_invoice,
            R.drawable.ic_selse_chalan, R.drawable.ic_loan_chalan, R.drawable.ic_loan_chalan,
            R.drawable.ic_sales_summary, R.drawable.ic_sales, R.drawable.ic_chalan_report, R.drawable.ic_chalan_report};

    private BusyDialog busyDialog;
    private Context context;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inv_activity);
        recyclerView = findViewById(R.id.inv_recyclerView);
        name = findViewById(R.id.inv_p_name);
        designation = findViewById(R.id.inv_p_designation);
        invH_textView = findViewById(R.id.inv_text);
        context = INV_Activity.this;
        handler = new Handler();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BroshkPlum-YzqJL.ttf");
        invH_textView.setTypeface(typeface);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        adapter = new INV_Adapter(invTitle, invimageItem, (INV_Adapter.SelectedItems) context);
        recyclerView.setAdapter(adapter);

        getLoginValueShowHeader2();


        if (NetworkHelpers.isNetworkAvailable(context)) {
            new OptionEnableDisableTask().execute();
        } else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLoginValueShowHeader2() {
//        <<<<<<<<<<<<<<<------ (Start getting login value by get Menu Activity To HRM Activity) -------->>>>>>>>>>>
        Bundle b = getIntent().getExtras();
        String person_Name = b.getString("persong_name");
        String persong_desig_dept = b.getString("desig_dept");

        name.setText(person_Name);
        designation.setText(persong_desig_dept);
//        <<<<<<<<<<<<<<<------ End getting login value -------->>>>>>>>>>>

    }

    @Override
    public void getSelectedItems(int position) {
        if (position == 0) {
            if (database_currentStock.equals("Current Stock") && access_currentStock.equals("0")) {
                Toast.makeText(INV_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("voucher", "--------if---" + database_currentStock + "==" + access_currentStock);
            } else {
                Log.d("voucher", "--------else---" + database_currentStock + "==" + access_currentStock);
                Intent intent = new Intent(INV_Activity.this, CurrentStock_Activity.class);
                startActivity(intent);
            }

        } else if (position == 1) {
            if (database_Bill_Invoice.equals("Bill / Invoice") && access_Bill_Invoice.equals("0")) {
                Toast.makeText(INV_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("voucher", "--------if---" + database_Bill_Invoice + "==" + access_Bill_Invoice);
            } else {
                Log.d("voucher", "--------else---" + database_Bill_Invoice + "==" + access_Bill_Invoice);
                Intent intent = new Intent(INV_Activity.this, Bill_Invoice_Activity.class);
                startActivity(intent);
            }

        } else if (position == 2) {
            if (database_SalesChalan.equals("Sales Chalan") && access_SalesChalan.equals("0")) {
                Toast.makeText(INV_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("voucher", "--------if---" + database_SalesChalan + "==" + access_SalesChalan);
            } else {
                Log.d("voucher", "--------else---" + database_SalesChalan + "==" + access_SalesChalan);
                Intent intent = new Intent(INV_Activity.this, Sales_Chalan_Activity.class);
                startActivity(intent);
            }
//                    Toast.makeText(INVActivity.this,"this is 3rd",Toast.LENGTH_SHORT).show();
        } else if (position == 3) {
            Toast.makeText(INV_Activity.this, "On progress", Toast.LENGTH_SHORT).show();

        } else if (position == 4) {
//                    Toast.makeText(INVActivity.this,"on progress",Toast.LENGTH_SHORT).show();
            if (database_expiry.equals("Expiry List") && access_Expiry.equals("0")) {
                Toast.makeText(INV_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("expiry", "--------if---" + database_expiry + "==" + access_Expiry);
            } else {
                Log.d("expiry", "--------else---" + database_expiry + "==" + access_Expiry);
                Intent intent = new Intent(INV_Activity.this, Expiory_List_Activity.class);
                startActivity(intent);
            }

        } else if (position == 5) {
            if (database_salesReport_Summary.equals("Sales Report (Summary)") && access_SalesReport_Summary.equals("0")) {
                Toast.makeText(INV_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("sLSummary", "--------if---" + database_salesReport_Summary + "==" + access_SalesReport_Summary);
            } else {
                Log.d("sLSummary", "--------else---" + database_salesReport_Summary + "==" + access_SalesReport_Summary);
                Intent intent = new Intent(INV_Activity.this, Sales_Report_Summary_Activity.class);
                startActivity(intent);
            }

        } else if (position == 6) {
            if (database_salesReport_Details.equals("Sales Report (Detail)") && access_SalesReport_Details.equals("0")) {
                Toast.makeText(INV_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("sLsDetails", "--------if---" + database_salesReport_Details + "==" + access_SalesReport_Details);
            } else {
                Log.d("sLsDetails", "--------else---" + database_salesReport_Details + "==" + access_SalesReport_Details);
                Intent intent = new Intent(INV_Activity.this, Sales_Report_Details_Activity.class);
                startActivity(intent);
            }

        } else if (position == 7) {
            if (database_chalanReport_Summary.equals("Chalan Report (Summary)") && access_chalanReport_Summary.equals("0")) {
                Toast.makeText(INV_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("chalan_R", "--------if---" + database_chalanReport_Summary + "==" + access_chalanReport_Summary);
            } else {
                Log.d("chalan_R", "--------else---" + database_chalanReport_Summary + "==" + access_chalanReport_Summary);
                Intent intent = new Intent(INV_Activity.this, Chalan_Report_Summary_Activity.class);
                startActivity(intent);
            }
//                    Toast.makeText(INVActivity.this,"On progress",Toast.LENGTH_SHORT).show();
        } else if (position == 8) {
            if (database_chalanReport_Details.equals("Chalan Report (Detail)") && access_chalanReport_Details.equals("0")) {
                Toast.makeText(INV_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("chalan_D", "--------if---" + database_chalanReport_Details + "==" + access_chalanReport_Details);
            } else {
                Log.d("chalan_D", "--------else---" + database_chalanReport_Details + "==" + access_chalanReport_Details);
                Intent intent = new Intent(INV_Activity.this, Chalan_Report_Details_Activity.class);
                startActivity(intent);
            }
//
        }

    }

    private class OptionEnableDisableTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();

                if (connection != null) {
                    Bundle b = getIntent().getExtras();
                    userName = b.getString("myName");
                    Log.d("login_valueeee", "--------------" + userName);
                    itemNameList = new ArrayList<HRM_AccessPermission_Entity>();

                    Statement stmt = connection.createStatement();
                    String query = "select a.N_OBJECT_ID position, a.V_OBJECT_NAME name,\n" +
                            "decode(a.N_ACTIVE_FLAG,0,0,decode(a.N_CAN_ACCESS,0,0,b.N_CAN_ACCESS)) enable_flag\n" +
                            "from SEC_OBJECT_MOBILE a, SEC_USER_OBJECT_PRIV_MOBILE b\n" +
                            "where a.N_OBJECT_ID=b.N_OBJECT_ID\n" +
                            "and b.N_USER_ID = (select N_USER_ID from sec_user where V_USER_NAME='" + userName.toUpperCase() + "')";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        itemNameList.add(new HRM_AccessPermission_Entity(rs.getString(1), rs.getString(2), rs.getString(3)));

                        Log.d("value1", "======res====1===========" + rs.getString(1));
                        Log.d("value2", "======res====2===========" + rs.getString(2));
                        Log.d("value3", "======res====3===========" + rs.getString(3));
                        Log.d("value4", "======res====++++=================");

                    }
//                database_Accounts = itemNameList.get(1).getTitle_name();
                    database_currentStock = itemNameList.get(5).getTitle_name();
                    database_Bill_Invoice = itemNameList.get(6).getTitle_name();
                    database_SalesChalan = itemNameList.get(7).getTitle_name();
//                database_LoanChalan =itemNameList.get(8).getTitle_name();
                    database_expiry = itemNameList.get(9).getTitle_name();
                    database_salesReport_Summary = itemNameList.get(10).getTitle_name();
                    database_salesReport_Details = itemNameList.get(11).getTitle_name();
                    database_chalanReport_Summary = itemNameList.get(12).getTitle_name();
                    database_chalanReport_Details = itemNameList.get(13).getTitle_name();


                    access_currentStock = itemNameList.get(5).getAccess();
                    access_Bill_Invoice = itemNameList.get(6).getAccess();
                    access_SalesChalan = itemNameList.get(7).getAccess();
//                access_LoanChalan = itemNameList.get(8).getAccess();
                    access_Expiry = itemNameList.get(9).getAccess();
                    access_SalesReport_Summary = itemNameList.get(10).getAccess();
                    access_SalesReport_Details = itemNameList.get(11).getAccess();
                    access_chalanReport_Summary = itemNameList.get(12).getAccess();
                    access_chalanReport_Details = itemNameList.get(13).getAccess();

//                access_LedgerPosition = itemNameList.get(17).getAccess();

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
            busyDialog.dismis();

        }
    }


}
