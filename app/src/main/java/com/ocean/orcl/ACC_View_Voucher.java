package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ACC_View_Voucher extends AppCompatActivity {
    private Connection connection;
    private ArrayList<View_Voucher_Entity> transactionList;
    private ArrayList<ACC_View_Voucher_Result_Entity> voucher_Result;
    private ACC_ViewVoucher_Result_Adapter restult_adapter;
    private View_Voucher_Adapter view_voucher_adapter;
    private Spinner transation_spinner;
    private TextView from_Date,to_date;
    private EditText referenceTextView;
    private String tr_typeVal;
    private Button button;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc__view__voucher);

        transation_spinner =findViewById(R.id.view_voucher_spinner);
        from_Date =findViewById(R.id.viewVoucher_from_date);
        to_date =findViewById(R.id.viewVoucher_to_date);
        referenceTextView =findViewById(R.id.viewVoucher_Reference_text);
        button =findViewById(R.id.voucher_btn);
        listView = findViewById(R.id.voucher_result_listView);
        new Transaction_Task().execute();
        currentDate();


        transation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View_Voucher_Entity  clickedItem = (View_Voucher_Entity) parent.getItemAtPosition(position);
                tr_typeVal =clickedItem.getTr_type_val();
                if(clickedItem.getTr_type().equals("<< Select Transaction Type >>")){

                }else {
//                    transaction_initList();
                    dateSetFROM();
                    dateSetTO();
                    new Result_Task().execute();
                    Toast.makeText(getApplicationContext(),"Selected : "+clickedItem.getTr_type(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Result_Task().execute();
            }
        });

    }
    private void transaction_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================View Voucher==Connected===========");
            if(connection != null){
                transactionList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select tr_type, tr_type_val\n" +
                        "from (\n" +
                        "Select -1 sl, '<< Select Transaction Type >>' tr_type, -1 tr_type_val\n" +
                        "from dual\n" +
                        "union all\n" +
                        "select sl,tr_type, tr_type_val from ACC_TR_TYPE\n" +
                        ")\n" +
                        "order by sl";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    transactionList.add(new View_Voucher_Entity(rs.getString(1),rs.getString(2)));
                    Log.d("value1","======view voucher====1==========="+rs.getString(1));
                    Log.d("value2","======view voucher=====2==========="+rs.getString(2));

                }
