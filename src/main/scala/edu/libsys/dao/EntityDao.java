package edu.libsys.dao;

import edu.libsys.entity.Item;

import java.sql.PreparedStatement;

/**
 * Created by spark on 3/11/17.
 */
public class EntityDao {

    private DBConn Conn = new DBConn();

    private void saveExample(String p1, String p2) {
        try {
            String sql = "INSERT INTO TABLE(M_ISBN, URL) VALUES(?,?);";
            PreparedStatement pstmt = Conn.conn.prepareStatement(sql);
            pstmt.setString(1, p1);
            pstmt.setString(2, p2);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("");
            e.printStackTrace();
        }
    }

    private void saveItem(Item item) {
        try {
            String sql = "INSERT INTO ITEM VALUES(?,?,?);";
            PreparedStatement pstmt = Conn.conn.prepareStatement(sql);
            pstmt.setInt(1, item.getProp_id());
            pstmt.setInt(2, item.getCount());
            pstmt.setInt(3, item.getCount());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Item insert ok");
            e.printStackTrace();
        }
    }

    private void updateItem(Item item) {
        try {
            String sql = "UPDATE ;";
            PreparedStatement pstmt = Conn.conn.prepareStatement(sql);
            pstmt.setInt(1, item.getProp_id());
            pstmt.setInt(2, item.getCount());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Item insert ok");
            e.printStackTrace();
        }
    }

    //private void savItemInto
}
