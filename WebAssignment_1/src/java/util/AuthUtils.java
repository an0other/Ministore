/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.UserDTO;
/**
 *
 * @author an0other
 */
public class AuthUtils {
    public static UserDTO getCurrentUser(HttpServletRequest request){
        HttpSession session=request.getSession();
        UserDTO user=(UserDTO) session.getAttribute("user");
        return user;
    }
    
    public static boolean isActive(HttpServletRequest request){
        UserDTO user=getCurrentUser(request);
        return user.isStatus();
    }
    
    public static boolean isAdmin(HttpServletRequest request){
        UserDTO user=getCurrentUser(request);
        if (user!=null && user.getRole().equals("ADMIN")) return true;
        return false;
    }
    
    public static boolean isLoggin(HttpServletRequest request){
        UserDTO user=getCurrentUser(request);
        if (user!=null) return true;
        return false;
    }
    
    public static String getUsername(HttpServletRequest request){
        UserDTO user=getCurrentUser(request);
        return user.getUsername();
    }
    
    public static boolean isMember(HttpServletRequest request){
        UserDTO user=getCurrentUser(request);
        if (user!=null && user.getRole().equals("MEMBER")) return true;
        return false;
    }
}