//                view_voucher_adapter =new View_Voucher_Adapter(getApplication(),transactionList);
//                transation_spinner.setAdapter(view_voucher_adapter);

            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(getApplicationContext(), " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void showResult_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================View_Voucher Result==Connected===========");
            Log.d("Date","================fromDate==========="+from_Date.getText()+" toDt"+to_date.getText());
            Log.d("value","=========Tr_typeVal = "+tr_typeVal+" Reference = "+referenceTextView.getText());
            if(connection != null){
                voucher_Result = new ArrayList<ACC_View_Voucher_Result_Entity>();

                Statement stmt=connection.createStatement();
                String query = "select to_char(D_VOUCHER_DT,'MON DD,RRRR') D_VOUCHER_DT, v.V_VOUCHER_NO, V_REF_VOUCHER, TR_TYPE,\n" +
                        "decode(N_VOUCHER_TYPE,1,'Debit',2,'Credit','Journal') VOUCHER_TYPE,\n" +
                        "V_NARRATION, sum(N_DR) amount\n" +
                        "from VW_ACC_VOUCHER_MST v,VW_ACC_VOUCHER_DTL c, ACC_TR_TYPE t\n" +
                        "where v.N_TR_TYPE = t.TR_TYPE_VAL\n" +
                        "and v.V_VOUCHER_NO = c.V_VOUCHER_NO\n" +
                        "and v.n_approved_flag =1\n" +
                        "and ('"+tr_typeVal+"' = -1 or N_TR_TYPE= '"+tr_typeVal+"')\n" +
//                        "and D_VOUCHER_DT = to_date('"+text_Date.getText()+"','MON DD,RRRR')\n" +
//                        "and D_VOUCHER_DT between to_date('"+from_Date.getText()+"','MON DD,RRRR') and to_date("+to_date.getText()+",'MON DD,RRRR')\n" +
                        "AND D_VOUCHER_DT BETWEEN to_date('"+from_Date.getText()+"','MON DD,RRRR') AND to_date('"+to_date.getText()+"','MON DD,RRRR')\n" +
                        "and V_REF_VOUCHER like '%'||upper('"+referenceTextView.getText().toString()+"')||'%'\n" +
                        "group by D_VOUCHER_DT, v.V_VOUCHER_NO, V_REF_VOUCHER, TR_TYPE,N_VOUCHER_TYPE,V_NARRATION\n" +
                        "order by v.D_VOUCHER_DT desc,V_REF_VOUCHER desc";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    voucher_Result.add(new ACC_View_Voucher_Result_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
//                    Log.d("value1","======res====1==========="+rs.getString(1));
//                    Log.d("value2","======res====2==========="+rs.getString(2));
//                    Log.d("value3","======res====3==========="+rs.getString(3));
//                    Log.d("value4","======res====4==========="+rs.getString(4));
//                    Log.d("value5","======res====5==========="+rs.getString(5));
//                    Log.d("value6","======res====6==========="+rs.getString(6));
//                    Log.d("value7","======res====7==========="+rs.getString(7));

                }

            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(getApplicationContext(), " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private class Transaction_Task extends AsyncTask<Void,Void,ArrayList<View_Voucher_Entity>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(ACC_View_Voucher.this);
//            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<View_Voucher_Entity> doInBackground(Void... voids) {
            transactionList = new ArrayList<>();
            transaction_initList();
            return transactionList;
        }




        @Override
        protected void onPostExecute(ArrayList<View_Voucher_Entity> view_voucher_entities) {
            view_voucher_adapter =new View_Voucher_Adapter(getApplication(),transactionList);
            transation_spinner.setAdapter(view_voucher_adapter);
            loadingBar.dismiss();
        }
    }
    private class Result_Task extends AsyncTask<Void,Void,ArrayList<ACC_View_Voucher_Result_Entity>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(ACC_View_Voucher.this);
            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait For Results.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<ACC_View_Voucher_Result_Entity> doInBackground(Void... voids) {
            voucher_Result = new ArrayList<ACC_View_Voucher_Result_Entity>();
            showResult_initList();
            return voucher_Result;
        }

        @Override
        protected void onPostExecute(ArrayList<ACC_View_Voucher_Result_Entity> bill_result_entities) {
            super.onPostExecute(bill_result_entities);
            restult_adapter =new ACC_ViewVoucher_Result_Adapter(getApplication(),voucher_Result);
            listView.setAdapter(restult_adapter);

            loadingBar.dismiss();
        }

    }
    private void currentDate(){
        String currentDate = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        from_Date.setText(currentDate.toUpperCase());
        to_date.setText(currentDate.toUpperCase());
    }
    private void dateSetFROM(){

        from_Date.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(ACC_View_Voucher.this, new DatePickerDialog.OnDateSetListener()
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
                        from_Date.setText(date.toUpperCase());
//                        new Sales_Chalan_Activity.Result_Task().execute();
//                        showResult_initList();
                        new Result_Task().execute();


                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }
    private void dateSetTO(){

        to_date.setOnClickListener(new View.OnClickListener() {
            final Calendar cal=Calendar.getInstance();
            int year=0,month=0,day=0;
            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year =cal.get(Calendar.YEAR);
                    month=cal.get(Calendar.MONTH);
                    day =cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker=new DatePickerDialog(ACC_View_Voucher.this, new DatePickerDialog.OnDateSetListener()
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
                        to_date.setText(date.toUpperCase());
//                        showResult_initList();
                        new Result_Task().execute();

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }
}
