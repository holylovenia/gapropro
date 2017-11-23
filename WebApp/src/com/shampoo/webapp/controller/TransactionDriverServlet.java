package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.TransactionBean;
import com.shampoo.webapp.model.TransactionDriverBean;
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

@WebServlet(name = "TransactionDriverServlet")
public class TransactionDriverServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TransactionDriverBean transactionDriverBean = new TransactionDriverBean();
        ArrayList<TransactionBean> temp = new ArrayList<>();

        try {
            TransactionClient transactionClient = new TransactionClient();
            String result = transactionClient.getTransaction().getVisibleDriverTransactions(new CookieHandler().getAccessTokenCookie(request));
            switch (result) {
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
                    response.getOutputStream().println("alert(\"Failed to get transaction history!\");");
                    response.getOutputStream().println("window.location =\"driverhistory.jsp\"");
                    response.getOutputStream().println("</script>");
                    break;
                default:
                    JSONArray json = new JSONArray(result);
                    System.out.println("JSON " + json);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = (JSONObject) json.get(i);
                        Integer ID = jsonObject.getInt("id");
                        Integer userID = jsonObject.getInt("user_id");
                        String userName = jsonObject.getString("user_name");
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
                        String profilePicture = jsonObject.getString("user_profile_picture");
                        Integer driver_show = jsonObject.getInt("driver_show");

                        TransactionBean transBean = new TransactionBean();
                        transBean.setID(ID);
                        transBean.setUserID(userID);
                        transBean.setDriverID(null);
                        transBean.setUserName(userName);
                        transBean.setDriverName(null);
                        transBean.setPickingPoint(picking_point);
                        transBean.setDestination(destination);
                        transBean.setDate(date);
                        transBean.setComment(comment);
                        transBean.setRating(rating);
                        transBean.setProfilePicture(profilePicture);
                        transBean.setUserShow(null);
                        transBean.setDriverShow(driver_show);

                        temp.add(transBean);
                    }
                    transactionDriverBean.setTransactionDriverArray(temp);
                    request.getSession().setAttribute("transactionDriverData", transactionDriverBean);
                    response.sendRedirect("driverhistory.jsp");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
