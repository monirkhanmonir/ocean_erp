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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.Leave_Log_Model;
import com.excellenceict.ocean_erp.adapter.Leave_Log_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Leave_Log_Activity extends AppCompatActivity implements Leave_Log_Adapter.SelecetedItems {
    private Connection connection;
    private BusyDialog busyDialog;
    private Context context;
    private Toolbar toolbar;
    private String yearText;
    private Leave_Log_Adapter adapter;
    private ArrayList<Leave_Log_Model> leaveItems;
    private RecyclerView recyclerView;
    private TextView textView;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_log_activity);
        context = Leave_Log_Activity.this;
        recyclerView = findViewById(R.id.leaveLog_recyclerView);
//        textView = findViewById(R.id.year_text);
        search = findViewById(R.id.leaveLog_search);

        toolbar = findViewById(R.id.leaveLog_ToolBarId);
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

        if (NetworkHelpers.isNetworkAvailable(context)) {
            new ViewTask().execute();
        } else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }




        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    @Override
    public void selectItem(int positon, Leave_Log_Model model) {
//        Toast.makeText(context, "position: "+positon+">>>"+model.getId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context,LeaveLogDetails_Activity.class);
        intent.putExtra("employee_ID", model.getId());
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
                Log.d("BD", "===========Connected===========");
                if (connection != null) {
                    leaveItems = new ArrayList<>();

                    Statement stmt = connection.createStatement();
//                    String query = "select V_PERSON_NO, V_EMP_NAME, V_DESIG_NAME, V_DEPT_NAME, V_LEAVE_TYPE," +
//                            " N_LEAVE_ALLOWED, N_LEAVE_COUNT, N_LEAVE_BALANCE\n" +
//                            "from VW_HRM_LEAVE_SUMMARY\n" +
//                            "where ('" + active_flag + "'=-1 or N_ACTIVE_FLAG = '" + active_flag + "')\n" +
//                            "and N_YEAR='" + yearText + "'\n" +
//                            "order by V_EMP_NAME, V_LEAVE_TYPE";


                    ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_FNAME, V_LNAME, V_DEPT_NAME, V_DESIG_NAME, V_PHONE_MOBILE, V_EMAIL_OFFICIAL\n" +
                            "from BAS_PERSON\n" +
                            "where N_ACTIVE_FLAG=1\n" +
                            "and N_PERSON_TYPE=0\n" +
                            "and V_PERSON_NO<>'ADMINISTRATOR'\n" +
                            "order by v_fname");

                    while (rs.next()) {

                        leaveItems.add(new Leave_Log_Model(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                rs.getString(5)));
                        Log.d("value1", "=========1===========" + rs.getString(1));
                        Log.d("value2", "=========2===========" + rs.getString(2));
                        Log.d("value3", "=========3===========" + rs.getString(3));
                        Log.d("value4", "=========4===========" + rs.getString(4));
                        Log.d("value5", "=========5===========" + rs.getString(5));
                        Log.d("value6", "=========6===========" + rs.getString(6));
                        Log.d("value7", "=========7===========" + rs.getString(7));


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

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            adapter = new Leave_Log_Adapter(leaveItems, (Leave_Log_Adapter.SelecetedItems) context);
            recyclerView.setAdapter(adapter);

            search.setActivated(true);
            search.setQueryHint("Search here...");
            search.onActionViewExpanded();
            search.setIconified(false);
            search.clearFocus();

            busyDialog.dismis();

        }
    }

}