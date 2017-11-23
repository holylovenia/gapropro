<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session" />
<jsp:useBean id="orderData" class="com.shampoo.webapp.model.OrderBean" scope="session" />

<% if (userData.getUserID() == null) response.sendRedirect("login.jsp");%>
<html>
<head>
    <title>Order</title>
    <link rel="stylesheet" type="text/css" href="styles/order.css">
    <link rel="manifest" href="etc/manifest.json">
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
    <div class="order-body" ng-app="userChat">
        <h2 class="order-header">
            MAKE AN ORDER
        </h2>
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

            <div class="order-section order-section-3 order-section-active">
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
        <div class="order-form">
            <div class="chat-box" ng-controller="chatController">
                <div class="chat-message">
                    <div class="message-wrapper" ng-repeat="x in message"
                         ng-class="x.sender_id == myId ? 'from-sender' : 'to-sender'">
                        <div class="message">{{x.message}}</div>
                    </div>
                </div>
                <div class="chat-input">
                    <input type="text" class="chat-input-message" ng-model="input" ng-keyup="sendMsgEnter($event)">
                    <button type="button" class="chat-input-button" ng-click="sendMsg()">Kirim</button>
                </div>
            </div>
            <div class="chat-close-button-container">
                <button type="button" class="chat-close-button" ng-click="closeChat">
                    Close
                </button>
            </div>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.6.2/firebase.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.6.2/firebase-messaging.js"></script>
<script>
    var app = angular.module("userChat", []);

    app.controller("chatController", function ($scope, $http, $rootScope, $window) {
        // Scope Declaration
        $rootScope.myId = <%=userData.getUserID()%>;
        $rootScope.targetId = <%=orderData.getDriverId()%>;
        $scope.tokenSet = false;
        $scope.message = [];
        $scope.input = "";

        $scope.closeChat = function () {
            $window.location.href = '/completeorder.jsp';
        };

        $scope.setupFirebase = function () {
            // Initialize Firebase and get token
            var config = {
                apiKey: "AIzaSyCSn3EiT3BZ-GXYq_uxvU-dFxx2C_rrW5I",
                authDomain: "gapropro-b0e07.firebaseapp.com",
                databaseURL: "https://gapropro-b0e07.firebaseio.com",
                projectId: "gapropro-b0e07",
                storageBucket: "gapropro-b0e07.appspot.com",
                messagingSenderId: "990522297808"
            };
            firebase.initializeApp(config);

            const messaging = firebase.messaging();
            messaging.requestPermission();
            messaging.getToken().then(function (currentToken) {
                if (currentToken) {
                    $http.post("http://localhost:3000/firebase/set_token", {
                        "userId": $rootScope.myId,
                        "firebaseToken": currentToken
                    });
                    $scope.tokenSet = true;
                } else {
                    console.log('No Instance ID token available. Request permission to generate one.');
                }
            });
            messaging.onMessage(function (payload) {
                console.log("Message received. ", payload);
                $scope.updateMsg();
            });
        };
        /**
         * Retrieve messages from db and display
         */
        $scope.updateMsg = function () {
            $http.post("http://localhost:3000/chat/get_chat_log", {
                "firstId": $rootScope.myId,
                "secondId": $rootScope.targetId
            }).then(function (response) {
                $scope.message = response.data.result;
            })
        };
        /**
         * Sends message to target
         */
        $scope.sendMsg = function () {
            $scope.input = $scope.input.trim();
            if ($scope.input.length !== 0) {
                var message = {
                    "sender_id": $rootScope.myId,
                    "receiver_id": $rootScope.targetId,
                    "message": $scope.input
                };
                $http.post("http://localhost:3000/chat/add_new_chat", {
                    "senderId": $rootScope.myId,
                    "receiverId": $rootScope.targetId,
                    "chatMessage": $scope.input
                }).then(function () {
                    $scope.updateMsg();
                });
            }
            $scope.message.push(message);
            $scope.input = "";
        };
        $scope.sendMsgEnter = function (event) {
            if (event.keyCode === 13) {
                $scope.sendMsg();
            }
        };

        /**
         * Choose driver
         */
        $scope.selectDriver = function () {
            $http.post("http://localhost:3000/availability/choose_driver", {
                "username": <%=userData.getUsername()%>,
                "senderId": $rootScope.myId,
                "receiverId": $rootScope.targetId
            });
        };

        // Scripts to run
        $scope.updateMsg();
        $scope.selectDriver();
        $scope.setupFirebase();

        $window.onfocus = function () {
            $scope.updateMsg();
        };

        //TODO : Change username to current username (use JSP)
        $http.post("http://localhost:3000/availability/choose_driver", {
            "username": "HoLAS_Tubis",
            "senderId": $rootScope.myId,
            "receiverId": $rootScope.targetId
        })
    });

</script>
</body>
</html>
