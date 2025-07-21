<%-- 
    Document   : userinformation
    Created on : Jun 10, 2025, 11:14:53 AM
    Author     : an0other
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.UserDTO" %>
<%@page import="util.AuthUtils" %>
<%@page import="model.OrderDTO" %>
<%@page import="java.util.List" %>
<%@page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="assets/css/userinformation_style.css"/>
        <title>Your Information</title>
    </head>
    <body>
        <%@ include file="partials/header.jsp" %>
        <%
            UserDTO user = (UserDTO) session.getAttribute("user");
            String username = user.getUsername();
            String success_notify = (String) request.getAttribute("success_notify");
            String fail_notify = (String) request.getAttribute("fail_notify");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        %>
        <h1>Hello <%= user.getFullname() %></h1>
        <% if (AuthUtils.isMember(request)) { %>
            <a href="changepassword.jsp" class="change-password">Change Password</a>
        <% } %>
        <% if (success_notify != null && !success_notify.isEmpty()) { %>
            <div id="success-notify" class="notification success"><%= success_notify %></div>
        <% } %>
        <% if (fail_notify != null && !fail_notify.isEmpty()) { %>
            <div id="fail-notify" class="notification fail"><%= fail_notify %></div>
        <% } %>
        
        <%
            String selectedStatus = request.getParameter("status");
            String selectedTime = request.getParameter("time");
        %>
        <form action="MainController" class="filter-form">
            <input type="hidden" name="action" value="filterOrder">
            <input type="hidden" name="username" value="<%= username %>">
            <select name="status">
                <option value="" <%= (selectedStatus == null || selectedStatus.isEmpty()) ? "selected" : "" %>>Status</option>
                < успех>
                <option value="Pending" <%= "Pending".equals(selectedStatus) ? "selected" : "" %>>Pending</option>
                <option value="Preparing" <%= "Preparing".equals(selectedStatus) ? "selected" : "" %>>Preparing</option>
                <option value="Delivering" <%= "Delivering".equals(selectedStatus) ? "selected" : "" %>>Delivering</option>
                <option value="Done" <%= "Done".equals(selectedStatus) ? "selected" : "" %>>Done</option>
                <option value="Canceled" <%= "Canceled".equals(selectedStatus) ? "selected" : "" %>>Canceled</option>
            </select>
            <select name="time">
                <option value="" <%= (selectedTime == null || selectedTime.isEmpty()) ? "selected" : "" %>>Order</option>
                <option value="newest" <%= "newest".equals(selectedTime) ? "selected" : "" %>>Newest</option>
                <option value="oldest" <%= "oldest".equals(selectedTime) ? "selected" : "" %>>Oldest</option>
                <option value="today" <%= "today".equals(selectedTime) ? "selected" : "" %>>Today</option>
                <option value="yesterday" <%= "yesterday".equals(selectedTime) ? "selected" : "" %>>Yesterday</option>
                <option value="lastweek" <%= "lastweek".equals(selectedTime) ? "selected" : "" %>>Last Week</option>
                <option value="lastmonth" <%= "lastmonth".equals(selectedTime) ? "selected" : "" %>>Last Month</option>
            </select>
            <input type="submit" value="Filter">
        </form>
        
        <%
            List<OrderDTO> list = (List<OrderDTO>) request.getAttribute("order_list");
            if (list != null && !list.isEmpty()) {
        %>
        <table>
            <thead>
                <tr>
                    <th>Order's ID</th>
                    <% if (AuthUtils.isAdmin(request)) { %><th>User</th><% } %>
                    <th>Price</th>
                    <% if (AuthUtils.isMember(request) || AuthUtils.isAdmin(request)) { %><th>Status</th><% } %>
                    <th>Date & Time</th>
                    <th>View</th>
                    <% if (AuthUtils.isAdmin(request)) { %><th>Action</th><% } %>
                </tr>
            </thead>
            <tbody>
                <% for (OrderDTO i : list) { %>
                <tr>
                    <td><%= i.getId() %></td>
                    <% if (AuthUtils.isAdmin(request)) { %><td><%= i.getUsername() %></td><% } %>
                    <td>$<%= String.format("%.2f", i.getTotal_price()) %></td>
                    <% if (AuthUtils.isMember(request) || AuthUtils.isAdmin(request)) { %>
                        <td><%= i.getStatus() %></td>
                    <% } %>
                    <td><%= i.getDate().format(formatter) %></td>
                    <td>
                        <form action="MainController" class="action-button" method="post">
                            <input type="hidden" name="action" value="getOrderItemsByOrderID">
                            <input type="hidden" name="order_id" value="<%= i.getId() %>">
                            <input type="hidden" name="status" value="<%= i.getStatus() %>">
                            <input type="hidden" name="time" value="<%= i.getDate().format(formatter) %>">
                            <input type="submit" value="View Order">
                        </form>
                    </td>
                    <% if (AuthUtils.isAdmin(request) && "Canceled".equals(i.getStatus())) { %>
                    <td>
                        <form action="MainController" class="action-button" method="post">
                            <input type="hidden" name="action" value="removeOrder">
                            <input type="hidden" name="id" value="<%= i.getId() %>">
                            <input type="submit" value="Remove">
                        </form>
                    </td>
                    <% } %>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else { %>
        <span class="no-orders">There isn't any order yet!</span>
        <% } %>
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

                // Change button color when select value changes
                document.querySelectorAll('.status-select').forEach(select => {
                    const form = select.closest('form');
                    const button = form.querySelector('input[type="submit"]');
                    const originalStatus = form.querySelector('input[name="order_current_status"]').value;

                    select.addEventListener('change', function() {
                        if (this.value !== originalStatus) {
                            button.style.backgroundColor = '#dc3545'; // Red for changed status
                        } else {
                            button.style.backgroundColor = '#2c2c2c'; // Original color
                        }
                    });
                });
            });
            document.querySelectorAll('form[action="MainController"]').forEach(form => {
                const actionInput = form.querySelector('input[name="action"]');
                const statusSelect = form.querySelector('.status-select');

                if (actionInput && actionInput.value === 'changeStatus' && statusSelect) {
                    form.addEventListener('submit', function(e) {
                        if (statusSelect.value === 'Canceled') {
                            const confirmCancel = confirm('Are you sure you want to cancel this order?');
                            if (!confirmCancel) {
                                e.preventDefault();
                            }
                        }
                    });
                }
            });
        </script>
    </body>
</html>