<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.UserDTO"%>
<%@page import="util.AuthUtils" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/register_style.css"/>
</head>
<body>
<%@ include file="partials/header.jsp" %>

<%
    String UsernameAlert = (String) request.getAttribute("UserNameAlert");
    String EmailAlert = (String) request.getAttribute("EmailAlert");
    Boolean ok=(Boolean) request.getAttribute("ok");
    UserDTO user = AuthUtils.getCurrentUser(request);
    if (user != null) {
        response.sendRedirect("userinformation.jsp");
    } else {
%>

<div class="register-wrapper">
    <div class="register-card">
        <h2 class="form-title">Register</h2>
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="register">

            <div class="mb-3">
                <label for="Username" class="form-label">Username</label>
                <input type="text" class="form-control" id="Username" name="username"
                       value="<%=(UsernameAlert==null && request.getParameter("username")!=null && ok==null)?request.getParameter("username"):""%>">
                <div class="error"><%= UsernameAlert == null ? "" : UsernameAlert %></div>
            </div>

            <div class="mb-3">
                <label for="Password" class="form-label">Password</label>
                <input type="password" class="form-control" id="Password" name="password">
            </div>

            <div class="mb-3">
                <label for="Email" class="form-label">Email</label>
                <input type="email" class="form-control" id="Email" name="email"
                       value="<%=(EmailAlert==null && request.getParameter("email")!=null && ok==null)?request.getParameter("email"):""%>">
                <div class="error"><%= EmailAlert == null ? "" : EmailAlert %></div>
            </div>

            <div class="mb-4">
                <label for="Fullname" class="form-label">Fullname</label>
                <input type="text" class="form-control" id="Fullname" name="fullname" value="<%=request.getAttribute("fullname")==null?"":(String) request.getAttribute("fullname")%>">
            </div>

            <div class="d-grid">
                <button type="submit" class="btn submit-btn">Register</button>
            </div>
        </form>
<% if (request.getAttribute("register_msg_success") != null) { %>
    <div class="alert alert-success alert-dismissible fade show mt-3" role="alert">
        <%= request.getAttribute("register_msg_success") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<% } %>

<% if (request.getAttribute("register_msg_fail") != null) { %>
    <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
        <%= request.getAttribute("register_msg_fail") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<% } %>

        <p class="form-note">Already have an account? <a href="login.jsp">Login here</a></p>
        
    </div>
</div>

<% } %>
<%@ include file="partials/footer.jsp" %>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
