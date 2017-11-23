package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.TransactionBean;
import com.shampoo.webapp.model.TransactionUserBean;
import com.shampoo.webapp.requester.TransactionClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "TransactionUserServlet")
public class TransactionUserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TransactionUserBean transactionUserBean = new TransactionUserBean();
        ArrayList<TransactionBean> temp = new ArrayList<>();

        try {
            TransactionClient transactionClient = new TransactionClient();
            String result = transactionClient.getTransaction().getVisibleUserTransactions(new CookieHandler().getAccessTokenCookie(request));
            System.out.println("TransactionUser " + result);
            if (result.equals("invalid") || result.equals("expired")) {
                response.getOutputStream().println("<script type=\"text/javascript\">");
                response.getOutputStream().println("alert(\"Your token is invalid or expired!\");");
                response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                response.getOutputStream().println("</script>");
            } else if (result.equals("invalid_ip")) {
                response.getOutputStream().println("<script type=\"text/javascript\">");
                response.getOutputStream().println("alert(\"Invalid ip address detected!\");");
                response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                response.getOutputStream().println("</script>");
            } else if (result.equals("invalid_agent")) {
                response.getOutputStream().println("<script type=\"text/javascript\">");
                response.getOutputStream().println("alert(\"Invalid user agent detected!\");");
                response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                response.getOutputStream().println("</script>");
            } else if (result.equals("invalid_malformed")) {
                response.getOutputStream().println("<script type=\"text/javascript\">");
                response.getOutputStream().println("alert(\"Malformed token detected!\");");
                response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                response.getOutputStream().println("</script>");
            } else if (result.equals("Error")) {
                response.getOutputStream().println("<script type=\"text/javascript\">");
                response.getOutputStream().println("alert(\"Failed to transaction history!\");");
                response.getOutputStream().println("window.location =\"history.jsp\"");
                response.getOutputStream().println("</script>");
            } else {
                JSONArray json = new JSONArray(result);
                JSONObject jsonObject;
                for (int i = 0; i < json.length(); i++) {
                    jsonObject = (JSONObject) json.get(i);
                    Integer ID = jsonObject.getInt("id");
                    Integer driverID = jsonObject.getInt("driver_id");
                    String driverName = jsonObject.getString("driver_name");
                    String picking_point = jsonObject.getString("picking_point");
                    String destination = jsonObject.getString("destination");
                    String date = jsonObject.getString("date");
                    String comment = jsonObject.getString("comment");
                    Integer rating;
                    if (jsonObject.get("rating") == null) {
                        rating = 0;
                    } else {
                        rating = jsonObject.getInt("rating");
                    }
                    String profilePicture = jsonObject.getString("driver_profile_picture");
                    Integer user_show = jsonObject.getInt("user_show");

                    TransactionBean transBean = new TransactionBean();
                    transBean.setID(ID);
                    transBean.setUserID(null);
                    transBean.setDriverID(driverID);
                    transBean.setUserName(null);
                    transBean.setDriverName(driverName);
                    transBean.setPickingPoint(picking_point);
                    transBean.setDestination(destination);
                    transBean.setDate(date);
                    transBean.setComment(comment);
                    transBean.setRating(rating);
                    transBean.setProfilePicture(profilePicture);
                    transBean.setUserShow(user_show);
                    transBean.setDriverShow(null);
                    temp.add(transBean);
                }
                transactionUserBean.setTransactionUserArray(temp);
                request.getSession().setAttribute("transactionUserData", transactionUserBean);
                response.sendRedirect("history.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
