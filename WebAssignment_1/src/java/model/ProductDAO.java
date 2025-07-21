/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Date;
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
public class ProductDAO {

    public static final String GET_ALL_PRODUCT_BY_CATEGORY = "select products.product_id, products.name, products.price, products.image_url, products.category_id, products.date, products.status "
            + "from products inner join categories on products.category_id=categories.category_id "
            + "where categories.name=?";
    public static final String GET_ALL_PRODUCT = "SELECT product_id, products.name, price, image_url, date, status, categories.name AS category_name, quantity FROM products INNER JOIN categories ON products.category_id = categories.category_id WHERE products.name LIKE ?";
    public static final String CREATE_NEW_PRODUCT = "insert into products (product_id, name, price, image_url, category_id, date, status, quantity) values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_PRODUCT_BY_ID = "select * from products where product_id=?";
    public static final String UPDATE_PRODUCT = "update products set name=?, price=?, image_url=?, category_id=?, status=?, quantity=? where product_id=?";
    public static final String UPDATE_QUANTITY= "update products set quantity=? where product_id=?";
    public static final String VALIDATE_PRODUCT= "select * from products where product_id=? and status=1";
    public static final String UPDATE_PRODUCT_STATUS="update products set status=1, quantity=? where product_id=?";
    
    public List<ProductDTO> selectAllProductByCategory(String category_in) {
        List<ProductDTO> list = new ArrayList<ProductDTO>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = GET_ALL_PRODUCT_BY_CATEGORY;
        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, category_in);

            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("product_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String img_url = rs.getString("image_url");
                String category = rs.getString("category_id");
                Timestamp ts = rs.getTimestamp("date");
                LocalDateTime date = ts.toLocalDateTime();
                boolean status = rs.getBoolean("status");
                int quantity=rs.getInt("quantity");
                list.add(new ProductDTO(id, name, price, img_url, category, date, status, quantity));
            }
        } catch (Exception e) {
            System.err.println(e);

        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return list;
    }

    public boolean createProduct(ProductDTO product) {
        boolean isDone = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = CREATE_NEW_PRODUCT;
        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getId());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getImg_url());
            ps.setString(5, product.getCategory());
            ps.setTimestamp(6, Timestamp.valueOf(product.getDate()));
            ps.setBoolean(7, product.isStatus());
            ps.setInt(8, product.getQuantity());
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

    public ProductDTO getProductByID(String id) {
        String sql = GET_PRODUCT_BY_ID;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductDTO product = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String img = rs.getString("image_url");
                String category = rs.getString("category_id");
                Timestamp ts = rs.getTimestamp("date");
                LocalDateTime date = ts.toLocalDateTime();
                boolean status = (rs.getInt("status") == 1) ? true : false;
                int quantity = rs.getInt("quantity");
                product = new ProductDTO(id, name, price, img, category, date, status, quantity);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return product;
    }

    public boolean isProductIDExist(String id) {
        if (this.getProductByID(id) != null) {
            return true;
        }
        return false;
    }

