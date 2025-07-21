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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.OrderDAO;
import model.OrderItemDAO;
import model.OrderItemDTO;
import model.ProductDTO;
import util.EmailUtils;
import util.Utils;

/**
 *
 * @author an0other
 */
@WebServlet(name = "OrderItemController", urlPatterns = {"/OrderItemController"})
public class OrderItemController extends HttpServlet {

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
            if ("addItem".equals(action)) {
                url = handleAddItem(request, response);
            } else if ("createOrderItem".equals(action)) {
                url = handleCreateOrderItem(request, response);
            } else if ("getOrderItemsByOrderID".equals(action)) {
                url = handleGetOrderItemsByOrderID(request, response);
            } else if ("getMaxQuantityOfItem".equals(action)) {
                url = handleGetMaxQuantityOfItem(request, response);
            } else if ("updateQuantity".equals(action)) {
                url = handleUpdateQuantity(request, response);
            } else if ("removeFromCart".equals(action)) {
                url = handleRemoveFromCart(request, response);
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

    private String handleAddItem(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        List<OrderItemDTO> list = (List<OrderItemDTO>) session.getAttribute("cart_list");
        String id_product = request.getParameter("idproduct");
        ProductController pcon = new ProductController();
        ProductDTO product = pcon.isProductIDValid(id_product);
        if (product == null || product.getQuantity() < 1) {
            request.setAttribute("fail_notify", "Add product to cart fail due to product not exist or out of stock");
            if (request.getParameter("current_page") != null && request.getParameter("current_page").equals("index")) {
                return "/MainController?action=get4thProduct";
            }
            return "/MainController?action=searchProduct";
        }
        if (list == null) {
            list = new ArrayList<OrderItemDTO>();
        }
        boolean isExist = false;
        for (OrderItemDTO i : list) {
            if (i.getProduct_id().equals(id_product)) {
                i.setQuantity(i.getQuantity() + 1);
                isExist = true;
                break;
            }
        }
        if (isExist == false) {
            double price = product.getPrice();
            String img = product.getImg_url();
            int orderid = (int) session.getAttribute("order_id");
            String name = product.getName();
            OrderItemDTO item = new OrderItemDTO(0, id_product, 1, price, orderid, name, img);
            list.add(item);
        }
        session.setAttribute("cart_list", list);
        request.setAttribute("success_notify", "Added product to your cart!");
        if (request.getParameter("current_page") != null && request.getParameter("current_page").equals("index")) {
            return "MainController?action=get4thProduct";
        }
        return "/MainController?action=searchProduct";
    }

    private String handleCreateOrderItem(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        List<OrderItemDTO> list = (List<OrderItemDTO>) session.getAttribute("cart_list");
        if (!list.isEmpty()) {
            int order_id = (int) request.getAttribute("order_id");
            OrderItemDAO oidao = new OrderItemDAO();
            oidao.createOrderItem(list, order_id);
            List<String> id_list = new ArrayList<>();
            List<Integer> quantityy_list = new ArrayList<>();
            for (OrderItemDTO i : list) {
                id_list.add(i.getProduct_id());
                quantityy_list.add(i.getQuantity());
            }
            ProductController pcon = new ProductController();
            if (pcon.buyProduct(id_list, quantityy_list) == false) {
                request.setAttribute("buy_msg", "Buy failed because your order exceed stock!");
                OrderDAO odao = new OrderDAO();
                odao.deleteOrder(order_id);
                return "MainController?action=getMaxQuantityOfItem";
            }

            try {
                EmailUtils.sendOrderEmail((String) request.getAttribute("user_email"), order_id, (LocalDateTime) request.getAttribute("now"), list, (Double) request.getAttribute("total_price"));
            } catch (Exception e) {
            }
            list.clear();
            session.setAttribute("cart_list", list);
            request.setAttribute("place_order_success_msg", "Place order successfully!");
        }
        return "cart.jsp";
    }

    private String handleGetOrderItemsByOrderID(HttpServletRequest request, HttpServletResponse response) {
        OrderItemDAO odao = new OrderItemDAO();
        int order_id = 0;
        try {
            order_id = Integer.parseInt(request.getParameter("order_id"));
        } catch (Exception e) {
            request.setAttribute("fail_notify", "OrderID invalid!");
            return "MainController?action=getAllOrder";
        }
        List<OrderItemDTO> list = odao.getOrderItemsByOrderID(order_id);
        request.setAttribute("order_item_list", list);
        request.setAttribute("mode", "view");
        return "cart.jsp";
    }

    private String handleGetMaxQuantityOfItem(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        List<OrderItemDTO> list = (List<OrderItemDTO>) session.getAttribute("cart_list");
        if (list == null || list.isEmpty()) {
            return "cart.jsp";
        }
        List<String> id_list = new ArrayList<>();
        for (OrderItemDTO i : list) {
            id_list.add(i.getProduct_id());
        }
        ProductController pcon = new ProductController();
        List<Integer> quantity_list = pcon.getProductQuantityByID(id_list);
        request.setAttribute("quantity_list", quantity_list);
        return "cart.jsp";
    }

    private String handleUpdateQuantity(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String mode = request.getParameter("mode");
        List<OrderItemDTO> list = (List<OrderItemDTO>) session.getAttribute("cart_list");
        String product_id = request.getParameter("product_id");
        int quantity = 0;
        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (Exception e) {
        }
        String max_quantity_str = request.getParameter("max_quantity_product");
        int maxx = 0;
        try {
            maxx = Integer.parseInt(max_quantity_str);
        } catch (Exception e) {
        }
        for (OrderItemDTO i : list) {
            if (i.getProduct_id().equals(product_id)) {
                i.setQuantity(Math.min(maxx, quantity));
                break;
            }
        }
        session.setAttribute("cart_list", list);
        return "MainController?action=getMaxQuantityOfItem";
    }

    private String handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response) {
        String product_id = request.getParameter("product_id");
        HttpSession session = request.getSession();
        List<OrderItemDTO> list = (List<OrderItemDTO>) session.getAttribute("cart_list");
        for (OrderItemDTO i : list) {
            if (i.getProduct_id().equals(product_id)) {
                list.remove(i);
                break;
            }
        }
        return "MainController?action=getMaxQuantityOfItem";
    }

    public boolean preCreateOrder(HttpSession session) {
        List<OrderItemDTO> tmp = (List<OrderItemDTO>) session.getAttribute("cart_list");
        if (tmp == null || tmp.size() < 1) {
            return false;
        }
        ProductController pcon=new ProductController();
        List<String> id_list = new ArrayList<>();
        List<Integer> quantityy_list = new ArrayList<>();
        for (OrderItemDTO i : tmp) {
            id_list.add(i.getProduct_id());
            quantityy_list.add(i.getQuantity());
        }
        if (!pcon.isEnough(id_list, quantityy_list)) return false;
        double total_price = 0;
        List<OrderItemDTO> list = new ArrayList<OrderItemDTO>();
        for (OrderItemDTO i : tmp) {
            if (i.getQuantity() > 0) {
                list.add(i);
                total_price += i.getQuantity() * i.getPrice();
            }
        }
        session.setAttribute("total_price", total_price);
        session.setAttribute("cart_list", list);
        return true;
    }

    public List<OrderItemDTO> getCartList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<OrderItemDTO> list = (List<OrderItemDTO>) session.getAttribute("cart_list");
        return list;
    }
}
