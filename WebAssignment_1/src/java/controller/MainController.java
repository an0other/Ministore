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

/**
 *
 * @author an0other
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    public static String MAIN_PAGE = "index.jsp";

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
        String url = MAIN_PAGE;
        try {
            String action = (String) request.getParameter("action");
            if (action == null) {
                action = "get4thProduct";
            }
            if (isUserRequest(action)) {
                url = "/UserController";
            } else if (isProductRequest(action)) {
                url = "/ProductController";
            } else if (isCategoryRequest(action)) {
                url = "/CategoryController";
            } else if (isOrderRequest(action)) {
                url = "/OrderController";
            } else if (isOrderItemRequest(action)) {
                url = "/OrderItemController";
            } else if (isEmailRequest(action)){
                url = "/EmailController";
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

    private boolean isUserRequest(String action) {
        String[] arr = {"login", "register", "logout", "changePassword", "forgetPassword"};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(action)) {
                return true;
            }
        }
        return false;
    }

    private boolean isProductRequest(String action) {
        String[] arr = {"addproduct", "showAllProduct", "deleteProduct", "searchProduct", "showProductByCategory", "activateProduct", "toEditForm", "get4thProduct", "filterProduct", "showProduct", "doActivateProduct"};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(action)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCategoryRequest(String action) {
        String[] arr = {"createcategory", "getCategoryMap"};
        for (String i : arr) {
            if (i.equals(action)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOrderRequest(String action) {
        String[] arr = {"createOrder", "getAllOrder", "changeStatus", "cancelOrder", "removeOrder", "filterOrder"};
        for (String i : arr) {
            if (i.equals(action)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOrderItemRequest(String action) {
        String[] arr = {"addItem", "createOrderItem", "getOrderItemsByOrderID", "getMaxQuantityOfItem", "updateQuantity", "removeFromCart"};
        for (String i : arr) {
            if (i.equals(action)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailRequest(String action) {
        String[] arr = {"verifyEmail", "verifyForgetPassword"};
        for (String i : arr) {
            if (i.equals(action)) {
                return true;
            }
        }
        return false;
    }

}
