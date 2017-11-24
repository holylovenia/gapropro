package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.UserBean;
import com.shampoo.webapp.requester.PreferredLocationClient;
import com.shampoo.webapp.requester.UserManagementClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        final String userAgent = request.getHeader("User-Agent");
        final String ipAddress = getClientIpAddr(request);
        System.out.println("User-Agent " + request.getHeader("User-Agent"));
        System.out.println("IP Address " + getClientIpAddr(request));

        CookieHandler cookieHandler = new CookieHandler();
        if (!(cookieHandler.isTextValid(username) && cookieHandler.isTextValid(password))) {
            response.sendRedirect("login.jsp");
        } else {

            String access_token = null;
            try {
                access_token = sendPost(username, password, userAgent, ipAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject access_token_JSON;
            if (access_token != null) {
                access_token_JSON = new JSONObject(access_token);
                if (!access_token_JSON.has("error")) {
                    String token = access_token_JSON.getString("access_token");
                    String token_encoded = Base64.getEncoder().encodeToString(token.getBytes());
                    Cookie access_token_cookie = new Cookie("access_token", token_encoded);
                    Cookie expiry_time_cookie = new Cookie("expiry_time", Integer.toString(access_token_JSON.getInt("expiry_time")));

                    response.addCookie(access_token_cookie);
                    response.addCookie(expiry_time_cookie);

                    //Generate New Session
                    request.getSession();

                    UserBean userBean = new UserBean();
                    try {
                        UserManagementClient userManagementClient = new UserManagementClient();
                        String result = userManagementClient.getUserManagement().getCurrentUserData(token);
                        switch (result) {
                            case "invalid":
                            case "expired":
                                response.getOutputStream().println("<script type=\"text/javascript\">");
                                response.getOutputStream().println("alert(\"Your token is expired or invalid!\");");
                                response.getOutputStream().println("window.location =\"login.jsp\"");
                                response.getOutputStream().println("</script>");
                                break;
                            case "Error":
                                response.getOutputStream().println("<script type=\"text/javascript\">");
                                response.getOutputStream().println("alert(\"Failed to login!\");");
                                response.getOutputStream().println("window.location =\"login.jsp\"");
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
                                JSONArray jsonArray = new JSONArray(result);
                                JSONObject json = jsonArray.getJSONObject(0);
                                // If token is expired
                                if (json.has("error")) {
                                    response.sendRedirect("login.jsp");
                                } else {

                                    int idJSON = json.getInt("id");
                                    String nameJSON = json.getString("name");
                                    String usernameJSON = json.getString("username");
                                    String emailJSON = json.getString("email");
                                    String phoneNumberJSON = json.getString("phone_no");
                                    String profpicJSON = json.getString("profile_picture");
                                    int asdriverJSON = json.getInt("is_driver");

                                    userBean.setUserID(idJSON);
                                    userBean.setName(nameJSON);
                                    userBean.setUsername(usernameJSON);
                                    userBean.setEmail(emailJSON);
                                    userBean.setPhoneNumber(phoneNumberJSON);
                                    userBean.setProfilePicture(profpicJSON);
                                    userBean.setDriverStatus(asdriverJSON);

                                    PreferredLocationClient preferredLocationClient = new PreferredLocationClient();
                                    String preferredLocationResult = preferredLocationClient.getPreferredLocation().getUserPreferredLocations(token);
                                    switch (preferredLocationResult) {
                                        case "invalid":
                                        case "expired":
                                            response.getOutputStream().println("<script type=\"text/javascript\">");
                                            response.getOutputStream().println("alert(\"Your token is invalid or expired!\");");
                                            response.getOutputStream().println("window.location =\"login.jsp\"");
                                            response.getOutputStream().println("</script>");
                                            break;
                                        case "Error":
                                            response.getOutputStream().println("<script type=\"text/javascript\">");
                                            response.getOutputStream().println("alert(\"Failed to login!\");");
                                            response.getOutputStream().println("window.location =\"login.jsp\"");
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
                                            JSONArray preferredLocationsJSON = new JSONArray(preferredLocationResult);
                                            ArrayList<String> preferredLocations = new ArrayList<>();
                                            for (int i = 0; i < preferredLocationsJSON.length(); i++) {
                                                JSONObject jsonObject = (JSONObject) preferredLocationsJSON.get(i);
                                                preferredLocations.add(jsonObject.getString("location"));
                                            }
                                            userBean.setPreferredLocation(preferredLocations);
                                            int votesJSON = json.getInt("votes");
                                            float ratingJSON = json.getFloat("rating");
                                            userBean.setRating(ratingJSON);
                                            userBean.setVotes(votesJSON);

                                            request.getSession().setAttribute("userData", userBean);

                                            if (asdriverJSON == 1) {
                                                response.sendRedirect("profile.jsp");
                                            } else {
                                                response.sendRedirect("order.jsp");
                                            }
                                            break;
                                    }
                                }
                                break;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    response.sendRedirect("login.jsp");
                }
            }
        }
    }

    private String sendPost(String _username, String _password, String userAgent, String ipAddress) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://localhost:9001/login");
        httpPost.addHeader("User-Agent", userAgent);
        httpPost.addHeader("X-Forwarded-For", ipAddress);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", _username));
        params.add(new BasicNameValuePair("password", _password));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity respEntity = response.getEntity();

            if (respEntity != null) {
                return EntityUtils.toString(respEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
