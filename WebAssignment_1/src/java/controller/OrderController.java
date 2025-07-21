/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.OrderDAO;
import model.OrderDTO;
import model.UserDTO;
import util.AuthUtils;
import util.EmailUtils;
import util.Utils;

/**
 *
 * @author an0other
 */
@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

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
        String url = "index.jsp";
        try {
            String action = request.getParameter("action");
            if ("createOrder".equals(action)) {
                url = handleCreateOrder(request, response);
            } else if ("getAllOrder".equals(action)) {
                url = handleGetAllOrder(request, response);
            } else if ("changeStatus".equals(action)) {
                url = handleChangeStatus(request, response);
            } else if ("cancelOrder".equals(action)) {
                url = handleCancelOrder(request, response);
            } else if ("removeOrder".equals(action)) {
                url = handleRemoveOrder(request, response);
            } else if ("filterOrder".equals(action)) {
                url = handleFilterOrder(request, response);
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

    private String handleCreateOrder(HttpServletRequest request, HttpServletResponse response) {
        OrderDAO orderdao = new OrderDAO();
        UserDTO user = AuthUtils.getCurrentUser(request);
        HttpSession session = request.getSession();
        OrderItemController oicon = new OrderItemController();
        if (oicon.preCreateOrder(session) == false) {
            request.setAttribute("place_order_fail_msg", "Buy Failed");
            return "MainController?action=getMaxQuantityOfItem";
        }
        double total_price = (double) session.getAttribute("total_price");
        LocalDateTime now = LocalDateTime.now();
        OrderDTO order = new OrderDTO(1, user.getUsername(), total_price, "Pending", now);
        int order_id = orderdao.createOrder(order);
        request.setAttribute("user_email", user.getEmail());
        request.setAttribute("total_price", total_price);
        request.setAttribute("now", now);
        if (order_id < 1) {
            request.setAttribute("place_order_fail_msg", "Place Order Failed!");
            return "cart.jsp";
        }
        request.setAttribute("order_id", order_id);
        session.setAttribute("total_price", (double) 0);
        return "MainController?action=createOrderItem";
    }

    private String handleGetAllOrder(HttpServletRequest request, HttpServletResponse response) {
        String username = AuthUtils.getUsername(request);
        OrderDAO odao = new OrderDAO();
        List<OrderDTO> list = odao.getAllOrder(username);
        if (AuthUtils.isAdmin(request)) {
            int cnt = 0;
            for (OrderDTO i : list) {
                if (i.getStatus().equals("Pending")) {
                    cnt++;
                }
            }
            if (cnt > 0) {
                request.setAttribute("fail_notify", "You have new " + String.valueOf(cnt) + " orders!");
            }
        }
        request.setAttribute("order_list", list);
        return "userinformation.jsp";
    }

    private String handleChangeStatus(HttpServletRequest request, HttpServletResponse response) {
        OrderDAO odao = new OrderDAO();
        String status = request.getParameter("order_status");
        String current_status = request.getParameter("order_current_status");
        if (current_status.equals("Canceled")) {
            request.setAttribute("fail_notify", "You can't change status of cancelled order");
            return "MainController?action=getAllOrder";
        }
        int order_id = 0;
        try {
            order_id = Integer.parseInt(request.getParameter("order_id"));
        } catch (Exception e) {
            request.setAttribute("fail_notify", "OrderID invalid!");
            return "MainController?action=getAllOrder";
        }
        request.setAttribute("username", "admin");
        if (odao.updateStatus(status, order_id)) {
            request.setAttribute("success_notify", "Update Successfully!");
        } else {
            request.setAttribute("fail_notify", "Update Failed!");
        }
        return "MainController?action=getAllOrder";
    }

    private String handleCancelOrder(HttpServletRequest request, HttpServletResponse response) {
        OrderDAO odao = new OrderDAO();
        int order_id = 0;
        try {
            order_id = Integer.parseInt(request.getParameter("order_id"));
        } catch (Exception e) {
            request.setAttribute("fail_notify", "Invalid OrderID");
            return "MainController?action=getAllOrder";
        }
        if (odao.updateStatus("Canceled", order_id)) {
            request.setAttribute("success_notify", "Update Successfully!");
        } else {
            request.setAttribute("fail_notify", "Update Failed!");
        }
        return "MainController?action=getAllOrder";
    }

    private String handleRemoveOrder(HttpServletRequest request, HttpServletResponse response) {
        OrderDAO odao = new OrderDAO();
        int id = 0;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            request.setAttribute("fail_notify", "Invalid OrderID");
            return "MainController?action=getAllOrder";
        }
        if ("Canceled".equals(odao.getOrderStatus(id)) && odao.deleteOrder(id)) {
            request.setAttribute("success_notify", "Remove Successfully!");
        } else {
            request.setAttribute("fail_notify", "Remove Failed");
        }
        return "MainController?action=getAllOrder";
    }

    private String handleFilterOrder(HttpServletRequest request, HttpServletResponse response) {
        List<OrderDTO> list = new ArrayList<>();
        List<OrderDTO> tmp = new ArrayList<>();
        OrderDAO odao = new OrderDAO();
        String username = request.getParameter("username");
        list = odao.getAllOrder("admin");
        String status = request.getParameter("status");
        if (status != null && !status.equals("") && !status.equals("null")) {
            for (OrderDTO i : list) {
                if (i.getStatus().equals(status)) {
                    tmp.add(i);
                }
            }
            list = new ArrayList(tmp);
            tmp.clear();
        }
        String time = request.getParameter("time");
        if (time != null && !time.equals("") && !time.equals("null")) {
            switch (time) {
                case "newest":
                    list.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
                    break;
                case "oldest":
                    list.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                    break;
                case "today":
                    LocalDate today = LocalDate.now();
                    list = list.stream()
                            .filter(order -> order.getDate().toLocalDate().equals(today))
                            .collect(Collectors.toList());
                    break;
                case "yesterday":
                    LocalDate yesterday = LocalDate.now().minusDays(1);
                    list = list.stream()
                            .filter(order -> order.getDate().toLocalDate().equals(yesterday))
                            .collect(Collectors.toList());
                    break;
                case "lastweek":
                    today = LocalDate.now();
                    LocalDate sevenDaysAgo = today.minusDays(7);
                    list = list.stream()
                            .filter(order -> {
                                LocalDate orderDate = order.getDate().toLocalDate();
                                return (orderDate.isAfter(sevenDaysAgo) || orderDate.equals(sevenDaysAgo))
                                        && orderDate.isBefore(today);
                            })
                            .collect(Collectors.toList());
                    break;
            }

        }
        request.setAttribute("order_list", list);
        return "userinformation.jsp";
    }

    public String getOrderStatus(int id) {
        String status = null;
        OrderDAO odao = new OrderDAO();
        status = odao.getOrderStatus(id);
        return status;
    }
}
