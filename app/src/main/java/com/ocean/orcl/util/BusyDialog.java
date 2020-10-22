package com.ocean.orcl.util;

import android.content.Context;


public class BusyDialog {

    private CustomProgressDialog progressDialog = null;

    public BusyDialog(Context c) {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(c);
            progressDialog.setCancelable(false);
        }
    }

    public void show() {
        try {
            progressDialog.show();
        } catch (Exception e) {

        }
    }

    public void dismis() {
       try {
           if (progressDialog != null) {
               progressDialog.dismiss();
               progressDialog = null;
           }
       }
       catch (Exception x){

       }
    }

}
