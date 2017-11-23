<%@ page import="static java.awt.GraphicsConfigTemplate.PREFERRED" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.NumberFormat" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session" />
<% if (userData.getUserID() == null) { response.sendRedirect("login.jsp"); } else {%>
<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" type="text/css" href="styles/style.css">
</head>
<body>
<div class="profile">
    <div class="profile-nav-top">
        <div class="profile-logo">
            <p class="profile-projek">
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
                <a href="handleLogout.jsp" class="profile-logout">Logout</a>
            </p>
        </div>
    </div>
    <div class="profile-nav-bot">
        <div class="profile-section" id="profile-section-1">
            <% if (userData.getDriverStatus()==1) {
                out.print("<a href=\"driverchat.jsp\")");
               } else {
                out.print("<a href=\"order.jsp\")");
               }
            %>
            class="profile-section-name" id="profile-name-1">
                ORDER
            </a>
        </div>
        <div class="profile-section" id="profile-section-2">
            <a href="/transactionuser" class="profile-section-name" id="profile-name-2">
                HISTORY
            </a>
        </div>
        <div class="profile-section" id="profile-section-3">
            <a href="profile.jsp" class="profile-section-name" id="profile-name-3">
                MY PROFILE
            </a>
        </div>
    </div>
    <div class="menu-body">
        <div class="profile-body">
            <div class="profile-main">
                <div class="profile-main-top">
                    <span class="profile-body-title">MY PROFILE</span>
                    <a href='editprofile.jsp' class="profile-edit">
                        <img src="img/pencil.svg" class="profile-pencil">
                    </a>
                </div>
                <div class="profile-main-mid">
                    <img src="images/<% out.print(userData.getProfilePicture()); %>" class="profile-photo">
                    <p class="profile-user-id"> <% out.print("@" + userData.getUsername()); %></p>
                    <p class="profile-identity">
                        <% out.print(userData.getName()); %> <br>
                        <% if (userData.getDriverStatus() == 1) {
                            out.print("DRIVER |");
                            NumberFormat formatter = new DecimalFormat("#0.00");
                        %>
                        <span class="profile-rating"> <img src="img/star.svg" class="profile-star"> <% out.print(formatter.format(userData.getRating())); %></span> (<% out.print(userData.getVotes()); %> votes)<br>
                        <% } else {
                            out.print("NON-DRIVER");
                        }
                        %>
                        <img src="img/mail.svg" class="mail"> <% out.print(userData.getEmail()); %><br>
                        <img src="img/telephone.svg" class="telephone"> <% out.print(userData.getPhoneNumber()); %>
                    </p>
                </div>

                <% if (userData.getDriverStatus() != 0) {
                    out.print("<div class=\"profile-main-bot\">\n");
                    out.print("<div class=\"profile-bot\">\n");
                    out.print("<span class=\"profile-body-title-bot\">PREFERRED LOCATIONS:</span>\n");
                    out.print("<a href=\"editpreferredlocation.jsp\" class=\"profile-edit\">\n");
                    out.print("<img src=\"img/pencil.svg\" class=\"profile-pencil\" >\n");
                    out.print("</a>\n");
                    out.print("</div>\n");
                    out.print("<div class=\"profile-location\">\n");
                    out.print("<ul class=\"profile-preferred-location\">\n");
                    int margin = 0;
                    if (userData.getPreferredLocation() != null) {
                        for (int i = 0; i < userData.getPreferredLocation().size(); i++) {
                            out.print("<li style='margin-left:" + Integer.toString(margin) + "px; margin-bottom:10px;'>" + userData.getPreferredLocation().get(i) + "</li>\n");
                            margin = margin + 50;
                        }
                    }

                    out.print("</ul>\n");
                    out.print("</div>\n");
                    out.print("</div>\n");
                }
                %>

            </div>
        </div>
    </div>
</div>
</body>
</html>
<% } %>