package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.DriverBean;
import com.shampoo.webapp.model.OrderBean;
import com.shampoo.webapp.requester.OrderClient;
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

@WebServlet(name = "OrderServlet")
public class OrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pickingPoint = request.getParameter("pickingPoint");
        String destination = request.getParameter("destination");
        String preferredDriverName = request.getParameter("preferredDriverName");

        CookieHandler cookieHandler = new CookieHandler();
        if (!(cookieHandler.isTextValid(pickingPoint) && cookieHandler.isTextValid(destination) && cookieHandler.isTextValid(preferredDriverName))) {
            response.sendRedirect("order.jsp");
        } else {
            OrderBean orderBean = new OrderBean();
            orderBean.setPickingPoint(pickingPoint);
            orderBean.setDestination(destination);
            orderBean.setPreferredDriverName(preferredDriverName);

            try {
                OrderClient orderClient = new OrderClient();
                String driversRawJson = orderClient.getOrder().getDrivers(cookieHandler.getAccessTokenCookie(request), preferredDriverName, pickingPoint, destination);
                switch (driversRawJson) {
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
                    case "Error":
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Failed to order!\");");
                        response.getOutputStream().println("window.location =\"order.jsp\"");
                        response.getOutputStream().println("</script>");
                        break;
                    default:
                        JSONArray driversJsonArray = new JSONArray(driversRawJson);
                        ArrayList<DriverBean> preferredDrivers = new ArrayList<>();
                        ArrayList<DriverBean> otherDrivers = new ArrayList<>();
                        JSONObject driverJsonObject;
                        for (int i = 0; i < driversJsonArray.length(); i++) {
                            DriverBean driverBean = new DriverBean();
                            driverJsonObject = driversJsonArray.getJSONObject(i);
                            driverBean.setDriverId(driverJsonObject.getInt("id"));
                            driverBean.setUsername(driverJsonObject.getString("username"));
                            driverBean.setName(driverJsonObject.getString("name"));
                            driverBean.setProfilePicture(driverJsonObject.getString("profile_picture"));
                            driverBean.setVotes(driverJsonObject.getInt("votes"));
                            driverBean.setRating(driverJsonObject.getFloat("rating"));
                            if (driverJsonObject.getInt("is_preferred") == 1) {
                                preferredDrivers.add(driverBean);
                            } else {
                                otherDrivers.add(driverBean);
                            }
                        }
                        orderBean.setPreferredDrivers(preferredDrivers);
                        orderBean.setOtherDrivers(otherDrivers);
                        request.getSession().setAttribute("orderData", orderBean);
                        response.sendRedirect("selectdriver.jsp");
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
