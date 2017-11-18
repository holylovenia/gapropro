<%@ page import="com.shampoo.webapp.requester.Transaction" %>
<%@ page import="com.shampoo.webapp.model.TransactionBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %><%--
  Created by IntelliJ IDEA.
  User: verenaseverina
  Date: 11/2/17
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session" />
<jsp:useBean id="transactionDriverData" class="com.shampoo.webapp.model.TransactionDriverBean" scope="session" />
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
            <div class="history-nav-section ">
                <a href="/transactionuser">MY PREVIOUS ORDERS</a>
            </div>
            <div class="history-nav-section history-nav-section-active">
                <a href="driverhistory.jsp">DRIVER HISTORY</a>
            </div>
        </div>
        <%
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEEEE, d MMMMM yyyy");
            if (transactionDriverData.getTransactionDriver_Array() != null) {
                System.out.println("SIZE " + transactionDriverData.getTransactionDriver_Array().size());
                for (int i = 0; i < transactionDriverData.getTransactionDriver_Array().size(); i++) {
                    System.out.println("TR" + i);
                    TransactionBean currentTransaction = transactionDriverData.getTransactionDriver_Array().get(i);
        %>
            <div class='history-container'>
                <img src="images/<% out.print(currentTransaction.getProfilePicture()); %>" class='history-image' />
                <div class='driver-history-detail-container'>
                    <form action='/hide' method='post'>
                        <input type='hidden' name='historyId' value="<% out.print(currentTransaction.getID()); %>" />
                        <input type='hidden' name='driverhistory' />
                        <input type='submit' class='hide-button' name='submitdriverhistory' value='HIDE' />
                    </form>
                    <div class='date'> <% try {
                        out.print(outputDateFormat.format(inputDateFormat.parse(currentTransaction.getDate())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } %> </div>
                    <h3 class='history-name'> <% out.print(currentTransaction.getUserName()); %> </h3>
                    <div class='history-location'> <% out.print(currentTransaction.getPickingPoint()); %> &#8594; <% out.print(currentTransaction.getDestination()); %> </div>
                    <div class='driver-history-rate'>
                        gave
                        <div class='driver-history-rate-star'>
                            <% out.print(currentTransaction.getRating()); %>
                        </div>
                        stars for this order
                    </div>
                    <div class='driver-history-comment-header'> and left comment: </div>
                    <div class='driver-history-comment-body'>
                        <% out.print(currentTransaction.getComment()); %>
                    </div>
                </div>
            </div>
        <%
                }
            }
        %>
    </div>
</div>
</body>
</html>
<% } %>