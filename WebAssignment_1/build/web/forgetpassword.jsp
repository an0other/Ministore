<%-- 
    Document   : forgetpassword
    Created on : Jul 15, 2025, 6:29:04 PM
    Author     : an0other
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Forget Password - MiniStore</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/forgetpassword_style.css"/>
</head>
<body>
    <%
        String success_notify = (String) request.getAttribute("success_notify");
        String fail_notify = (String) request.getAttribute("fail_notify");
    %>
    <%@ include file="partials/header.jsp" %>
    <div class="container">
        <div class="forget-password-container">
            <h2 class="text-center fw-bold mb-4">Forget Password</h2>
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="forgetPassword">
                <div class="mb-3">
                    <label for="username" class="form-label">Enter your username</label>
                    <input type="text" class="form-control" id="username" name="username"
                           placeholder="Enter your username" required>
                </div>
                <div class="d-grid mb-3">
                    <button type="submit" class="btn forget-password-btn w-100">Send Code</button>
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