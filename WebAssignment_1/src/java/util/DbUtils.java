/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author an0other
 */
public class DbUtils {

    private static final String DB_NAME = "WEB_MINISTORE";
    private static final String DB_USER_NAME = "sa";
    private static final String DB_USER_PASSWORD = "12345";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;databaseName=" + DB_NAME;
        conn = DriverManager.getConnection(url, DB_USER_NAME, DB_USER_PASSWORD);
        return conn;
    }

    public static void main(String[] args) {
        try {
            System.out.println(DbUtils.getConnection());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs){
        try {
            if (conn!=null) conn.close();
            if (rs !=null ) rs.close();
            if (ps!=null) ps.close();
        } catch (Exception e) {
            System.err.println(e);
        }       
    }
    
}
