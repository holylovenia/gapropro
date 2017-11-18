package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.UserBean;
import com.shampoo.webapp.requester.PreferredLocationClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "DeleteLocationServlet")
public class DeleteLocationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String location = request.getParameter("location");

        if (location != null) {
            PreferredLocationClient preferredLocationClient = null;
            try {
                preferredLocationClient = new PreferredLocationClient();
                String result = preferredLocationClient.getPreferredLocation().removePreferredLocation(new CookieHandler().getAccessTokenCookie(request), location);
                if (result.equals("Successful")) {
                    UserBean userData = (UserBean) request.getSession().getAttribute("userData");
                    ArrayList<String> preferredLocation = new ArrayList<String>();
                    preferredLocation = userData.getPreferredLocation();
                    for (int i = 0; i < preferredLocation.size(); i++) {
                        if (preferredLocation.get(i).equals(location)) {
                            preferredLocation.remove(i);
                            break;
                        }
                    }
                    userData.setPreferredLocation(preferredLocation);
                    response.sendRedirect("editpreferredlocation.jsp");
                } else if (result.equals("invalid") || result.equals("expired")) {
                    response.getOutputStream().println("<script type=\"text/javascript\">");
                    response.getOutputStream().println("alert(\"Your token is invalid or expired!\");");
                    response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                    response.getOutputStream().println("</script>");
                } else {
                    response.getOutputStream().println("<script type=\"text/javascript\">");
                    response.getOutputStream().println("alert(\"Failed to delete preferred location!\");");
                    response.getOutputStream().println("window.location =\"editpreferredlocation.jsp\"");
                    response.getOutputStream().println("</script>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
