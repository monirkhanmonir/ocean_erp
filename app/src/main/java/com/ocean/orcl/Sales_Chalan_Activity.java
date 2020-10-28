package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ocean.orcl.util.BusyDialog;
import com.ocean.orcl.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Sales_Chalan_Activity extends AppCompatActivity {
    private Connection connection;
    TextView text_formDate, text_toDate;
    String groupItem_id, item_id, customer_id, customer_contact;
    private ListView listView;
    private ArrayList<Billinvoice_Group_Entity> groupNameList;
    private ArrayList<Billinvoice_Customer_Entity> customerNameList;
    private ArrayList<Billinvoice_item_Entity> itemNameList;
    private ArrayList<SalesChalan_Result_Entity> resultList;
    private SalesChalan_Results_CustomAdapter result_adapter;

    private TextView j_salesChalan_customer_spinner, j_salesChalan_Group_spinner, j_salesChalan_item_spinner;
    private Dialog sales_chalan_dailog;

    private BusyDialog busyDialog;
    private Context context;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales__chalan);
        text_formDate = findViewById(R.id.salesChalan_from_date);
        text_toDate = findViewById(R.id.salesChalan_to_date);
        listView = findViewById(R.id.salesChalan_result_listView);

        j_salesChalan_customer_spinner = findViewById(R.id.salesChalan_customer_spinner);
        j_salesChalan_Group_spinner = findViewById(R.id.salesChalan_Group_spinner);
        j_salesChalan_item_spinner = findViewById(R.id.salesChalan_item_spinner);

        context = Sales_Chalan_Activity.this;
        handler = new Handler();

        new CustomerName_Task().execute();
        currentDate();
        dateSetFROM();
        dateSetTO();


        j_salesChalan_customer_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerNameList == null) {
                    Toast.makeText(Sales_Chalan_Activity.this, "Check your internate connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                sales_chalan_dailog = new Dialog(Sales_Chalan_Activity.this);
                sales_chalan_dailog.setContentView(R.layout.manufacture_dailog_spinner);
                sales_chalan_dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                sales_chalan_dailog.show();


                SearchView salesChalanSpinnerSearchView = sales_chalan_dailog.findViewById(R.id.spinner_search);
                ListView salesChalanSpinnerListView = sales_chalan_dailog.findViewById(R.id.spinnerItemList);

                ImageView sales_chalan_image = sales_chalan_dailog.findViewById(R.id.spinner_icon_img);
                sales_chalan_image.setImageResource(R.drawable.ic_selse_chalan);
                salesChalanSpinnerSearchView.setQueryHint("Search here...");
                salesChalanSpinnerSearchView.onActionViewExpanded();
                salesChalanSpinnerSearchView.setIconified(false);
                salesChalanSpinnerSearchView.clearFocus();

                ImageView calcen_btn = sales_chalan_dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sales_chalan_dailog.dismiss();
                    }
                });

                final ArrayAdapter<Billinvoice_Customer_Entity> salesChalanAdapter = new ArrayAdapter<Billinvoice_Customer_Entity>(
                        Sales_Chalan_Activity.this, android.R.layout.simple_list_item_1, customerNameList
                );


                salesChalanSpinnerListView.setAdapter(salesChalanAdapter);
                salesChalanSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        salesChalanAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                salesChalanSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (salesChalanAdapter.getItem(position).getCustomer_Name().equals("<< Select Customer >>")) {
                            Toast.makeText(Sales_Chalan_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            j_salesChalan_Group_spinner.setText("Select Group");
                            j_salesChalan_item_spinner.setText("Select Item Name");

                            groupNameList = null;
                            itemNameList = null;

                            customer_id = salesChalanAdapter.getItem(position).getCustomer_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new SalesGroupNameTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            sales_chalan_dailog.dismiss();
                            j_salesChalan_customer_spinner.setText(salesChalanAdapter.getItem(position).getCustomer_Name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        j_salesChalan_Group_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groupNameList == null) {
                    Toast.makeText(Sales_Chalan_Activity.this, "Please Select customer", Toast.LENGTH_SHORT).show();
                    return;
                }

                sales_chalan_dailog = new Dialog(Sales_Chalan_Activity.this);
                sales_chalan_dailog.setContentView(R.layout.manufacture_dailog_spinner);
                sales_chalan_dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                sales_chalan_dailog.show();


                SearchView salesChalanSpinnerSearchView = sales_chalan_dailog.findViewById(R.id.spinner_search);
                ListView salesChalanSpinnerListView = sales_chalan_dailog.findViewById(R.id.spinnerItemList);

                ImageView sales_chalan_image = sales_chalan_dailog.findViewById(R.id.spinner_icon_img);
                sales_chalan_image.setImageResource(R.drawable.ic_selse_chalan);
                salesChalanSpinnerSearchView.setQueryHint("Search here...");
                salesChalanSpinnerSearchView.onActionViewExpanded();
                salesChalanSpinnerSearchView.setIconified(false);
                salesChalanSpinnerSearchView.clearFocus();

                ImageView calcen_btn = sales_chalan_dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sales_chalan_dailog.dismiss();
                    }
                });

                final ArrayAdapter<Billinvoice_Group_Entity> salesChalanAdapter = new ArrayAdapter<Billinvoice_Group_Entity>(
                        Sales_Chalan_Activity.this, android.R.layout.simple_list_item_1, groupNameList
                );


                salesChalanSpinnerListView.setAdapter(salesChalanAdapter);
                salesChalanSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        salesChalanAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                salesChalanSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (salesChalanAdapter.getItem(position).getItemGroup_Name().equals("<< Select Group >>")) {
                            Toast.makeText(Sales_Chalan_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            j_salesChalan_item_spinner.setText("Select Item Name");

                            itemNameList = null;


                            groupItem_id = salesChalanAdapter.getItem(position).getItemGroup_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new SalesChalanNameTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            sales_chalan_dailog.dismiss();
                            j_salesChalan_Group_spinner.setText(salesChalanAdapter.getItem(position).getItemGroup_Name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        j_salesChalan_item_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groupNameList == null) {
                    Toast.makeText(Sales_Chalan_Activity.this, "Please Select Group", Toast.LENGTH_SHORT).show();
                    return;
                }

                sales_chalan_dailog = new Dialog(Sales_Chalan_Activity.this);
                sales_chalan_dailog.setContentView(R.layout.manufacture_dailog_spinner);
                sales_chalan_dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                sales_chalan_dailog.show();

                SearchView salesChalanSpinnerSearchView = sales_chalan_dailog.findViewById(R.id.spinner_search);
                ListView salesChalanSpinnerListView = sales_chalan_dailog.findViewById(R.id.spinnerItemList);

                ImageView sales_chalan_image = sales_chalan_dailog.findViewById(R.id.spinner_icon_img);
                sales_chalan_image.setImageResource(R.drawable.ic_selse_chalan);
                salesChalanSpinnerSearchView.setQueryHint("Search here...");
                salesChalanSpinnerSearchView.onActionViewExpanded();
                salesChalanSpinnerSearchView.setIconified(false);
                salesChalanSpinnerSearchView.clearFocus();

                ImageView calcen_btn = sales_chalan_dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sales_chalan_dailog.dismiss();
                    }
                });

                final ArrayAdapter<Billinvoice_item_Entity> salesChalanAdapter = new ArrayAdapter<Billinvoice_item_Entity>(
                        Sales_Chalan_Activity.this, android.R.layout.simple_list_item_1, itemNameList
                );


                salesChalanSpinnerListView.setAdapter(salesChalanAdapter);
                salesChalanSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        salesChalanAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                salesChalanSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (salesChalanAdapter.getItem(position).getItem_name().equals("<< Select Item Name >>")) {
                            Toast.makeText(Sales_Chalan_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            item_id = salesChalanAdapter.getItem(position).getItem_id();
                            new Result_Task().execute();
                            sales_chalan_dailog.dismiss();
                            j_salesChalan_item_spinner.setText(salesChalanAdapter.getItem(position).getItem_name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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

                connection = com.ocean.orcl.ODBC.Db.createConnection();
                Log.d("connection", "================Chalan Customer==Connected===========");
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
                        Log.d("value1", "======Customer====1===========" + rs.getString(1));
                        Log.d("value2", "======Customer====2===========" + rs.getString(2));
                    }
                }

                connection.close();

            } catch (Exception e) {

                Toast.makeText(Sales_Chalan_Activity.this, " " + e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return customerNameList;
        }

        @Override
        protected void onPostExecute(ArrayList<Billinvoice_Customer_Entity> billinvoice_customer_entities) {
            busyDialog.dismis();
        }
    }


    private class Result_Task extends AsyncTask<Void, Void, ArrayList<SalesChalan_Result_Entity>> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected ArrayList<SalesChalan_Result_Entity> doInBackground(Void... voids) {
            resultList = new ArrayList<SalesChalan_Result_Entity>();
            try {

                connection = com.ocean.orcl.ODBC.Db.createConnection();
                if (connection != null) {
                    resultList = new ArrayList<SalesChalan_Result_Entity>();

                    Statement stmt = connection.createStatement();
                    String query = "Select m.chalan_ID,chalan_NO,to_char(chalan_DATE,'MON DD,RRRR')chalan_DATE,\n" +
                            "Sum((Nvl(C.chalan_RATE,0)*Nvl(C.chalan_QTY, 0))+Nvl(C.VAT_AMT,0)-(Nvl(C.DISCOUNT_AMOUNT,0))) amount\n" +
                            "From vw_Inv_CHALANMst m, vw_Inv_CHALANChd C, Inv_Item I,inv_itemgroup ig,inv_mu u, inv_contact r\n" +
                            "Where m.chalan_Id = c.chalan_Id\n" +
                            "And C.ITEM_ID = I.ITEM_ID\n" +
                            "and  I.MU_ID=u.MU_ID\n" +
                            "And I.ITEMGROUP_ID = ig.ITEMGROUP_ID\n" +
                            "and m.CONTACT_ID=r.CONTACT_ID\n" +
                            "And ('" + customer_id + "' =-1 or m.CONTACT_ID = '" + customer_id + "')\n" +
                            "And ('" + groupItem_id + "' =-1 or ig.ITEMGROUP_ID = '" + groupItem_id + "')\n" +
                            "And ('" + item_id + "' =-1 or C.item_Id = '" + item_id + "')\n" +
                            "AND M.chalan_DATE BETWEEN to_date('" + text_formDate.getText() + "','MON DD,RRRR') AND to_date('" + text_toDate.getText() + "','MON DD,RRRR')\n" +
                            "Group By m.chalan_ID,chalan_NO,chalan_DATE\n" +
                            "Order By m.chalan_ID,chalan_NO,chalan_DATE";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        resultList.add(new SalesChalan_Result_Entity(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                    }

                }
                busyDialog.dismis();

                connection.close();

            } catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }
            return resultList;
        }

        @Override
        protected void onPostExecute(ArrayList<SalesChalan_Result_Entity> bill_result_entities) {
            busyDialog.dismis();
            result_adapter = new SalesChalan_Results_CustomAdapter(getApplication(), resultList);
            listView.setAdapter(result_adapter);
        }

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

                DatePickerDialog mDatePicker = new DatePickerDialog(Sales_Chalan_Activity.this, new DatePickerDialog.OnDateSetListener() {
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

                DatePickerDialog mDatePicker = new DatePickerDialog(Sales_Chalan_Activity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void currentDate() {
        String currentDate = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(new Date());
        text_formDate.setText(currentDate.toUpperCase());
        text_toDate.setText(currentDate.toUpperCase());
    }


    private class SalesGroupNameTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                connection = com.ocean.orcl.ODBC.Db.createConnection();
                Log.d("connection", "================Chalan Group==Connected===========");
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

    private class SalesChalanNameTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                connection = com.ocean.orcl.ODBC.Db.createConnection();
                Log.d("connection", "================Chalan Item==Connected===========");
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
            busyDialog.dismis();
        }
    }


}
