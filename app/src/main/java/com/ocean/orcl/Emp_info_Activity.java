package com.ocean.orcl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ocean.orcl.adapter.CustomEmpInfoAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Emp_info_Activity extends AppCompatActivity implements CustomEmpInfoAdapter.SelectedEmployee {
    private Connection connection;
    private Toolbar jEmp_info_ToolBarId;
    private CustomEmpInfoAdapter adapter;

    private ArrayList<EmpInfo_Entity> empInfoItems = new ArrayList<EmpInfo_Entity>();

    private RecyclerView empView;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_info);
        empView = findViewById(R.id.empInfo_listView);
        search = findViewById(R.id.search_empInfo);
        jEmp_info_ToolBarId = findViewById(R.id.emp_info_ToolBarId);
        this.setSupportActionBar(jEmp_info_ToolBarId);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection", "=============emp_DB========Connect===========");
            if (connection != null) {
                empInfoItems = new ArrayList<EmpInfo_Entity>();
            }

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select V_PERSON_NO, V_FNAME, V_LNAME, V_DEPT_NAME, V_DESIG_NAME, V_PHONE_MOBILE, V_EMAIL_OFFICIAL\n" +
                    "from BAS_PERSON\n" +
                    "where N_ACTIVE_FLAG=1\n" +
                    "and N_PERSON_TYPE=0\n" +
                    "and V_PERSON_NO<>'ADMINISTRATOR'\n" +
                    "order by v_fname");

            while (rs.next()) {

                empInfoItems.add(new EmpInfo_Entity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));

            }

            empView.setLayoutManager(new LinearLayoutManager(Emp_info_Activity.this));
            empView.addItemDecoration(new DividerItemDecoration(Emp_info_Activity.this,DividerItemDecoration.VERTICAL));
            adapter = new CustomEmpInfoAdapter(empInfoItems, (CustomEmpInfoAdapter.SelectedEmployee) Emp_info_Activity.this);
            empView.setAdapter(adapter);
            search.setActivated(true);
            search.setQueryHint("Search here...");
            search.onActionViewExpanded();
            search.setIconified(false);
            search.setSubmitButtonEnabled(true);
            search.clearFocus();
//            search.setIconifiedByDefault(false);


            connection.close();


        }catch (Exception e) {

            Toast.makeText(Emp_info_Activity.this, "" + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Emp_info_Activity.this.adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public void getSelectedEmployee(EmpInfo_Entity emp_info) {
        Intent intent = new Intent(getApplicationContext(),EmpInfo_detiles_Activity.class);
        intent.putExtra("person_number",emp_info.getPersong_no());
        startActivity(intent);
    }

    @Override
    public void getSelectedNumber(String phone) {
       // Toast.makeText(this, "Number: "+phone, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }
}
