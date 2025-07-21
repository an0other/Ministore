<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.AuthUtils" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Create Category</title>

        <!-- ✅ Bootstrap 5 CDN -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="assets/css/categoryform_style.css"/>
    </head>
    <body>
        <%@ include file="partials/header.jsp" %>

        <%
            String categoryID_message = (String) request.getAttribute("categoryidmessage");
            String categoryName_message = (String) request.getAttribute("categorynamemessage");
            String category_id = (String) request.getAttribute("category_id");
            String category_name = (String) request.getAttribute("category_name");
            String successMsg = (String) request.getAttribute("createsuccess_msg");
            String failMsg = (String) request.getAttribute("createfail_msg");
        %>

        <div class="form-container bg-white">
            <h3 class="text-center mb-4">Create Category</h3>
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="createcategory">

                <div class="mb-3">
                    <label for="id" class="form-label">Category ID*</label>
                    <input type="text" class="form-control" id="id" name="category_id" required
                           value="<%= category_id == null ? "" : category_id %>">
                    <% if (categoryID_message != null) { %>
                        <div class="form-text-error"><%= categoryID_message %></div>
                    <% } %>
                </div>

                <div class="mb-3">
                    <label for="name" class="form-label">Category Name*</label>
                    <input type="text" class="form-control" id="name" name="category_name" required
                           value="<%= category_name == null ? "" : category_name %>">
                    <% if (categoryName_message != null) { %>
                        <div class="form-text-error"><%= categoryName_message %></div>
                    <% } %>
                </div>

                <div class="d-flex justify-content-between">
                    <button type="submit" class="btn btn-black">Create</button>
                    <button type="reset" class="btn btn-black">Reset</button>
                </div>

            </form>

            <% if (successMsg != null) { %>
                <div class="message success"><%= successMsg %></div>
            <% } %>

            <% if (failMsg != null) { %>
                <div class="message fail"><%= failMsg %></div>
            <% } %>
        </div>

        <%@ include file="partials/footer.jsp" %>
    </body>
</html>
