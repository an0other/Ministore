<%-- 
    Document   : changepassword
    Created on : Jun 26, 2025, 10:25:23 PM
    Author     : an0other
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Password</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="assets/css/changepassword_style.css"/>
    </head>
    <body>
        <%@ include file="partials/header.jsp" %>
        <%
            String current_password = (String) request.getAttribute("current_password");
            String old_password_msg = (String) request.getAttribute("old_password_msg");
            String new_password_msg = (String) request.getAttribute("new_password_msg");
            String success_msg = (String) request.getAttribute("success_msg");
            String fail_msg = (String) request.getAttribute("fail_msg");
        %>
        <div class="form-container">
            <h3 class="form-title">Change Password</h3>
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="changePassword">
                <%if (request.getAttribute("isForgetPassword")==null){%><div class="mb-3">
                    <label for="current_password" class="form-label">Current Password</label>
                    <input type="password" class="form-control" id="current_password" name="current_password" value="<%= (current_password == null) ? "" : current_password %>" required>
                    <% if (old_password_msg != null && !old_password_msg.isEmpty()) { %>
                        <div class="form-text-error"><%= old_password_msg %></div>
                    <% } %>
                </div><%} else {%>
                    <input type="hidden" name="isForgetPassword" value="ok">
                <%}%>
                <div class="mb-3">
                    <label for="new_password1" class="form-label">New Password</label>
                    <input type="password" class="form-control" id="new_password1" name="new_password1" required>
                    <% if (new_password_msg != null && !new_password_msg.isEmpty()) { %>
                        <div class="form-text-error"><%= new_password_msg %></div>
                    <% } %>
                </div>
                <div class="mb-3">
                    <label for="new_password2" class="form-label">Re-enter New Password</label>
                    <input type="password" class="form-control" id="new_password2" name="new_password2" required>
                </div>
                <div class="d-flex justify-content-between">
                    <button type="submit" class="btn btn-black">Change</button>
                </div>
            </form>
            <% if (success_msg != null && !success_msg.isEmpty()) { %>
                <div id="success-notify" class="notification success"><%= success_msg %></div>
            <% } %>
            <% if (fail_msg != null && !fail_msg.isEmpty()) { %>
                <div id="fail-notify" class="notification fail"><%= fail_msg %></div>
            <% } %>
        </div>
        <%@ include file="partials/footer.jsp" %>
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
    </body>
</html>