package com.shampoo.service.identity;

import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
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
                String access_token = new AccessToken().generateAccessToken(username, email, password);
                json.put("access_token", access_token);
                json.put("expiry_time", DatabaseManager.expiredDelay);
                databaseManager.updateAccessToken(userID, access_token);
                databaseManager.updateExpiredTime(access_token);
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
}
