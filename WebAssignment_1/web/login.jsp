<%-- 
    Document   : login
    Author     : an0other
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.UserDTO"%>
<%@page import="util.AuthUtils" %>
<%
    UserDTO user = AuthUtils.getCurrentUser(request);
    if (user != null) {
        request.setAttribute("username", user.getUsername());
        request.getRequestDispatcher("MainController?action=getAllOrder").forward(request, response);
        return;
    }
    String success_notify = (String) request.getAttribute("success_notify");
    String fail_notify = (String) request.getAttribute("fail_notify");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - MiniStore</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/login_style.css"/>
</head>
<body>
<%@ include file="partials/header.jsp" %>
    <div class="container">
        <div class="login-container">
            <h2 class="text-center fw-bold mb-4">LOGIN</h2>
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="login">
                <div class="mb-3">
                    <label for="Username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="Username" name="username"
                           placeholder="Enter your username" required
                           value="<%=request.getAttribute("username") == null ? "" : (String)request.getAttribute("username")%>">
                </div>
                <div class="mb-3">
                    <label for="Password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="Password" name="password"
                           placeholder="Enter your password" required>
                </div>
                <div class="d-grid mb-3">
                    <button type="submit" class="btn login-btn w-100">Login</button>
                </div>
                <div class="text-center">
                    <p class="mb-1">Don't have an account?
                        <a href="register.jsp" class="link-primary">Register here</a>
                    </p>
                    <p class="forget-password-link mb-1">
                        <a href="forgetpassword.jsp" class="link-primary">Forget Password?</a>
                    </p>
                </div>
            </form>
        </div>
    </div>
    <div class="notification-container">
        <% if (success_notify != null) { %>
            <div class="message-container success-message" id="success-notify">
                <%= success_notify %>
            </div>
        <% } %>
        <% if (fail_notify != null) { %>
            <div class="message-container error-message" id="error-notify">
                <%= fail_notify %>
            </div>
        <% } %>
    </div>
<%@ include file="partials/footer.jsp" %>
    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const successNotify = document.getElementById('success-notify');
            const errorNotify = document.getElementById('error-notify');

            function showNotification(element) {
                if (element) {
                    element.style.display = 'block';
                    element.style.opacity = '1';
                    element.classList.add('bounce-in');
                    setTimeout(() => {
                        element.classList.remove('bounce-in');
                        element.style.opacity = '0';
                        setTimeout(() => {
                            element.style.display = 'none';
                        }, 500); // Match transition duration
                    }, 3000); // Display for 3 seconds
                }
            }

            // Show only one notification at a time (success takes precedence)
            if (successNotify) {
                showNotification(successNotify);
            } else if (errorNotify) {
                showNotification(errorNotify);
            }
        });
    </script>
</body>
</html>