package com.shampoo.service.identity;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    databaseManager.register(username, email, name, password, phoneNumber, isDriver, defaultImagePath);
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
}
