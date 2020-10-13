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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AccActivity extends AppCompatActivity {

    private Connection connection;
    ListView listView;
    private TextView name,designation,invH_textView;
    private String userName;
    private String database_Accounts,database_ViewVoucher,database_PartyDueStatement,database_LedgerPosition;
    private String access_Accounts,access_ViewVoucher,access_PartyDueStatement,access_LedgerPosition;
    private ArrayList<HRM_AccessPermission_Entity> itemNameList;
    String accTitle[] = {"Chart of Accounts","View Voucher","Party Due Statement","Ledger Position"};
    int [] accimageItem = {R.drawable.prtydue, R.drawable.expencess,R.drawable.expencess,R.drawable.expencess};

//    String accTitle[] = {"Party Due","Expencess"};
//    int [] accimageItem = {R.drawable.prtydue, R.drawable.expencess};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);
        listView = findViewById(R.id.acc_listView);
        name =findViewById(R.id.acc_p_name);
        designation =findViewById(R.id.acc_p_designation);
        invH_textView =findViewById(R.id.accHead_textview);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BroshkPlum-YzqJL.ttf");
        invH_textView.setTypeface(typeface);

        MyAdapter adapter = new MyAdapter(this,accTitle,accimageItem);
//        MyAdapter adapter = new MyAdapter(this,accTitle);
        listView.setAdapter(adapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(position ==0 ){
                   Toast.makeText(AccActivity.this,"on progress", Toast.LENGTH_SHORT).show();
               }
               else if(position ==1 ){
                   if(database_ViewVoucher.equals("View Voucher") && access_ViewVoucher.equals("0")){
                       Toast.makeText(AccActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                       Log.d("voucher","--------if---"+database_ViewVoucher+"=="+access_ViewVoucher);
                   }else{
                       Log.d("voucher","--------else---"+database_ViewVoucher+"=="+access_ViewVoucher);
                       Intent intent = new Intent(getApplicationContext(),ACC_View_Voucher.class);
                       startActivity(intent);
                   }

               }else if(position ==2 ){
                   if(database_PartyDueStatement.equals("Party Due Statement") && access_PartyDueStatement.equals("0")){
                       Toast.makeText(AccActivity.this,"You Can't Access",Toast.LENGTH_SHORT).show();
                   }else{
                       Intent intent = new Intent(getApplicationContext(),Party_Due_Statement_Activity.class);
                       startActivity(intent);
                   }
               }else if(position ==3 ){
                   Toast.makeText(AccActivity.this,"on progress",Toast.LENGTH_SHORT).show();
               }
           }
       });
        getLoginValueShowHeader2();
        OptionEnableDisable();

    }
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String title[];
        int img[];


        MyAdapter( Context context,String title[] ,int imgs[]) {
            super(context, R.layout.acclistview,R.id.acc_textView,title);
            this.context =context;
            this.title =title;
            this.img =imgs;
        }
//MyAdapter( Context context,String title[]) {
////    super(context, R.layout.acclistview,R.id.acc_textView,title);
////    this.context =context;
////    this.title =title;
////}


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.acclistview,parent,false);
            ImageView images = row.findViewById(R.id.imgIcon_acc);
            TextView mytitle =row.findViewById(R.id.acc_textView);

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
    private void OptionEnableDisable(){

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
                database_ViewVoucher = itemNameList.get(15).getTitle_name();
                database_PartyDueStatement =itemNameList.get(16).getTitle_name();
                access_ViewVoucher =itemNameList.get(15).getAccess();
                access_PartyDueStatement = itemNameList.get(16).getAccess();
//                access_PartyDueStatement = itemNameList.get(16).getAccess();
//                access_LedgerPosition = itemNameList.get(17).getAccess();

            }

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(AccActivity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
}
