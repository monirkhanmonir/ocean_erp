package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.Model.CurrentStock_Entity_F;
import com.excellenceict.ocean_erp.Model.CurrentStock_Group_Entity_B;
import com.excellenceict.ocean_erp.Model.CurrentStock_ItemName_Entity_C;
import com.excellenceict.ocean_erp.Model.CurrentStock_Manufacturer_Entity_A;
import com.excellenceict.ocean_erp.Model.CurrentStock_Quantity_Entity_E;
import com.excellenceict.ocean_erp.util.BusyDialog;
import com.excellenceict.ocean_erp.util.Helper;
import com.excellenceict.ocean_erp.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

 public class CurrentStock_Activity extends AppCompatActivity {
    private static String TAG = "CurrentStock_Activity";

    private Connection connection;
    private ListView listView;
    private CustomAdapter_CurrentStock adapter;
    private ArrayList<CurrentStock_Entity_F> currentStockItems;
    private String manufacture_id, itemGroup_Id, item_Id, quantity;
    private ArrayList<CurrentStock_Manufacturer_Entity_A> manufactureList;
    private ArrayList<CurrentStock_Group_Entity_B> groupnameList;
    private ArrayList<CurrentStock_ItemName_Entity_C> itemNameList;
    private ArrayList<CurrentStock_Quantity_Entity_E> qtyList;
    private String defaultQuantity;


    //for search
    private TextView manufacture_dropdown, j_stock_group_dropdown_btn, j_stock_item_dropdown_btn,
            j_stock_quentity_dropdown_btn, j_currentStock_textView;

    private Dialog dailog;
    private BusyDialog busyDialog;
    private Context context;
    private Handler handler;


    private String datePatern ="dd-MM-yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePatern);

    private String timePattern = "hh:mm a";
    private SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timePattern);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_stock_activity);

