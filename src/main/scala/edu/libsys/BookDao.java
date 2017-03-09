package edu.libsys;

/**
 * Created by spark on 3/9/17.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDao {
    private final String DRIVER = "org.mariadb.jdbc.Driver";
    private final String URL = "jdbc:mariadb://localhost/LIBSYS";
    private final String USER = "root";
    private final String PASSWD = "123456a";
    private Connection conn = null;

    //Test connection
    public static void main(String[] args) {
        BookDao bookDao = new BookDao();
        bookDao.closeConn();
    }

    public BookDao() {
        try {
            Class.forName(DRIVER);// load the Connecting class
            conn = DriverManager.getConnection(URL, USER, PASSWD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveRecord(String isbn, String URL) {
        try {
            String sql = "INSERT INTO ASORD_MARC(M_ISBN, URL) VALUES(?,?);";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.setString(2, URL);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConn() {
        return conn;
    }

    private void closeConn() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
