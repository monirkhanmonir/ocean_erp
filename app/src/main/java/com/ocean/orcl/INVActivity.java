package com.ocean.orcl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class INVActivity extends AppCompatActivity {
    private Connection connection;
    private String userName;
    ListView listView;
    private TextView name,designation ,invH_textView;
    private String database_currentStock,database_Bill_Invoice,database_SalesChalan,database_LoanChalan,database_expiry,database_salesReport_Summary,database_salesReport_Details,database_chalanReport_Summary,database_chalanReport_Details;
    private String access_currentStock,access_Bill_Invoice,access_SalesChalan,access_LoanChalan,access_Expiry,access_SalesReport_Summary,access_SalesReport_Details,access_chalanReport_Summary,access_chalanReport_Details;
    private ArrayList<HRM_AccessPermission_Entity> itemNameList;
    String invTitle[] = {"Current Stock","Bill/Invoice","Sales Chalan","Loan Chalan","Expiry list","Sales Report(Summary)","Sales Report(Details)","Chalan Report(Summary)","Chalan Report(Details)"};

    int [] invimageItem = {R.drawable.ic_stock_inventory, R.drawable.ic_bill_invoice, R.drawable.ic_selse_chalan,R.drawable.ic_loan_chalan,R.drawable.ic_loan_chalan,R.drawable.ic_sales_summary,R.drawable.ic_sales,R.drawable.ic_chalan_report,R.drawable.ic_chalan_report};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv);
        listView = findViewById(R.id.inv_listView);
        name =findViewById(R.id.inv_p_name);
        designation =findViewById(R.id.inv_p_designation);
        invH_textView =findViewById(R.id.hrm_textView);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BroshkPlum-YzqJL.ttf");
        invH_textView.setTypeface(typeface);

        MyAdapter adapter = new MyAdapter(this,invTitle,invimageItem);
//        MyAdapter adapter = new MyAdapter(this,invTitle);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    if(database_currentStock.equals("Current Stock") && access_currentStock.equals("0")){
                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                        Log.d("voucher","--------if---"+database_currentStock+"=="+access_currentStock);
                    }else{
                        Log.d("voucher","--------else---"+database_currentStock+"=="+access_currentStock);
                        Intent intent = new Intent(INVActivity.this,CurrentStock_Activity.class);
                        startActivity(intent);
                    }

                }else if(position == 1){
                    if(database_Bill_Invoice.equals("Bill / Invoice") && access_Bill_Invoice.equals("0")){
                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                        Log.d("voucher","--------if---"+database_Bill_Invoice+"=="+access_Bill_Invoice);
                    }else{
                        Log.d("voucher","--------else---"+database_Bill_Invoice+"=="+access_Bill_Invoice);
                        Intent intent = new Intent(INVActivity.this,Bill_Invoice_Activity.class);
                        startActivity(intent);
                    }

                }else if(position == 2){
                    if(database_SalesChalan.equals("Sales Chalan") && access_SalesChalan.equals("0")){
                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                        Log.d("voucher","--------if---"+database_SalesChalan+"=="+access_SalesChalan);
                    }else{
                        Log.d("voucher","--------else---"+database_SalesChalan+"=="+access_SalesChalan);
                        Intent intent = new Intent(INVActivity.this,Sales_Chalan_Activity.class);
                        startActivity(intent);
                    }
//                    Toast.makeText(INVActivity.this,"this is 3rd",Toast.LENGTH_SHORT).show();
                }else if(position == 3){
                    Toast.makeText(INVActivity.this,"On progress",Toast.LENGTH_SHORT).show();
//                    if(database_LoanChalan.equals("Loan Chalan") && access_LoanChalan.equals("0")){
//                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
//                        Log.d("voucher","--------if---"+database_expiry+"=="+access_LoanChalan);
//                    }else{
//                        Log.d("voucher","--------else---"+database_LoanChalan+"=="+access_LoanChalan);
//                        Intent intent = new Intent(INVActivity.this,Expiory_List.class);
//                        startActivity(intent);
//                    }
                }
                else if(position == 4){
//                    Toast.makeText(INVActivity.this,"on progress",Toast.LENGTH_SHORT).show();
                    if(database_expiry.equals("Expiry List") && access_Expiry.equals("0")){
                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                        Log.d("expiry","--------if---"+database_expiry+"=="+access_Expiry);
                    }else{
                        Log.d("expiry","--------else---"+database_expiry+"=="+access_Expiry);
                        Intent intent = new Intent(INVActivity.this,Expiory_List.class);
                        startActivity(intent);
                    }

                }else if(position == 5){
                    if(database_salesReport_Summary.equals("Sales Report (Summary)") && access_SalesReport_Summary.equals("0")){
                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                        Log.d("sLSummary","--------if---"+database_salesReport_Summary+"=="+access_SalesReport_Summary);
                    }else{
                        Log.d("sLSummary","--------else---"+database_salesReport_Summary+"=="+access_SalesReport_Summary);
                        Intent intent = new Intent(INVActivity.this,Sales_Report_Summary_Activity.class);
                        startActivity(intent);
                    }

                }else if(position == 6){
                    if(database_salesReport_Details.equals("Sales Report (Detail)") && access_SalesReport_Details.equals("0")){
                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                        Log.d("sLsDetails","--------if---"+database_salesReport_Details+"=="+access_SalesReport_Details);
                    }else{
                        Log.d("sLsDetails","--------else---"+database_salesReport_Details+"=="+access_SalesReport_Details);
                        Intent intent = new Intent(INVActivity.this,Sales_Report_Details_Activity.class);
                        startActivity(intent);
                    }

                }else if(position == 7){
                    if(database_chalanReport_Summary.equals("Chalan Report (Summary)") && access_chalanReport_Summary.equals("0")){
                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                        Log.d("chalan_R","--------if---"+database_chalanReport_Summary+"=="+access_chalanReport_Summary);
                    }else{
                        Log.d("chalan_R","--------else---"+database_chalanReport_Summary+"=="+access_chalanReport_Summary);
                        Intent intent = new Intent(INVActivity.this,Chalan_Report_Summary_Activity.class);
                        startActivity(intent);
                    }
//                    Toast.makeText(INVActivity.this,"On progress",Toast.LENGTH_SHORT).show();
                }else if(position == 8){
                    if(database_chalanReport_Details.equals("Chalan Report (Detail)") && access_chalanReport_Details.equals("0")){
                        Toast.makeText(INVActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                        Log.d("chalan_D","--------if---"+database_chalanReport_Details+"=="+access_chalanReport_Details);
                    }else{
                        Log.d("chalan_D","--------else---"+database_chalanReport_Details+"=="+access_chalanReport_Details);
                        Intent intent = new Intent(INVActivity.this,Chalan_Report_Details_Activity.class);
                        startActivity(intent);
                    }
//
                }
            }
        });
        getLoginValueShowHeader2();
        OptionEnableDisable_access();
    }
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String title[];
        int img[];

        MyAdapter( Context context,String title[] ,int imgs[]) {
            super(context, R.layout.invlistview,R.id.inv_textView,title);
            this.context =context;
            this.title =title;
            this.img =imgs;
        }
