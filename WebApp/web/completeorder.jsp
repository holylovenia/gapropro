<%@ page import="com.shampoo.webapp.model.OrderBean" %>
<%@ page import="com.shampoo.webapp.model.DriverBean" %>
<%@ page import="java.util.Objects" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session"/>
<jsp:useBean id="orderData" class="com.shampoo.webapp.model.OrderBean" scope="session"/>
<% if (userData.getUserID() == null) response.sendRedirect("login.jsp");%>
<html>
<head>
    <title>Complete Order</title>
    <link rel="stylesheet" type="text/css" href="styles/order.css">
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
                <span>Hi, </span> Budi
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

            <div class="order-section order-section-2">
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

            <div class="order-section order-section-4 order-section-active">
                <div class="order-section-circle">
                    <p> 4 </p>
                </div>
                <div class="order-section-text">
                    Complete your Order
                </div>
            </div>
        </div>
        <h2 class="complete-order-header">
            HOW WAS IT?
        </h2>

        <%
            Integer selectedDriverId = orderData.getDriverId();
            Integer counter = 0;
            boolean found = false;
            DriverBean selectedDriver = null;
            while(!found && counter < orderData.getPreferredDrivers().size()) {
                if(selectedDriverId == orderData.getPreferredDrivers().get(counter).getDriverId()) {
                    selectedDriver = orderData.getPreferredDrivers().get(counter);
                    found = true;
                } else {
                    counter++;
                }
            }
            counter = 0;
            while(!found && counter < orderData.getOtherDrivers().size()) {
                if(selectedDriverId == orderData.getOtherDrivers().get(counter).getDriverId()) {
                    selectedDriver = orderData.getOtherDrivers().get(counter);
                    found = true;
                } else {
                    counter++;
                }
            }
        %>

        <div class="complete-order-container">
            <img src="<% out.print("images/" + selectedDriver.getProfilePicture()); %>" class="complete-order-image"/>
            <p class="complete-order-username"><% out.print(selectedDriver.getUsername()); %></p>
            <p class="complete-order-name"><% out.print(selectedDriver.getName()); %></p>

        </div>
        <form action="/completeorder" method="post" class="complete-order-comment-rate" ng-app="finishOrder"
              ng-controller="finishOrderController">
            <input type="hidden" name="driverId" value=""/>
            <input type="hidden" name="originCity" value=""/>
            <input type="hidden" name="destinationCity" value=""/>
            <input type="hidden" name="date" value=""/>
            <div class="rating-5-star">
                <input type="radio" name="rating" id="star-5" value="5"/> <label title="5" class="star-5"
                                                                                 for="star-5"></label>
                <input type="radio" name="rating" id="star-4" value="4"/> <label title="4" class="star-4"
                                                                                 for="star-4"></label>
                <input type="radio" name="rating" id="star-3" value="3"/> <label title="3" class="star-3"
                                                                                 for="star-3"></label>
                <input type="radio" name="rating" id="star-2" value="2"/> <label title="2" class="star-2"
                                                                                 for="star-2"></label>
                <input type="radio" name="rating" id="star-1" value="1"/> <label title="1" class="star-1"
                                                                                 for="star-1"></label>
            </div>
            <textarea class="comment-container" name="comment" placeholder="Your comment..."></textarea>
            <input class="order-complete-button" type="submit" name="submit" value="complete order"
                   ng-click="finishOrder()"/>
        </form>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.6.2/firebase.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.6.2/firebase-messaging.js"></script>
<script>
    var app = angular.module("finishOrder", []);
    $rootScope.targetId = <%=orderData.getDriverId()%>;

    /**
     * Finish Order Controller
     * Controls finish order (close driver-side chat)
     */
    app.controller("finishOrderController", function ($scope, $http, $rootScope, $window) {

        $scope.finishOrder = function () {
            $http.post("http://localhost:3000/chat/finish_chat", {
                "receiverId": $rootScope.targetId
            })
        };

    });

</script>

</body>
</html>