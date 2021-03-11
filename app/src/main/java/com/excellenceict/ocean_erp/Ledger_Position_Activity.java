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
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.LedgerPositionFirstView_model;
import com.excellenceict.ocean_erp.Model.Ledger_position_FinalModel;
import com.excellenceict.ocean_erp.adapter.Ledger_position_Adapter;
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

public class Ledger_Position_Activity extends AppCompatActivity {
    private Connection connection;
    private Toolbar toolbar;
    private Dialog dailog;
    private BusyDialog busyDialog;
    private Context context;
    private ArrayList<LedgerPositionFirstView_model> dropdownitemList;
    private TextView spinnerText;
    private TextView text_formDate, text_toDate;
    private RadioGroup radioGroup;
    private String ledgrerVal;
    private String opBalVal = "1"; //yes value Default;
    private RecyclerView recyclerView;
    private ArrayList<Ledger_position_FinalModel> recycler_Items;
    private Ledger_position_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ledger_position_activity);
        context = Ledger_Position_Activity.this;
        toolbar = findViewById(R.id.ledger_ToolBarId);
        spinnerText = findViewById(R.id.ledger_spinner);
        text_formDate = findViewById(R.id.ledger_from_date);
        text_toDate = findViewById(R.id.ledger_to_date);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        recyclerView = findViewById(R.id.ledgerRecylerView);

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);
                switch (index) {
                    case 0: // first button
                        opBalVal = "0"; // No value.
                        new ResultTask().execute();
                        break;

                    case 1: // secondbutton
                        opBalVal = "1"; // Yes value.
                        new ResultTask().execute();
                        break;
                }
            }
        });

        if (NetworkHelpers.isNetworkAvailable(context)) {
            new ViewDropDownTask().execute();
            CurrentDate();
        } else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }

        spinnerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropdownitemList == null) {
                    Toast.makeText(context, "Check your internate connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(Ledger_Position_Activity.this);
                dailog.setContentView(R.layout.ledger_position_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();

                SearchView SpinnerSearchView = dailog.findViewById(R.id.spinner_search_ledger);
                ListView SpinnerListView = dailog.findViewById(R.id.spinnerItemListView);

                ImageView ledger_image = dailog.findViewById(R.id.spinner_icon_img1);
                ledger_image.setImageResource(R.drawable.ic_loan_chalan);
                SpinnerSearchView.setQueryHint("Search here...");
                SpinnerSearchView.onActionViewExpanded();
                SpinnerSearchView.setIconified(false);
                SpinnerSearchView.clearFocus();
                ImageView calcen_btn = dailog.findViewById(R.id.spinner_close_icon_img1);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dailog.dismiss();
                    }
                });

