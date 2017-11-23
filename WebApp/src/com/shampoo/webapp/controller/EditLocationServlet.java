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

@WebServlet(name = "EditLocationServlet")
public class EditLocationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newLocation = request.getParameter("location2");
        String oldLocation = request.getParameter("location1");

        if (newLocation != null) {
            PreferredLocationClient preferredLocationClient = null;
            try {
                preferredLocationClient = new PreferredLocationClient();
                String result = preferredLocationClient.getPreferredLocation().editPreferredLocation(new CookieHandler().getAccessTokenCookie(request), oldLocation, newLocation);
                switch (result) {
                    case "Successful":
                        UserBean userData = (UserBean) request.getSession().getAttribute("userData");
                        ArrayList<String> preferredLocation = userData.getPreferredLocation();
                        for (int i = 0; i < preferredLocation.size(); i++) {
                            if (preferredLocation.get(i).equals(oldLocation)) {
                                preferredLocation.set(i, newLocation);
                                break;
                            }
                        }
                        userData.setPreferredLocation(preferredLocation);
                        response.sendRedirect("editpreferredlocation.jsp");
                        break;
                    case "invalid":
                    case "expired":
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Your token is invalid or expired!\");");
                        response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                        response.getOutputStream().println("</script>");
                        break;
                    case "invalid_ip":
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Invalid ip address detected!\");");
                        response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                        response.getOutputStream().println("</script>");
                        break;
                    case "invalid_agent":
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Invalid user agent detected!\");");
                        response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                        response.getOutputStream().println("</script>");
                        break;
                    case "invalid_malformed":
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Malformed token detected!\");");
                        response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                        response.getOutputStream().println("</script>");
                        break;
                    default:
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Failed to edit preferred location!\");");
                        response.getOutputStream().println("window.location =\"editpreferredlocation.jsp\"");
                        response.getOutputStream().println("</script>");
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
