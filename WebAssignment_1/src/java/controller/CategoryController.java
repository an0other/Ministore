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
import java.util.ArrayList;
import java.util.List;
import model.CategoryDAO;
import model.CategoryDTO;

/**
 *
 * @author an0other
 */
@WebServlet(name = "CategoryController", urlPatterns = {"/CategoryController"})
public class CategoryController extends HttpServlet {

    public static final String CATEGORY_FORM = "categoryform.jsp";

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
        String url = CATEGORY_FORM;
        try {
            String action = request.getParameter("action");
            if ("createcategory".equals(action)) {
                url = handleCreateCategory(request, response);
            } else if ("getCategoryMap".equals(action)){
                url=handleGetCategoryMap(request, response);
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

    private String handleCreateCategory(HttpServletRequest request, HttpServletResponse response) {
        CategoryDAO cdao = new CategoryDAO();
        boolean ok = true;
        String id = request.getParameter("category_id");
        if (cdao.isCategoryIDExist(id)) {
            ok = false;
            request.setAttribute("categoryidmessage", "This Category ID is already existed!");
        } else request.setAttribute("category_id", id);
        String name = request.getParameter("category_name");
        if (cdao.isCategoryNameExist(name)) {
            ok = false;
            request.setAttribute("categorynamemessage", "This Category Name is already existed!");
        } else request.setAttribute("category_name", name);
        if (ok) {
            if (cdao.createCategory(new CategoryDTO(id, name))){
                request.setAttribute("category_id", "");
                request.setAttribute("category_name", "");
                request.setAttribute("createsuccess_msg", "Create Successfully!");
            } else {
                request.setAttribute("createfail_msg", "Create Failed!");
            }
        }
        return "categoryform.jsp";
    }

    public List<String> getAllCategory(){     
        List<String> list=new ArrayList<String>();
        CategoryDAO cdao=new CategoryDAO();
        for (CategoryDTO i: cdao.getAllCategory()) list.add(i.getName());
        return list;
    }
    
    public List<String> getAllCategoryID(){
        List<String> list=new ArrayList<String>();
        CategoryDAO cdao=new CategoryDAO();
        for (CategoryDTO i: cdao.getAllCategory()) list.add(i.getId());
        return list;
    }

    private String handleGetCategoryMap(HttpServletRequest request, HttpServletResponse response) {
        CategoryDAO cdao=new CategoryDAO();
        request.setAttribute("category_map", cdao.getAllCategoryMap());
        return "productform.jsp";
    }
}
