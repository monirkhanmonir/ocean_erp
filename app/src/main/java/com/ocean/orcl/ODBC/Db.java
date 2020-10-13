package com.ocean.orcl.ODBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {

    private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@202.125.75.236:1521:orcl";
//    private static final String DEFAULT_URL = "jdbc:oracle:thin:@192.168.0.8:1521:orcl";
    private static final String DEFAULT_USERNAME = "erp";
    private static final String DEFAULT_PASSWORD = "erp";

    private Connection connection;

//
    //<<<<<<<<<<<<<<<<<<..........................use Oncreate method this lines...................>>>>>>


//     if (android.os.Build.VERSION.SDK_INT > 9) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//    }
//     try {
//        this.connection = createConnection();
//        Toast.makeText(OrclActivity.this, "Connected",
//                Toast.LENGTH_SHORT).show();
//        Statement stmt=connection.createStatement();
//        StringBuffer stringBuffer = new StringBuffer();
//        ResultSet rs=stmt.executeQuery("select * from cat");
//
//        while(rs.next()) {
//            stringBuffer.append( rs.getString(1)+"\n");
//        }
//
////            tv.setText(stringBuffer.toString());
//        connection.close();
//    }
//        catch (Exception e) {
//
//        Toast.makeText(OrclActivity.this, ""+e,
//                Toast.LENGTH_SHORT).show();
//        e.printStackTrace();
//    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<..........................use Oncreate method this lines...................>>>>>>


    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

}
