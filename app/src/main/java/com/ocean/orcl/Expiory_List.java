package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class Expiory_List extends AppCompatActivity {
    private Connection connection;
    private ListView listView;
    private TextView toDate;
    private CustomAdapter_CurrentStock adapter;
    private ArrayList<CurrentStock_Entity_F> expiryItems;
    private String manufacture_id,itemGroup_Id,item_Id,quantity;
    private Spinner manufacture_spinner,group_spinner,itemName_spinner,udNo_spinner,quantity_spinner;
    private ArrayList<CurrentStock_Manufacturer_Entity_A> manufactureList;
    private ArrayList<CurrentStock_Group_Entity_B> groupnameList;
    private ArrayList<CurrentStock_ItemName_Entity_C> itemNameList;
    //    private ArrayList<CurrentStock_UD_No_Entity_D> udNoList;

    private TextView j_Expiry_menufacture_spinner,j_Expiry_group_spinner, j_Expiry_item_spinner;
    private Dialog dailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiory_list);
        toDate =findViewById(R.id.expiry_dateTex);
       // manufacture_spinner = (Spinner) findViewById(R.id.Expiry_menufacture_spinner);
      //  group_spinner = (Spinner) findViewById(R.id.Expiry_group_spinner);
     //    itemName_spinner = (Spinner) findViewById(R.id.Expiry_item_spinner);
        listView =findViewById(R.id.expiry_listView);
        j_Expiry_menufacture_spinner = findViewById(R.id.Expiry_menufacture_spinner);
        j_Expiry_group_spinner = findViewById(R.id.Expiry_group_spinner);
        j_Expiry_item_spinner = findViewById(R.id.Expiry_item_spinner);

        CurrentDate();
