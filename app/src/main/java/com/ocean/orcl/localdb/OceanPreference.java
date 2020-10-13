package com.ocean.orcl.localdb;

import android.content.Context;
import android.content.SharedPreferences;

public class OceanPreference {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;


    public OceanPreference(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("localnfo",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setRegistrationStatusInPrefarence(Boolean status){
        editor.putBoolean("reg_status",status);
        editor.commit();
    }

    public Boolean getRegistrationStatusFromPrefarence(){
        return  sharedPreferences.getBoolean("reg_status",true);
    }

}