//               ...................use remove null values....................
                for (int i = 0; i < dropdownitemList.size(); i++) {
                    if (dropdownitemList.get(i).getHeadCode() == null) {
                        dropdownitemList.remove(i);
                    }
                }

                final ArrayAdapter<LedgerPositionFirstView_model> adapter = new ArrayAdapter<LedgerPositionFirstView_model>(
                        context, android.R.layout.simple_spinner_dropdown_item, dropdownitemList
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

                        ledgrerVal = adapter.getItem(position).getHeadCode();

                        if (NetworkHelpers.isNetworkAvailable(context)) {
                            new ResultTask().execute();
                            dateSetTO();
                            dateSetFROM();
                        } else {
                            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                        }

                        dailog.dismiss();
                        spinnerText.setText(adapter.getItem(position).getHeadCode());

                    }
                });
            }
        });
    }

    private void dateSetTO() {

        text_toDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int year = 0, month = 0, day = 0;

            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                        text_toDate.setText(date.toUpperCase());

                        new ResultTask().execute();

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
        text_formDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int year = 0, month = 0, day = 0;

            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                        text_formDate.setText(date.toUpperCase());
//                        new Chalan_Report_Details_Activity.Result_Task().execute();

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    private void dateSetFROM() {

        text_formDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int year = 0, month = 0, day = 0;

            @Override
            public void onClick(View v) {
                if (year == 0 || month == 0 || day == 0) {

                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                        text_formDate.setText(date.toUpperCase());
                        new ResultTask().execute();

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }

    private void CurrentDate() {
        String currentDate = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        text_formDate.setText(currentDate.toUpperCase());
        text_toDate.setText(currentDate.toUpperCase());
    }

    private class ViewDropDownTask extends AsyncTask<Void, Void, Void> {
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
                    dropdownitemList = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select distinct V_HEAD_CODE\n" +
                            "from ACC_HEAD\n" +
                            "order by V_HEAD_CODE";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        dropdownitemList.add(new LedgerPositionFirstView_model(rs.getString(1)));
                        Log.d("value", "====1====" + rs.getString(1));
                    }

                    busyDialog.dismis();
                }
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

    private class ResultTask extends AsyncTask<Void, Void, Void> {
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
                    recycler_Items = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select V_HEAD_CODE,V_SUB_HEAD_NAME, \n" +
                            "CASE WHEN SUM(od)+SUM(d)>SUM(c) + SUM(oc) then\n" +
                            "SUM(od)+SUM(d)-(SUM(c) + SUM(oc))\n" +
                            "else\n" +
                            "SUM(oc)+SUM(c)-(SUM(d) + SUM(od))\n" +
                            "end bal, \n" +
                            "replace(replace((CASE WHEN SUM(d) + SUM(od) >  SUM(C) + SUM(oc)\n" +
                            "      THEN ' (Dr)'\n" +
                            "   WHEN SUM(d) + SUM(od) <  SUM(c) + SUM(oc)\n" +
                            "      THEN ' (Cr)'\n" +
                            "   ELSE ''\n" +
                            "   END),' (',''),')','') bal_sign\n" +
                            "from(\n" +
                            "    SELECT * FROM (select \n" +
                            "        h.V_HEAD_CODE,'['||h.V_SUB_HEAD_CODE||'] - '||h.V_SUB_HEAD_NAME V_SUB_HEAD_NAME,\n" +
                            "        SUM(decode('" + opBalVal + "',1,N_DR,0)) od, SUM(decode('" + opBalVal + "',1,N_CR,0)) oc,0 d,0 c\n" +
                            "    from ACC_HEAD h,ACC_BUDGET t\n" +
                            "        where h.V_SUB_HEAD_CODE = t.V_SUB_HEAD_CODE\n" +
                            "        and t.N_PROJECT_ID=1\n" +
                            "        and t.N_OPENING_BAL_FLAG =1\n" +
                            "        and 1='" + opBalVal + "'\n" +
                            "        and exists (SELECT h.V_SUB_HEAD_CODE FROM ACC_HEAD h WHERE h.V_SUB_HEAD_CODE=t.V_SUB_HEAD_CODE and V_HEAD_CODE='" + ledgrerVal + "')\n" +
                            "    group by h.V_HEAD_CODE , h.V_SUB_HEAD_CODE , h.V_SUB_HEAD_NAME \n" +
                            ")\n" +
                            "UNION\n" +
                            "SELECT * FROM (\n" +
                            "    select h.V_HEAD_CODE,'['||h.V_SUB_HEAD_CODE||'] - '||h.V_SUB_HEAD_NAME V_SUB_HEAD_NAME,sum(decode('" + opBalVal + "',1,N_DR,0)) od,sum(decode('" + opBalVal + "',1,N_CR,0)) oc,0 d,0 c\n" +
                            "    from ACC_HEAD h, VW_ACC_V_LED t\n" +
                            "    where vr_dt < to_date('" + text_formDate.getText() + "','MON DD,RRRR')\n" +
                            "    and h.V_SUB_HEAD_CODE = t.SUB_HEAD\n" +
                            "    and exists (SELECT h.V_SUB_HEAD_CODE FROM ACC_HEAD h WHERE h.V_SUB_HEAD_CODE=t.SUB_HEAD and V_HEAD_CODE='" + ledgrerVal + "')\n" +
                            "    and h.N_PROJECT_ID=1 \n" +
                            "    and t.SUB_HEAD<>t.R_HEAD_NAME\n" +
                            "    group by h.V_HEAD_CODE , h.V_SUB_HEAD_CODE , h.V_SUB_HEAD_NAME \n" +
                            ")\n" +
                            "union\n" +
                            "SELECT * FROM (\n" +
                            "    select h.V_HEAD_CODE,'['||h.V_SUB_HEAD_CODE||'] - '||h.V_SUB_HEAD_NAME V_SUB_HEAD_NAME,0 od,0 oc,sum(N_DR) d,sum(N_CR) c\n" +
                            "    from ACC_HEAD h, VW_ACC_V_LED t\n" +
                            "    where vr_dt between to_date('" + text_formDate.getText() + "','MON DD,RRRR') AND to_date('" + text_toDate.getText() + "','MON DD,RRRR')\n" +
                            "    and h.V_SUB_HEAD_CODE = t.SUB_HEAD\n" +
                            "    and exists (SELECT h.V_SUB_HEAD_CODE FROM ACC_HEAD h WHERE h.V_SUB_HEAD_CODE=t.SUB_HEAD and V_HEAD_CODE='" + ledgrerVal + "')\n" +
                            "   and h.N_PROJECT_ID=1\n" +
                            "and t.SUB_HEAD<>t.R_HEAD_NAME\n" +
                            "    group by h.V_HEAD_CODE , h.V_SUB_HEAD_CODE , h.V_SUB_HEAD_NAME \n" +
                            ")\n" +
                            ")\n" +
                            "group by V_HEAD_CODE, V_SUB_HEAD_NAME \n" +
                            "ORDER BY V_HEAD_CODE, V_SUB_HEAD_NAME";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        recycler_Items.add(new Ledger_position_FinalModel(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getString(4)));
                        Log.d("value1", "====1====" + rs.getString(1));
                        Log.d("value2", "====2====" + rs.getString(2));
                        Log.d("value3", "====3====" + rs.getString(3));
                        Log.d("value3", "====3====" + rs.getString(4));
                    }

                    busyDialog.dismis();
                }
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
            adapter = new Ledger_position_Adapter(recycler_Items);
            recyclerView.setAdapter(adapter);
            busyDialog.dismis();
        }
    }

}
