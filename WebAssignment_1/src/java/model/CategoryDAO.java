/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.DbUtils;

/**
 *
 * @author an0other
 */
public class CategoryDAO {

    public static final String GET_CATEGORY_BY_NAME = "select * from categories where name=?";
    public static final String notExistId = "-1010";
    public static final String GET_CATEGORY_BY_ID = "select * from categories where category_id=?";
    public static final String CREATE_NEW_CATEGORY="insert into categories values(?, ?)";
    public static final String GET_ALL_CATEGORY="select * from categories";
    
    public boolean isCategoryNameExist(String name){
        boolean isExist = false;
        String sql = GET_CATEGORY_BY_NAME;
        Connection conn=null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn=DbUtils.getConnection();
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);

            rs = ps.executeQuery();

            if (rs.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isExist;
    }

    public boolean isCategoryIDExist(String id) {
        boolean isExist = false;
        String sql = GET_CATEGORY_BY_ID;
        Connection conn=null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn=DbUtils.getConnection();
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isExist;
    }
    
    public boolean createCategory(CategoryDTO category){
        boolean isDone=false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        String sql=CREATE_NEW_CATEGORY;
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(sql);
            ps.setString(1, category.getId());
            ps.setString(2, category.getName());
            
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
    
    public List<CategoryDTO> getAllCategory(){
        List<CategoryDTO> list=new ArrayList<CategoryDTO>();
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(GET_ALL_CATEGORY);
            
            rs=ps.executeQuery();
            
            while (rs.next()){
                String id=rs.getString("category_id");
                String name=rs.getString("name");
                list.add(new CategoryDTO(id, name));
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return list;
    }
    
    public Map<Integer, String> getAllCategoryMap(){
        Map<Integer, String> map=new HashMap<>();
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        
        try {
            conn=DbUtils.getConnection();
            
            ps=conn.prepareStatement(GET_ALL_CATEGORY);
            
            rs=ps.executeQuery();
            
            while (rs.next()){
                int id=rs.getInt("category_id");
                String name=rs.getString("name");
                map.put(id, name);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return map;
    }
}
