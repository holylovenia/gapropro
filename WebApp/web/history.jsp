<%@ page import="com.shampoo.webapp.controller.TransactionUserServlet" %>
<%@ page import="com.shampoo.webapp.model.TransactionBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %><%--
  Created by IntelliJ IDEA.
  User: verenaseverina
  Date: 11/2/17
  Time: 5:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session" />
<jsp:useBean id="transactionUserData" class="com.shampoo.webapp.model.TransactionUserBean" scope="session" />
<% if (userData.getUserID() == null) { response.sendRedirect("login.jsp"); } else {%>
<html>
<head>
    <title>History</title>
    <link rel="stylesheet" type="text/css" href="styles/history.css">
</head>
<body>
<div class="container">
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
    <div class="history-body">
        <h1 class="history-header">TRANSACTION HISTORY</h1>
        <div class="history-nav-bar">
            <div class="history-nav-section history-nav-section-active">
                <a href="history.jsp">MY PREVIOUS ORDERS</a>
            </div>
            <div class="history-nav-section">
                <a href="/transactiondriver">DRIVER HISTORY</a>
            </div>
        </div>
        <%
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEEEE, d MMMMM yyyy");
            if (transactionUserData.getTransactionUser_Array() != null) {
            for (int i = 0; i < transactionUserData.getTransactionUser_Array().size(); i++) {
                TransactionBean currentTransaction = transactionUserData.getTransactionUser_Array().get(i);
        %>
            <div class='history-container'>
                <img src="<% out.print("images/" + currentTransaction.getProfilePicture()); %>" class='history-image' />
                <div class='history-detail-container'>
                    <form action='/hide' method='post'>
                        <input type='hidden' name='historyId' value="<% out.print(currentTransaction.getID()); %>" />
                        <input type='submit' class='hide-button' name='submithistory' value='HIDE' />
                    </form>
                    <div class='date'> <% try {
                        out.print(outputDateFormat.format(inputDateFormat.parse(currentTransaction.getDate())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } %> </div>
                    <h3 class='history-name'> <% out.print(currentTransaction.getDriverName()); %> </h3>
                    <div class='history-location'> <% out.print(currentTransaction.getPickingPoint()); %> &#8594; <% out.print(currentTransaction.getDestination()); %> </div>
                    <div class='history-rate'>
                        You rated:
                        <div class='history-rate-star'>
                            <% int rate = currentTransaction.getRating();
                                for (int j= 0; j < rate; j++) {%>
                                &#9734;
                            <% } %>
                        </div>
                    </div>
                    <div class='history-comment-header'> You Commented: </div>
                    <div class='history-comment-body'>
                        <% out.print(currentTransaction.getComment()); %>
                    </div>
                </div>
            </div>
            <% }
            }
        %>
    </div>
</div>
</body>
</html>
<% } %>