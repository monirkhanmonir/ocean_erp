package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.adapter.PartyDueStatement_Adapter;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Party_Due_Statement_Activity extends AppCompatActivity {
    private Connection connection;
    String sub_header_name;
    String sub_header_code = "-1";
    private ArrayList<PartyDueStatement_Entity> customerNameList;
    private ArrayList<PartyDueStatement_Result_Entity> resultList;
    private PartyDueStatement_Adapter adapter;
    private RecyclerView recyclerView;
    private TextView toDate;

    private TextView j_partyDue_statement_spinner;
    private Dialog dialog;
    private BusyDialog busyDialog;
    private Context context;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partydue_statement_activity);
        //customer_spinner =findViewById(R.id.partyDue_statement_spinner);
        recyclerView = findViewById(R.id.partyDueStatement_recyclerView);
        j_partyDue_statement_spinner = findViewById(R.id.partyDue_statement_spinner);
        toDate = findViewById(R.id.partyDue_dateTex);
        toolbar = findViewById(R.id.partyDueStatementToolBarId);
        context = Party_Due_Statement_Activity.this;

        //controll toolbar
        //controll toolbar
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


        CurrentDate();
        DateSetTO();
        j_partyDue_statement_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(customerNameList==null){
                    Toast.makeText(Party_Due_Statement_Activity.this, "Check your internate connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog = new Dialog(Party_Due_Statement_Activity.this);
                dialog.setContentView(R.layout.manufacture_dailog_spinner);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                SearchView SpinnerSearchView = dialog.findViewById(R.id.spinner_search);
                ListView SpinnerListView = dialog.findViewById(R.id.spinnerItemList);

                ImageView current_stock_image = dialog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_loan_chalan);
                SpinnerSearchView.setQueryHint("Search here...");
                SpinnerSearchView.onActionViewExpanded();
                SpinnerSearchView.setIconified(false);
                SpinnerSearchView.clearFocus();

                ImageView calcen_btn = dialog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                final ArrayAdapter<PartyDueStatement_Entity> adapter = new ArrayAdapter<PartyDueStatement_Entity>(
                        Party_Due_Statement_Activity.this, android.R.layout.simple_spinner_dropdown_item,customerNameList
                );

                SpinnerListView.setAdapter(adapter);

                SpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                SpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(adapter.getItem(position).getSub_head_name().equals("Customer")){
                            sub_header_code = adapter.getItem(position).getSub_head_code();
                            Toast.makeText(Party_Due_Statement_Activity.this, "Please select an item: "+sub_header_code, Toast.LENGTH_SHORT).show();
                        }else {

                            sub_header_code = adapter.getItem(position).getSub_head_code();

                            if(NetworkHelpers.isNetworkAvailable(context)){
                                new Result_Task().execute();
                                DateSetTO();
                            }else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                            j_partyDue_statement_spinner.setText(adapter.getItem(position).getSub_head_name());
                        }

                    }
                });
            }
        });

        if(NetworkHelpers.isNetworkAvailable(context)){
            new CustomerName_Task().execute();
        }else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }

        if(NetworkHelpers.isNetworkAvailable(context)){
            new Result_Task().execute();
        }else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }
    }



    private class CustomerName_Task extends AsyncTask<Void,Void,ArrayList<PartyDueStatement_Entity>> {

        @Override
        protected void onPreExecute() {
           busyDialog = new BusyDialog(context);
          // busyDialog.show();
        }

        @Override
        protected ArrayList<PartyDueStatement_Entity> doInBackground(Void... voids) {
            customerNameList = new ArrayList<>();
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection","================party Due statement==Connected===========");
                if(connection != null){
                    customerNameList = new ArrayList<>();
                    Statement stmt=connection.createStatement();
                    String query = "select V_SUB_HEAD_NAME,V_SUB_HEAD_CODE \n" +
                            "from( \n" +
                            "select 1 sl, 'Customer' V_SUB_HEAD_NAME, '-1' V_SUB_HEAD_CODE \n" +
                            "from dual\n" +
                            "union\n" +
                            "select distinct 2 sl,replace(V_SUB_HEAD_NAME,'_'||replace(V_SUB_HEAD_CODE,UD_NO||'_',''),'') V_SUB_HEAD_NAME, UD_NO V_SUB_HEAD_CODE\n" +
                            "from ACC_HEAD\n" +
                            "where V_HEAD_CODE = 'DEBTORHEAD'\n" +
                            "and   N_PROJECT_ID = 1   \n" +
                            ")\n" +
                            "order by sl,V_SUB_HEAD_NAME";

                    ResultSet rs=stmt.executeQuery(query);

                    while(rs.next()) {
                        customerNameList.add(new PartyDueStatement_Entity(rs.getString(1),rs.getString(2)));
                    }

                }
              //  busyDialog.dismis();
                connection.close();
            }
            catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }

            return customerNameList;
        }

    }
    private class Result_Task extends AsyncTask<Void,Void,ArrayList<PartyDueStatement_Result_Entity>> {

        @Override
        protected void onPreExecute() {
         busyDialog = new BusyDialog(context);
         busyDialog.show();
        }

        @Override
        protected ArrayList<PartyDueStatement_Result_Entity> doInBackground(Void... voids) {
            resultList = new ArrayList<>();
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("query","=========customer_Header ="+sub_header_name);
                Log.d("query1","=========customer_Header_code ="+sub_header_code);
                if(connection != null){
                    resultList = new ArrayList<PartyDueStatement_Result_Entity>();

                    Statement stmt=connection.createStatement();
                    String query1,query2;
                    query1="select fnc$dateto(to_date('"+toDate.getText()+"','MON DD,RRRR')) from dual";
                    stmt.executeUpdate(query1);

                    query2 = "select T_DATE,s.UD_NO, s.V_SUB_HEAD_NAME, s.ANALYZER, s.ROCHE, s.SYSMEX\n" +
                            "from VW_ACC_DUE_STATEMENT s\n" +
                            "where ('"+sub_header_code+"' ='-1' or s.UD_NO like '%'||'"+sub_header_code+"'||'%')\n" +
                            "order by s.V_SUB_HEAD_NAME";

                    ResultSet rs=stmt.executeQuery(query2);

                    while(rs.next()) {
                        resultList.add(new PartyDueStatement_Result_Entity(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
                    }
                    busyDialog.dismis();
                }
                connection.close();

            }
            catch (Exception e) {
                busyDialog.dismis();
               e.printStackTrace();
            }

            return resultList;
        }

        @Override
        protected void onPostExecute(ArrayList<PartyDueStatement_Result_Entity> acc_partyDueStatement_entities) {

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            adapter = new PartyDueStatement_Adapter(resultList);
            recyclerView.setAdapter(adapter);

            busyDialog.dismis();

        }
    }

    private void DateSetTO(){

        toDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(Party_Due_Statement_Activity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday)
                    {
//
                        year = selectedyear;
                        month = selectedmonth;
                        day = selectedday;

                        cal.setTimeInMillis(0);
                        cal.set(year, month, day, 0, 0, 0);
                        Date chosenDate = cal.getTime();

                        // Format the date using style medium and US locale
                        DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
                        String date = df_medium_us.format(chosenDate);
                        toDate.setText(date.toUpperCase());
                        new Result_Task().execute();
//
                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    private void CurrentDate(){
        String currentDate = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        toDate.setText(currentDate.toUpperCase());
    }

}
