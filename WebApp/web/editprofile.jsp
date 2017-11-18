<%--
  Created by IntelliJ IDEA.
  User: verenaseverina
  Date: 11/2/17
  Time: 6:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session" />
<% if (userData.getUserID() == null) { response.sendRedirect("login.jsp"); } else {%>
<html>
<head>
    <title>Edit Profile</title>
    <link rel="stylesheet" type="text/css" href="styles/style.css">
    <script src="../js/handler.js"></script>
</head>
<body>
<div class="editprofile">
    <div class="editprofile-title">
        EDIT PROFILE INFORMATION
    </div>
    <div class="editprofile-container">
        <div class="editprofile-left">
            <div class="editprofile-image-container">
                <img src="images/<% out.print(userData.getProfilePicture()); %>" class="editprofile-image">
            </div>
            <div class="editprofile-form-input-name">
                <div>
                    Your Name
                </div>
                <div>
                    Phone
                </div>
                <div>
                    Status Driver
                </div>
            </div>
            <div class="editprofile-back-container">
                <a href='profile.jsp' class="editprofile-back"> Back </a>
            </div>
        </div>
        <div class="editprofile-right">
            <form action='/editprofile' method='post' enctype='multipart/form-data' onsubmit='return validateEmpty("editprofile-name","editprofile-phone","editprofile-submit")'>
                <div class="editprofile-form-top">
                    <div class="editprofile-form-top-title">
                        Update profile picture
                    </div>
                    <div>
                        <input type='file' name='newImage' id='editprofile-new-image' onchange='changePath()'>
                        <label for="editprofile-new-image" class="editprofile-image-label">
                            <div class="editprofile-image-path" id="editprofile-image-path"></div>
                            <div class="editprofile-image-button">Browse ...</div>
                        </label>
                    </div>
                </div>
                <div class="editprofile-form-bot">
                    <input type='text' name='name' value='<% out.print(userData.getName());%>' class="editprofile-name" id="editprofile-name">
                    <input type='text' name='phonenumber' value='<% out.print(userData.getPhoneNumber());%>' class="editprofile-phone" id="editprofile-phone">
                    <label class="editprofile-switch">
                        <%
                            System.out.println("DRIVER STATUS " + userData.getDriverStatus());
                            if (userData.getDriverStatus() == 1) {
                                System.out.println("ADA");
                                out.print("<input type=\"checkbox\" name=\"driverstatus\" checked>");
                                System.out.println("DRIVERRRRRR");
                            } else {
                                System.out.println("TIDAK ADA");
                                out.print("<input type=\"checkbox\" name=\"driverstatus\">");
                                System.out.println("USERRRRR");
                            }

                        %>
                        <span class="editprofile-slider editprofile-round"></span>
                    </label>
                    <input type='submit' name='submit' value='save' class='editprofile-submit' id='editprofile-submit'>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<% } %>