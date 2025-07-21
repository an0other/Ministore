<%-- 
    Document   : cart
    Created on : Jun 9, 2025, 10:07:24 PM
    Author     : an0other
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.OrderItemDTO"%>
<%@page import="java.util.List" %>
<%@page import="util.AuthUtils" %>
<%@page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
  <head>
        <title>Cart Page</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="format-detection" content="telephone=no">
        <meta name="mobile-web-app-capable" content="yes">
        <meta name="author" content="">
        <meta name="keywords" content="">
        <meta name="description" content="">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="assets/css/style.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
        <link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet">
        <link rel="stylesheet" href="assets/css/cartstyle.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.css" />
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Jost:wght@300;400;500&family=Lato:wght@300;400;700&display=swap" rel="stylesheet">
        <!-- Font Awesome 5 Free CDN -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <script src="assets/js/modernizr.js"></script>
    </head>
  <body>
    <%@ include file="partials/header.jsp" %>
    <%
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String mode = (String) request.getAttribute("mode");
        List<OrderItemDTO> list = null;
        List<Integer> quantity_list = null;
        if (mode != null && mode.equals("view")) {
            list = (List<OrderItemDTO>) request.getAttribute("order_item_list");
        } else {
            list = (List<OrderItemDTO>) session.getAttribute("cart_list");
            quantity_list = (List<Integer>) request.getAttribute("quantity_list");
        }
        String order_id = null;
        String status = null;
        if (mode != null && mode.equals("view")) {
            order_id = request.getParameter("order_id");
            status = request.getParameter("status");
        }
        double sum = 0;
    %>
    <% if (mode != null && mode.equals("view")) { %>
    <form action="MainController">
        <input type="hidden" name="action" value="filterOrder">
        <input type="hidden" name="status" value="<%= request.getParameter("status") %>">
        <input type="hidden" name="time" value="<%= request.getParameter("time") %>">
        <button type="submit" class="btn btn-outline-primary go-back-btn">
            <i class="fas fa-arrow-left mr-2"></i> Go Back
        </button>
    </form>
    <% } %>
    <% if (order_id != null) { %>
        <div class="order-details">
            <h4 class="order-id"><i class="fas fa-hashtag mr-2"></i>Order ID: <%= order_id %></h4>
            <h4 class="order-date"><i class="fas fa-calendar-alt mr-2"></i>Date: <%= request.getParameter("time") %></h4>
        </div>
    <% } %>
    <main class="page">
      <section class="shopping-cart dark">
        <div class="container">
          <div class="content">
            <div class="row">
              <div class="col-md-12 col-lg-8">
                <div class="items">
                  <%
                    if (list == null || list.isEmpty()) {
                  %>
                  <div class="alert alert-light text-center p-5">
                      <svg width="64" height="64" fill="#6c757d" class="mb-3" viewBox="0 0 16 16">
                          <use xlink:href="#cart-outline"></use>
                      </svg>
                      <h4 class="text-secondary">Your cart is empty</h4>
                      <p class="mb-4">Looks like you haven’t added anything to your cart yet.</p>
                      <a href="MainController?action=searchProduct" class="btn btn-outline-primary">
                          Continue Shopping
                      </a>
                  </div>
                  <%
                    } else {
                      for (int k = 0; k < list.size(); k++) {
                          OrderItemDTO i = list.get(k);
                          if (i.getQuantity() > 0) {
                              int quantity = 0;
                              if (mode == null) {
                                  quantity = Math.min(i.getQuantity(), quantity_list.get(k));
                              } else {
                                  quantity = i.getQuantity();
                              }
                              sum += i.getPrice() * quantity;
                  %>
                  <div class="product">
                    <div class="row">
                      <div class="col-md-3">
                        <img class="img-fluid mx-auto d-block image" src="image?name=<%= i.getImg() %>" />
                      </div>
                      <div class="col-md-8">
                        <div class="info">
                          <div class="row">
                            <div class="col-md-5 product-name">
                              <div class="product-name">
                                <a href="#"><%= i.getProduct_name() %></a>
                              </div>
                            </div>
                            <div class="col-md-4 quantity">
                              <label for="quantity">Quantity:</label>
                              <form action="MainController" method="post" class="update-form">
                                <input type="hidden" name="action" value="updateQuantity">
                                <input type="hidden" name="product_id" value="<%= i.getProduct_id() %>">
                                <% if (mode == null) { %>
                                    <input type="hidden" name="max_quantity_product" value="<%= quantity_list.get(k) %>">
                                <% } %>
                                <% if (quantity == 0) { %>
                                    <input type="number" name="quantity" value="0" class="form-control quantity-input" disabled />
                                    <small class="text-danger">Out of stock</small>
                                <% } else { %>
                                    <input type="number" name="quantity" value="<%= quantity %>" min="1" max="<%= quantity %>" class="form-control quantity-input" onchange="this.form.submit();" />
                                <% } %>
                              </form>
                            </div>
                            <div class="col-md-3 price text-end">
                              <span class="d-block">$<%= i.getPrice() %></span>
                              <% if (mode == null) { %>
                                  <form action="MainController" method="post">
                                    <input type="hidden" name="action" value="removeFromCart">
                                    <input type="hidden" name="product_id" value="<%= i.getProduct_id() %>">
                                    <button type="submit" class="btn btn-sm btn-outline-danger mt-2">Remove</button>
                                  </form>
                                  <% if (request.getAttribute("buy_msg") != null) { %>
                                      <div class="message-container error-message">
                                          <%= (String) request.getAttribute("buy_msg") %>
                                      </div>
                                  <% } %>
                              <% } %>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <% } } } %>
                </div>
              </div>
              <% session.setAttribute("total_price", sum); %>
              <div class="col-md-12 col-lg-4">
                <div class="summary">
                  <h3>Summary</h3>
                  <div class="summary-item">
                    <span class="text">Subtotal</span><span class="price">$<%= sum %></span>
                  </div>
                  <div class="summary-item">
                    <span class="text">Discount</span><span class="price">$0</span>
                  </div>
                  <div class="summary-item">
                    <span class="text">Shipping</span><span class="price">$0</span>
                  </div>
                  <div class="summary-item">
                    <span class="text">Total</span><span class="price">$<%= sum %></span>
                  </div>
                  <% if (mode == null) { %>
                      <a href="MainController?action=createOrder" class="btn btn-black btn-lg btn-block">Buy</a>
                  <% } %>
                  <% if (request.getAttribute("place_order_success_msg") != null) { %>
                      <div class="message-container success-message">
                          <%= request.getAttribute("place_order_success_msg") %>
                      </div>
                      <script>
                          console.log('Success message added to DOM:', '<%= request.getAttribute("place_order_success_msg") %>');
                      </script>
                  <% } %>
                  <% if (request.getAttribute("place_order_fail_msg") != null) { %>
                      <div class="message-container error-message">
                          <%= request.getAttribute("place_order_fail_msg") %>
                      </div>
                  <% } %>
                  <% if (mode != null && mode.equals("view") && AuthUtils.isAdmin(request)) { %>
                  <form action="MainController" class="change-status-form" method="post">
                          <input type="hidden" name="action" value="changeStatus">
                          <input type="hidden" name="order_id" value="<%= order_id %>">
                          <input type="hidden" name="order_current_status" value="<%= status %>">
                          <select name="order_status" class="status-select">
                              <option value="Pending" <%= "Pending".equals(status) ? "selected" : "" %>>Pending</option>
                              <option value="Preparing" <%= "Preparing".equals(status) ? "selected" : "" %>>Preparing</option>
                              <option value="Delivering" <%= "Delivering".equals(status) ? "selected" : "" %>>Delivering</option>
                              <option value="Done" <%= "Done".equals(status) ? "selected" : "" %>>Done</option>
                              <option value="Canceled" <%= "Canceled".equals(status) ? "selected" : "" %>>Canceled</option>
                          </select>
                          <input type="submit" value="Update Status" class="btn btn-black btn-block">
                      </form>
                  <% } %>
                  <% if (mode != null && AuthUtils.isMember(request) && !status.equals("Canceled")) { %>
                  <form action="MainController" class="action-button" method="post">
                          <input type="hidden" name="action" value="cancelOrder">
                          <input type="hidden" name="order_id" value="<%= order_id %>">
                          <input type="submit" value="Cancel Order" class="btn btn-black btn-lg btn-block">
                      </form>
                  <% } %>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
    <canvas id="fireworks-canvas"></canvas>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script>
    (function () {
        var canvas = document.getElementById('fireworks-canvas');
        var ctx = canvas.getContext('2d');
        if (!ctx) {
            console.error('Canvas context not supported');
            return;
        }

        var cw = window.innerWidth;
        var ch = window.innerHeight;
        canvas.width = cw;
        canvas.height = ch;

        var fireworks = [];
        var particles = [];
        var hue = 120;
        var timerTotal = 80;
        var timerTick = 0;
        var animationRunning = false;

        function random(min, max) {
            return Math.random() * (max - min) + min;
        }

        function calculateDistance(p1x, p1y, p2x, p2y) {
            var xDistance = p1x - p2x;
            var yDistance = p1y - p2y;
            return Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        }

        function Firework(sx, sy, tx, ty) {
            this.x = sx;
            this.y = sy;
            this.sx = sx;
            this.sy = sy;
            this.tx = tx;
            this.ty = ty;
            this.distanceToTarget = calculateDistance(sx, sy, tx, ty);
            this.distanceTraveled = 0;
            this.coordinates = [];
            this.coordinateCount = 5;
            while (this.coordinateCount--) {
                this.coordinates.push([this.x, this.y]);
            }
            this.angle = Math.atan2(ty - sy, tx - sx);
            this.speed = 3;
            this.acceleration = 1.08;
            this.brightness = random(60, 90);
            this.targetRadius = 2;
        }

        Firework.prototype.update = function (index) {
            this.coordinates.pop();
            this.coordinates.unshift([this.x, this.y]);
            this.targetRadius = this.targetRadius < 12 ? this.targetRadius + 0.5 : 2;
            this.speed *= this.acceleration;
            var vx = Math.cos(this.angle) * this.speed;
            var vy = Math.sin(this.angle) * this.speed;
            this.distanceTraveled = calculateDistance(this.sx, this.sy, this.x + vx, this.y + vy);
            if (this.distanceTraveled >= this.distanceToTarget) {
                createParticles(this.tx, this.ty);
                fireworks.splice(index, 1);
            } else {
                this.x += vx;
                this.y += vy;
            }
        };

        Firework.prototype.draw = function () {
            try {
                ctx.beginPath();
                ctx.moveTo(this.coordinates[this.coordinates.length - 1][0], this.coordinates[this.coordinates.length - 1][1]);
                ctx.lineTo(this.x, this.y);
                ctx.strokeStyle = 'hsl(' + hue + ', 100%, ' + this.brightness + '%)';
                ctx.lineWidth = 3;
                ctx.stroke();
                ctx.beginPath();
                ctx.arc(this.tx, this.ty, this.targetRadius, 0, Math.PI * 2);
                ctx.stroke();
            } catch (e) {
                console.error('Error drawing firework:', e);
            }
        };

        function Particle(x, y) {
            this.x = x;
            this.y = y;
            this.coordinates = [];
            this.coordinateCount = 10;
            while (this.coordinateCount--) {
                this.coordinates.push([this.x, this.y]);
            }
            this.angle = random(0, Math.PI * 2);
            this.speed = random(2, 15);
            this.friction = 0.93;
            this.gravity = 1.5;
            this.hue = random(hue - 50, hue + 50);
            this.brightness = random(60, 90);
            this.alpha = 1;
            this.decay = random(0.01, 0.02);
        }

        Particle.prototype.update = function (index) {
            this.coordinates.pop();
            this.coordinates.unshift([this.x, this.y]);
            this.speed *= this.friction;
            this.x += Math.cos(this.angle) * this.speed;
            this.y += Math.sin(this.angle) * this.speed + this.gravity;
            this.alpha -= this.decay;
            if (this.alpha <= this.decay) {
                particles.splice(index, 1);
            }
        };

        Particle.prototype.draw = function () {
            try {
                ctx.beginPath();
                ctx.moveTo(this.coordinates[this.coordinates.length - 1][0], this.coordinates[this.coordinates.length - 1][1]);
                ctx.lineTo(this.x, this.y);
                ctx.strokeStyle = 'hsla(' + this.hue + ', 100%, ' + this.brightness + '%, ' + this.alpha + ')';
                ctx.lineWidth = 2;
                ctx.stroke();
            } catch (e) {
                console.error('Error drawing particle:', e);
            }
        };

        function createParticles(x, y) {
            var particleCount = 40;
            while (particleCount--) {
                particles.push(new Particle(x, y));
            }
        }

        function loop() {
            if (!animationRunning) return;
            try {
                requestAnimationFrame(loop);
                hue += 1.0;
                ctx.globalCompositeOperation = 'destination-out';
                ctx.fillStyle = 'rgba(0, 0, 0, 0.4)';
                ctx.fillRect(0, 0, cw, ch);
                ctx.globalCompositeOperation = 'lighter';

                for (let i = fireworks.length - 1; i >= 0; i--) {
                    if (fireworks[i] && typeof fireworks[i].update === 'function') {
                        fireworks[i].draw();
                        fireworks[i].update(i);
                    }
                }

                while (particles.length > 800) {
                    particles.shift();
                }

                for (let i = particles.length - 1; i >= 0; i--) {
                    if (particles[i] && typeof particles[i].update === 'function') {
                        particles[i].draw();
                        particles[i].update(i);
                    }
                }

                if (timerTick >= timerTotal) {
                    for (let j = 0; j < 3; j++) {
                        fireworks.push(new Firework(
                            cw / 2,
                            ch,
                            random(0.2 * cw, 0.8 * cw),
                            random(0, ch / 2)
                        ));
                    }
                    timerTick = 0;
                } else {
                    timerTick++;
                }
            } catch (e) {
                console.error('Error in animation loop:', e);
                animationRunning = false;
            }
        }

        window.addEventListener('resize', function () {
            cw = canvas.width = window.innerWidth;
            ch = canvas.height = window.innerHeight;
        });

        function startFireworks() {
            console.log('Starting fireworks animation');
            canvas.style.display = 'block';
            animationRunning = true;
            fireworks = [];
            particles = [];
            loop();
        }

        document.addEventListener('DOMContentLoaded', function () {
            if (document.querySelector('.success-message')) {
                startFireworks();
            } else {
                setTimeout(() => {
                    if (document.querySelector('.success-message')) {
                        startFireworks();
                    }
                }, 1000);

                const observer = new MutationObserver(() => {
                    if (document.querySelector('.success-message')) {
                        startFireworks();
                        observer.disconnect();
                    }
                });
                observer.observe(document.body, { childList: true, subtree: true });
            }
        });

        window.addEventListener('unload', function () {
            animationRunning = false;
            fireworks = [];
            particles = [];
            canvas.style.display = 'none';
        });
    })();

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

        if (actionInput && actionInput.value === 'cancelOrder') {
            form.addEventListener('submit', function(e) {
                const confirmCancel = confirm('Are you sure you want to cancel this order?');
                if (!confirmCancel) {
                    e.preventDefault();
                }
            });
        }
    });
    </script>

    <%@ include file="partials/footer.jsp" %>
  </body>
</html>