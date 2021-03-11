package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.Leave_LogDetails_Model;
import com.excellenceict.ocean_erp.Model.Leave_Log_Model;
import com.excellenceict.ocean_erp.adapter.Leave_LogDetails_Adapter;
import com.excellenceict.ocean_erp.adapter.Leave_Log_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class LeaveLogDetails_Activity extends AppCompatActivity {
    private Connection connection;
    private BusyDialog busyDialog;
    private Context context;
    private Toolbar toolbar;
    private String yearText, id, titles;
    private TextView name, desigantion, department;

    private Leave_LogDetails_Adapter adapter;
    private ArrayList<Leave_LogDetails_Model> leaveItems;
    private RecyclerView recyclerView;
    private TextView textView, title;
    private Button button;
    private String emp_name, emp_designation, emp_department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_log_details_activity);
        context = LeaveLogDetails_Activity.this;
        title = findViewById(R.id.leaveLog_detailsText);
        recyclerView = findViewById(R.id.leaveDetails_recyclerView);
//        textView = findViewById(R.id.year_text);
        button = findViewById(R.id.leave_button);

        name = findViewById(R.id.leaveDetails_name);
        desigantion = findViewById(R.id.leaveDetails_designation);
        department = findViewById(R.id.leaveDetails_department);

        toolbar = findViewById(R.id.leaveLogDetails_ToolBarId);
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
//                       onSearchBlood(catagories.get(which));
                        button.setText(String.valueOf(list.get(which)));
                        yearText = String.valueOf(list.get(which));
                        new ViewTask().execute();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        if (NetworkHelpers.isNetworkAvailable(context)) {
            id = getIntent().getStringExtra("employee_ID");
            yearText = button.getText().toString();
//            Toast.makeText(context, "userID: " + id, Toast.LENGTH_SHORT).show();
            new ViewTask().execute();
        } else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }
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

                    String query = "select V_EMP_NAME, V_DESIG_NAME, V_DEPT_NAME, V_LEAVE_TYPE, N_LEAVE_ALLOWED," +
                            " N_LEAVE_COUNT, N_LEAVE_BALANCE\n" +
                            "from VW_HRM_LEAVE_SUMMARY\n" +
                            "where V_PERSON_NO = '" + id + "'\n" +
                            "and N_YEAR='" + yearText + "'\n" +
                            "order by V_EMP_NAME, V_LEAVE_TYPE";


                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {

                        leaveItems.add(new Leave_LogDetails_Model(rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));

                        titles = rs.getString(1);
                        emp_name = rs.getString(1);
                        emp_designation = rs.getString(2);
                        emp_department = rs.getString(3);
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
            adapter = new Leave_LogDetails_Adapter(leaveItems);
            recyclerView.setAdapter(adapter);
            title.setText(titles);
            name.setText(emp_name);
            desigantion.setText(emp_designation);
            department.setText(emp_department);

            busyDialog.dismis();

        }
    }
}