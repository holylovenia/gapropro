package com.shampoo.service.identity;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Logout")
public class Logout extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = new JSONObject();
        String temp = request.getParameter("userID");
        Integer userID = Integer.parseInt(temp);
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(databaseManager.invalidateToken(userID) > 0) {
                jsonObject.put("status", "logout success");
            } else {
                jsonObject.put("status", "logout failed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonObject.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
