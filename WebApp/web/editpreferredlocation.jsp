<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session"/>
<% if (userData.getUserID() == null) {
    response.sendRedirect("login.jsp");
} else {%>
<html>
<head>
    <title>Edit Preferred Location</title>
    <link rel="stylesheet" type="text/css" href="styles/style.css">
    <script src="../js/handler.js"></script>
</head>
<body>
<div class="edit-profile">
    <div class="edit-profile-table">
        <div class="edit-profile-table-title">
            <b>EDIT PREFERRED LOCATIONS</b>
        </div>
        <div class="edit-profile-table-content">
            <table class="edit-profile-table">
                <tr>
                    <th class="edit-profile-table-num edit-profile-table-row">No</th>
                    <th class="edit-profile-table-loc edit-profile-table-row">Location</th>
                    <th class="edit-profile-table-act edit-profile-table-row">Actions</th>
                </tr>
                <%
                    int count = 1;
                    ArrayList<String> locations = userData.getPreferredLocation();
                    if (locations != null) {
                        for (int i = 0; i < locations.size(); i++) {
                            String location = locations.get(i);
                %>
                <tr>
                    <td class='edit-profile-table-row'><b><% out.print(count); %></b></td>
                    <td class='edit-profile-table-row' id="edit-profile-row-<% out.print(count); %>"><b><%
                        out.print(location); %></b></td>
                    <td class='edit-profile-table-row'>

                        <form action='/editlocation' method='post'>
                            <img src='img/pencil.svg' class='edit-profile-icon-edit'
                                 id="edit-profile-icon-edit-<% out.print(count); %>"
                                 onclick=updateLocation(<% out.print(count); %>)>
                            <input type='hidden' name='location1' id="location1-<% out.print(count); %>"
                                   value='<% out.print(location); %>'>
                            <input type='hidden' name='location2' id="location2-<% out.print(count); %>" value=''>
                            <button type='submit' class='edit-location-button'
                                    id='edit-location-button-update-<% out.print(count); %>'></button>
                        </form>

                        <form action='/deletelocation' method='post'>
                            <img src='img/cross.svg' class='edit-profile-icon-delete'
                                 onclick=removeLocation(<% out.print(count); %>)>
                            <input type='hidden' name='location' value='<% out.print(location); %>'>
                            <button type='submit' class='edit-location-button'
                                    id='edit-location-button-delete-<% out.print(count); %>'></button>
                        </form>
                    </td>
                </tr>
                <%
                            count++;
                        }
                    }
                %>
            </table>
        </div>
    </div>
    <div class="edit-profile-edit-form">
        <div class="edit-profile-edit-form-title">
            <b>ADD NEW LOCATION:</b>
        </div>
        <div>
            <form action='/addlocation' method='POST'
                  onsubmit='return validateEmpty("edit-profile-preferedlocation-input","dummy","edit-profile-submit")'>
                <input type='text' name='location' class='edit-profile-input' id='edit-profile-preferedlocation-input'>
                <input type='submit' value='ADD' class='edit-profile-submit' id='edit-profile-submit'>
            </form>
        </div>
        <a href='profile.jsp' class="edit-profile-back-link">
            <div class="edit-profile-back">
                Back
            </div>
        </a>
    </div>
</div>
</body>
</html>
<% } %>