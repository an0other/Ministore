<%-- 
    Document   : noticepage
    Created on : Jul 7, 2025, 9:28:55 AM
    Author     : an0other
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="assets/css/noticepage_style.css"/>
    </head>
    <body>
        <%@ include file="partials/header.jsp" %>
        <%
            String notice_msg = (String) request.getAttribute("notice_msg");
            String return_link = (String) request.getAttribute("return_link");
        %>
        <div class="notice-container">
            <h3 class="notice-title">Error</h3>
            <a href="<%= return_link == null ? "index.jsp" : return_link %>" class="btn-custom">Go back</a>
        </div>
        <% if (notice_msg != null && !notice_msg.isEmpty()) { %>
            <div id="notice-notify" class="notification error"><%= notice_msg %></div>
        <% } %>
        <%@ include file="partials/footer.jsp" %>
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const noticeNotify = document.getElementById('notice-notify');
                if (noticeNotify) {
                    setTimeout(() => {
                        noticeNotify.classList.add('hidden');
                    }, 5000);
                }
            });
        </script>
    </body>
</html>