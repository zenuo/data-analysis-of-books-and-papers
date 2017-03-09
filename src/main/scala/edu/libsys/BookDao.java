package edu.libsys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDao {

    private DBConn dbConn = new DBConn();

    //Test connection
    public static void main(String[] args) {
        BookDao bookDao = new BookDao();
    }

    private void saveRecord(String isbn, String URL) {
        try {
            String sql = "INSERT INTO ASORD_MARC(M_ISBN, URL) VALUES(?,?);";
            PreparedStatement pstmt = dbConn.conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.setString(2, URL);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
