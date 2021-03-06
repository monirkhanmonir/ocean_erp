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

import com.excellenceict.ocean_erp.Model.Billinvoice_Customer_Model;
import com.excellenceict.ocean_erp.Model.Billinvoice_Group_Model;
import com.excellenceict.ocean_erp.Model.Billinvoice_item_Model;
import com.excellenceict.ocean_erp.Model.Chalan_Report_Summary_Model;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.Helper;
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

public class Chalan_Report_Summary_Activity extends AppCompatActivity {
    private Connection connection;
    private TextView text_formDate, text_toDate;

    private String groupItem_id = "-1";
    private String item_id = "-1";
    private String customer_id = "-1";
    private String customer_contact = "-1";

    private ListView listView;
    private ArrayList<Billinvoice_Group_Model> groupNameList;
    private ArrayList<Billinvoice_Customer_Model> customerNameList;
    private ArrayList<Billinvoice_item_Model> itemNameList;
    private ArrayList<Chalan_Report_Summary_Model> resultList;
    private Chalan_Report_SummaryResult_Customadapter result_adapter;

    private Dialog chalan_report_customer_dailog;
    private TextView j_chalan_customer_dropdown_btn, j_chalanReportSummary_Group_spinner, j_chalanReportSummary_itemName_spinner;

