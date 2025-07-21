/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DbUtils;
import jakarta.servlet.http.HttpServletRequest;
import util.EncryptUtils;

/**
 *
 * @author an0other
 */
public class UserDAO {

    public static final String GET_USER_BY_USERNAME = "select * from users where username=?";
    public static final String CREATE_NEW_USER = "insert into users (username, password, email, full_name, role, status, code) values (?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_PASSWORD = "update users set password=? where username=? an status=1";
    public static final String IS_USER_INFORMATION_EXIST = "select * from users where ?=?";
    public static final String ACTIVATE_ACCOUNT = "update users set status=1 where username=? and status=0";

    public UserDTO getUserByUsername(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = GET_USER_BY_USERNAME;
        UserDTO user = null;
        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            rs = ps.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                String email = rs.getString("email");
                String fullname = rs.getString("full_name");
                String role = rs.getString("role");
                String status_string = rs.getString("status");
                boolean status = "1".equals(status_string);
                user = new UserDTO(username, password, email, fullname, role, status);
            }
        } catch (Exception e) {
            System.err.print(e);
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return user;
    }

    public boolean login(String username, String password) {
        UserDTO user = this.getUserByUsername(username);

        if (user != null && user.getPassword().equals(EncryptUtils.encryptSHA256(password)) && user.isStatus()) {
            return true;
        }
        return false;
    }

    public boolean isExist(String para, String value) {
        boolean ans = false;
        String sql = "select * from users where " + para + "=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, value);

            rs = ps.executeQuery();
            if (rs.next()) {
                ans = true;
            }
        } catch (Exception e) {
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return ans;
    }

    public boolean createNewUser(UserDTO user, HttpServletRequest request) {
        boolean isDone = false;
        boolean continuous = true;
        if (isExist("username", user.getUsername())) {
            //get message
            request.setAttribute("UserNameAlert", "This username is already exist!");
            continuous = false;
        }
        if (isExist("email", user.getEmail())) {
            //get message
            request.setAttribute("EmailAlert", "This email is already exist!");
            continuous = false;
        }
        if (continuous == false) {
            return false;
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(CREATE_NEW_USER);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullname());
            ps.setString(5, user.getRole());
            ps.setInt(6, 0);
            ps.setString(7, user.getCode());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isDone = true;
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }

    public boolean updatePassword(UserDTO user) {
        boolean isDone = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(UPDATE_PASSWORD);
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getUsername());

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

    public boolean isUserInformationExist(String part, String value) {
        boolean ok = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(IS_USER_INFORMATION_EXIST);
            ps.setString(1, part);
            ps.setString(2, value);

            rs = ps.executeQuery();

            if (rs.next()) {
                ok = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return ok;
    }

    public boolean activateAccount(String username) {
        boolean isDone = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(ACTIVATE_ACCOUNT);
            ps.setString(1, username);

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

    public boolean verifyAccount(String code) {
        boolean isDone = false;
        String sql = "update users set status=1, code=null where code=? and status=0";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, code);

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

    public boolean isUsernameExist(String username) {
        boolean isDone = false;
        String sql = "select * from users where username=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            if (rs.next()) {
                isDone = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }

    public boolean updateCode(String username, String code) {
        boolean isDone = false;
        String sql = "update users set code=? where username=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(2, username);
            ps.setString(1, code);

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

    public UserDTO getUserByCode(String code) {
        UserDTO user = null;
        String sql = "select username, password, email, full_name, role, status from users where code=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, code);

            rs = ps.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String fullname = rs.getString("full_name");
                String role = rs.getString("role");
                String status_string = rs.getString("status");
                boolean status = "1".equals(status_string);
                user = new UserDTO(username, password, email, fullname, role, status);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return user;
    }

    public boolean updateCodetoNull(String code) {
        boolean isDone = false;
        String sql = "update users set code=null where code=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, code);

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

    public boolean isEmailExist(String email) {
        boolean isDone = false;
        String sql = "select * from users where email=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            rs = ps.executeQuery();

            if (rs.next()) {
                isDone = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }

    public boolean isEmailExistWithCode(String email) {
        boolean isDone = false;
        String sql = "select * from users where email=? and code is not null";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            rs = ps.executeQuery();

            if (rs.next()) {
                isDone = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }

    public boolean isUsernameExistWithCode(String username) {
        boolean isDone = false;
        String sql = "select * from users where username=? and code is not null";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            if (rs.next()) {
                isDone = true;
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            DbUtils.close(conn, ps, rs);
        }
        return isDone;
    }

    public boolean updateRegisterInformationWithEmail(UserDTO user) {
        boolean isDone = false;
        String sql = "update users set username=?,  password=?, full_name=?, code=? where email=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getCode());
            ps.setString(5, user.getEmail());

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
}