//        DateSetTO();
       new ManufactureInfoTask().execute();

        j_Expiry_menufacture_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(manufactureList==null){
                    Toast.makeText(Expiory_List.this, "Check your internate connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(Expiory_List.this);
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

                final ArrayAdapter<CurrentStock_Manufacturer_Entity_A> adapter = new ArrayAdapter<CurrentStock_Manufacturer_Entity_A>(
                        Expiory_List.this, android.R.layout.simple_spinner_dropdown_item,manufactureList
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
                        if(adapter.getItem(position).getMenufacture_Name().equals("<< Select Manufacturer >>")){
                            Toast.makeText(Expiory_List.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        }else {

                            j_Expiry_group_spinner.setText("Select Group");
                            j_Expiry_item_spinner.setText("Select Item Name");

                            groupnameList =null;
                            itemNameList =null;

                            manufacture_id = adapter.getItem(position).getManufacture_Id();
                            groupName_initList();
                            dailog.dismiss();
                            j_Expiry_menufacture_spinner.setText(adapter.getItem(position).getMenufacture_Name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



        j_Expiry_group_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(groupnameList==null){
                    Toast.makeText(Expiory_List.this, "please Select manufacturer", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(Expiory_List.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();


                SearchView spinnerSearchView = dailog.findViewById(R.id.spinner_search);
                ListView SpinnerListView = dailog.findViewById(R.id.spinnerItemList);

                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_loan_chalan);
                spinnerSearchView.setQueryHint("Search here...");
                spinnerSearchView.onActionViewExpanded();
                spinnerSearchView.setIconified(false);
                spinnerSearchView.clearFocus();

                final ArrayAdapter<CurrentStock_Group_Entity_B> adapter = new ArrayAdapter<CurrentStock_Group_Entity_B>(
                        Expiory_List.this, android.R.layout.simple_spinner_dropdown_item,groupnameList
                );


                SpinnerListView.setAdapter(adapter);

                spinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                        if(adapter.getItem(position).getItem_groupName().equals("<< Select Group >>")){
                            Toast.makeText(Expiory_List.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        }else {

                            j_Expiry_item_spinner.setText("Select Item Name");

                            itemNameList =null;


                            itemGroup_Id = adapter.getItem(position).getItem_Id();
                            itemName_initList();
                            dailog.dismiss();
                            j_Expiry_group_spinner.setText(adapter.getItem(position).getItem_groupName());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        j_Expiry_item_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itemNameList==null){
                    Toast.makeText(Expiory_List.this, "Please Select group", Toast.LENGTH_SHORT).show();
                    return;
                }

                dailog = new Dialog(Expiory_List.this);
                dailog.setContentView(R.layout.manufacture_dailog_spinner);
                dailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dailog.show();


                SearchView spinnerSearchView = dailog.findViewById(R.id.spinner_search);
                ListView SpinnerListView = dailog.findViewById(R.id.spinnerItemList);

                ImageView current_stock_image = dailog.findViewById(R.id.spinner_icon_img);
                current_stock_image.setImageResource(R.drawable.ic_loan_chalan);
                spinnerSearchView.setQueryHint("Search here...");
                spinnerSearchView.onActionViewExpanded();
                spinnerSearchView.setIconified(false);
                spinnerSearchView.clearFocus();

                final ArrayAdapter<CurrentStock_ItemName_Entity_C> adapter = new ArrayAdapter<CurrentStock_ItemName_Entity_C>(
                        Expiory_List.this, android.R.layout.simple_spinner_dropdown_item,itemNameList
                );

                SpinnerListView.setAdapter(adapter);

                spinnerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                        if(adapter.getItem(position).getItem_Name().equals("<< Select Group >>")){
                            Toast.makeText(Expiory_List.this, "Please select an item", Toast.LENGTH_SHORT).show();
                        }else {

                            //     j_Expiry_item_spinner.setText("Select Item Name");

                          //  itemNameList =null;


                            item_Id = adapter.getItem(position).getItem_Id();

                     DateSetTO();
                     new Result_Task().execute();
                            dailog.dismiss();
                            j_Expiry_item_spinner.setText(adapter.getItem(position).getItem_Name());
                        }


                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


//        manufacture_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                CurrentStock_Manufacturer_Entity_A clickedItem = (CurrentStock_Manufacturer_Entity_A) parent.getItemAtPosition(position);
//                String manufacture_name =clickedItem.getMenufacture_Name();
//                manufacture_id =clickedItem.getManufacture_Id();
//                if(manufacture_name.equals("<< Select Manufacturer >>")){
//                    //nothing do
//                }else{
//                    groupName_initList();
//                    Toast.makeText(getApplication(),"selected : "+manufacture_name,Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                CurrentStock_Group_Entity_B clickedItem = (CurrentStock_Group_Entity_B) parent.getItemAtPosition(position);
//                String groupname =clickedItem.getItem_groupName();
//                itemGroup_Id =clickedItem.getItem_Id();
////                Log.d("group_id","======test==========="+groupId+"\n"+"G_name "+groupname);
//
//
//                if(itemGroup_Id.equals("-1")){
//                    //nothing do
//                }else{
//                    itemName_initList();
////                    udNo_initList();
////                    quantity_initList();
//                    Toast.makeText(getApplication(),"selected : "+groupname,Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


//        itemName_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                CurrentStock_ItemName_Entity_C clickedItem = (CurrentStock_ItemName_Entity_C) parent.getItemAtPosition(position);
//                item_Id =clickedItem.getItem_Id();
//                String item_name = clickedItem.getItem_Name();
//
//                if(item_name.equals("<< Select Item Name >>")){
//                }else {
//                    DateSetTO();
//                    new Result_Task().execute();
//                    Toast.makeText(Expiory_List.this,"selected : "+item_name,Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


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

                DatePickerDialog mDatePicker=new DatePickerDialog(Expiory_List.this, new DatePickerDialog.OnDateSetListener()
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
//                        Final_query();

                    }
                }, year, month, day);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
    }
    private void CurrentDate(){
        String currentDate = new SimpleDateFormat("MMM dd,yyyy",Locale.getDefault()).format(new Date());
        toDate.setText(currentDate.toUpperCase());
        toDate.setText(currentDate.toUpperCase());
    }
    private void manufacturer_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Expiry query==Connected===========");
            if(connection != null){
                manufactureList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select MANUFACTURER_ID,MANUFACTURER_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 MANUFACTURER_ID,'<< Select Manufacturer >>' MANUFACTURER_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "Select distinct 2 sl,MANUFACTURER_ID, MANUFACTURER_NAME\n" +
                        "From INV_MANUFACTURER)\n" +
                        "order by sl,MANUFACTURER_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    manufactureList.add(new CurrentStock_Manufacturer_Entity_A(rs.getString(1),rs.getString(2)));
//                    Log.d("value1","==========1==========="+rs.getString(1));
//                    Log.d("value1","==========2==========="+rs.getString(2));
                }
            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Expiory_List.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void groupName_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Expiry query==Connected===========");
            if(connection != null){
                groupnameList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select ITEMGROUP_ID,ITEMGROUP_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 ITEMGROUP_ID,'<< Select Group >>' ITEMGROUP_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "SELECT distinct 2 sl, g.ITEMGROUP_ID, g.ITEMGROUP_NAME\n" +
                        "FROM INV_ITEMGROUP g, inv_item i\n" +
                        "where g.ITEMGROUP_ID=i.ITEMGROUP_ID \n" +
                        "and ('"+manufacture_id+"'=-1 or i.MANUFACTURER_ID='"+manufacture_id+"')\n" +
                        ")\n" +
                        "order by sl,ITEMGROUP_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    groupnameList.add(new CurrentStock_Group_Entity_B(rs.getString(1),rs.getString(2)));
                    Log.d("value1","======Group====1==========="+rs.getString(1));
                    Log.d("value2","======Group====2==========="+rs.getString(2));

                }
            }
//            group_adapter =new Current_Stock_GroupName_Adapter(getApplication(),groupnameList);
//            group_spinner.setAdapter(group_adapter);

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Expiory_List.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void itemName_initList(){
        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Expiry item_Name query==Connected===========");
            Log.d("item_id_nested","=====Group id====="+itemGroup_Id);
            Log.d("manufacture_id_nested","=====manufacture id====="+manufacture_id);
            if(connection != null){
                itemNameList = new ArrayList<>();

                Statement stmt=connection.createStatement();
                String query = "select ITEM_ID,ITEM_NAME\n" +
                        "from(\n" +
                        "select 1 sl,-1 ITEM_ID,'<< Select Item Name >>' ITEM_NAME\n" +
                        "from dual\n" +
                        "union all\n" +
                        "SELECT distinct 2 sl, ITEM_ID, ITEM_NAME||' ('||UD_NO||')' ITEM_NAME\n" +
                        "FROM INV_ITEM\n" +
                        "WHERE ('"+manufacture_id+"'=-1 or MANUFACTURER_ID='"+manufacture_id+"') \n" +
                        "and ('"+itemGroup_Id+"'=-1 or ITEMGROUP_ID='"+itemGroup_Id+"')\n" +
                        ")\n" +
                        "order by sl,ITEM_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
//                            groupnameList.add(new CurrentStock_Group_Entity_B(rs.getString(1),rs.getString(2)));
                    Log.d("value1","==========1==========="+rs.getString(1));
                    Log.d("value2","==========2==========="+rs.getString(2));
                    itemNameList.add(new CurrentStock_ItemName_Entity_C(rs.getString(1),rs.getString(2)));
                }
//                itmeName_adapter =new Current_Stock_ItemName_Adapter(Expiory_List.this,itemNameList);
//                itemName_spinner.setAdapter(itmeName_adapter);

            }

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Expiory_List.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void Final_query(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Query Final==Connected===========");
            Log.d("R_query","================date=========== "+toDate.getText());
            Log.d("R_query","================item id=========== "+item_Id);

            if(connection != null){
                expiryItems = new ArrayList<CurrentStock_Entity_F>();

                Statement stmt=connection.createStatement();
                String query = "Select LOT_NO,to_char(EXP_DATE,'MON DD,RRRR') EXP_DATE,sum(CURR_STOCK) ||' '||MU_NAME CURR_STOCK\n" +
                        "From INV_ITEMSTOCK_SUMMARY@inv_core s \n" +
                        "where ITEM_ID='"+item_Id+"' \n" +
//                        "and EXP_DATE <='"+toDate.getText()+"' \n" +
                        "and EXP_DATE <= to_date('"+toDate.getText()+"','MON DD,RRRR') \n" +
                        "group by LOT_NO,EXP_DATE, MU_NAME \n" +
                        "having Sum(Curr_Stock)>0 \n"+
                        "Order By LOT_NO";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    expiryItems.add(new CurrentStock_Entity_F(rs.getString(1),rs.getString(2),rs.getString(3)));

                Log.d("value1","==========1==========="+rs.getString(1));
                Log.d("value2","==========2==========="+rs.getString(2));
                Log.d("value2","==========2==========="+rs.getString(3));

                }

            }
//        adapter = new CustomAdapter_CurrentStock(this,expiryItems);
//        listView.setAdapter(adapter);

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Expiory_List.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private class ManufactureInfoTask  extends AsyncTask<Void,Void,ArrayList<CurrentStock_Manufacturer_Entity_A>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(Expiory_List.this);
//            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<CurrentStock_Manufacturer_Entity_A> doInBackground(Void... voids) {
            manufactureList = new ArrayList<>();
//            groupName_initList();
            manufacturer_initList();;

            return manufactureList;
        }

        @Override
        protected void onPostExecute(ArrayList<CurrentStock_Manufacturer_Entity_A> currentStock_manufacturer_entity_as) {
            loadingBar.dismiss();
//            manufacture_adapter =new Current_Stock_Manufacturer_Adapter(getApplication(),manufactureList);
//            manufacture_spinner.setAdapter(manufacture_adapter);

        }

    }
    private class Result_Task extends AsyncTask<Void,Void,ArrayList<CurrentStock_Entity_F>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(Expiory_List.this);
            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait For Results.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<CurrentStock_Entity_F> doInBackground(Void... voids) {
            expiryItems = new ArrayList<CurrentStock_Entity_F>();
//            groupName_initList();
//            manufacturer_initList();;
            Final_query();
            return expiryItems;
        }

        @Override
        protected void onPostExecute(ArrayList<CurrentStock_Entity_F> currentStock_manufacturer_entity_as) {
            loadingBar.dismiss();
            adapter = new CustomAdapter_CurrentStock(Expiory_List.this,expiryItems);
            listView.setAdapter(adapter);


        }

    }


}
