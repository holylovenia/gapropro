<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session" />
<% if (userData.getUserID() == null) { response.sendRedirect("login.jsp"); } else {%>
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
            <div class="order-section order-section-1 order-section-active">
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
            <form action="/order" method="post" onsubmit='return validateEmpty("order-answer-pickingpoint","order-answer-destination","order-next-button")'>
                <table class="order-input-type">
                    <tr>
                        <td class="order-asking"> Picking point </td>
                        <td> <input type="text" class="order-answer" id="order-answer-pickingpoint" name="pickingPoint" /> </td>
                    </tr>
                    <tr>
                        <td class="order-asking"> Destination </td>
                        <td> <input type="text" class="order-answer" id="order-answer-destination" name="destination" /> </td>
                    </tr>
                    <tr>
                        <td class="order-asking"> Preferred Driver </td>
                        <td> <input type="text" class="order-answer" name="preferredDriverName" placeholder="(optional)" /> </td>
                    </tr>
                </table>
                <div class="order-button-container">
                    <input type="submit" class="next-button" id="order-next-button" name="submit" value="next" />
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<% } %>