package com.shampoo.service.identity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Login extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddr(request);
        JSONObject json = new JSONObject();
        try {
            //Connection Handling & Request
            DatabaseManager databaseManager = new DatabaseManager();
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Integer userID = databaseManager.login(username, password);
            if (userID != null) {
                ResultSet resultSet = databaseManager.fetchUserData(userID);
                String email = resultSet.getString("email");
                String access_token = new AccessToken().generateAccessToken(username, email, password) + "#" + userAgent + "#" + ipAddress;
                System.out.println("Access token: " + access_token);
                json.put("access_token", access_token);
                json.put("expiry_time", DatabaseManager.expiredDelay);
                databaseManager.updateAccessToken(userID, access_token);
                databaseManager.updateExpiredTime(access_token);
                sendPost(userID);
            } else {
                json.put("error", "user not found");
            }

            response.setContentType("application/json");
            response.getWriter().write(json.toString());

            databaseManager.closeConnection();
        } catch (IOException | SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void sendPost(Integer userId) {
        org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://localhost:3000/availability/set_online");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userId", userId.toString()));
        params.add(new BasicNameValuePair("onlineStatus", "1"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity respEntity = response.getEntity();

            if (respEntity != null) {
                String content = EntityUtils.toString(respEntity);
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientIpAddr(HttpServletRequest request) {
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
