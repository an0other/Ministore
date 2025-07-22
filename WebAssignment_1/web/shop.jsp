<%-- 
    Document   : shop
    Created on : Jun 13, 2025, 10:54:43 AM
    Author     : an0other
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.ProductDTO" %>
<%@page import="java.util.List" %>
<%@page import="util.AuthUtils" %>
<%@page import="java.util.ArrayList" %>
<%@page import="model.CategoryDTO" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Shop Page</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="format-detection" content="telephone=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="author" content="">
        <meta name="keywords" content="">
        <meta name="description" content="">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/filter_style.css"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.css" />
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Jost:wght@300;400;500&family=Lato:wght@300;400;700&display=swap" rel="stylesheet">
        <style>
            .notification {
                position: fixed;
                bottom: 20px;
                right: 20px;
                padding: 15px 20px;
                border-radius: 5px;
                color: white;
                font-size: 16px;
                z-index: 1000;
                opacity: 1;
                transition: opacity 0.5s ease-out;
            }
            .notification.success {
                background-color: #28a745; /* Green for success */
            }
            .notification.fail {
                background-color: #dc3545; /* Red for fail */
            }
            .notification.hidden {
                opacity: 0;
                pointer-events: none;
            }
        </style>
        <script src="assets/js/modernizr.js"></script>
    </head>
        <%
            int total_page=(Integer) request.getAttribute("total_page");
            int currentPage=(Integer) request.getAttribute("page");
            String page_str=String.valueOf(currentPage);
        %>
    <body data-bs-spy="scroll" data-bs-target="#navbar" data-bs-root-margin="0px 0px -40%" data-bs-smooth-scroll="true" tabindex="0">
        <%@ include file="partials/header.jsp" %>
        <img src="assets/images/solvethatshi.jpg" alt="solvethatshi" style="max-height: 90px; max-width: 1540px; height: auto; width: auto;"/>
        <div class="mini-filter-bar">
            <form action="MainController" class="mini-filter-form">
                <input type="hidden" name="action" value="searchProduct">
                <input type="hidden" name="keyword" value="<%= (request.getParameter("keyword") == null || request.getParameter("keyword").equals("")) ? "" : request.getParameter("keyword") %>">
                <input type="hidden" name="page" value="<%=currentPage%>">
                <select name="category" class="mini-select">
                    <option value="">All</option>
                    <% List<String> category_list = (List<String>) request.getAttribute("category_list");
                       for (String i : category_list) { %>
                    <option value="<%=i%>" <%=i.equals(request.getParameter("category")) ? "selected" : ""%>><%=i%></option>
                    <% } %>
                </select>

                <select name="cost_range" class="mini-select">
                    <option value="">Price</option>
                    <option value="0-1000" <%= "0-1000".equals(request.getParameter("cost_range")) ? "selected" : ""%>>≤ $1k</option>
                    <option value="1000-2000" <%= "1000-2000".equals(request.getParameter("cost_range")) ? "selected" : ""%>>$1k-$2k</option>
                    <option value="2000-3000" <%= "2000-3000".equals(request.getParameter("cost_range")) ? "selected" : ""%>>$2k-$3k</option>
                    <option value="3000-INF" <%= "3000-INF".equals(request.getParameter("cost_range")) ? "selected" : ""%>>≥ $3k</option>
                </select>

                <select name="sort" class="mini-select">
                    <option value="">Sort</option>
                    <option value="Cost Low to High" <%= "Cost Low to High".equals(request.getParameter("sort")) ? "selected" : ""%>>Price ↑</option>
                    <option value="Cost High to Low" <%= "Cost High to Low".equals(request.getParameter("sort")) ? "selected" : ""%>>Price ↓</option>
                    <option value="Newest" <%= "Newest".equals(request.getParameter("sort")) ? "selected" : ""%>>Newest</option>
                </select>

                <button type="submit" class="mini-filter-btn">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path d="M22 3H2l8 9.46V19l4 2v-8.54L22 3z"/>
                    </svg>
                </button>
            </form>
        </div>
        <% 
            String successNotify = (String) request.getAttribute("success_notify");
            String failNotify = (String) request.getAttribute("fail_notify");
            if (successNotify != null && !successNotify.isEmpty()) { %>
            <div id="success-notify" class="notification success"><%= successNotify %></div>
        <% } %>
        <% if (failNotify != null && !failNotify.isEmpty()) { %>
            <div id="fail-notify" class="notification fail"><%= failNotify %></div>
        <% } %>
        <%        
            List<ProductDTO> list = new ArrayList<ProductDTO>();
            list = (List<ProductDTO>) request.getAttribute("product_list");
            keyword = (request.getParameter("keyword") == null || request.getParameter("keyword").equals("")) ? "" : request.getParameter("keyword");
            if (list == null || list.size() < 1) { 
        %>
        <h1 style="color: red">Sorry we don't have any products to display yet!</h1>
        <% } else { %>
        <div id="mobile-products" class="product-store position-relative padding-large no-padding-top">
            <div class="container">
                <div class="row">
                    <% for (ProductDTO i : list) if ((!AuthUtils.isAdmin(request) && i.getQuantity()>0) || AuthUtils.isAdmin(request)) { %>
                    <div class="col-md-3 mb-3">
                        <div class="product-card position-relative">
                            <div class="image-holder">
                                <img src="image?name=<%=i.getImg_url()%>" alt="product-item" class="img-fluid" style="width: 310px; height: 418px; object-fit: cover;">
                            </div>
                            <div class="cart-concern position-absolute">
                                <% if (AuthUtils.isAdmin(request)) { %>
                                <div class="cart-button d-flex">
                                    <a href="MainController?action=toEditForm&productid=<%=i.getId()%>&keyword<%=request.getParameter("keyword")%>&category=<%=request.getParameter("category")%>&cost_range=<%=request.getParameter("cost_range")%>&sort=<%=request.getParameter("sort")%>&page=<%=page_str%>" class="btn btn-medium btn-black">
                                        Edit<svg class="cart-outline">
                                            <use xlink:href="#cart-outline"></use>
                                        </svg>
                                    </a>
                                </div>
                                <div class="cart-button d-flex">
                                    <% 
                                        String url = "";
                                        if (i.isStatus()) url = "MainController?action=deleteProduct&productid=" + i.getId() + "&keyword=" + request.getParameter("keyword")+"&category="+request.getParameter("category")+"&cost_range="+request.getParameter("cost_range")+"&sort="+request.getParameter("sort")+"&page="+page_str;
                                        else url = "MainController?action=activateProduct&productid=" + i.getId() + "&keyword=" + request.getParameter("keyword")+"&category="+request.getParameter("category")+"&cost_range="+request.getParameter("cost_range")+"&sort="+request.getParameter("sort")+"&page="+page_str;
                                    %>
                                    <a href="<%=url%>" class="btn btn-medium btn-black">
                                        <%= i.isStatus() ? "Delete" : "Activate" %><svg class="cart-outline">
                                            <use xlink:href="#cart-outline"></use>
                                        </svg>
                                    </a>
                                </div> 
                                <% } else { %>
                                <div class="cart-button d-flex">
                                    <a href="MainController?action=addItem&keyword=<%=keyword%>&category=<%=request.getParameter("category")%>&cost_range=<%=request.getParameter("cost_range")%>&sort=<%=request.getParameter("sort")%>&idproduct=<%=i.getId()%>&page=<%=page_str%>" class="btn btn-medium btn-black">
                                        Add to Cart<svg class="cart-outline">
                                            <use xlink:href="#cart-outline"></use>
                                        </svg>
                                    </a>
                                </div> 
                                <% } %>
                            </div>
                            <div class="card-detail d-flex justify-content-between align-items-baseline pt-3">
                                <h3 class="card-title text-uppercase">
                                    <span style="font-size: 23px"><%= i.getName() %></span>
                                </h3>
                                <span class="item-price text-primary">$<%=i.getPrice()%></span>
                            </div>
                            <div class="text-muted small ps-2 pb-2">
                                In Stock: <%=i.getQuantity()%>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
        <% } %>
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const successNotify = document.getElementById('success-notify');
                const failNotify = document.getElementById('fail-notify');

                if (successNotify) {
                    setTimeout(() => {
                        successNotify.classList.add('hidden');
                    }, 5000);
                }

                if (failNotify) {
                    setTimeout(() => {
                        failNotify.classList.add('hidden');
                    }, 5000);
                }
            });
        </script>
        <nav>
          <ul class="pagination justify-content-center">
            <% for (int i = 1; i <= total_page; i++) { %>
                <li class="page-item <%= (i == currentPage ? "active" : "") %>">
                    <a class="page-link" href="MainController?action=searchProduct&page=<%=i%>&keyword<%=request.getParameter("keyword")%>&category=<%=request.getParameter("category")%>&cost_range=<%=request.getParameter("cost_range")%>&sort=<%=request.getParameter("sort")%>"><%=i%></a>
                </li>
            <% } %>
          </ul>
        </nav>
        <%@ include file="partials/footer.jsp" %>
    </body>
</html>