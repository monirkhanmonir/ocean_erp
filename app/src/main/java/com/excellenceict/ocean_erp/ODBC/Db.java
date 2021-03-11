package com.excellenceict.ocean_erp.ODBC;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static android.content.Context.WIFI_SERVICE;

public class Db {

    private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@202.125.75.236:1522:orcl1";
//    private static final String DEFAULT_URL1 = "jdbc:oracle:thin:@192.168.0.12:1522:orcl1";
    private static final String DEFAULT_USERNAME = "erp";
    private static final String DEFAULT_PASSWORD = "erp";

    private Connection connection;

    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {

                return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
            }


    }

