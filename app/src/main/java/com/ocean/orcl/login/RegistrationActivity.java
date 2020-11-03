package com.ocean.orcl.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.ocean.orcl.Billinvoice_item_Entity;
import com.ocean.orcl.HRM_AccessPermission_Entity;
import com.ocean.orcl.LoginActivity;
import com.ocean.orcl.R;
import com.ocean.orcl.Test_Activity;
import com.ocean.orcl.localdb.OceanPreference;
import com.ocean.orcl.util.BusyDialog;
import com.ocean.orcl.util.NetworkHelpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    private static String TAG ="RegistrationActivity";
    private BusyDialog busyDialog;
    private Context context;
    private Connection connection;

    private CountryCodePicker ccp;
    private EditText phoneText, codeText;
    private Button continueNextButton;
    private String checker="", phoneNumber="",otpPhoneNUmber;
    RelativeLayout relativeLayout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResentToken;
    private ProgressDialog loadingBar;
    private TextView jCompLogoName;
    private WebView jUpdateNewsReg;

    private OceanPreference preference;
    private String checkNumberUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        phoneText = findViewById(R.id.phoneText);
        codeText = findViewById(R.id.codeText);
        continueNextButton = findViewById(R.id.continueNextButton);
        relativeLayout = findViewById(R.id.phoneAuth);
        phoneText = findViewById(R.id.phoneText);
        jCompLogoName = findViewById(R.id.compLogoName);
        jUpdateNewsReg = findViewById(R.id.updateNewsReg);

        context = RegistrationActivity.this;

        preference = new OceanPreference(RegistrationActivity.this);

        if (preference.getRegistrationStatusFromPrefarence()) {

            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else {


        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);


        //for web current news
        jUpdateNewsReg.loadUrl("http://www.excellenceict.com/web-marketing");
        WebSettings settings = jUpdateNewsReg.getSettings();
        settings.setJavaScriptEnabled(true);
        jUpdateNewsReg.setWebViewClient(new WebViewClient());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BroshkPlum-YzqJL.ttf");
        jCompLogoName.setTypeface(typeface);

        continueNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                otpPhoneNUmber = phoneText.getText().toString();




                if (continueNextButton.getText().equals("Submit") || checker.equals("Code Sent")) {

                    String verificationCode = codeText.getText().toString();
                    if (verificationCode.equals("")) {
                        Toast.makeText(RegistrationActivity.this, "Please write verification first", Toast.LENGTH_SHORT).show();
                    } else {

                        loadingBar.setTitle("Code Verification");
                        loadingBar.setMessage("Pease wait, while we are verifying your code");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                        signInWithPhoneAuthCredential(credential);
                    }


                } else {
                    phoneNumber = ccp.getFullNumberWithPlus();

                    if (!phoneNumber.equals("")) {

                        loadingBar.setTitle("Phone Number Verification");
                        loadingBar.setMessage("Pease wait, while we are verifying your phone number");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();


                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, RegistrationActivity.this, mCallbacks);

                    } else {
                        Toast.makeText(RegistrationActivity.this, "Please write valid phone number.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(RegistrationActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
                continueNextButton.setText("Continue");
                codeText.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mVerificationId = s;
                mResentToken = forceResendingToken;

                relativeLayout.setVisibility(View.GONE);
                checker = "Code Sent";
                continueNextButton.setText("Submit");
                codeText.setVisibility(View.VISIBLE);
                loadingBar.dismiss();
                Toast.makeText(RegistrationActivity.this, "Code hase been sent, please check.", Toast.LENGTH_SHORT).show();
            }
        };


    }

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Congratulations", Toast.LENGTH_SHORT).show();
                            //sendUserToMainActivityes();

                            if(NetworkHelpers.isNetworkAvailable(context)){
                                new UserPhoneNumberStore().execute();
//                                check_UserNumber();

                            }else {
                                Toast.makeText(context, R.string.alertInternet, Toast.LENGTH_SHORT).show();
                            }



                        } else {
                            loadingBar.dismiss();
                            String e  = task.getException().toString();
                            Toast.makeText(RegistrationActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void sendUserToMainActivityes(){

        preference.setRegistrationStatusInPrefarence(true);
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
    private void check_UserNumber(){

        try {

            connection = com.ocean.orcl.ODBC.Db.createConnection();
            Log.d("connection","================Test DB==Connected==========="+otpPhoneNUmber);

            if(connection != null){

                Statement stmt=connection.createStatement();
                String query = "select pkg$sec.fnc$check_otp_log ('"+otpPhoneNUmber+"') a from dual";

                ResultSet rs=stmt.executeQuery(query);

                while(rs.next()) {
                    Log.d("value1","==========1========="+rs.getString(1));
                    checkNumberUser =rs.getString(1);
//                    if (checkNumberUser.equals("0")){
//                        UserNumberInsert();
//                        sendUserToMainActivityes();
//                        loadingBar.dismiss();
//                    }else {
//                        sendUserToMainActivityes();
//                        loadingBar.dismiss();
//                        Toast.makeText(context, "already get your number", Toast.LENGTH_SHORT).show();
//                    }

                }
            }

            connection.close();

        }
        catch (Exception e) {

            Toast.makeText(RegistrationActivity.this, " " + e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }
    private void UserNumberInsert(){
        try {
            connection = com.ocean.orcl.ODBC.Db.createConnection();
            if (connection != null) {
            }
            Statement stmt = connection.createStatement();
            String sql = "begin pkg$sec.prc$insert_otp_log ('"+otpPhoneNUmber+"'); end;";
            stmt.executeUpdate(sql);
            Log.d("success","======Success Insert number========");
            connection.commit();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private class UserPhoneNumberStore extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            busyDialog = new BusyDialog(context);
            busyDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            check_UserNumber();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (checkNumberUser.equals("0")){
                UserNumberInsert();
                sendUserToMainActivityes();
                loadingBar.dismiss();
            }else {
                sendUserToMainActivityes();
                loadingBar.dismiss();
//                Toast.makeText(context, "already get your number", Toast.LENGTH_SHORT).show();
            }
            busyDialog.dismis();
        }
    }
}