<%-- 
    Document   : productform
    Created on : Jul 10, 2025
    Author     : an0other
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.AuthUtils" %>
<%@page import="model.ProductDTO" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%
    if (request.getAttribute("category_map") == null) {
        request.getRequestDispatcher("MainController?action=getCategoryMap").forward(request, response);
        return;
    }
    Map<Integer, String> category_map = (HashMap<Integer, String>) request.getAttribute("category_map");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/productform_style.css"/>
</head>
<body>
<%@ include file="partials/header.jsp" %>

<%
    String id = (String) request.getAttribute("id");
    String name = (String) request.getAttribute("name");
    Double price = (Double) request.getAttribute("price");
    String img = (String) request.getAttribute("img");
    String selectedCategoryId = (String) request.getAttribute("category");
    String status = (String) request.getAttribute("status");
    Integer quantity = (Integer) request.getAttribute("quantity");
    String mode = (String) request.getAttribute("mode");
    boolean forceDefault = (selectedCategoryId == null);
%>
<div class="form-container bg-white">
    <h3 class="form-title">Product Form</h3>
    <form action="ProductController" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="<%=mode!=null && mode.equals("active")?"doActivateProduct":"addproduct"%>">
        <input type="hidden" name="page" value="<%=request.getParameter("page")%>">
        <input type="hidden" name="mode" value="<%=mode%>">
        <input type="hidden" name="category" value="<%=request.getParameter("category")==null?"":request.getParameter("category")%>">
        <input type="hidden" name="current_page" value="<%=request.getParameter("current_page")==null?"":request.getParameter("current_page")%>">
        <input type="hidden" name="cost_range" value="<%=request.getParameter("cost_range")==null?"":request.getParameter("cost_range")%>">
        <input type="hidden" name="sort" value="<%=request.getParameter("sort")==null?"":request.getParameter("sort")%>">
        <input type="hidden" name="keyword" value="<%= (request.getParameter("keyword") == null || request.getParameter("keyword").equals("")) ? "" : request.getParameter("keyword") %>">
        <div class="mb-3">
            <label for="product_id" class="form-label">Product ID*</label>
            <input type="text" class="form-control" id="product_id" name="productid"
                   value="<%= id == null ? "" : id %>" <%= mode != null ? "readonly" : "" %>>
            <% if (request.getAttribute("productid_msg") != null) { %>
                <div class="form-text-error"><%= request.getAttribute("productid_msg") %></div>
            <% } %>
        </div>

        <div class="mb-3">
            <label for="name" class="form-label">Product Name*</label>
            <input type="text" class="form-control" id="name" name="name"
                   value="<%= name == null ? "" : name %>" <%= (mode != null && mode.equals("active")) ? "readonly" : "required" %>>
            <% if (request.getAttribute("name_msg") != null) { %>
                <div class="form-text-error"><%= request.getAttribute("name_msg") %></div>
            <% } %>
        </div>

        <div class="mb-3">
            <label for="price" class="form-label">Price*</label>
            <input type="number" step="any" class="form-control" id="price" name="price"
                   value="<%= price == null ? "" : price %>" <%= (mode != null && mode.equals("active")) ? "readonly" : "required" %>>
            <% if (request.getAttribute("price_msg") != null) { %>
                <div class="form-text-error"><%= request.getAttribute("price_msg") %></div>
            <% } %>
        </div>

        <div class="mb-3">
            <label for="img_url" class="form-label">Image URL*</label>
            <input type="file" name="imageurl" accept="image/*" <%= (mode != null && mode.equals("active")) ? "disabled" : ((mode != null && mode.equals("edit"))?"":"required") %>>
            <% String imagePath = (String) request.getAttribute("img"); %>
            <% if (imagePath != null) { %>
                <img src="image?name=<%=imagePath%>" alt="Uploaded image" width="150">
            <% } %>
            <% if (request.getAttribute("img_msg") != null) { %>
                <div class="form-text-error"><%= request.getAttribute("img_msg") %></div>
            <% } %>
        </div>

        <div class="mb-3">
            <label for="category_id" class="form-label">Category*</label>
            <select class="form-select" id="category_id" name="categoryid" <%= (mode != null && mode.equals("active")) ? "disabled" : "required" %>>
                <option value="" <%= forceDefault ? "selected" : "" %>>Choose a category</option>
                <% for (int i : category_map.keySet()) {
                    String selected = (!forceDefault && String.valueOf(i).equals(selectedCategoryId)) ? "selected" : "";
                %>
                    <option value="<%= i %>" <%= selected %>><%= category_map.get(i) %></option>
                <% } %>
            </select>
            <% if (request.getAttribute("category_msg") != null) { %>
                <div class="form-text-error"><%= request.getAttribute("category_msg") %></div>
            <% } %>
        </div>

        <div class="form-check mb-3">
            <input type="checkbox" class="form-check-input" id="status" name="status" value="on" <%= (status != null) ? "checked" : "" %> <%= (mode != null && mode.equals("active")) ? "disabled" : "" %>>
            <label class="form-check-label" for="status">Available</label>
        </div>

        <div class="mb-3">
            <label for="quantity" class="form-label">Quantity</label>
            <input type="number" step="any" class="form-control" id="quantity" name="quantity" min="0"
                   value="<%= quantity == null ? "" : quantity %>">
            <% if (request.getAttribute("quantity_msg") != null) { %>
                <div class="form-text-error"><%= request.getAttribute("quantity_msg") %></div>
            <% } %>
        </div>

        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-dark"><%= mode == null ? "Add" : (mode.equals("edit") ? "Update" : "Active") %></button>
        </div>
    </form>
    <% if (request.getAttribute("completeAddProduct") != null) { %>
        <div id="complete-add-notify" class="notification success"><%= request.getAttribute("completeAddProduct") %></div>
    <% } %>
    <% if (request.getAttribute("failAddProduct") != null) { %>
        <div id="fail-add-notify" class="notification fail"><%= request.getAttribute("failAddProduct") %></div>
    <% } %>
    <% if (request.getAttribute("update_success_msg") != null) { %>
        <div id="update-success-notify" class="notification success"><%= request.getAttribute("update_success_msg") %></div>
    <% } %>
    <% if (request.getAttribute("update_fail_msg") != null) { %>
        <div id="update-fail-notify" class="notification fail"><%= request.getAttribute("update_fail_msg") %></div>
    <% } %>
</div>

<%@ include file="partials/footer.jsp" %>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const statusCheckbox = document.getElementById("status");
        const quantityInput = document.getElementById("quantity");
        quantityInput.disabled = !statusCheckbox.checked;
        statusCheckbox.addEventListener("change", () => {
            quantityInput.disabled = !statusCheckbox.checked;
        });

        // Handle notification fade-out
        const notifications = [
            document.getElementById("complete-add-notify"),
            document.getElementById("fail-add-notify"),
            document.getElementById("update-success-notify"),
            document.getElementById("update-fail-notify")
        ];
        notifications.forEach(notify => {
            if (notify) {
                setTimeout(() => {
                    notify.classList.add("hidden");
                }, 5000);
            }
        });
    });
</script>
</body>
</html>