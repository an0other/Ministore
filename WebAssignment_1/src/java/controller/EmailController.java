/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.UserDAO;

/**
 *
 * @author an0other
 */
@WebServlet(name = "EmailController", urlPatterns = {"/EmailController"})
public class EmailController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url="index.jsp";
        try {
            String action=request.getParameter("action");
            if ("verifyEmail".equals(action)){
                url=handleVerifyEmail(request, response);
            } else if ("verifyForgetPassword".equals(action)){
                url=handleVerifyForgetPassword(request, response);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String handleVerifyEmail(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            request.setAttribute("fail_notify", "Your account has not been activate due to wrong code");
            return "login.jsp";
        }

        UserDAO udao = new UserDAO();
        boolean verified = udao.verifyAccount(code);

        if (verified) {
            request.setAttribute("success_notify", "Your account has been activate");
        } else {
            request.setAttribute("fail_notify", "Your account has not been activate due to wrong code");
        }
        return "login.jsp";
    }

    private String handleVerifyForgetPassword(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            request.setAttribute("fail_notify", "Your account has not been activate due to wrong code");
            return "login.jsp";
        }

        UserDAO udao = new UserDAO();
        HttpSession session=request.getSession();
        session.setAttribute("user", udao.getUserByCode(code));
        if (session.getAttribute("user")!=null){
            request.setAttribute("isForgetPassword", true);
            udao.updateCodetoNull(code);
            return "changepassword.jsp";
        } else {
            request.setAttribute("fail_notify", "Your code invalid");
        }
        return "login.jsp";
    }
}