//        udNo_spinner = (Spinner) findViewById(R.id.ud_no_spinner);
//        j_currentStock_textView = findViewById(R.id.currentStock_textView);

        listView = findViewById(R.id.qty_listView);
        manufacture_dropdown = findViewById(R.id.manufacture_dropdown_btn);
        j_stock_group_dropdown_btn = findViewById(R.id.stock_group_dropdown_btn);
        j_stock_item_dropdown_btn = findViewById(R.id.stock_item_dropdown_btn);
        j_stock_quentity_dropdown_btn = findViewById(R.id.stock_quentity_dropdown_btn);

        context = CurrentStock_Activity.this;
        handler = new Handler();


        //controll toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.currentStockToolBarId);
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


        //for manufacture group dropdown option

        manufacture_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manufactureList == null) {
                    Toast.makeText(CurrentStock_Activity.this, "Check your internate connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(CurrentStock_Activity.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                dailog.show();


                SearchView manufactureSpinnerSearchView = dailog.findViewById(R.id.spinner_search);
                ListView manufactureSpinnerListView = dailog.findViewById(R.id.spinnerItemList);

                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_stock_inventory);
                manufactureSpinnerSearchView.setQueryHint("Search here...");
                manufactureSpinnerSearchView.onActionViewExpanded();
                manufactureSpinnerSearchView.setIconified(false);
                manufactureSpinnerSearchView.clearFocus();

                ImageView calcen_btn = dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Test","Click Close");
                        dailog.dismiss();
                    }
                });


                final ArrayAdapter<CurrentStock_Manufacturer_Entity_A> menuAdapter = new ArrayAdapter<CurrentStock_Manufacturer_Entity_A>(
                        CurrentStock_Activity.this, android.R.layout.simple_list_item_1, manufactureList
                );


                manufactureSpinnerListView.setAdapter(menuAdapter);

                manufactureSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        menuAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                manufactureSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (menuAdapter.getItem(position).getMenufacture_Name().equals("<< Select Manufacturer >>")) {
                            Toast.makeText(CurrentStock_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            j_stock_group_dropdown_btn.setText("Select Group");
                            j_stock_item_dropdown_btn.setText("Select Item Name");
                            j_stock_quentity_dropdown_btn.setText("Select Quantity");

                            groupnameList = null;
                            itemNameList = null;
                            qtyList = null;

                            manufacture_id = menuAdapter.getItem(position).getManufacture_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new GroupNameTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }
                            dailog.dismiss();
                            manufacture_dropdown.setText(menuAdapter.getItem(position).getMenufacture_Name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        //for stock group dropdown item option

        j_stock_group_dropdown_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groupnameList == null) {
                    Toast.makeText(CurrentStock_Activity.this, "Please select menufacturar.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(CurrentStock_Activity.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();


                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_stock_inventory);

                ListView stockGroupSpinnerListView = dailog.findViewById(R.id.spinnerItemList);
                SearchView manufactureSpinnerSearchView = dailog.findViewById(R.id.spinner_search);
                manufactureSpinnerSearchView.setQueryHint("Search here...");
                manufactureSpinnerSearchView.onActionViewExpanded();
                manufactureSpinnerSearchView.setIconified(false);
                manufactureSpinnerSearchView.clearFocus();

                ImageView calcen_btn = dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Test","Click Close");
                        dailog.dismiss();
                    }
                });

                final ArrayAdapter<CurrentStock_Group_Entity_B> groupAdapter = new ArrayAdapter<CurrentStock_Group_Entity_B>(
                        CurrentStock_Activity.this, android.R.layout.simple_list_item_1, groupnameList
                );


                stockGroupSpinnerListView.setAdapter(groupAdapter);

                manufactureSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                stockGroupSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (groupAdapter.getItem(position).getItem_groupName().equals("<< Select Group >>")) {
                            Toast.makeText(CurrentStock_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

                            j_stock_item_dropdown_btn.setText("Select Item Name");
                            j_stock_quentity_dropdown_btn.setText("Select Quentity");

                            itemNameList = null;
                            qtyList = null;

                            itemGroup_Id = groupAdapter.getItem(position).getItem_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new ItemNameTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            dailog.dismiss();
                            j_stock_group_dropdown_btn.setText(groupAdapter.getItem(position).getItem_groupName());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        //for item name dropdown option

        j_stock_item_dropdown_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemNameList == null) {
                    Toast.makeText(CurrentStock_Activity.this, "Please select group.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(CurrentStock_Activity.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();


                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_stock_inventory);

                ListView itemNameSpinnerListView = dailog.findViewById(R.id.spinnerItemList);
                SearchView itemNameSpinnerSearchView = dailog.findViewById(R.id.spinner_search);
                itemNameSpinnerSearchView.setQueryHint("Search here...");
                itemNameSpinnerSearchView.onActionViewExpanded();
                itemNameSpinnerSearchView.setIconified(false);
                itemNameSpinnerSearchView.clearFocus();

                ImageView calcen_btn = dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Test","Click Close");
                        dailog.dismiss();
                    }
                });

                final ArrayAdapter<CurrentStock_ItemName_Entity_C> itemAdapter = new ArrayAdapter<CurrentStock_ItemName_Entity_C>(
                        CurrentStock_Activity.this, android.R.layout.simple_list_item_1, itemNameList
                );

                if (itemNameList == null) {
                    return;
                }

                itemNameSpinnerListView.setAdapter(itemAdapter);

                itemNameSpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        itemAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                itemNameSpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (itemAdapter.getItem(position).getItem_Name().equals("<< Select Item Name >>")) {
                            Toast.makeText(CurrentStock_Activity.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        } else {

//                            j_stock_quentity_dropdown_btn.setText("Select Quantity");

//                            qtyList = null;

                            item_Id = itemAdapter.getItem(position).getItem_Id();

                            if (NetworkHelpers.isNetworkAvailable(context)) {
                                new StockQuantityTask().execute();
                            } else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }

                            dailog.dismiss();
                            j_stock_item_dropdown_btn.setText(itemAdapter.getItem(position).getItem_Name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        //for stock_quentity_dropdown_btn option

        j_stock_quentity_dropdown_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qtyList == null) {
                    Toast.makeText(CurrentStock_Activity.this, "Please select item name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(CurrentStock_Activity.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();


                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_stock_inventory);

                ListView stockQuentitySpinnerListView = dailog.findViewById(R.id.spinnerItemList);
                SearchView stockQuentitySpinnerSearchView = dailog.findViewById(R.id.spinner_search);
                stockQuentitySpinnerSearchView.setQueryHint("Search here...");
                stockQuentitySpinnerSearchView.onActionViewExpanded();
                stockQuentitySpinnerSearchView.setIconified(false);
                stockQuentitySpinnerSearchView.clearFocus();

                ImageView calcen_btn = dailog.findViewById(R.id.spinner_close_icon_img);
                calcen_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Test","Click Close");
                        dailog.dismiss();
                    }
                });

                final ArrayAdapter<CurrentStock_Quantity_Entity_E> quentityAdapter = new ArrayAdapter<CurrentStock_Quantity_Entity_E>(
                        CurrentStock_Activity.this, android.R.layout.simple_list_item_1, qtyList
                );

                stockQuentitySpinnerListView.setAdapter(quentityAdapter);

                stockQuentitySpinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        quentityAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                stockQuentitySpinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        quantity = quentityAdapter.getItem(position).getQty();

                        Log.d(TAG, "print quantity: " + quantity);
                        Log.d(TAG, "print quantity 1: " + quentityAdapter.getItem(position).getQty1());
                        String setqtyValText = quentityAdapter.getItem(position).getQty1();
                        j_stock_quentity_dropdown_btn.setText(setqtyValText.substring(2, setqtyValText.length() -2));
                        dailog.dismiss();

                        if (NetworkHelpers.isNetworkAvailable(context)) {
                            new CurrectStpockResultTask().execute();
                        } else {
                            Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


        //-----For Dialogue box show in asynTask------
        //        manufacturer_initList();
        new ManufactureInfoTask().execute();
    }


    private class ManufactureInfoTask extends AsyncTask<Void, Void, ArrayList<CurrentStock_Manufacturer_Entity_A>> {
        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected ArrayList<CurrentStock_Manufacturer_Entity_A> doInBackground(Void... voids) {
            manufactureList = new ArrayList<>();
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "================Stock query==Connected===========");
                if (connection != null) {
                    manufactureList = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select MANUFACTURER_ID,MANUFACTURER_NAME\n" +
                            "from(\n" +
                            "select 1 sl,-1 MANUFACTURER_ID,'<< Select Manufacturer >>' MANUFACTURER_NAME\n" +
                            "from dual\n" +
                            "union all\n" +
                            "Select distinct 2 sl,MANUFACTURER_ID, MANUFACTURER_NAME\n" +
                            "From INV_MANUFACTURER)\n" +
                            "order by sl,MANUFACTURER_NAME";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        manufactureList.add(new CurrentStock_Manufacturer_Entity_A(rs.getString(1), rs.getString(2)));
                    }
                }
                busyDialog.dismis();
                connection.close();

            } catch (Exception e) {
                busyDialog.dismis();
                e.printStackTrace();
            }

            return manufactureList;
        }

        @Override
        protected void onPostExecute(ArrayList<CurrentStock_Manufacturer_Entity_A> currentStock_manufacturer_entity_as) {
            busyDialog.dismis();
        }

    }

    private class GroupNameTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "================Stock query==Connected===========");
                if (connection != null) {

                    groupnameList = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select ITEMGROUP_ID,ITEMGROUP_NAME\n" +
                            "from(\n" +
                            "select 1 sl,-1 ITEMGROUP_ID,'<< Select Group >>' ITEMGROUP_NAME\n" +
                            "from dual\n" +
                            "union all\n" +
                            "SELECT distinct 2 sl, g.ITEMGROUP_ID, g.ITEMGROUP_NAME\n" +
                            "FROM INV_ITEMGROUP g, inv_item i\n" +
                            "where g.ITEMGROUP_ID=i.ITEMGROUP_ID \n" +
                            "and ('" + manufacture_id + "'=-1 or i.MANUFACTURER_ID='" + manufacture_id + "')\n" +
                            ")\n" +
                            "order by sl,ITEMGROUP_NAME";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        groupnameList.add(new CurrentStock_Group_Entity_B(rs.getString(1), rs.getString(2)));
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

    private class ItemNameTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                if (connection != null) {
                    itemNameList = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select ITEM_ID,ITEM_NAME\n" +
                            "from(\n" +
                            "select 1 sl,-1 ITEM_ID,'<< Select Item Name >>' ITEM_NAME\n" +
                            "from dual\n" +
                            "union all\n" +
                            "SELECT distinct 2 sl, ITEM_ID, ITEM_NAME||' ('||UD_NO||')' ITEM_NAME\n" +
                            "FROM INV_ITEM\n" +
                            "WHERE ('" + manufacture_id + "'=-1 or MANUFACTURER_ID='" + manufacture_id + "') \n" +
                            "and ('" + itemGroup_Id + "'=-1 or ITEMGROUP_ID='" + itemGroup_Id + "')\n" +
                            ")\n" +
                            "order by sl,ITEM_NAME";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        Log.d("value1", "==========1===========" + rs.getString(1));
                        Log.d("value2", "==========2===========" + rs.getString(2));
                        itemNameList.add(new CurrentStock_ItemName_Entity_C(rs.getString(1), rs.getString(2)));
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

    private class StockQuantityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Log.d("connection", "================Qty query==Connected===========");
                if (connection != null) {
                    qtyList = new ArrayList<>();

                    Statement stmt = connection.createStatement();
                    String query = "select qty,qty1\n" +
                            "from(\n" +
                            "select 2 sl,1 qty,'<< Quantity: >0 >>' qty1\n" +
                            "from dual\n" +
                            "union all\n" +
                            "select 3 sl,2 qty,'<< Quantity: 0 >>' qty1\n" +
                            "from dual\n" +
                            ")\n" +
                            "order by sl,qty";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        Log.d("qty", "==========1===========" + rs.getString(1));
                        Log.d("qty1", "==========2===========" + rs.getString(2));
                        qtyList.add(new CurrentStock_Quantity_Entity_E(rs.getString(1), rs.getString(2)));
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
            if(NetworkHelpers.isNetworkAvailable(context)){
               if(qtyList.size()>0){
                   quantity = qtyList.get(0).getQty();
                   new CurrectStpockResultTask().execute();
                   String qtyVal = qtyList.get(0).getQty1();
                   j_stock_quentity_dropdown_btn.setText(qtyVal.substring(2,qtyVal.length() -2));


               }else {
                   Log.d(TAG,"qtyList is empty");
               }

            }else {
                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class CurrectStpockResultTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();

                if (connection != null) {
                    currentStockItems = new ArrayList<CurrentStock_Entity_F>();

                    Statement stmt = connection.createStatement();
                    String query = "Select LOT_NO,to_char(EXP_DATE,'MON DD,RRRR') EXP_DATE,sum(CURR_STOCK) ||' '||MU_NAME CURR_STOCK\n" +
                            "From VW_CURRENTSTOCK_SUMMARY s\n" +
                            "where ITEM_ID='" + item_Id + "'\n" +
                            "and ('" + quantity + "' = -1 or ('" + quantity + "'= 1 and CURR_STOCK>0) or ('" + quantity + "' = 2 and CURR_STOCK=0))\n" +
                            "group by LOT_NO,EXP_DATE, MU_NAME\n" +
                            "Order By LOT_NO";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        currentStockItems.add(new CurrentStock_Entity_F(rs.getString(1), rs.getString(2), rs.getString(3)));

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
            adapter = new CustomAdapter_CurrentStock(CurrentStock_Activity.this, currentStockItems);
            listView.setAdapter(adapter);
            Helper.getListViewSize(listView);
        }
    }

}
