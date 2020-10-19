package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Party_Due_Statement_Activity extends AppCompatActivity {
    private Connection connection;
    String sub_header_name;
    String sub_header_code = "-1";
    private ArrayList<ACC_partyDueStatement_Entity> customerNameList;
    private ArrayList<Acc_PartyDueStatement_Result_Entity> resultList;
    private ACC_PartyDueResult_Adapter result_adapter;

    private ListView listView;

    private TextView j_partyDue_statement_spinner;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_due_statement);
        //customer_spinner =findViewById(R.id.partyDue_statement_spinner);
        listView = findViewById(R.id.partyDueStatement_result_listView);
        j_partyDue_statement_spinner = findViewById(R.id.partyDue_statement_spinner);


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

                final ArrayAdapter<ACC_partyDueStatement_Entity> adapter = new ArrayAdapter<ACC_partyDueStatement_Entity>(
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

                            new Result_Task().execute();
                            dialog.dismiss();
                            j_partyDue_statement_spinner.setText(adapter.getItem(position).getSub_head_name());
                        }

                        //    Toast.makeText(CurrentStock_Activity.this, "Selected: "+menuAdapter.getItem(position).getMenufacture_Name(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



//        customer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ACC_partyDueStatement_Entity  clickedItem = (ACC_partyDueStatement_Entity) parent.getItemAtPosition(position);
//                sub_header_name =clickedItem.getSub_head_name();
//                sub_header_code = clickedItem.getSub_head_code();
//                if(clickedItem.getSub_head_name().equals("Customer")){
//
//                }else {
////                    showResult_initList();
//                    new Result_Task().execute();
//                    Toast.makeText(getApplicationContext(),"selected: "+clickedItem.getSub_head_name(),Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        new CustomerName_Task().execute();
        new Result_Task().execute();


    }


    private void Customer_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
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
                    customerNameList.add(new ACC_partyDueStatement_Entity(rs.getString(1),rs.getString(2)));
                    Log.d("value1","======partyDue====1==========="+rs.getString(1));
                    Log.d("value2","======partyDue====2==========="+rs.getString(2));


                }

            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Party_Due_Statement_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void showResult_initList(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();

            Log.d("query","=========customer_Header ="+sub_header_name);
            Log.d("query1","=========customer_Header_code ="+sub_header_code);
            if(connection != null){
                resultList = new ArrayList<Acc_PartyDueStatement_Result_Entity>();

                Statement stmt=connection.createStatement();
                String query = "select s.UD_NO, s.V_SUB_HEAD_NAME, s.ANALYZER, s.ROCHE, s.SYSMEX\n" +
                        "from VW_ACC_DUE_STATEMENT s\n" +
                        "where ('"+sub_header_code+"' ='-1' or s.UD_NO like '%'||'"+sub_header_code+"'||'%')\n" +
                        "order by s.V_SUB_HEAD_NAME";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    resultList.add(new Acc_PartyDueStatement_Result_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
//                    Log.d("value1","======res====1==========="+rs.getString(1));
//                    Log.d("value2","======res====2==========="+rs.getString(2));
//                    Log.d("value3","======res====3==========="+rs.getString(3));
//                    Log.d("value4","======res====4==========="+rs.getString(4));
//                    Log.d("value5","======res====5==========="+rs.getString(5));
//                    Log.d("value4","======res====4==========="+rs.getString(4));

                }

            }


            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(Party_Due_Statement_Activity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private class CustomerName_Task extends AsyncTask<Void,Void,ArrayList<ACC_partyDueStatement_Entity>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(Party_Due_Statement_Activity.this);
//            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<ACC_partyDueStatement_Entity> doInBackground(Void... voids) {
            customerNameList = new ArrayList<>();
            Customer_initList();
            return customerNameList;
        }



        @Override
        protected void onPostExecute(ArrayList<ACC_partyDueStatement_Entity> acc_partyDueStatement_entities) {
//            customer_adapter =new ACC_Party_Due_Adapter(getApplication(),customerNameList);
//            customer_spinner.setAdapter(customer_adapter);
            loadingBar.dismiss();
        }
    }


    private class Result_Task extends AsyncTask<Void,Void,ArrayList<Acc_PartyDueStatement_Result_Entity>> {
        ProgressDialog loadingBar;
        @Override
        protected void onPreExecute() {
            loadingBar = new ProgressDialog(Party_Due_Statement_Activity.this);
//            loadingBar.setTitle("Loading...");
            loadingBar.setMessage("Please Wait for Results.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

        }

        @Override
        protected ArrayList<Acc_PartyDueStatement_Result_Entity> doInBackground(Void... voids) {
            resultList = new ArrayList<>();
            showResult_initList();
            return resultList;
        }



        @Override
        protected void onPostExecute(ArrayList<Acc_PartyDueStatement_Result_Entity> acc_partyDueStatement_entities) {
            result_adapter =new ACC_PartyDueResult_Adapter(getApplication(),resultList);
            listView.setAdapter(result_adapter);
            loadingBar.dismiss();
        }
    }

}
