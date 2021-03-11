package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.excellenceict.ocean_erp.util.BusyDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChangePassWord_Activity extends AppCompatActivity {
    private Button buttonPassChange;
    private BusyDialog mBusyDialog;
    private Connection connection;
    private String userName, confirmOldPass, newPass,databaseErrorMassage;
    TextInputEditText oldPassEditText, newPassEditText, recoberPassEditText;
    TextView errorText;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        //get store user name.....
        SharedPreferences sharedPreferences = getSharedPreferences(Login_Activity.globalPreference, MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "user name found");
        confirmOldPass = sharedPreferences.getString("passwordKew", "user password found");
        handler = new Handler();

        buttonPassChange = findViewById(R.id.passwordBtn);
        oldPassEditText = findViewById(R.id.old_password);
        newPassEditText = findViewById(R.id.newPassword);
        recoberPassEditText = findViewById(R.id.confirmPass);
        errorText =findViewById(R.id.errorMessage);
        errorText.setVisibility(View.GONE);

        //controll toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.changePass_toolbar);
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

        buttonPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputOldPass = oldPassEditText.getText().toString();
                newPass = newPassEditText.getText().toString();
                String confirmPass = recoberPassEditText.getText().toString();
                Log.d("passsssssssss","========="+newPass+"========"+confirmPass);

                if (confirmOldPass.equals(userInputOldPass)) {
                    if(confirmOldPass.equals(newPass) ){
                        Toast.makeText(ChangePassWord_Activity.this, "old and new password are same", Toast.LENGTH_SHORT).show();
                    }else {
                        if (newPass.equals(confirmPass) && !newPass.isEmpty() && !confirmPass.isEmpty()) {

                            new ValidCheckPassword().execute();
                        } else {
                            Toast.makeText(ChangePassWord_Activity.this, "New password mismatch", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(ChangePassWord_Activity.this, "old password mismatch", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private class ValidCheckPassword  extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            mBusyDialog = new BusyDialog(ChangePassWord_Activity.this);
            mBusyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery("select erp.pkg$sec.fnc$check_password_strength (1, '"+newPass+"') from dual");

                while (rs.next()) {

                    databaseErrorMassage= rs.getString(1);
                    Log.d("EEEEEEEEEEEEEE","=====error=====>> "+databaseErrorMassage);

                }

                mBusyDialog.dismis();
                connection.close();
            }catch (Exception e) {

                mBusyDialog.dismis();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(databaseErrorMassage.equals("VALID")){
                errorText.setVisibility(View.GONE);
               final AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassWord_Activity.this);
                builder.setIcon(R.drawable.ic_changepass);
                builder.setTitle("Valid Password");
                builder.setMessage("Do you want to change password?" );
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Changepasword().execute();
                        Intent intent = new Intent(ChangePassWord_Activity.this, Login_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }else {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(databaseErrorMassage);
                errorText.setTextColor(Color.parseColor("#FF0000"));
                mBusyDialog.dismis();
            }
        }
    }
    private class Changepasword extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mBusyDialog = new BusyDialog(ChangePassWord_Activity.this);
            mBusyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {

                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();

                Statement stmt = connection.createStatement();

                String sql = " update SEC_USER \n" +
                        "    set V_PASSWORD = PKG$SEC.FNC$ENCRYPT('" + newPass + "'),\n" +
                        "    N_PASS_CHANGED_FLAG=1,\n" +
                        "    N_PASSWORD_CHANGE_DATE = sysdate \n" +
                        "    where v_user_name = '" + userName + "'";

                stmt.executeUpdate(sql);
                connection.commit();
                mBusyDialog.dismis();
                connection.close();
            } catch (Exception e) {

                mBusyDialog.dismis();
                e.printStackTrace();
            }


            return null;
        }
    }
}