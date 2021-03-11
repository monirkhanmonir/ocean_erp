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

import com.excellenceict.ocean_erp.adapter.CustomACCVoucherResultAdapter;
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

public class View_Voucher_Activity extends AppCompatActivity {
    private Connection connection;
    private ArrayList<View_Voucher_Entity> transactionList;
    private ArrayList<View_Voucher_Result_Entity> voucher_Result;
    private TextView from_Date, to_date;
    // private EditText referenceTextView;
    private String tr_typeVal = "-1";
    //private Button button;
    private RecyclerView recyclerView;

    private Dialog dialog;
    private TextView j_acc_view_voucher_spinner;
    private CustomACCVoucherResultAdapter voucherListadapter;
    private SearchView j_viewVoucher_Reference_search;

    private BusyDialog busyDialog;
    private Context context;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view__voucher_activity);

        from_Date = findViewById(R.id.viewVoucher_from_date);
        to_date = findViewById(R.id.viewVoucher_to_date);
        recyclerView = findViewById(R.id.voucher_result_listView);

        j_acc_view_voucher_spinner = findViewById(R.id.acc_view_voucher_spinner);
        j_viewVoucher_Reference_search = findViewById(R.id.viewVoucher_Reference_search);
        toolbar = findViewById(R.id.accViewVoucherToolBarId);

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


        j_viewVoucher_Reference_search.setActivated(true);
        j_viewVoucher_Reference_search.setQueryHint("Search Ref/Note...");
        j_viewVoucher_Reference_search.onActionViewExpanded();
        j_viewVoucher_Reference_search.setIconified(false);
        j_viewVoucher_Reference_search.clearFocus();
        context = View_Voucher_Activity.this;

        new Transaction_Task().execute();
//        currentDate();

        j_acc_view_voucher_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (transactionList == null) {
                    Toast.makeText(View_Voucher_Activity.this, "Check your internate connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog = new Dialog(View_Voucher_Activity.this);
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

                final ArrayAdapter<View_Voucher_Entity> adapter = new ArrayAdapter<View_Voucher_Entity>(
                        View_Voucher_Activity.this, android.R.layout.simple_spinner_dropdown_item, transactionList
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
                        if (adapter.getItem(position).getTr_type_val().equals("<< Select Transaction Type >>")) {
                            Toast.makeText(View_Voucher_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            tr_typeVal = adapter.getItem(position).getTr_type_val();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new Result_Task().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                            j_acc_view_voucher_spinner.setText(adapter.getItem(position).getTr_type());
                        }

                    }
                });
            }
        });


        j_viewVoucher_Reference_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("CLASS", "SEARCH DATA>>>" + newText);
                if (voucherListadapter != null) {
                    voucherListadapter.getFilter().filter(newText);

                } else {
//                    Toast.makeText(context, "please select item", Toast.LENGTH_SHORT).show();

                }

                return false;
            }
        });
    }


    private class Transaction_Task extends AsyncTask<Void, Void, ArrayList<View_Voucher_Entity>> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected ArrayList<View_Voucher_Entity> doInBackground(Void... voids) {

            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "================View Voucher==Connected===========");
                if (connection != null) {
                    transactionList = new ArrayList<>();
                    Statement stmt = connection.createStatement();
                    String query = "select tr_type, tr_type_val\n" +
                            "from (\n" +
                            "Select -1 sl, '<< Select Transaction Type >>' tr_type, -1 tr_type_val\n" +
                            "from dual\n" +
                            "union all\n" +
                            "select sl,tr_type, tr_type_val from ACC_TR_TYPE\n" +
                            ")\n" +
                            "order by sl";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        transactionList.add(new View_Voucher_Entity(rs.getString(1), rs.getString(2)));
                        Log.d("value1", "======view voucher====1===========" + rs.getString(1));
                        Log.d("value2", "======view voucher=====2===========" + rs.getString(2));

                    }
                }
                busyDialog.dismis();
                connection.close();

            } catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }

            return transactionList;
        }

        @Override
        protected void onPostExecute(ArrayList<View_Voucher_Entity> view_voucher_entities) {
            currentDate();
            dateSetFROM();
            dateSetTO();
            new Result_Task().execute();
            busyDialog.dismis();
        }
    }

    private class Result_Task extends AsyncTask<Void, Void, ArrayList<View_Voucher_Result_Entity>> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected ArrayList<View_Voucher_Result_Entity> doInBackground(Void... voids) {
            voucher_Result = new ArrayList<View_Voucher_Result_Entity>();

            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                if (connection != null) {
                    voucher_Result = new ArrayList<View_Voucher_Result_Entity>();

                    Statement stmt = connection.createStatement();
                    String query = "select to_char(D_VOUCHER_DT,'MON DD,RRRR') D_VOUCHER_DT, v.V_VOUCHER_NO, V_REF_VOUCHER, TR_TYPE,\n" +
                            "decode(N_VOUCHER_TYPE,1,'Debit',2,'Credit','Journal') VOUCHER_TYPE,\n" +
                            "V_NARRATION, sum(N_DR) amount\n" +
                            "from VW_ACC_VOUCHER_MST v,VW_ACC_VOUCHER_DTL c, ACC_TR_TYPE t\n" +
                            "where v.N_TR_TYPE = t.TR_TYPE_VAL\n" +
                            "and v.V_VOUCHER_NO = c.V_VOUCHER_NO\n" +
                            "and v.n_approved_flag =1\n" +
                            "and ('" + tr_typeVal + "' = -1 or N_TR_TYPE= '" + tr_typeVal + "')\n" +
                            "AND D_VOUCHER_DT BETWEEN to_date('" + from_Date.getText() + "','MON DD,RRRR') AND to_date('" + to_date.getText() + "','MON DD,RRRR')\n" +
                            "group by D_VOUCHER_DT, v.V_VOUCHER_NO, V_REF_VOUCHER, TR_TYPE,N_VOUCHER_TYPE,V_NARRATION\n" +
                            "order by v.D_VOUCHER_DT desc,V_REF_VOUCHER desc";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        voucher_Result.add(new View_Voucher_Result_Entity(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
                    }
                    busyDialog.dismis();
                }
                connection.close();

            } catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }
            return voucher_Result;
        }

        @Override
        protected void onPostExecute(ArrayList<View_Voucher_Result_Entity> bill_result_entities) {
            super.onPostExecute(bill_result_entities);

            recyclerView.setLayoutManager(new LinearLayoutManager(View_Voucher_Activity.this));
            recyclerView.addItemDecoration(new DividerItemDecoration(View_Voucher_Activity.this, DividerItemDecoration.VERTICAL));
            voucherListadapter = new CustomACCVoucherResultAdapter(voucher_Result);
            recyclerView.setAdapter(voucherListadapter);

            busyDialog.dismis();
        }

    }

    private void currentDate() {
        String currentDate = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        from_Date.setText(currentDate.toUpperCase());
        to_date.setText(currentDate.toUpperCase());
    }

    private void dateSetFROM() {

        from_Date.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int year = 0, month = 0, day = 0;

            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker = new DatePickerDialog(View_Voucher_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
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

                        if (NetworkHelpers.isNetworkAvailable(context)) {
                            new Result_Task().execute();
                        } else {
                            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                        }


                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    private void dateSetTO() {

        to_date.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int year = 0, month = 0, day = 0;

            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker = new DatePickerDialog(View_Voucher_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
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