    private BusyDialog busyDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chalan_report_summary_activity);

        text_formDate = findViewById(R.id.chalanReportSummary_from_date);
        text_toDate = findViewById(R.id.chalanReportSummary_to_date);
        listView = findViewById(R.id.chalanReportSummary_result_listView);

        j_chalan_customer_dropdown_btn = findViewById(R.id.chalan_customer_dropdown_btn);
        j_chalanReportSummary_Group_spinner = findViewById(R.id.chalanReportSummary_Group_spinner);
        j_chalanReportSummary_itemName_spinner = findViewById(R.id.chalanReportSummary_itemName_spinner);

        context = Chalan_Report_Summary_Activity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.chalan_Report_summeryToolBarId);
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
        dateSetFROM();
        dateSetTO();

        j_chalan_customer_dropdown_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerNameList == null) {
                    Toast.makeText(Chalan_Report_Summary_Activity.this, "Check your internate connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                chalan_report_customer_dailog = new Dialog(Chalan_Report_Summary_Activity.this);
                chalan_report_customer_dailog.setContentView(R.layout.manufacture_dailog_spinner);
                chalan_report_customer_dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                chalan_report_customer_dailog.show();


                SearchView customerSpinnerSearchView = chalan_report_customer_dailog.findViewById(R.id.spinner_search);
                ListView customerSpinnerListView = chalan_report_customer_dailog.findViewById(R.id.spinnerItemList);

                ImageView current_stock_image = chalan_report_customer_dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_chalan_report);
                customerSpinnerSearchView.setQueryHint("Search here...");
                customerSpinnerSearchView.onActionViewExpanded();
                customerSpinnerSearchView.setIconified(false);
                customerSpinnerSearchView.clearFocus();

                ImageView calcen_btn = chalan_report_customer_dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chalan_report_customer_dailog.dismiss();
                    }
                });

                final ArrayAdapter<Billinvoice_Customer_Model> customerAdapter = new ArrayAdapter<Billinvoice_Customer_Model>(
                        Chalan_Report_Summary_Activity.this, android.R.layout.simple_list_item_1, customerNameList
                );

                customerSpinnerListView.setAdapter(customerAdapter);
                customerSpinnerListView.setHeaderDividersEnabled(true);

                customerSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        customerAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                customerSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (customerAdapter.getItem(position).getCustomer_Name().equals("<< Select Customer >>")) {
                            Toast.makeText(Chalan_Report_Summary_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            j_chalanReportSummary_Group_spinner.setText("Select Group");
                            j_chalanReportSummary_itemName_spinner.setText("Select Item Name");

                            groupNameList = null;
                            itemNameList = null;

                            groupItem_id = "-1";
                            item_id = "-1";

                            customer_id = customerAdapter.getItem(position).getCustomer_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new ChalanReportSummaryGroupTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            chalan_report_customer_dailog.dismiss();
                            j_chalan_customer_dropdown_btn.setText(customerAdapter.getItem(position).getCustomer_Name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        j_chalanReportSummary_Group_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupNameList == null) {
                    Toast.makeText(Chalan_Report_Summary_Activity.this, "Please select customer name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                chalan_report_customer_dailog = new Dialog(Chalan_Report_Summary_Activity.this);
                chalan_report_customer_dailog.setContentView(R.layout.manufacture_dailog_spinner);
                chalan_report_customer_dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                chalan_report_customer_dailog.show();


                SearchView groupSpinnerSearchView = chalan_report_customer_dailog.findViewById(R.id.spinner_search);
                ListView groupSpinnerListView = chalan_report_customer_dailog.findViewById(R.id.spinnerItemList);

                ImageView chalan_image = chalan_report_customer_dailog.findViewById(R.id.spinner_icon_img);
                chalan_image.setImageResource(R.drawable.ic_chalan_report);
                groupSpinnerSearchView.setQueryHint("Search here...");
                groupSpinnerSearchView.onActionViewExpanded();
                groupSpinnerSearchView.setIconified(false);
                groupSpinnerSearchView.clearFocus();

                ImageView calcen_btn = chalan_report_customer_dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chalan_report_customer_dailog.dismiss();
                    }
                });

                final ArrayAdapter<Billinvoice_Group_Model> groupAdapter = new ArrayAdapter<Billinvoice_Group_Model>(
                        Chalan_Report_Summary_Activity.this, android.R.layout.simple_list_item_1, groupNameList
                );

                groupSpinnerListView.setAdapter(groupAdapter);
                groupSpinnerListView.setHeaderDividersEnabled(true);

                groupSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        groupAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                groupSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (groupAdapter.getItem(position).getItemGroup_Name().equals("<< Select Group >>")) {
                            Toast.makeText(Chalan_Report_Summary_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            j_chalanReportSummary_itemName_spinner.setText("Select Item Name");

                            itemNameList = null;

                            groupItem_id = groupAdapter.getItem(position).getItemGroup_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new ChalanReportSummaryItemNameTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            chalan_report_customer_dailog.dismiss();
                            j_chalanReportSummary_Group_spinner.setText(groupAdapter.getItem(position).getItemGroup_Name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        j_chalanReportSummary_itemName_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemNameList == null) {
                    Toast.makeText(Chalan_Report_Summary_Activity.this, "Select Item Group.", Toast.LENGTH_SHORT).show();
                    return;
                }

                chalan_report_customer_dailog = new Dialog(Chalan_Report_Summary_Activity.this);
                chalan_report_customer_dailog.setContentView(R.layout.manufacture_dailog_spinner);
                chalan_report_customer_dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                chalan_report_customer_dailog.show();


                SearchView itemNameSpinnerSearchView = chalan_report_customer_dailog.findViewById(R.id.spinner_search);
                ListView itemNameSpinnerListView = chalan_report_customer_dailog.findViewById(R.id.spinnerItemList);

                ImageView chalan_image = chalan_report_customer_dailog.findViewById(R.id.spinner_icon_img);
                chalan_image.setImageResource(R.drawable.ic_chalan_report);
                itemNameSpinnerSearchView.setQueryHint("Search here...");
                itemNameSpinnerSearchView.onActionViewExpanded();
                itemNameSpinnerSearchView.setIconified(false);
                itemNameSpinnerSearchView.clearFocus();

                ImageView calcen_btn = chalan_report_customer_dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chalan_report_customer_dailog.dismiss();
                    }
                });

                final ArrayAdapter<Billinvoice_item_Model> itemNameAdapter = new ArrayAdapter<Billinvoice_item_Model>(
                        Chalan_Report_Summary_Activity.this, android.R.layout.simple_list_item_1, itemNameList
                );

                itemNameSpinnerListView.setAdapter(itemNameAdapter);

                itemNameSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        itemNameAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                itemNameSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (itemNameAdapter.getItem(position).getItem_name().equals("<< Select Item Name >>")) {
                            Toast.makeText(Chalan_Report_Summary_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            item_id = itemNameAdapter.getItem(position).getItem_id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new Result_Task().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            chalan_report_customer_dailog.dismiss();
                            j_chalanReportSummary_itemName_spinner.setText(itemNameAdapter.getItem(position).getItem_name());
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

                DatePickerDialog mDatePicker = new DatePickerDialog(Chalan_Report_Summary_Activity.this, new DatePickerDialog.OnDateSetListener() {
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

                DatePickerDialog mDatePicker = new DatePickerDialog(Chalan_Report_Summary_Activity.this, new DatePickerDialog.OnDateSetListener() {
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

    private class CustomerName_Task extends AsyncTask<Void, Void, ArrayList<Billinvoice_Customer_Model>> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected ArrayList<Billinvoice_Customer_Model> doInBackground(Void... voids) {
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
                        customerNameList.add(new Billinvoice_Customer_Model(rs.getString(1), rs.getString(2)));
                        Log.d("value1", "======Customer====1===========" + rs.getString(1));
                        Log.d("value2", "======Customer====2===========" + rs.getString(2));
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
        protected void onPostExecute(ArrayList<Billinvoice_Customer_Model> billinvoice_customer_entities) {
            new Result_Task().execute();
            busyDialog.dismis();
        }
    }

    private class Result_Task extends AsyncTask<Void, Void, ArrayList<Chalan_Report_Summary_Model>> {

        @Override
        protected void onPreExecute() {

            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected ArrayList<Chalan_Report_Summary_Model> doInBackground(Void... voids) {
            resultList = new ArrayList<Chalan_Report_Summary_Model>();
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                if (connection != null) {
                    resultList = new ArrayList<Chalan_Report_Summary_Model>();
                    Statement stmt = connection.createStatement();
                    String query = "Select I.Item_Name,Sum(Fnc$Convert_Mu(C.ITEM_ID,C.chalan_Qty,C.Mu_Id)) Sell_Qty,u.MU_NAME,\n" +
                            "Sum((Nvl(C.chalan_RATE,0)*Nvl(C.chalan_QTY, 0))+Nvl(C.VAT_AMT,0)-(Nvl(C.DISCOUNT_AMOUNT,0))) sale_amount\n" +
                            "From vw_Inv_chalanMst m, vw_Inv_chalanChd C, Inv_Item I,inv_itemgroup ig,inv_mu u, inv_contact r\n" +
                            "Where m.chalan_Id = c.chalan_Id\n" +
                            "And C.ITEM_ID = I.ITEM_ID\n" +
                            "and  I.MU_ID=u.MU_ID\n" +
                            "And I.ITEMGROUP_ID = ig.ITEMGROUP_ID\n" +
                            "and m.CONTACT_ID=r.CONTACT_ID\n" +
                            "and c.chalan_QTY>0 and c.chalan_RATE>0\n" +
                            "And ('" + customer_id + "' = -1 or m.CONTACT_ID = '" + customer_id + "')\n" +
                            "And ('" + groupItem_id + "' = -1 or ig.ITEMGROUP_ID = '" + groupItem_id + "')\n" +
                            "And ('" + item_id + "' = -1 or C.item_Id ='" + item_id + "')\n" +
                            "AND M.chalan_DATE BETWEEN to_date('" + text_formDate.getText() + "','MON DD,RRRR') AND to_date('" + text_toDate.getText() + "','MON DD,RRRR')\n" +
                            "Group By C.ITEM_ID,I.ITEM_Name,i.UD_NO,ig.ITEMGROUP_NAME,u.MU_NAME\n" +
                            "Order By I.Item_Name";

                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        resultList.add(new Chalan_Report_Summary_Model(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
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
        protected void onPostExecute(ArrayList<Chalan_Report_Summary_Model> chalan_report_summary_entities) {
            busyDialog.dismis();
            result_adapter = new Chalan_Report_SummaryResult_Customadapter(getApplication(), resultList);
            listView.setAdapter(result_adapter);
            Helper.getListViewSize(listView);
        }
    }

    private class ChalanReportSummaryGroupTask extends AsyncTask<Void, Void, Void> {

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
                        groupNameList.add(new Billinvoice_Group_Model(rs.getString(1), rs.getString(2)));

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
            new Result_Task().execute();
            busyDialog.dismis();
        }
    }

    private class ChalanReportSummaryItemNameTask extends AsyncTask<Void, Void, Void> {

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
                        itemNameList.add(new Billinvoice_item_Model(rs.getString(1), rs.getString(2)));
                        Log.d("value1", "======Item====1===========" + rs.getString(1));
                        Log.d("value2", "======Item====2===========" + rs.getString(2));

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
            new Result_Task().execute();
            busyDialog.dismis();
        }
    }

}
