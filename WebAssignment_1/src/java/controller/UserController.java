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
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import model.UserDAO;
import model.UserDTO;
import util.EmailUtils;
import util.EncryptUtils;
import util.Utils;

/**
 *
 * @author an0other
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    public static final String LOGIN_PAGE = "login.jsp";
    public static final String REGISTER_PAGE = "register.jsp";

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
        String url = LOGIN_PAGE;
        try {
            String action = request.getParameter("action");
            if (action.equals("login")) {
                url = handleLogin(request, response);
            } else if ("logout".equals(action)) {
                url = handleLogout(request, response);
            } else if ("register".equals(action)) {
                url = handleRegister(request, response);
            } else if ("changePassword".equals(action)) {
                url = handleChangePassword(request, response);
            } else if ("forgetPassword".equals(action)) {
                url = handleForgetPassword(request, response);
            }
        } catch (Exception e) {
            System.err.print(e);
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

    private String handleLogin(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            session.invalidate();
        }
        UserDAO userdao = new UserDAO();
        String message = "";
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        if (userdao.login(username, password)) {
            UserDTO user = userdao.getUserByUsername(username);
            session.setAttribute("user", user);
            session.setAttribute("order_id", (int) 1010);
            return "MainController?action=get4thProduct";
        } else {
            message = "Incorrect Username or Password";
            request.setAttribute("username", username);
        }
        request.setAttribute("fail_notify", message);
        return "login.jsp";
    }

    private String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user != null) {
            user = null;
            session.invalidate();
        }
        return "index.jsp";
    }

    private String handleRegister(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserDAO userdao = new UserDAO();
        boolean ok = true;
        if (session.getAttribute("user") != null) {
            session.invalidate();
        }
        String username = request.getParameter("username");
        if (username != null && username.equals("null") && userdao.isUsernameExist(username)) {
            if (!userdao.isUsernameExistWithCode(username)) {
                ok = false;
                request.setAttribute("UserNameAlert", "This Username is existed!");
            }
        }
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        boolean isEmailExistWithCode = false;
        if (userdao.isEmailExist(email)) {
            if (userdao.isEmailExistWithCode(email)) {
                isEmailExistWithCode = true;
            } else {
                ok = false;
                request.setAttribute("EmailAlert", "This Email is existed!");
            }
        }
        String fullname = request.getParameter("fullname");
        String verifyCode = UUID.randomUUID().toString();

        UserDTO user = new UserDTO(username, EncryptUtils.encryptSHA256(password), email, fullname, "MEMBER", true, verifyCode);
        if (ok) {
            if ((isEmailExistWithCode && userdao.updateRegisterInformationWithEmail(user)) || userdao.createNewUser(user, request)) {
                request.setAttribute("register_msg_success", "Register Successfully!A Code has been send to your Email");
                try {
                    EmailUtils.sendVerificationEmail(email, verifyCode, request.getParameter("username"));
                } catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
                request.setAttribute("ok", true);
            } else {
                request.setAttribute("register_msg_fail", "Register Failed");
                request.setAttribute("fullname", fullname);
            }
        }
        return "register.jsp";
    }

    private String handleChangePassword(HttpServletRequest request, HttpServletResponse response) {
        UserDAO udao = new UserDAO();
        HttpSession session = request.getSession();
        String isForgetPassword = request.getParameter("isForgetPassword");
        boolean ok = false;
        if (isForgetPassword != null && !isForgetPassword.equals("") && !isForgetPassword.equals("null")) {
            ok = true;
        }
        String current_password = request.getParameter("current_password");
        String new_password1 = request.getParameter("new_password1");
        String new_password2 = request.getParameter("new_password2");
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (ok || EncryptUtils.encryptSHA256(current_password).equals(user.getPassword())) {
            if (new_password1.equals(new_password2)) {
                user.setPassword(EncryptUtils.encryptSHA256(new_password2));
                if (udao.updatePassword(user) == false) {
                    request.setAttribute("fail_msg", "Change password failed!");
                } else {
                    request.setAttribute("success_msg", "Change password successfully!");
                    if (ok) {
                        session.invalidate();
                        request.setAttribute("success_notify", "Change Password Successfully!");
                        return "MainController?action=login";
                    }
                }
            } else {
                request.setAttribute("isForgetPassword", true);
                request.setAttribute("current_password", current_password);
                request.setAttribute("new_password_msg", "The new password must be the same");
            }
        } else {
            request.setAttribute("old_password_msg", "The current password incorrect!");
        }
        return "changepassword.jsp";
    }

    private String handleForgetPassword(HttpServletRequest request, HttpServletResponse response) {
        String verifyCode = UUID.randomUUID().toString();
        String username = request.getParameter("username");
        UserDAO udao = new UserDAO();
        if (!udao.isUsernameExist(username)) {
            request.setAttribute("fail_notify", "Username not exist!");
            return "forgetpassword.jsp";
        }
        UserDTO user = udao.getUserByUsername(username);
        udao.updateCode(username, verifyCode);
        String email = user.getEmail();
        try {
            EmailUtils.sendForgetPassword(email, verifyCode);
        } catch (Exception e) {
        }
        request.setAttribute("success_notify", "A Code has been send to your email");
        return "forgetpassword.jsp";
    }

}
