/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 *
 * @author an0other
 */
public class OrderDAO {

    public static final String CREATE_ORDER = "INSERT INTO orders OUTPUT INSERTED.id VALUES (?, ?, ?, ?)";
    public static final String GET_ALL_ORDER_USER = "select * from orders where username =? order by date desc";
    public static final String GET_ALL_ORDER_ADMIN = "select * from orders order by date desc";
    public static final String UPDATE_STATUS = "update orders set status=? where id=?";
    public static final String DELETE_ORDER = "delete from orders where id=?";
    public static final String GET_ORDER_STATUS_BY_ID="select status from orders where id=?";
    
    public int createOrder(OrderDTO order) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int id = 0;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(CREATE_ORDER);
            ps.setString(1, order.getUsername());
            ps.setDouble(2, order.getTotal_price());
            ps.setString(3, order.getStatus());
            ps.setTimestamp(4, Timestamp.valueOf(order.getDate()));
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return id;
    }

    public List<OrderDTO> getAllOrder(String username) {
        List<OrderDTO> list = new ArrayList<OrderDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            if ("admin".equals(username)) {
                ps = conn.prepareStatement(GET_ALL_ORDER_ADMIN);
            } else {
                ps = conn.prepareStatement(GET_ALL_ORDER_USER);
                ps.setString(1, username);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String user=rs.getString("username");
                double total = rs.getDouble("total_price");
                String status = rs.getString("status");
                Timestamp ts = rs.getTimestamp("date");
                LocalDateTime date = ts.toLocalDateTime();
                list.add(new OrderDTO(id, user, total, status, date));
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return list;
    }

    public boolean updateStatus(String status, int order_id) {
        boolean isDone = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(UPDATE_STATUS);
            ps.setString(1, status);
            ps.setInt(2, order_id);

            int rowsAffected = ps.executeUpdate();

            isDone = rowsAffected > 0;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }

    public boolean deleteOrder(int id) {
        boolean isDone = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(DELETE_ORDER);
            ps.setInt(1, id);
            
            int rowsAffected=ps.executeUpdate();
            
            isDone=rowsAffected>0;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }
    
    public String getOrderStatus(int id){
        String status=null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(GET_ORDER_STATUS_BY_ID);
            ps.setInt(1, id);
            
            rs=ps.executeQuery();
            
            if (rs.next()){
                status=rs.getString("status");
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return status;
    }
}
