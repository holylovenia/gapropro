package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.DriverBean;
import com.shampoo.webapp.model.OrderBean;
import com.shampoo.webapp.requester.OrderClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
                String token_encoded = cookieHandler.getAccessTokenCookie(request);
                byte[] token_byte = Base64.getDecoder().decode(token_encoded);
                String token = new String(token_byte);
                String driversRawJson = orderClient.getOrder().getDrivers(token, preferredDriverName, pickingPoint, destination);
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
                        JSONArray onlineUsersJsonArray = new JSONObject(sendGet()).getJSONArray("result");
                        ArrayList<DriverBean> preferredDrivers = new ArrayList<>();
                        ArrayList<DriverBean> otherDrivers = new ArrayList<>();
                        JSONObject driverJsonObject;
                        JSONObject onlineUserJsonObject;
                        for (int i = 0; i < driversJsonArray.length(); i++) {
                            for(int j = 0; j < onlineUsersJsonArray.length(); j++) {
                                driverJsonObject = driversJsonArray.getJSONObject(i);
                                onlineUserJsonObject = onlineUsersJsonArray.getJSONObject(j);
                                if(driverJsonObject.getInt("id") == onlineUserJsonObject.getInt("user_id")) {
                                    DriverBean driverBean = new DriverBean();
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

    private String sendGet() throws IOException {
        org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://localhost:3000/availability/get_available_users");
        ResponseHandler<String> handler = new BasicResponseHandler();
        HttpResponse response = httpClient.execute(httpGet);
        return handler.handleResponse(response);
    }
}
