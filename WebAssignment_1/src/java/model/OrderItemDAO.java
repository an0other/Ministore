/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.DbUtils;

/**
 *
 * @author an0other
 */
public class OrderItemDAO {
    public static final String CREATE_ORDER_ITEM="insert into order_items values(?, ?, ?, ?)";
    public static final String GET_ORDER_ITEM_BY_ORDERID="select order_items.order_item_id, order_items.order_id, order_items.price, order_items.product_id, order_items.quantity, products.name, products.image_url from order_items inner join products on order_items.product_id=products.product_id where order_items.order_id=?";
    public static final String UPDATE_ITEM="update order_items set quantity=? where order_item_id=?";
    
    public boolean createOrderItem(List<OrderItemDTO> list, int order_id){
        boolean isDone=false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(CREATE_ORDER_ITEM);
            
            for (OrderItemDTO i: list){
                ps.setString(1, i.getProduct_id());
                ps.setInt(2, i.getQuantity());
                ps.setDouble(3, i.getPrice());
                ps.setInt(4, order_id);
                
                ps.addBatch();
            }
            ps.executeBatch();
            isDone=true;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }
    
    public List<OrderItemDTO> getOrderItemsByOrderID(int order_id){
        List<OrderItemDTO> order_list=new ArrayList<>();
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(GET_ORDER_ITEM_BY_ORDERID);
            ps.setInt(1, order_id);
            
            rs=ps.executeQuery();
            
            while (rs.next()){
                int id=rs.getInt("order_item_id");
                double price=rs.getDouble("price");
                String product_id=rs.getString("product_id");
                int quantity=rs.getInt("quantity");
                String name=rs.getString("name");
                String img=rs.getString("image_url");
                order_list.add(new OrderItemDTO(id, product_id, quantity, price, order_id, name, img));
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return order_list;
    }
    
    public boolean updateItem(List<OrderItemDTO> list){
        boolean isDone=false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        int rowsAffected=0;
        
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(UPDATE_ITEM);
            for (OrderItemDTO i: list){
                ps.setInt(1, i.getQuantity());
                ps.setInt(2, i.getId());
                rowsAffected+=ps.executeLargeUpdate();
            }
            isDone=(rowsAffected==list.size());
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }
}