//MyAdapter( Context context,String title[]) {
//    super(context, R.layout.invlistview,R.id.inv_textView,title);
//    this.context =context;
//    this.title =title;
//}

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.invlistview,parent,false);
            ImageView images = row.findViewById(R.id.imgIcon_inv);
            TextView mytitle =row.findViewById(R.id.inv_textView);

            images.setImageResource(img[position]);
            mytitle.setText(title[position]);



            return row;
        }
    }
    private void getLoginValueShowHeader2(){
//        <<<<<<<<<<<<<<<------ (Start getting login value by get Menu Activity To HRM Activity) -------->>>>>>>>>>>
        Bundle b = getIntent().getExtras();
        String person_Name = b.getString("persong_name");
        String persong_desig_dept = b.getString("desig_dept");

        name.setText(person_Name);
        designation.setText(persong_desig_dept);
//        <<<<<<<<<<<<<<<------ End getting login value -------->>>>>>>>>>>

    }
    private void OptionEnableDisable_access(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();

            if(connection != null){
                Bundle b = getIntent().getExtras();
                userName = b.getString("myName");
                Log.d("login_valueeee","--------------"+userName);
                itemNameList = new ArrayList<HRM_AccessPermission_Entity>();

                Statement stmt=connection.createStatement();
                String query = "select a.N_OBJECT_ID position, a.V_OBJECT_NAME name,\n" +
                        "decode(a.N_ACTIVE_FLAG,0,0,decode(a.N_CAN_ACCESS,0,0,b.N_CAN_ACCESS)) enable_flag\n" +
                        "from SEC_OBJECT_MOBILE a, SEC_USER_OBJECT_PRIV_MOBILE b\n" +
                        "where a.N_OBJECT_ID=b.N_OBJECT_ID\n" +
                        "and b.N_USER_ID = (select N_USER_ID from sec_user where V_USER_NAME='"+userName.toUpperCase()+"')";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    itemNameList.add(new HRM_AccessPermission_Entity(rs.getString(1),rs.getString(2),rs.getString(3)));

                    Log.d("value1","======res====1==========="+rs.getString(1));
                    Log.d("value2","======res====2==========="+rs.getString(2));
                    Log.d("value3","======res====3==========="+rs.getString(3));
                    Log.d("value4","======res====++++=================");

                }
//                database_Accounts = itemNameList.get(1).getTitle_name();
                database_currentStock = itemNameList.get(5).getTitle_name();
                database_Bill_Invoice =itemNameList.get(6).getTitle_name();
                database_SalesChalan =itemNameList.get(7).getTitle_name();
//                database_LoanChalan =itemNameList.get(8).getTitle_name();
                database_expiry =itemNameList.get(9).getTitle_name();
                database_salesReport_Summary =itemNameList.get(10).getTitle_name();
                database_salesReport_Details =itemNameList.get(11).getTitle_name();
                database_chalanReport_Summary =itemNameList.get(12).getTitle_name();
                database_chalanReport_Details =itemNameList.get(13).getTitle_name();


                access_currentStock =itemNameList.get(5).getAccess();
                access_Bill_Invoice = itemNameList.get(6).getAccess();
                access_SalesChalan = itemNameList.get(7).getAccess();
//                access_LoanChalan = itemNameList.get(8).getAccess();
                access_Expiry = itemNameList.get(9).getAccess();
                access_SalesReport_Summary = itemNameList.get(10).getAccess();
                access_SalesReport_Details = itemNameList.get(11).getAccess();
                access_chalanReport_Summary = itemNameList.get(12).getAccess();
                access_chalanReport_Details = itemNameList.get(13).getAccess();

//                access_LedgerPosition = itemNameList.get(17).getAccess();

            }

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(INVActivity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

}
