package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.MyLeave_Model;
import com.excellenceict.ocean_erp.adapter.My_Leave_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class My_Leave_Activity extends AppCompatActivity {
    private Context context;
    private int userID;
    private Toolbar toolbar;
    private Connection connection;
    private BusyDialog busyDialog;
    private String yearText;
    private My_Leave_Adapter adapter;
    private ArrayList<MyLeave_Model> leaveItems;
    private RecyclerView recyclerView;
    private Button button;
    private TextView id, name, desigantion, department;
    private String emp_name, emp_designation, emp_department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_leave_activity);
        context = My_Leave_Activity.this;

        recyclerView = findViewById(R.id.myleave_recyclerView);
        button = findViewById(R.id.myleave_button);

        name = findViewById(R.id.myleave_name);
        desigantion = findViewById(R.id.myleave_designation);
        department = findViewById(R.id.myleave_department);

        SharedPreferences sharedPreferences = getSharedPreferences(Login_Activity.globalPreference, MODE_PRIVATE);
        userID = sharedPreferences.getInt("userId", 404);
//        Toast.makeText(context, "store ID " + userID, Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.myleave_ToolBarId);
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

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final List<Integer> list;
        list = new ArrayList<>();
        for (int i = year; i >= 2000; i--) {
            list.add(i);
        }
        /** --------------Increment for current Year-----------
         * final List<Integer> list1 = new ArrayList<>();
         for (int j = 2000; j <= year; j++) {
         list1.add(j);
         Log.d("value :","===V=="+list1+"==i=="+j);
         }**/
        button.setText(String.valueOf(year));
        yearText = button.getText().toString();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Year");
                builder.setIcon(R.drawable.ic_calender);
                ArrayAdapter arrayAdapter = new ArrayAdapter<Integer>(
                        context,
                        android.R.layout.simple_list_item_single_choice,
                        list
                );

                builder.setSingleChoiceItems(arrayAdapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        button.setText(String.valueOf(list.get(which)));
                        yearText = String.valueOf(list.get(which));
//                        Toast.makeText(context, "Selected: "+list.get(which), Toast.LENGTH_SHORT).show();
                        new ViewTask().execute();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

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
                Log.d("DB", "===========Connected===========");
                if (connection != null) {
                    leaveItems = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select V_EMP_NAME, V_DESIG_NAME, V_DEPT_NAME, V_LEAVE_TYPE, N_LEAVE_ALLOWED, N_LEAVE_COUNT, N_LEAVE_BALANCE\n" +
                            "from VW_HRM_LEAVE_SUMMARY\n" +
                            "where N_PERSON_ID = '" + userID + "'\n" +
                            "and N_YEAR='" + yearText + "'\n" +
                            "order by V_EMP_NAME, V_LEAVE_TYPE";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        leaveItems.add(new MyLeave_Model(rs.getString(1), rs.getString(2), rs.getString(3),
                                rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));

                        emp_name = rs.getString(1);
                        emp_designation = rs.getString(2);
                        emp_department = rs.getString(3);
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
            adapter = new My_Leave_Adapter(leaveItems);
            recyclerView.setAdapter(adapter);

            name.setText(emp_name);
            desigantion.setText(emp_designation);
            department.setText(emp_department);

            busyDialog.dismis();

        }
    }
}