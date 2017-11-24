<%@ page import="com.shampoo.webapp.model.DriverBean" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.NumberFormat" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session" />
<jsp:useBean id="orderData" class="com.shampoo.webapp.model.OrderBean" scope="session" />
<% if (userData.getUserID() == null) { response.sendRedirect("login.jsp"); } else {%>
<html>
<head>
    <title>Select Driver</title>
    <link rel="stylesheet" type="text/css" href="styles/order.css">
    <script src="js/handler.js"></script>
</head>
<body>
<div class="container-wide">
    <div class="nav-top">
        <div class="logo">
            <p class="projek">
                <b class="pr">PR</b> <b class="strip">-</b> <b class="ojek">OJEK</b>
            </p>
            <p class="wush">
                wushh... wushh... ngeeeeeenggg...
            </p>
        </div>
        <div class="profile-link">
            <p>
                <span>Hi, </span> <% out.print("<b>" + userData.getUsername() + "</b>!");%>
            </p>
            <p>
                <a href="handleLogout.jsp" class="logout">Logout</a>
            </p>
        </div>
    </div>
    <div class="nav-bot">
        <div class="section" id="section-order">
            <a href="order.jsp" class="section-name" id="order">
                ORDER
            </a>
        </div>
        <div class="section" id="section-history">
            <a href="/transactionuser" class="section-name" id="history">
                HISTORY
            </a>
        </div>
        <div class="section" id="section-profile">
            <a href="profile.jsp" class="section-name" id="profile">
                MY PROFILE
            </a>
        </div>
    </div>
    <div class="order-body">
        <h1 class="order-header">
            MAKE AN ORDER
        </h1>
        <div class="order-nav-bar">
            <div class="order-section order-section-1">
                <div class="order-section-circle">
                    <p> 1 </p>
                </div>
                <div class="order-section-text">
                    Select Destination
                </div>
            </div>

            <div class="order-section order-section-2 order-section-active">
                <div class="order-section-circle">
                    <p> 2 </p>
                </div>
                <div class="order-section-text">
                    Select a Driver
                </div>
            </div>

            <div class="order-section order-section-3">
                <div class="order-section-circle">
                    <p> 3 </p>
                </div>
                <div class="order-section-text">
                    Chat Driver
                </div>
            </div>

            <div class="order-section order-section-4">
                <div class="order-section-circle">
                    <p> 4 </p>
                </div>
                <div class="order-section-text">
                    Complete your Order
                </div>
            </div>
        </div>

        <div class="preferred-driver-container">
            <h2 class="select-driver-header"> PREFERRED DRIVERS: </h2>
            <% NumberFormat formatter = new DecimalFormat("#0.00");
                if(orderData.getPreferredDrivers().size() == 0) { %>
            <div class='no-driver-found'>
                <p>Nothing to display :(</p>
            </div>
            <% } else {
                DriverBean currentDriver;
                for(int i = 0; i < orderData.getPreferredDrivers().size(); i++) {
                    currentDriver = orderData.getPreferredDrivers().get(i); %>
            <div class='driver-container'>
                <img class='driver-image' src='<% out.print("images/" + currentDriver.getProfilePicture()); %>'>
                <div class='driver-detail-container'>
                    <h2 class='driver-name'> <% out.print(currentDriver.getName()); %> </h2>
                    <div class='driver-rating-votes'>
                        <img src='img/star.svg' class='image-rating'/>
                        <p class='driver-rating-details'> <% out.print(formatter.format(currentDriver.getRating())); %> </p>
                        <p class='driver-vote-details'> (<% out.print(currentDriver.getVotes()); %> votes) </p>
                    </div>
                    <form class='choose-button-container' action='/selectdriver' method='post'>
                        <input type='hidden' name='driverId' value='<% out.print(currentDriver.getDriverId()); %>'/>
                        <input type='hidden' name='originCity' value='<% out.print(orderData.getPickingPoint()); %>' />
                        <input type='hidden' name='destinationCity' value='<% out.print(orderData.getDestination()); %>' />
                        <input class='choose-you-button' type='submit' name='submit' value='i choose you!' onclick='clicked(event)'/>
                    </form>
                </div>
            </div>
            <% }
            } %>
        </div>

        <div class="other-driver-container">
            <h2 class="select-driver-header"> OTHER DRIVERS: </h2>
            <% if(orderData.getOtherDrivers().size() == 0) { %>
            <div class='no-driver-found'>
                <p>Nothing to display :(</p>
            </div>
            <% } else {
                DriverBean currentDriver;
                for(int i = 0; i < orderData.getOtherDrivers().size(); i++) {
                    currentDriver = orderData.getOtherDrivers().get(i);
                    System.out.println(Integer.toString(i) + currentDriver.getName()); %>
            <div class='driver-container'>
                <img class='driver-image' src='<% out.print("images/" + currentDriver.getProfilePicture()); %>'>
                <div class='driver-detail-container'>
                    <h2 class='driver-name'> <% out.print(currentDriver.getName()); %> </h2>
                    <div class='driver-rating-votes'>
                        <img src='img/star.svg' class='image-rating'/>
                        <p class='driver-rating-details'><% out.print(formatter.format(currentDriver.getRating())); %></p>
                        <p class='driver-vote-details'> (<% out.print(currentDriver.getVotes()); %> votes) </p>
                    </div>
                    <form class='choose-button-container' action='/selectdriver' method='post'>
                        <input type='hidden' name='driverId' value='<% out.print(currentDriver.getDriverId()); %>'/>
                        <input type='hidden' name='originCity' value='<% out.print(orderData.getPickingPoint()); %>' />
                        <input type='hidden' name='destinationCity' value='<% out.print(orderData.getDestination()); %>' />
                        <input class='choose-you-button' type='submit' name='submit' value='i choose you!' id='choose-you-button' onclick='clicked(event)'>
                    </form>
                </div>
            </div>
            <% }
            } %>
        </div>
    </div>
</div>
</body>
</html>
<% } %>