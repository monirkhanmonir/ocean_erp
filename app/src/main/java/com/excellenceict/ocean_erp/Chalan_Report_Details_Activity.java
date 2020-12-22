package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class Chalan_Report_Details_Activity extends AppCompatActivity {
    private Connection connection;
    TextView text_formDate, text_toDate;
    String groupItem_id, item_id, customer_id, customer_contact;
    private ListView listView;
    private ArrayList<Billinvoice_Group_Entity> groupNameList;
    private ArrayList<Billinvoice_Customer_Entity> customerNameList;
    private ArrayList<Billinvoice_item_Entity> itemNameList;
    private ArrayList<SalesReport_DetailsResult_Entity> resultList;
    private Sales_Report_DetailsResult_Customadapter result_adapter;

    private Dialog dailog;
    private TextView j_chalanReportDetails_customer_spinner, j_chalanReportDetails_Group_spinner,
            j_chalanReportDetails_item_spinner;
    private BusyDialog busyDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalan_report_details);

        text_formDate = findViewById(R.id.chalanReportDetails_from_date);
        text_toDate = findViewById(R.id.chalanReportDetails_to_date);
        listView = findViewById(R.id.chalanReportDetails_result_listView);

        j_chalanReportDetails_customer_spinner = findViewById(R.id.chalanReportDetails_customer_spinner);
        j_chalanReportDetails_Group_spinner = findViewById(R.id.chalanReportDetails_Group_spinner);
        j_chalanReportDetails_item_spinner = findViewById(R.id.chalanReportDetails_item_spinner);

        context = Chalan_Report_Details_Activity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.chalan_report_detailsToolBarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (NetworkHelpers.isNetworkAvailable(context)) {
            new CustomerName_Task().execute();
        } else {
            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
        }


        CurrentDate();

        j_chalanReportDetails_customer_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerNameList == null) {
                    Toast.makeText(Chalan_Report_Details_Activity.this, "Check your internate connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(Chalan_Report_Details_Activity.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();

                SearchView SpinnerSearchView = dailog.findViewById(R.id.spinner_search);
                ListView SpinnerListView = dailog.findViewById(R.id.spinnerItemList);

                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_loan_chalan);
                SpinnerSearchView.setQueryHint("Search here...");
                SpinnerSearchView.onActionViewExpanded();
                SpinnerSearchView.setIconified(false);
                SpinnerSearchView.clearFocus();

                final ArrayAdapter<Billinvoice_Customer_Entity> adapter = new ArrayAdapter<Billinvoice_Customer_Entity>(
                        Chalan_Report_Details_Activity.this, android.R.layout.simple_list_item_1, customerNameList
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
                        if (adapter.getItem(position).getCustomer_Name().equals("<< Select Customer >>")) {
                            Toast.makeText(Chalan_Report_Details_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            j_chalanReportDetails_Group_spinner.setText("Select Group");
                            j_chalanReportDetails_item_spinner.setText("Select Item Name");

                            groupNameList = null;
                            itemNameList = null;

                            customer_id = adapter.getItem(position).getCustomer_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new ChalanReportGroupNameTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            dailog.dismiss();
                            j_chalanReportDetails_customer_spinner.setText(adapter.getItem(position).getCustomer_Name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        j_chalanReportDetails_Group_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupNameList == null) {
                    Toast.makeText(Chalan_Report_Details_Activity.this, "Select Customer", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(Chalan_Report_Details_Activity.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();

                SearchView SpinnerSearchView = dailog.findViewById(R.id.spinner_search);
                ListView SpinnerListView = dailog.findViewById(R.id.spinnerItemList);

                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_loan_chalan);
                SpinnerSearchView.setQueryHint("Search here...");
                SpinnerSearchView.onActionViewExpanded();
                SpinnerSearchView.setIconified(false);
                SpinnerSearchView.clearFocus();

                ImageView calcen_btn = dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dailog.dismiss();
                    }
                });

                final ArrayAdapter<Billinvoice_Group_Entity> adapter = new ArrayAdapter<Billinvoice_Group_Entity>(
                        Chalan_Report_Details_Activity.this, android.R.layout.simple_list_item_1, groupNameList
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
                        if (adapter.getItem(position).getItemGroup_Name().equals("<< Select Group >>")) {
                            Toast.makeText(Chalan_Report_Details_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            j_chalanReportDetails_item_spinner.setText("Select Item Name");

                            itemNameList = null;

                            groupItem_id = adapter.getItem(position).getItemGroup_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new ChalanReportItemNameTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            dailog.dismiss();
                            j_chalanReportDetails_Group_spinner.setText(adapter.getItem(position).getItemGroup_Name());
                        }
                    }
                });
            }
        });

        j_chalanReportDetails_item_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemNameList == null) {
                    Toast.makeText(Chalan_Report_Details_Activity.this, "Select Customer", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(Chalan_Report_Details_Activity.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();

                SearchView SpinnerSearchView = dailog.findViewById(R.id.spinner_search);
                ListView SpinnerListView = dailog.findViewById(R.id.spinnerItemList);

                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_loan_chalan);
                SpinnerSearchView.setQueryHint("Search here...");
                SpinnerSearchView.onActionViewExpanded();
                SpinnerSearchView.setIconified(false);
                SpinnerSearchView.clearFocus();

                ImageView calcen_btn = dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dailog.dismiss();
                    }
                });

                final ArrayAdapter<Billinvoice_item_Entity> adapter = new ArrayAdapter<Billinvoice_item_Entity>(
                        Chalan_Report_Details_Activity.this, android.R.layout.simple_list_item_1, itemNameList
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
                        if (adapter.getItem(position).getItem_name().equals("<< Select Item Name >>")) {
                            Toast.makeText(Chalan_Report_Details_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            item_id = adapter.getItem(position).getItem_id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new Result_Task().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            dateSetFROM();
                            dateSetTO();
                            dailog.dismiss();
                            j_chalanReportDetails_item_spinner.setText(adapter.getItem(position).getItem_name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
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

                DatePickerDialog mDatePicker = new DatePickerDialog(Chalan_Report_Details_Activity.this, new DatePickerDialog.OnDateSetListener() {
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
//                        showResult_initList();
                        new Result_Task().execute();

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

                DatePickerDialog mDatePicker = new DatePickerDialog(Chalan_Report_Details_Activity.this, new DatePickerDialog.OnDateSetListener() {
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
                        new Result_Task().execute();

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


    private class CustomerName_Task extends AsyncTask<Void, Void, ArrayList<Billinvoice_Customer_Entity>> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected ArrayList<Billinvoice_Customer_Entity> doInBackground(Void... voids) {
            customerNameList = new ArrayList<>();

            try {

                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "================salesReport Customer==Connected===========");
                if (connection != null) {
                    customerNameList = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select CONTACT_ID,CONTACT_NAME\n" +
                            "from(\n" +
                            "select 1 sl,-1 CONTACT_ID,'<< Select Customer >>' CONTACT_NAME\n" +
                            "from dual\n" +
                            "union all\n" +
                            "SELECT 2 sl, CONTACT_ID, CONTACT_NAME\n" +
                            "FROM INV_CONTACT\n" +
                            ")\n" +
                            "order by sl,CONTACT_NAME";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        customerNameList.add(new Billinvoice_Customer_Entity(rs.getString(1), rs.getString(2)));
                    }
                    busyDialog.dismis();
                }
                connection.close();
            } catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }

            return customerNameList;
        }

        @Override
        protected void onPostExecute(ArrayList<Billinvoice_Customer_Entity> billinvoice_customer_entities) {
            busyDialog.dismis();
        }
    }

    private class Result_Task extends AsyncTask<Void, Void, ArrayList<SalesReport_DetailsResult_Entity>> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected ArrayList<SalesReport_DetailsResult_Entity> doInBackground(Void... voids) {
            resultList = new ArrayList<SalesReport_DetailsResult_Entity>();

            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                if (connection != null) {
                    resultList = new ArrayList<SalesReport_DetailsResult_Entity>();
                    Statement stmt = connection.createStatement();
                    String query = "Select I.Item_Name,Sum(Fnc$Convert_Mu(C.ITEM_ID,C.chalan_Qty,C.Mu_Id)) Sell_Qty,u.MU_NAME,\n" +
                            "Fnc$ContactName(M.Contact_Id) Contact_Name,m.chalan_DATE, m.chalan_NO,\n" +
                            "sum((Nvl(C.chalan_RATE,0)*Nvl(C.chalan_QTY, 0))+Nvl(C.VAT_AMT,0)-(Nvl(C.DISCOUNT_AMOUNT,0))) sales_amount\n" +
                            "From vw_Inv_chalanMst M,vw_Inv_chalanChd C,Inv_Item I,inv_itemgroup ig,Inv_MU U, inv_contact r\n" +
                            "Where M.chalan_ID = C.chalan_ID\n" +
                            "And C.ITEM_ID = I.ITEM_ID\n" +
                            "And c.MU_ID = U.MU_ID\n" +
                            "And I.ITEMGROUP_ID(+) = ig.ITEMGROUP_ID\n" +
                            "and m.CONTACT_ID=r.CONTACT_ID\n" +
                            "and c.chalan_QTY>0 and c.chalan_RATE>0\n" +
                            "And ('" + customer_id + "' = -1 or m.CONTACT_ID = '" + customer_id + "')\n" +
                            "And ('" + groupItem_id + "' = -1 or ig.ITEMGROUP_ID = '" + groupItem_id + "')\n" +
                            "And ('" + item_id + "' = -1 or C.item_Id ='" + item_id + "')\n" +
                            "AND M.chalan_DATE BETWEEN to_date('" + text_formDate.getText() + "','MON DD,RRRR') AND to_date('" + text_toDate.getText() + "','MON DD,RRRR')\n" +
                            "group by I.Item_Name,u.MU_NAME,Fnc$ContactName(M.Contact_Id),m.chalan_NO, m.chalan_DATE\n" +
                            "Order By m.chalan_NO, m.chalan_DATE, Fnc$ContactName(M.Contact_Id)";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        resultList.add(new SalesReport_DetailsResult_Entity(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
                    }
                    busyDialog.dismis();
                }
                connection.close();
            } catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }
            return resultList;
        }

        @Override
        protected void onPostExecute(ArrayList<SalesReport_DetailsResult_Entity> salesReport_detailsResult_entities) {
            super.onPostExecute(salesReport_detailsResult_entities);
            result_adapter = new Sales_Report_DetailsResult_Customadapter(getApplication(), resultList);
            listView.setAdapter(result_adapter);
            busyDialog.dismis();
        }
    }

    private class ChalanReportGroupNameTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "================salesReport Group==Connected===========");
                if (connection != null) {
                    groupNameList = new ArrayList<>();
                    Statement stmt = connection.createStatement();
                    String query = "select ITEMGROUP_ID,ITEMGROUP_NAME\n" +
                            "from(\n" +
                            "select 1 sl,-1 ITEMGROUP_ID,'<< Select Group >>' ITEMGROUP_NAME\n" +
                            "from dual\n" +
                            "union all\n" +
                            "SELECT 2 sl, g.ITEMGROUP_ID, g.ITEMGROUP_NAME\n" +
                            "FROM INV_ITEMGROUP g\n" +
                            ")\n" +
                            "order by sl,ITEMGROUP_NAME";

                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        groupNameList.add(new Billinvoice_Group_Entity(rs.getString(1), rs.getString(2)));
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

    private class ChalanReportItemNameTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "================salesReport Item==Connected===========");
                if (connection != null) {
                    itemNameList = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select ITEM_ID,ITEM_NAME\n" +
                            "from(\n" +
                            "select 1 sl,-1 ITEM_ID,'<< Select Item Name >>' ITEM_NAME\n" +
                            "from dual\n" +
                            "union all\n" +
                            "SELECT 2 sl, ITEM_ID, ITEM_NAME||' ('||UD_NO||')' ITEM_NAME\n" +
                            "FROM INV_ITEM\n" +
                            "WHERE ('" + groupItem_id + "'=-1 or ITEMGROUP_ID='" + groupItem_id + "')\n" +
                            ")\n" +
                            "order by sl,ITEM_NAME";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        itemNameList.add(new Billinvoice_item_Entity(rs.getString(1), rs.getString(2)));
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

}