//    public List<ProductDTO> getAllActiveProduct(List<ProductDTO> list) {
//        List<ProductDTO> listt = new ArrayList<ProductDTO>();
//
//        for (ProductDTO i : list) {
//            if (i.isStatus()) {
//                listt.add(i);
//            }
//        }
//
//        return listt;
//    }
    public List<ProductDTO> searchProduct(String name) {
        List<ProductDTO> listt = new ArrayList<>();

        String sql = GET_ALL_PRODUCT;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("product_id");
                String p_name = rs.getString("name");
                double price = rs.getDouble("price");
                String img = rs.getString("image_url");
                String p_category = rs.getString("category_name");
                int quantity=rs.getInt("quantity");
                Timestamp ts = rs.getTimestamp("date");
                LocalDateTime date = ts.toLocalDateTime();
                boolean status = (rs.getInt("status") == 1) ? true : false;
                listt.add(new ProductDTO(id, p_name, price, img, p_category, date, status, quantity));
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return listt;
    }

    public List<ProductDTO> getAllProductByCategory(String category) {
        List<ProductDTO> list = new ArrayList<ProductDTO>();
        String sql = GET_ALL_PRODUCT_BY_CATEGORY;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, category);

            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("product_id");
                String p_name = rs.getString("name");
                double price = rs.getDouble("price");
                String img = rs.getString("image_url");
                String p_category = rs.getString("category_id");
                Timestamp ts = rs.getTimestamp("date");
                LocalDateTime date = ts.toLocalDateTime();
                boolean status = (rs.getInt("status") == 1) ? true : false;
                int quantity=rs.getInt("quantity");
                list.add(new ProductDTO(id, p_name, price, img, p_category, date, status, quantity));
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return list;
    }

    public boolean updateProduct(ProductDTO product) {
        boolean isDone = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = UPDATE_PRODUCT;
        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getImg_url());
            ps.setString(4, product.getCategory());
            ps.setBoolean(5, product.isStatus());
            ps.setInt(6, (product.isStatus()==false)?0:product.getQuantity());
            ps.setString(7, product.getId());
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

    public boolean deleteProduct(ProductDTO product) {
        product.setStatus(false);
        return this.updateProduct(product);
    }

    public boolean activateProduct(ProductDTO product) {
        product.setStatus(true);
        return this.updateProduct(product);
    }

    public List<ProductDTO> get4thLatestProduct(String categoryname) {
        List<ProductDTO> list = new ArrayList<ProductDTO>();
        String sql = "	select top 4 * from products inner join categories on products.category_id=categories.category_id where status=1 and categories.name=? order by date desc";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, categoryname);

            rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("product_id");
                String p_name = rs.getString("name");
                double price = rs.getDouble("price");
                String img = rs.getString("image_url");
                String p_category = rs.getString("category_id");
                Timestamp ts = rs.getTimestamp("date");
                LocalDateTime date = ts.toLocalDateTime();
                boolean status = (rs.getInt("status") == 1) ? true : false;
                int quantity= rs.getInt("quantity");
                list.add(new ProductDTO(id, p_name, price, img, p_category, date, status, quantity));
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return list;
    }

    public List<Integer> getProductQuantityByID(List<String> id_list){
        List<Integer> quantity_list=new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql="select quantity from products where product_id=?";
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(sql);
        
            for (String i: id_list){
                ps.setString(1, i);
                
                rs=ps.executeQuery();
                
                if (rs.next()){
                    int quantity=rs.getInt("quantity");
                    quantity_list.add(quantity);
                }
            }
            
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return quantity_list;
    }
    
    public boolean updateProductQuantity(List<Integer> quantity_list, List<String> id_list){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isDone=false;
        int rowAffected=0;
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(UPDATE_QUANTITY);
            for (int i=0;i<quantity_list.size();i++){
                ps.setInt(1, quantity_list.get(i));
                ps.setString(2, id_list.get(i));
                
                rowAffected+=ps.executeUpdate();
            }
            
            isDone=(rowAffected==quantity_list.size());
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }
    
    public ProductDTO isValidProduct(String id){
        ProductDTO product=null;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(VALIDATE_PRODUCT);
            ps.setString(1, id);
            
            rs=ps.executeQuery();
            
            if (rs.next()){
                String p_name = rs.getString("name");
                double price = rs.getDouble("price");
                String img = rs.getString("image_url");
                String p_category = rs.getString("category_id");
                Timestamp ts = rs.getTimestamp("date");
                LocalDateTime date = ts.toLocalDateTime();
                boolean status = (rs.getInt("status") == 1) ? true : false;
                int quantity= rs.getInt("quantity");
                product=new ProductDTO(id, p_name, price, img, p_category, date, status, quantity);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return product;
    }
    
    public boolean activateProduct(String id, int quantity){
        boolean isDone=false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(UPDATE_PRODUCT_STATUS);
            ps.setInt(1, quantity);
            ps.setString(2, id);
            
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
}
