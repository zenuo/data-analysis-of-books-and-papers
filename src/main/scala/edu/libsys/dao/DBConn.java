package edu.libsys.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    private final String DRIVER = "org.mariadb.jdbc.Driver";
    private final String URL = "jdbc:mariadb://localhost/LIBSYS";
    private final String USER = "spark";
    private final String PASSWD = "123456a";

    //public database connection
    public static Connection conn = null;

    //test
    public static void main(String[] args) {
        DBConn dbConn = new DBConn();
        try {
            if (!conn.isClosed()) {
                System.out.println("Database connection is OK.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbConn.close();
    }

    //constructor; connect to db
    public DBConn() {
        if (conn == null) {
            try {
                Class.forName(DRIVER);// load the Connecting class
                conn = DriverManager.getConnection(URL, USER, PASSWD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //close connection
    private void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
