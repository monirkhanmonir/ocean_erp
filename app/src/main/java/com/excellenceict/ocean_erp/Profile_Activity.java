package com.excellenceict.ocean_erp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.excellenceict.ocean_erp.util.BusyDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Profile_Activity extends AppCompatActivity {
    private int userID;
    private String error;
    private BusyDialog mBusyDialog;
    private Connection connection;
    private Button updatebtn;
    private TextView salutaionT, f_nameT, l_nameT, nick_nameT, statusT,
            blood_GroupT, religionT, place_birthT, dateOfBirthT,
            mobile_noT, emergency_contactT, presonal_EmailT, present_addressT,
            permanent_addressT, national_IdT, passport_noT, drivingLicence_noT,
            tin_noT, join_DateT, departmentT, designationT, pabxT, reportT,
            login_timeT, logout_timeT, base_locationT, office_emailT;

    private String root, fName, lName, nickName, gender, blood, reli, birth, birthDate,
            mbl, emContact, email, address, permanent_address, national_Id, passport_no,
            drivingLicence_no, tin_no, join_Date, department, designation, pabx, report,
            login_time, logout_time, base_location, office_email;

    private RelativeLayout status_relativeLayout, religon_relativeLayout,
            mobileNo_relativeLayout, emergency_relativeLayout,
            personalEmail_relativeLayout, paresentAddress_relativeLayout,
            placeBirth_relativeLayout, bloodGroup_relativeLayout, userName_RelativeLayout;

    private TextInputEditText statusEdit, religionEdit, birthEdit,
            mobileNoEdit, emergencyEdit, personalEmailEdit, presentAddressEdit,
            placeBirthEdit, bloodGroupEdit;

    private TextView satatus_hint, bloodGroup_hint, religion_hint, mobile_hint,
            emergency_hint, personalEmail_hint, placeBirth_hint, presentAddress_hint;
    private String changeBlood, changeReligion, changeStatus, changeMobile,
            changeEmergencyContact, changeEmail, changePesonalAddrss, changeBirth;
    private TextView mobileNoError, statusError;
    private RelativeLayout birthDate_blank, parmanentAddress_blank, nationalId_blank,
            passportNo_blank, drivingLicence_blank, tinNo_blank, joinDate_blank,
            department_blank, designation_blank, pabx_blnk, report_blank,
            loginTime_blank, logoutTime_blank, office_base_blank, emailOffice_blank;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        //get user_ID(value) in SharedPreferences...............
        SharedPreferences sharedPreferences = getSharedPreferences(Login_Activity.globalPreference, MODE_PRIVATE);
        userID = sharedPreferences.getInt("userId", 500);
//        Toast.makeText(this, "user id: " + userID, Toast.LENGTH_SHORT).show();

        updatebtn = findViewById(R.id.prof_updateBtn);

        salutaionT = findViewById(R.id.prof_salutation);
        f_nameT = findViewById(R.id.prof_first_name);
        l_nameT = findViewById(R.id.prof_lastName);
        nick_nameT = findViewById(R.id.prof_nickName);
        statusT = findViewById(R.id.prof_status);

        blood_GroupT = findViewById(R.id.prof_blood_Grp);
        religionT = findViewById(R.id.prof_religion);
        place_birthT = findViewById(R.id.prof_birth);
        dateOfBirthT = findViewById(R.id.prof_birthDate);
        mobile_noT = findViewById(R.id.prof_mobileNO);

        emergency_contactT = findViewById(R.id.prof_emergencyContact);
        presonal_EmailT = findViewById(R.id.prof_personalEmail);
        present_addressT = findViewById(R.id.prof_presentAdrs);
        permanent_addressT = findViewById(R.id.prof_permanentAddress);
        national_IdT = findViewById(R.id.prof_nationalID);

        passport_noT = findViewById(R.id.prof_passportNo);
        drivingLicence_noT = findViewById(R.id.prof_drivingNo);
        tin_noT = findViewById(R.id.prof_tinNo);
        join_DateT = findViewById(R.id.prof_JoinDateNo);
        departmentT = findViewById(R.id.prof_departmentName);

        designationT = findViewById(R.id.prof_designationName);
        pabxT = findViewById(R.id.prof_pabx);
        pabx_blnk = findViewById(R.id.pabx_bg_error);
        reportT = findViewById(R.id.prof_report);
        login_timeT = findViewById(R.id.prof_loginTime);
        logout_timeT = findViewById(R.id.prof_logOutTime);

        base_locationT = findViewById(R.id.prof_baseLocaton);
        office_emailT = findViewById(R.id.prof_officeEmail);

        birthDate_blank = findViewById(R.id.birthDate_error);
        birthDate_blank.setVisibility(View.GONE);

        parmanentAddress_blank = findViewById(R.id.permanentAddress_error);
        parmanentAddress_blank.setVisibility(View.GONE);

        nationalId_blank = findViewById(R.id.nationalID_error);
        nationalId_blank.setVisibility(View.GONE);

        passportNo_blank = findViewById(R.id.passportNO_error);
        passportNo_blank.setVisibility(View.GONE);

        drivingLicence_blank = findViewById(R.id.driverLicence_error);
        drivingLicence_blank.setVisibility(View.GONE);

        tinNo_blank = findViewById(R.id.tinNo_error);
        tinNo_blank.setVisibility(View.GONE);

        joinDate_blank = findViewById(R.id.joindate_error);
        joinDate_blank.setVisibility(View.GONE);

        department_blank = findViewById(R.id.department_error);
        department_blank.setVisibility(View.GONE);

        designation_blank = findViewById(R.id.designation_error);
        designation_blank.setVisibility(View.GONE);

        report_blank = findViewById(R.id.report_error);
        report_blank.setVisibility(View.GONE);

        loginTime_blank = findViewById(R.id.loginTime_error);
        loginTime_blank.setVisibility(View.GONE);

        logoutTime_blank = findViewById(R.id.logoutTime_error);
        logoutTime_blank.setVisibility(View.GONE);

        office_base_blank = findViewById(R.id.baseLine_error);
        office_base_blank.setVisibility(View.GONE);

        emailOffice_blank = findViewById(R.id.emailOffice_error);
        emailOffice_blank.setVisibility(View.GONE);

        pabx_blnk.setVisibility(View.GONE);

        userName_RelativeLayout = findViewById(R.id.prof_nameRelativeLayout);
        status_relativeLayout = findViewById(R.id.prof_layout_statusIcon);
        statusEdit = findViewById(R.id.prof_statusEditText);
        satatus_hint = findViewById(R.id.prof_statusHint);
        statusEdit.setVisibility(View.GONE);
        statusError = findViewById(R.id.status_errorText);
        statusError.setVisibility(View.GONE);

        religon_relativeLayout = findViewById(R.id.prof_religionIcon);
        religionEdit = findViewById(R.id.prof_religionEditText);
        religion_hint = findViewById(R.id.prof_religionHint);
        religionEdit.setVisibility(View.GONE);

        mobileNo_relativeLayout = findViewById(R.id.prof_mobileIcon);
        mobile_hint = findViewById(R.id.prof_mobileHint);
        mobileNoEdit = findViewById(R.id.prof_mobileEditText);
        mobileNoEdit.setVisibility(View.GONE);
        mobileNoError = findViewById(R.id.mobilenumberError);
        mobileNoError.setVisibility(View.GONE);

        emergency_relativeLayout = findViewById(R.id.prof_emergencyIcon);
        emergency_hint = findViewById(R.id.prof_emengencyHint);
        emergencyEdit = findViewById(R.id.prof_emergencyEditText);
        emergencyEdit.setVisibility(View.GONE);

        personalEmail_relativeLayout = findViewById(R.id.prof_personalEmailIcon);
        personalEmail_hint = findViewById(R.id.prof_personalEmailHint);
        personalEmailEdit = findViewById(R.id.prof_personalEmailEditText);
        personalEmailEdit.setVisibility(View.GONE);

        paresentAddress_relativeLayout = findViewById(R.id.prof_presentAddressIcon);
        presentAddress_hint = findViewById(R.id.prof_presentAddressHint);
        presentAddressEdit = findViewById(R.id.prof_presenAddressEditText);
        presentAddressEdit.setVisibility(View.GONE);

        placeBirth_relativeLayout = findViewById(R.id.prof_placeBirthIcon);
        placeBirth_hint = findViewById(R.id.prof_placeBirthHint);
        placeBirthEdit = findViewById(R.id.prof_placeBirthEditText);
        placeBirthEdit.setVisibility(View.GONE);

        bloodGroup_relativeLayout = findViewById(R.id.prof_bloodGroupIcon);
        bloodGroup_hint = findViewById(R.id.prof_bloodGroupHint);
        bloodGroupEdit = findViewById(R.id.prof_bloodGroupEditText);
        bloodGroupEdit.setVisibility(View.GONE);
        updatebtn.setVisibility(View.GONE);
        handler = new Handler();

        //controll toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.prof_profile_toolbar);
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

        new ProfileTask().execute();
        status_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusT.setVisibility(View.GONE);
                satatus_hint.setVisibility(View.GONE);
                statusEdit.setVisibility(View.VISIBLE);
                statusEdit.setText(gender);
                updatebtn.setVisibility(View.VISIBLE);
                statusError.setVisibility(View.VISIBLE);
                statusError.setText("only enter S or M");
                statusError.setTextColor(Color.parseColor("#FF0000"));

            }
        });
        religon_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                religionT.setVisibility(View.GONE);
                religion_hint.setVisibility(View.GONE);
                religionEdit.setVisibility(View.VISIBLE);
                religionEdit.setText(reli);
                updatebtn.setVisibility(View.VISIBLE);

            }
        });
        mobileNo_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_noT.setVisibility(View.GONE);
                mobile_hint.setVisibility(View.GONE);
                mobileNoEdit.setVisibility(View.VISIBLE);
                mobileNoEdit.setText(mbl);
                updatebtn.setVisibility(View.VISIBLE);

            }
        });
        emergency_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergency_contactT.setVisibility(View.GONE);
                emergency_hint.setVisibility(View.GONE);
                emergencyEdit.setVisibility(View.VISIBLE);
                emergencyEdit.setText(emContact);
                updatebtn.setVisibility(View.VISIBLE);
            }
        });
        personalEmail_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presonal_EmailT.setVisibility(View.GONE);
                personalEmail_hint.setVisibility(View.GONE);
                personalEmailEdit.setVisibility(View.VISIBLE);
                personalEmailEdit.setText(email);
                updatebtn.setVisibility(View.VISIBLE);
            }
        });
        paresentAddress_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                present_addressT.setVisibility(View.GONE);
                presentAddress_hint.setVisibility(View.GONE);
                presentAddressEdit.setVisibility(View.VISIBLE);
                presentAddressEdit.setText(address);
                updatebtn.setVisibility(View.VISIBLE);
            }
        });
        placeBirth_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_birthT.setVisibility(View.GONE);
                placeBirth_hint.setVisibility(View.GONE);
                placeBirthEdit.setVisibility(View.VISIBLE);
                placeBirthEdit.setText(birth);
                updatebtn.setVisibility(View.VISIBLE);

            }
        });
        bloodGroup_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blood_GroupT.setVisibility(View.GONE);
                bloodGroup_hint.setVisibility(View.GONE);
                bloodGroupEdit.setVisibility(View.VISIBLE);
                bloodGroupEdit.setText(blood);
                updatebtn.setVisibility(View.VISIBLE);
            }
        });
        userName_RelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, Profile_NameUpdate_Activity.class);

                intent.putExtra("salutation", root);
                intent.putExtra("fName", fName);
                intent.putExtra("lName", lName);
                intent.putExtra("nName", nickName);
                intent.putExtra("status", gender);
                intent.putExtra("religion", reli);
                intent.putExtra("mobile", mbl);
                intent.putExtra("emgContact", emContact);
                intent.putExtra("pEmail", email);
                intent.putExtra("pAddress", address);
                intent.putExtra("pBirth", birth);
                intent.putExtra("bloodG", blood);
                startActivity(intent);
                finish();
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new updateTask().execute();
            }
        });

    }

    private class ProfileTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mBusyDialog = new BusyDialog(Profile_Activity.this);
            mBusyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();
                Statement stmt = connection.createStatement();

                String query1 = "select V_SALUTATION, V_FNAME, V_LNAME, V_NICK_NAME, V_M_STATUS, \n" +
                        "V_BLOOD_GRP, V_RELIGION, V_PLACE_OF_BIRTH, to_char(D_DOB,'MON DD,RRRR')D_DOB, " +
                        "V_PHONE_MOBILE,V_PHONE_HOME, V_EMAIL_PERSONAL,\n" +
                        "V_PR_ADDR1, \n" +
                        "V_PE_ADDR1, \n" +
                        "V_NATIONAL_ID, V_PASSPORT_NO, V_DRIVINGLC_NO, V_TIN_NO, \n" +
                        "to_char(D_JOIN_DATE,'MON DD,RRRR') D_JOIN_DATE, V_DEPT_NAME, V_DESIG_NAME, V_PABX_EXT, pkg$hr.FNC$REPORTING_PERSON_NAME(N_REPORTING_TO) reports_to, \n" +
                        "V_LOGIN_TIME, V_LOGOUT_TIME, V_BASE_LOCATION,V_EMAIL_OFFICIAL \n" +
                        "from BAS_PERSON\n" +
                        "where n_person_id = '" + userID + "'";

                ResultSet rs = stmt.executeQuery(query1);
                if (rs.next()) {
                    Log.d("1", "===========" + rs.getString(1));
                    Log.d("2", "===========" + rs.getString(2));
                    Log.d("3", "===========" + rs.getString(3));
                    Log.d("4", "===========" + rs.getString(4));
                    Log.d("5", "===========" + rs.getString(5));
                    Log.d("6", "===========" + rs.getString(6));
                    Log.d("7", "===========" + rs.getString(7));
                    Log.d("8", "===========" + rs.getString(8));
                    Log.d("9", "===========" + rs.getString(9));
                    Log.d("10", "===========" + rs.getString(10));
                    Log.d("11", "===========" + rs.getString(11));
                    Log.d("12", "===========" + rs.getString(12));
                    Log.d("13", "===========" + rs.getString(13));
                    Log.d("14", "===========" + rs.getString(14));
                    Log.d("15", "===========" + rs.getString(15));
                    Log.d("16", "===========" + rs.getString(16));
                    Log.d("17", "===========" + rs.getString(17));
                    Log.d("18", "===========" + rs.getString(18));
                    Log.d("19", "===========" + rs.getString(19));
                    Log.d("20", "===========" + rs.getString(20));
                    Log.d("21", "===========" + rs.getString(21));
                    Log.d("22", "===========" + rs.getString(22));
                    Log.d("23", "===========" + rs.getString(23));
                    Log.d("24", "===========" + rs.getString(24));
                    Log.d("25", "===========" + rs.getString(25));
                    Log.d("26", "===========" + rs.getString(26));
                    Log.d("27", "===========" + rs.getString(27));


                    root = rs.getString(1);
                    fName = rs.getString(2);
                    lName = rs.getString(3);
                    nickName = rs.getString(4);
                    gender = rs.getString(5);

                    blood = rs.getString(6);
                    reli = rs.getString(7);
                    birth = rs.getString(8);
                    birthDate = rs.getString(9);
                    mbl = rs.getString(10);

                    emContact = rs.getString(11);
                    email = rs.getString(12);
                    address = rs.getString(13);
                    permanent_address = rs.getString(14);
                    national_Id = rs.getString(15);

                    passport_no = rs.getString(16);
                    drivingLicence_no = rs.getString(17);
                    tin_no = rs.getString(18);
                    join_Date = rs.getString(19);
                    department = rs.getString(20);

                    designation = rs.getString(21);
                    pabx = rs.getString(22);
                    report = rs.getString(23);
                    login_time = rs.getString(24);
                    logout_time = rs.getString(25);

                    base_location = rs.getString(26);
                    office_email = rs.getString(27);

                    mBusyDialog.dismis();

                } else {
                    mBusyDialog.dismis();
                }

            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
                mBusyDialog.dismis();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            salutaionT.setText(root);
            f_nameT.setText(fName);
            l_nameT.setText(lName);
            nick_nameT.setText("(" + nickName + ")");
            statusT.setText(gender);

            blood_GroupT.setText(blood);
            religionT.setText(reli);
            place_birthT.setText(birth);
            dateOfBirthT.setText(birthDate);
            mobile_noT.setText(mbl);

            emergency_contactT.setText(emContact);
            presonal_EmailT.setText(email);
            present_addressT.setText(address);
            permanent_addressT.setText(permanent_address);
            national_IdT.setText(national_Id);

            passport_noT.setText(passport_no);
            drivingLicence_noT.setText(drivingLicence_no);
            tin_noT.setText(tin_no);
            join_DateT.setText(join_Date);
            departmentT.setText(department);

            designationT.setText(designation);
            passport_noT.setText(passport_no);
            pabxT.setText(pabx);
            reportT.setText(report);
            login_timeT.setText(login_time);

            logout_timeT.setText(logout_time);
            base_locationT.setText(base_location);
            office_emailT.setText(office_email);

            if (pabxT.getText().toString().equals(null) || pabxT.getText().toString() == "") {
                pabx_blnk.setVisibility(View.VISIBLE);
            }
            if (dateOfBirthT.getText().toString().equals(null) || dateOfBirthT.getText().toString() == "") {
                birthDate_blank.setVisibility(View.VISIBLE);
            }
            if (permanent_addressT.getText().toString().equals(null) || permanent_addressT.getText().toString() == "") {
                parmanentAddress_blank.setVisibility(View.VISIBLE);
            }
            if (national_IdT.getText().toString().equals(null) || national_IdT.getText().toString() == "") {
                nationalId_blank.setVisibility(View.VISIBLE);
            }
            if (passport_noT.getText().toString().equals(null) || passport_noT.getText().toString() == "") {
                passportNo_blank.setVisibility(View.VISIBLE);
            }
            if (drivingLicence_noT.getText().toString().equals(null) || drivingLicence_noT.getText().toString() == "") {
                drivingLicence_blank.setVisibility(View.VISIBLE);
            }
            if (tin_noT.getText().toString().equals(null) || tin_noT.getText().toString() == "") {
                tinNo_blank.setVisibility(View.VISIBLE);
            }
            if (join_DateT.getText().toString().equals(null) || join_DateT.getText().toString() == "") {
                joinDate_blank.setVisibility(View.VISIBLE);
            }
            if (departmentT.getText().toString().equals(null) || departmentT.getText().toString() == "") {
                department_blank.setVisibility(View.VISIBLE);
            }
            if (designationT.getText().toString().equals(null) || designationT.getText().toString() == "") {
                designation_blank.setVisibility(View.VISIBLE);
            }
            if (reportT.getText().toString().equals(null) || reportT.getText().toString() == "") {
                report_blank.setVisibility(View.VISIBLE);
            }
            if (login_timeT.getText().toString().equals(null) || login_timeT.getText().toString() == "") {
                loginTime_blank.setVisibility(View.VISIBLE);
            }
            if (logout_timeT.getText().toString().equals(null) || logout_timeT.getText().toString() == "") {
                logoutTime_blank.setVisibility(View.VISIBLE);
            }
            if (base_locationT.getText().toString().equals(null) || base_locationT.getText().toString() == "") {
                office_base_blank.setVisibility(View.VISIBLE);
            }
            if (office_emailT.getText().toString().equals(null) || office_emailT.getText().toString() == "") {
                emailOffice_blank.setVisibility(View.VISIBLE);
            }

            mBusyDialog.dismis();
        }
    }

    private class updateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mBusyDialog = new BusyDialog(Profile_Activity.this);
            mBusyDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String sta = statusEdit.getText().toString();
            String mob = mobileNoEdit.getText().toString();
            String emg = emergencyEdit.getText().toString();
            String emails = personalEmailEdit.getText().toString();
            String adds = presentAddressEdit.getText().toString();
            String births = placeBirthEdit.getText().toString();
            try {
                String address2 = "N/A";
                String address3 = "N/A";
                connection = com.excellenceict.ocean_erp.ODBC.Db.createConnection();

                Statement stmt = connection.createStatement();
                Log.d("DB", "======Connected========");
                if (bloodGroupEdit.getText().toString().isEmpty()) {
                    changeBlood = blood;
                } else {
                    changeBlood = bloodGroupEdit.getText().toString();
                }
                if (religionEdit.getText().toString().isEmpty()) {
                    changeReligion = reli;
                } else {
                    changeReligion = religionEdit.getText().toString();
                }
                if (sta.isEmpty()) {
                    changeStatus = gender;
                } else {
                    changeStatus = statusEdit.getText().toString();
                }
                if (mob.isEmpty()) {
                    changeMobile = mbl;
                    Log.d("MMMMMMMMM", "====if========" + changeMobile);
                } else {
                    changeMobile = mobileNoEdit.getText().toString();
                    Log.d("MMMMMMMMM", "====else========" + changeMobile);
                }
                if (emg.isEmpty()) {
                    changeEmergencyContact = emContact;
                } else {
                    changeEmergencyContact = emergencyEdit.getText().toString();
                }
                if (emails.isEmpty()) {
                    changeEmail = email;
                } else {
                    changeEmail = personalEmailEdit.getText().toString();
                }
                if (adds.isEmpty()) {
                    changePesonalAddrss = address;
                } else {
                    changePesonalAddrss = presentAddressEdit.getText().toString();
                }
                if (births.isEmpty()) {
                    changeBirth = birth;
                    Log.d("RRRR", "===========>" + changeBirth.toLowerCase());
                    Log.d("rrr", "===========>");

                } else {
                    changeBirth = placeBirthEdit.getText().toString();
                    Log.d("nnn", "===========>");
                    Log.d("NNNN", "===========>" + changeBirth);
                }

                String sql = "Update BAS_PERSON\n" +
                        "set V_SALUTATION=upper('" + root + "'), \n" +
                        "V_FNAME=upper('" + fName + "'), \n" +
                        "V_LNAME=upper('" + lName + "'), \n" +
                        "V_NICK_NAME=upper('" + nickName + "'), \n" +
                        "V_M_STATUS=upper('" + changeStatus + "'), \n" +
                        "V_BLOOD_GRP=upper('" + changeBlood + "'), \n" +
                        "V_RELIGION=upper('" + changeReligion + "'), \n" +
                        "V_PLACE_OF_BIRTH=upper('" + changeBirth + "'), \n" +
                        "V_PHONE_MOBILE=upper('" + changeMobile + "'),\n" +
                        "V_PHONE_HOME=upper('" + changeEmergencyContact + "'), \n" +
                        "V_EMAIL_PERSONAL=lower('" + changeEmail + "'),\n" +
                        "V_PR_ADDR1=upper('" + changePesonalAddrss + "') \n" +
//                        "V_PR_ADDR2=upper('" + address2 + "'), \n" +
//                        "V_PR_ADDR3=upper('" + address3 + "')\n" +
                        "where n_person_id = '" + userID + "'";

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

        @Override
        protected void onPostExecute(Void aVoid) {
            String mobNumberGetTextView = mobile_noT.getText().toString();
            String mobNumberGetEditText = changeMobile;
            Log.d("valuessssssssss", "=============>>>> " + mobNumberGetTextView + "===" + mobNumberGetEditText);
            if (mobNumberGetEditText.matches("^(?:\\+88|88)?(01[3-9]\\d{8})$")) {
                mobileNoError.setVisibility(View.GONE);
//                mobileNoEdit.setVisibility(View.GONE);
//                new updateTask().execute();
                finish();
                startActivity(getIntent());

            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mobileNoError.setVisibility(View.VISIBLE);
//                        mobileNoEdit.setVisibility(View.VISIBLE);
                        mobileNoError.setText("number mustbe 11-14 digits");
                        mobileNoError.setTextColor(Color.parseColor("#FF0000"));
                        Log.d("tessssssss", "=============>>>> ");
                    }
                });

            }
        }
    }
}