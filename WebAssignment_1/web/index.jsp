<%-- 
    Document   : index
    Created on : Jun 9, 2025, 9:43:50 PM
    Author     : an0other
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.AuthUtils" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="model.ProductDTO" %>
<%@page import="model.CategoryDTO" %>
<%
    if (request.getAttribute("done")==null){
        request.getRequestDispatcher("MainController?action=get4thProduct").forward(request, response);
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>An0other's MiniStore</title>
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
                transition: opacity 0.3s ease-out;
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
    <body data-bs-spy="scroll" data-bs-target="#navbar" data-bs-root-margin="0px 0px -40%" data-bs-smooth-scroll="true" tabindex="0">
        <%@ include file="partials/header.jsp" %>
        <section id="billboard" class="position-relative overflow-hidden bg-light-blue">
            <div class="swiper main-swiper">
                <div class="swiper-wrapper">
                    <div class="swiper-slide">
                        <div class="container">
                            <div class="row d-flex align-items-center">
                                <div class="col-md-6">
                                    <div class="banner-content">
                                        <h1 class="display-2 text-uppercase text-dark pb-5">Your Products Are Great.</h1>
                                        <a href="MainController?action=searchProduct" class="btn btn-medium btn-dark text-uppercase btn-rounded-none">Shop Product</a>
                                    </div>
                                </div>
                                <div class="col-md-5">
                                    <div class="image-holder">
                                        <img src="assets/images/banner-image.png" alt="banner">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="swiper-slide">
                        <div class="container">
                            <div class="row d-flex flex-wrap align-items-center">
                                <div class="col-md-6">
                                    <div class="banner-content">
                                        <h1 class="display-2 text-uppercase text-dark pb-5">Technology Hack You Won't Get</h1>
                                        <a href="MainController?action=showAllProduct" class="btn btn-medium btn-dark text-uppercase btn-rounded-none">Shop Product</a>
                                    </div>
                                </div>
                                <div class="col-md-5">
                                    <div class="image-holder">
                                        <img src="assets/images/banner-image.png" alt="banner">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="swiper-icon swiper-arrow swiper-arrow-prev">
                <svg class="chevron-left" style="margin-left: -100px">
                    <use xlink:href="#chevron-left" />
                </svg>
            </div>
            <div class="swiper-icon swiper-arrow swiper-arrow-next">
                <svg class="chevron-right" style="margin-right: -100px">
                    <use xlink:href="#chevron-right" />
                </svg>
            </div>
        </section>
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
            List<String> category_list = (List<String>) request.getAttribute("category_list");
            List<ProductDTO> product_list = null;
            for (String c : category_list) {
                String name = c + "_list";
                product_list = (List<ProductDTO>) request.getAttribute(name);
                if (product_list != null && !product_list.isEmpty()) {
        %>                                           
        <section id="mobile-products" class="product-store position-relative padding-large no-padding-top">
            <div class="container">
                <div class="row">
                    <div class="display-header d-flex justify-content-between pb-3">
                        <h2 class="display-7 text-dark text-uppercase"><%= c %> Products</h2>
                        <div class="btn-right">
                            <a href="MainController?action=searchProduct&category=<%= c %>" class="btn btn-medium btn-normal text-uppercase">Go to Shop</a>
                        </div>
                    </div>
                    <div class="swiper product-swiper">
                        <div class="swiper-wrapper">
                            <% for (ProductDTO i : product_list) { %>
                            <div class="swiper-slide">
                                <div class="product-card position-relative">
                                    <div class="image-holder">
                                        <img src="image?name=<%=i.getImg_url()%>" alt="product-item" class="img-fluid" style="width: 310px; height: 418px; object-fit: cover;">
                                    </div>
                                    <div class="cart-concern position-absolute">
                                        <% if (AuthUtils.isAdmin(request)) { %>
                                <div class="cart-button d-flex">
                                    <a href="MainController?action=toEditForm&productid=<%=i.getId()%>" class="btn btn-medium btn-black">
                                        Edit<svg class="cart-outline">
                                            <use xlink:href="#cart-outline"></use>
                                        </svg>
                                    </a>
                                </div>
                                <div class="cart-button d-flex">
                                    <% 
                                        String url = "";
                                        if (i.isStatus()) url = "MainController?action=deleteProduct&productid=" + i.getId() + "&keyword=" + request.getParameter("keyword")+"&category="+request.getParameter("category")+"&cost_range="+request.getParameter("cost_range")+"&sort="+request.getParameter("sort")+"&current_page=index";
                                        else url = "MainController?action=activateProduct&productid=" + i.getId() + "&keyword=" + request.getParameter("keyword")+"&category="+request.getParameter("category")+"&cost_range="+request.getParameter("cost_range")+"&sort="+request.getParameter("sort")+"&current_page=index";
                                    %>
                                    <a href="<%=url%>" class="btn btn-medium btn-black">
                                        <%= i.isStatus() ? "Delete" : "Activate" %><svg class="cart-outline">
                                            <use xlink:href="#cart-outline"></use>
                                        </svg>
                                    </a>
                                </div> 
                                <% } else { %>
                                        <div class="cart-button d-flex">
                                            <a href="MainController?action=addItem&idproduct=<%= i.getId() %>&current_page=index" class="btn btn-medium btn-black">Add to Cart<svg class="cart-outline"><use xlink:href="#cart-outline"></use></svg></a>
                                        </div><%}%>
                                    </div>
                                    <div class="card-detail d-flex justify-content-between align-items-baseline pt-3">
                                        <h3 class="card-title text-uppercase">
                                            <span style="font-size: 23px"><%= i.getName() %></span>
                                        </h3>
                                        <span class="item-price text-primary">$<%= i.getPrice() %></span>
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
            </div>
            <div class="swiper-pagination position-absolute text-center"></div>
        </section>
        <% } } %>
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
        <%@ include file="partials/footer.jsp" %>
    </body>
</html>