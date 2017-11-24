<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session"/>

<% if (userData.getUserID() == null) response.sendRedirect("login.jsp");%>
<html>
<head>
    <title>Order</title>
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
            <% if (userData.getDriverStatus() == 1) {
                out.print("<a href=\"driverchat.jsp\" class=\"section-name\" id=\"order\">");
            } else {
                out.print("<a href=\"order.jsp\" class=\"section-name\" id=\"order\">");
            }
            %>
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
    <div class="order-body" ng-app="driverChat">
        <h1 class="order-header">
            LOOKING FOR AN ORDER
        </h1>
        <div class="order-status" ng-controller="orderController">
            <button type="button" class="find-order" ng-show="!finding_order" ng-click="findOrder()">
                Find Order
            </button>

            <div class="status" ng-show="finding_order">Finding Order</div>
            <div class="chat-username" ng-show="finding_order">{{username}}</div>
            <button type="button" class="cancel" ng-show="finding_order && !order_found" ng-click="cancelFindOrder()">
                Cancel
            </button>
        </div>
        <div class="order-form" ng-show="order_found">
            <div class="chat-box" ng-controller="chatController">
                <div class="chat-message">
                    <div class="message-wrapper" ng-repeat="x in message"
                         ng-class="x.sender_id == myId ? 'from-sender' : 'to-sender'">
                        <div class="message">{{x.message}}</div>
                    </div>
                </div>
                <div class="chat-input">
                    <input type="text" class="chat-input-message" ng-keyup="sendMsgEnter($event)" ng-model="input">
                    <button type="button" class="chat-input-button" ng-click="sendMsg()">Kirim</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.6.2/firebase.js"></script>
<script src="https://www.gstatic.com/firebasejs/4.6.2/firebase-messaging.js"></script>
<script>
    var app = angular.module("driverChat", []);
    /**
     * Order Controller
     * Mengatur setting pengambilan order
     */
    app.controller("orderController", function ($scope, $http, $rootScope) {
        $rootScope.finding_order = false;
        $rootScope.order_found = false;
        $rootScope.foundUsername = "";
        $scope.findOrder = function () {
            $rootScope.finding_order = true;
            $http.post("http://localhost:3000/availability/set_finding_order", {
                "userId": $rootScope.myId,
                "findingOrder": 1
            })
        };
        $scope.cancelFindOrder = function () {
            $rootScope.finding_order = false;
            $http.post("http://localhost:3000/availability/set_finding_order", {
                "userId": $rootScope.myId,
                "findingOrder": 0
            })
        }
    });

    /**
     * Chat Controller
     * Mengatur pengiriman dan penerimaan pesan, serta mengambil history
     */
    app.controller("chatController", function ($scope, $http, $rootScope, $window) {
        $rootScope.myId = <%= userData.getUserID() %>;
        $rootScope.targetId = 1;
        $scope.tokenSet = false;
        $scope.message = [];
        $scope.input = "";

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
                var data = JSON.parse(payload.data.notification);
                if (data.type === "connect" && $rootScope.finding_order) { //Redundant, should be protected by server side
                    $rootScope.finding_order = false;
                    $rootScope.targetId = data.target;
                    $rootScope.username = data.username;
                    $rootScope.order_found = true;
                    $http.post("http://localhost:3000/availability/set_finding_order", {
                        "userId": $rootScope.myId,
                        "findingOrder": 0
                    });
                    $scope.$apply();
                }
                else if (data.type === "close") {
                    $rootScope.finding_order = false;
                    $rootScope.order_found = false;
                    $scope.$apply();
                    console.log("Asd");
                }
                else {
                    $scope.updateMsg();
                }
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
            //$scope.message.push(message);
            $scope.input = "";
        };
        $scope.sendMsgEnter = function (event) {
            if (event.keyCode === 13) {
                $scope.sendMsg();
            }
        };

        // Scripts to run
        $scope.updateMsg();
        $scope.setupFirebase();

        $window.onfocus = function () {
            if ($rootScope.order_found) {
                $scope.updateMsg();
            }
        }
    });

</script>
</body>
</html>
