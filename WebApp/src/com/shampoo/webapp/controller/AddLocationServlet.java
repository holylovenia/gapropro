package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.UserBean;
import com.shampoo.webapp.requester.PreferredLocation;
import com.shampoo.webapp.requester.PreferredLocationClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "AddLocationServlet")
public class AddLocationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newLocation = request.getParameter("location");
        if (newLocation != null) {
            try {
                PreferredLocationClient preferredLocationClient = new PreferredLocationClient();
                String result = preferredLocationClient.getPreferredLocation().addPreferredLocation(new CookieHandler().getAccessTokenCookie(request), newLocation);
                if (result.equals("Successful")) {
                    UserBean userData = (UserBean) request.getSession().getAttribute("userData");
                    ArrayList<String> preferredLocation = userData.getPreferredLocation();
                    preferredLocation.add(newLocation);
                    userData.setPreferredLocation(preferredLocation);
                    response.sendRedirect("editpreferredlocation.jsp");
                } else if (result.equals("invalid") || result.equals("expired")) {
                    response.getOutputStream().println("<script type=\"text/javascript\">");
                    response.getOutputStream().println("alert(\"Your token is invalid or expired!\");");
                    response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                    response.getOutputStream().println("</script>");
                } else {
                    response.getOutputStream().println("<script type=\"text/javascript\">");
                    response.getOutputStream().println("alert(\"Failed to add location!\");");
                    response.getOutputStream().println("window.location =\"editpreferredlocation.jsp\"");
                    response.getOutputStream().println("</script>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("editpreferredlocation.jsp");
        }
    }
}
