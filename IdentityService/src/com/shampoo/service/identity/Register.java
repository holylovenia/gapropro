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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmpassword");
        String phoneNumber = request.getParameter("phonenumber");
        String asDriver = request.getParameter("asdriver");
        int isDriver = Integer.parseInt(asDriver);
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddr(request);

        if (confirmPassword.compareTo(password) == 0) {
            DatabaseManager databaseManager = null;
            try {
                databaseManager = new DatabaseManager();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String defaultImagePath = "default.png";
            boolean dataValid = false;
            try {
                if (databaseManager != null) {
                    dataValid = databaseManager.validateRegister(username, email);
                }
                System.out.println(Boolean.toString(dataValid));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (dataValid) {
                try {
                    databaseManager.register(username, email, name, password, phoneNumber, isDriver, defaultImagePath, userAgent, ipAddress);
                    int userId = databaseManager.fetchUserId(username);
                    System.out.println("MASUK");
                    sendPost(userId);
                    ResultSet encryptedData = databaseManager.fetchToken(username);
                    String access_token = encryptedData.getString("access_token");
                    jsonObject.put("access_token", access_token);
                    jsonObject.put("expiry_time", DatabaseManager.expiredDelay);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                jsonObject.put("error", "username/email already taken");
            }
        } else {
            jsonObject.put("error", "wrong confirm password");
        }
        response.setContentType("application/json");
        response.getWriter().write(jsonObject.toString());
    }

    private void sendPost(Integer userId) {
        org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://localhost:3000/availability/add_new_user");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userId", userId.toString()));

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
