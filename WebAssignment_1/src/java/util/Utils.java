/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author an0other
 */
public class Utils {
    public static void setMessage(HttpServletRequest request, String name, String message){
        request.setAttribute(name, message);
    }
    
    public static void getKeyword(HttpServletRequest request){
        String keyword=request.getParameter("keyword");
        if (keyword==null) keyword="";
        request.setAttribute("keyword", keyword);
    }
    
    public static void getCategory(HttpServletRequest request){
        String category=request.getParameter("category");
        if (category==null) category="";
        request.setAttribute("category", category);
    }
}
