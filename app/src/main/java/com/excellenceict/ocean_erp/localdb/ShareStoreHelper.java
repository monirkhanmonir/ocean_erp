package com.excellenceict.ocean_erp.localdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ShareStoreHelper {

        public static String getPref(String key, Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(key, "not found");

    }
    public static int getPrefInt(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("userId", 404);

    }
}
