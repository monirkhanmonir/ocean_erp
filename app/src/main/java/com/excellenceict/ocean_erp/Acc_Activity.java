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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.adapter.Accounts_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Acc_Activity extends AppCompatActivity implements Accounts_Adapter.SelecetedItems {

    private Connection connection;
    private RecyclerView recyclerView;
    private TextView name, designation, invH_textView;
    private String userName;
    private String database_Accounts, database_ViewVoucher, database_PartyDueStatement, database_LedgerPosition;
    private String access_Accounts, access_ViewVoucher, access_PartyDueStatement, access_LedgerPosition;
    private ArrayList<HRM_AccessPermission_Entity> itemNameList;
    private Accounts_Adapter adapter;

    String title[] = {"Chart of Accounts", "View Voucher", "Party Due Statement", "Ledger Position"};
    int[] imageItem = {R.drawable.ic_account_chart, R.drawable.ic_bill_invoice,
            R.drawable.ic_chalan_report, R.drawable.ic_position};

    private BusyDialog busyDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_activity);
        recyclerView = findViewById(R.id.acc_recyclerView);
        name = findViewById(R.id.acc_p_name);
        designation = findViewById(R.id.acc_p_designation);
        invH_textView = findViewById(R.id.accHead_textview);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BroshkPlum-YzqJL.ttf");
        invH_textView.setTypeface(typeface);
        context = Acc_Activity.this;


        if (NetworkHelpers.isNetworkAvailable(context)) {
            new OptionEnableDisableTask().execute();
            getLoginValueShowHeader2();
        } else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        adapter = new Accounts_Adapter(title, imageItem, (Accounts_Adapter.SelecetedItems) context);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void selectItem(int position) {
        if (position == 0) {
            startActivity(new Intent(context, ChartOfAccounts_Activity.class));
        } else if (position == 1) {
            if (database_ViewVoucher.equals("View Voucher") && access_ViewVoucher.equals("0")) {
                Toast.makeText(Acc_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
                Log.d("voucher", "--------if---" + database_ViewVoucher + "==" + access_ViewVoucher);
            } else {
                Log.d("voucher", "--------else---" + database_ViewVoucher + "==" + access_ViewVoucher);
                Intent intent = new Intent(context, View_Voucher_Activity.class);
                startActivity(intent);
            }

        } else if (position == 2) {
            if (database_PartyDueStatement.equals("Party Due Statement") && access_PartyDueStatement.equals("0")) {
                Toast.makeText(Acc_Activity.this, "You Can't Access", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), Party_Due_Statement_Activity.class);
                startActivity(intent);
            }
        } else if (position == 3) {
            startActivity(new Intent(getApplicationContext(), Ledger_Position_Activity.class));
        }

    }

    private void getLoginValueShowHeader2() {
        Bundle b = getIntent().getExtras();
        String person_Name = b.getString("persong_name");
        String persong_desig_dept = b.getString("desig_dept");

        name.setText(person_Name);
        designation.setText(persong_desig_dept);

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
                    database_ViewVoucher = itemNameList.get(15).getTitle_name();
                    database_PartyDueStatement = itemNameList.get(16).getTitle_name();
                    access_ViewVoucher = itemNameList.get(15).getAccess();
                    access_PartyDueStatement = itemNameList.get(16).getAccess();

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
