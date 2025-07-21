/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import model.CategoryDAO;
import model.CategoryDTO;
import model.ProductDAO;
import model.ProductDTO;
import util.AuthUtils;
import util.Utils;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;
import util.ImageUtils;
import static util.ImageUtils.resizeImage;

/**
 *
 * @author an0other
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 5 * 1024 * 1024, // 5MB
        maxRequestSize = 10 * 1024 * 1024) // 10MB
@WebServlet(name = "ProductController", urlPatterns = {"/ProductController"})
public class ProductController extends HttpServlet {

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
        String url = MainController.MAIN_PAGE;
        try {
            String action = request.getParameter("action");
            if ("addproduct".equals(action)) {
                url = handleAddProduct(request, response);
            } else if ("deleteProduct".equals(action)) {
                url = handleDeleteProduct(request, response);
            } else if ("searchProduct".equals(action)) {
                url = handleSearchProduct(request, response);
            } else if ("activateProduct".equals(action)) {
                url = handleActivateProduct(request, response);
            } else if ("toEditForm".equals(action)) {
                url = handleToEditForm(request, response);
            } else if ("get4thProduct".equals(action)) {
                url = handleGet4thProduct(request, response);
            } else if ("filterProduct".equals(action)) {
                url = handleFilterProduct(request, response);
            } else if ("showProduct".equals(action)) {
                url = handleShowProduct(request, response);
            } else if ("doActivateProduct".equals(action)) {
                url = handleDoActivateProduct(request, response);
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

    private String handleAddProduct(HttpServletRequest request, HttpServletResponse response) {
        boolean ok = true;
        ProductDAO pdao = new ProductDAO();
        String id = request.getParameter("productid");
        ProductDTO productt = pdao.getProductByID(id);
        String mode = request.getParameter("mode");
        if (mode.equals("null")) {
            mode = null;
        }
        if (mode != null) {
            request.setAttribute("mode", mode);
        }
        if (mode == null && pdao.isProductIDExist(id)) {
            request.setAttribute("productid_msg", "Product's ID is already exist!");
            ok = false;
        } else {
            request.setAttribute("id", id);
        }
        String name = request.getParameter("name");
        request.setAttribute("name", name);
        double price = 0;
        try {
            price = Double.parseDouble(request.getParameter("price"));
            request.setAttribute("price", price);
        } catch (Exception e) {
            ok = false;
            request.setAttribute("price_msg", "Price must be a number");
        }
        String img = "";
        try {
            Part filePart = request.getPart("imageurl");
            if (mode != null && mode.equals("edit") && (filePart == null || filePart.getSize() == 0)) {
                img = productt.getImg_url();
                request.setAttribute("img", img);
            } else {
                String fileName = new File(filePart.getSubmittedFileName()).getName();
                String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

                // Chỉ cho phép jpg/jpeg/png
                if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
                    ok = false;
                    request.setAttribute("img_msg", "Only JPG, JPEG, and PNG files are allowed.");
                    return "productform.jsp";
                }

                BufferedImage originalImage = ImageIO.read(filePart.getInputStream());
                if (originalImage == null) {
                    ok = false;
                    request.setAttribute("img_msg", "Invalid image file.");
                    return "productform.jsp";
                }

                int origWidth = originalImage.getWidth();
                int origHeight = originalImage.getHeight();

                // Tạo thư mục upload nếu chưa có
                String uploadPath = "D:/MiniStoreUpload";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String filePath = uploadPath + File.separator + fileName;

                if (origWidth >= 310 && origHeight >= 418) {
                    // Resize nếu đủ lớn
                    BufferedImage resizedImage = ImageUtils.resizeImage(originalImage, 310, 418);
                    ImageIO.write(resizedImage, extension, new File(filePath));
                } else {
                    // Không resize → lưu ảnh gốc
                    try ( InputStream input = filePart.getInputStream()) {
                        Files.copy(input, new File(filePath).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                }

                img = fileName;
                request.setAttribute("img", img);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
            request.setAttribute("img_msg", "Your image seems not valid.");
        }
        request.setAttribute("img", img);
        String category = request.getParameter("categoryid");
        CategoryDAO categorydao = new CategoryDAO();
        if (categorydao.isCategoryIDExist(category) == false) {
            request.setAttribute("category_msg", "Category id isn't exist yet!");
            ok = false;
        } else {
            request.setAttribute("category", category);
        }
        LocalDateTime date = LocalDateTime.now();
        boolean status = false;
        if (request.getParameter("status") != null && request.getParameter("status").equals("on")) {
            status = true;
        }
        request.setAttribute("status", request.getParameter("status"));
        int quantity = 0;
        if (status) {
            try {
                quantity = Integer.parseInt(request.getParameter("quantity"));
                request.setAttribute("quantity", quantity);
            } catch (Exception e) {
                request.setAttribute("quantity_msg", "Quantity must be an integer!");
                ok = false;
            }
        }
        if (ok) {
            if (mode != null) {
                ProductDTO product = new ProductDTO(id, name, price, img, category, date, status, quantity);
                if (pdao.updateProduct(product)) {
                    request.setAttribute("success_notify", "Update Successfully!");
                } else {
                    request.setAttribute("fail_notify", "Update Failed!");
                }
                if (request.getParameter("current_page") != null && request.getParameter("current_page").equals("index")) {
                    return "index.jsp";
                } else {
                    return "MainController?action=searchProduct";
                }
            } else if (pdao.createProduct(new ProductDTO(id, name, price, img, category, date, status, quantity))) {
                request.setAttribute("completeAddProduct", "Add Product Successfully!");
                request.setAttribute("id", null);
                request.setAttribute("name", null);
                request.setAttribute("price", null);
                request.setAttribute("img", null);
                request.setAttribute("category", null);
                request.setAttribute("status", null);
                request.setAttribute("quantity", null);
            } else {
                request.setAttribute("failAddProduct", "Add Product Fail!");
            }
        }
        return "productform.jsp";
    }

    private String handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("productid");
        ProductDAO pdao = new ProductDAO();
        ProductDTO product = pdao.isValidProduct(id);
        if (product == null) {
            request.setAttribute("fail_notify", "Delete Product Fail");
            return "/MainController?action=searchProduct";
        }
        if (pdao.deleteProduct(product)) {
            request.setAttribute("success_notify", "Delete product successfully!");
        } else {
            request.setAttribute("fail_notify", "Delete Product Fail");
        }
        if (request.getParameter("current_page") != null && request.getParameter("current_page").equals("index")) {
            return "index.jsp";
        } else {
            return "MainController?action=searchProduct";
        }
    }

    private String handleSearchProduct(HttpServletRequest request, HttpServletResponse response) {
        ProductDAO productdao = new ProductDAO();
        String keyword = (String) request.getParameter("keyword");
        if (keyword == null || keyword.equals("null")) {
            keyword = "";
        }
        List<ProductDTO> list = productdao.searchProduct(keyword);
        CategoryController ccon = new CategoryController();
        request.setAttribute("category_list", ccon.getAllCategory());
        request.setAttribute("product_list", list);
        return "MainController?action=filterProduct";
    }

    private String handleActivateProduct(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("productid");
        ProductDAO pdao = new ProductDAO();
        ProductDTO product = pdao.getProductByID(id);
        if (product == null || product.isStatus() == true) {
            request.setAttribute("fail_notify", "Activate product fail!");
            return "/MainController?action=searchProduct";
        }
        request.setAttribute("mode", "active");
        request.setAttribute("id", product.getId());
        request.setAttribute("name", product.getName());
        request.setAttribute("price", product.getPrice());
        request.setAttribute("img", product.getImg_url());
        request.setAttribute("category", product.getCategory());
        request.setAttribute("status", product.isStatus() ? "ok" : "ok");
        return "productform.jsp";
    }

    private String handleToEditForm(HttpServletRequest request, HttpServletResponse response) {
        ProductDAO pdao = new ProductDAO();
        ProductDTO product = pdao.getProductByID(request.getParameter("productid"));
        if (product == null) {
            request.setAttribute("fail_notify", "ProductID invalid!");
            return "MainController?action=searchProduct";
        }
        request.setAttribute("id", product.getId());
        request.setAttribute("name", product.getName());
        request.setAttribute("price", product.getPrice());
        request.setAttribute("img", product.getImg_url());
        request.setAttribute("category", product.getCategory());
        request.setAttribute("status", product.isStatus() ? "ok" : null);
        request.setAttribute("quantity", product.getQuantity());
        request.setAttribute("mode", "edit");
        return "productform.jsp";
    }

    private String handleGet4thProduct(HttpServletRequest request, HttpServletResponse response) {
        CategoryController ccon = new CategoryController();
        ProductDAO pdao = new ProductDAO();
        List<String> category_list = ccon.getAllCategory();
        for (String i : category_list) {
            request.setAttribute(i + "_list", pdao.get4thLatestProduct(i));
        }
        request.setAttribute("category_list", category_list);
        request.setAttribute("done", true);
        return "index.jsp";
    }

    private String handleFilterProduct(HttpServletRequest request, HttpServletResponse response) {
        String category = request.getParameter("category");
        String cost_range = request.getParameter("cost_range");
        int pos = 0;
        String cost_low = null;
        String cost_high = null;
        if (cost_range != null && !cost_range.equals("") && !cost_range.equals("null")) {
            for (int i = 0; i < cost_range.length(); i++) {
                if (cost_range.charAt(i) == '-') {
                    pos = i;
                }
            }
            cost_low = cost_range.substring(0, pos);
            cost_high = cost_range.substring(pos + 1, cost_range.length());
            if (cost_high.equals("INF")) {
                cost_high = "10000000";
            }
        }
        String mode = request.getParameter("sort");
        List<ProductDTO> product_list = (ArrayList<ProductDTO>) request.getAttribute("product_list");
        List<ProductDTO> tmp = new ArrayList<ProductDTO>();
        if (category != null && !category.equals("") && !category.equals("null")) {
            for (ProductDTO i : product_list) {
                if (i.getCategory().equals(category)) {
                    tmp.add(i);
                }
            }
            product_list = new ArrayList<>(tmp);
            tmp.clear();
        }
        if (cost_low != null) {
            double low = Double.parseDouble(cost_low);
            double high = Double.parseDouble(cost_high);
            for (ProductDTO i : product_list) {
                if (i.getPrice() >= low && i.getPrice() <= high) {
                    tmp.add(i);
                }
            }
            product_list = new ArrayList<>(tmp);
            tmp.clear();
        }
        if (mode != null && !mode.equals("") && !mode.equals("null")) {
            switch (mode) {
                case "Cost Low to High":
                    product_list.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
                    break;
                case "Cost High to Low":
                    product_list.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                    break;
                case "Newest":
                    product_list.sort((p1, p2) -> p2.getDate().compareTo(p1.getDate()));
                    break;
                case "Oldest":
                    product_list.sort((p1, p2) -> p1.getDate().compareTo(p2.getDate()));
                    break;
            }
        }
        request.setAttribute("product_list", product_list);
        return "MainController?action=showProduct";
    }

    private String handleShowProduct(HttpServletRequest request, HttpServletResponse response) {
        List<ProductDTO> product_list = (ArrayList<ProductDTO>) request.getAttribute("product_list");
        int totalPages = (int) Math.ceil((double) product_list.size() / 12);
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
        }
        page = Math.min(page, totalPages);
        page = Math.max(1, page);
        List<ProductDTO> tmp = new ArrayList<>();
        if (!AuthUtils.isAdmin(request)) {
            for (ProductDTO i : product_list) {
                if (i.getQuantity() > 0) {
                    tmp.add(i);
                }
            }
            product_list = new ArrayList(tmp);
        }
        tmp.clear();
        for (int i = 12 * (page - 1); i < Math.min(product_list.size(), 12 * page); i++) {
            tmp.add(product_list.get(i));
        }
        product_list = tmp;
        request.setAttribute("product_list", product_list);
        request.setAttribute("total_page", totalPages);
        request.setAttribute("page", page);
        return "shop.jsp";
    }

    public List<Integer> getProductQuantityByID(List<String> id_list) {
        ProductDAO pdao = new ProductDAO();
        return pdao.getProductQuantityByID(id_list);
    }

    public boolean buyProduct(List<String> id_list, List<Integer> quantityy_list) {
        List<Integer> quantity_list = this.getProductQuantityByID(id_list);
        for (int i = 0; i < quantity_list.size(); i++) {
            if (quantity_list.get(i) < quantityy_list.get(i)) {
                return false;
            }
            quantity_list.set(i, quantity_list.get(i) - quantityy_list.get(i));
        }
        ProductDAO pdao = new ProductDAO();
        return pdao.updateProductQuantity(quantity_list, id_list);
    }

    public ProductDTO isProductIDValid(String id) {
        ProductDAO pdao = new ProductDAO();
        return pdao.isValidProduct(id);
    }

    private String handleDoActivateProduct(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("productid");
        ProductDAO pdao = new ProductDAO();
        ProductDTO product = pdao.getProductByID(id);
        if (product == null || product.isStatus() == true) {
            request.setAttribute("fail_notify", "Activate product fail!");
            return "/MainController?action=searchProduct";
        }
        int quantity = 1;
        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (Exception e) {
            request.setAttribute("mode", "active");
            request.setAttribute("id", product.getId());
            request.setAttribute("name", product.getName());
            request.setAttribute("price", product.getPrice());
            request.setAttribute("img", product.getImg_url());
            request.setAttribute("category", product.getCategory());
            request.setAttribute("status", product.isStatus() ? "ok" : "ok");
            request.setAttribute("quantity_msg", "Quantity must be an integer!");
            return "productform.jsp";
        }
        if (quantity < 1) {
            request.setAttribute("mode", "active");
            request.setAttribute("id", product.getId());
            request.setAttribute("name", product.getName());
            request.setAttribute("price", product.getPrice());
            request.setAttribute("img", product.getImg_url());
            request.setAttribute("category", product.getCategory());
            request.setAttribute("status", product.isStatus() ? "ok" : "ok");
            request.setAttribute("quantity_msg", "Quantity must be > 0!");
            return "productform.jsp";
        }
        if (pdao.activateProduct(id, quantity)) {
            request.setAttribute("success_notify", "Activate product successfully");
        } else {
            request.setAttribute("fail_notify", "Activate product fail");
        }
        if (request.getParameter("current_page") != null && request.getParameter("current_page").equals("index")) {
            return "index.jsp";
        } else {
            return "MainController?action=searchProduct";
        }
    }

    public boolean isEnough(List<String> id_list, List<Integer> quantityy_list) {
        List<Integer> quantity_list = this.getProductQuantityByID(id_list);
        for (int i = 0; i < quantity_list.size(); i++) {
            if (quantity_list.get(i) < quantityy_list.get(i)) {
                return false;
            }
        }
        return true;
    }
}
