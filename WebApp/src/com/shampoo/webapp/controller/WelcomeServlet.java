package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.UserBean;
import com.shampoo.webapp.requester.PreferredLocationClient;
import com.shampoo.webapp.requester.UserManagementClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

@WebServlet(name = "WelcomeServlet")
public class WelcomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token_encoded = new CookieHandler().getAccessTokenCookie(request);
        if (token_encoded != null) {
            byte[] token_byte = Base64.getDecoder().decode(token_encoded);
            String access_token = new String(token_byte);
            UserBean userBean = new UserBean();
            try {
                UserManagementClient userManagementClient = new UserManagementClient();
                String token = new String(token_byte);
                String currentUserData = userManagementClient.getUserManagement().getCurrentUserData(token);
                if (currentUserData.equals("expired") || currentUserData.equals("invalid") || currentUserData.equals("Error") ||
                        currentUserData.equals("invalid_ip") || currentUserData.equals("invalid_agent") || currentUserData.equals("invalid_malformed")) {
                    response.sendRedirect("login.jsp");
                } else {
                    JSONArray jsonArray = new JSONArray(currentUserData);
                    JSONObject json = jsonArray.getJSONObject(0);
                    if (json != null) {
                        //IF Token Expired
                        if (json.has("error")) {
                            response.sendRedirect("login.jsp");
                        }

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
                        String preferredLocationResult = preferredLocationClient.getPreferredLocation().getUserPreferredLocations(access_token);
                        if(preferredLocationResult.equals("expired") || preferredLocationResult.equals("invalid") || preferredLocationResult.equals("Error") ||
                                preferredLocationClient.equals("invalid_ip") || preferredLocationClient.equals("invalid_agent") || preferredLocationClient.equals("invalid_malformed")) {
                            response.sendRedirect("login.jsp");
                        } else {
                            JSONArray preferredLocationsJSON = new JSONArray(preferredLocationResult);
                            ArrayList<String> preferredLocations = new ArrayList<String>();
                            for (int i = 0; i < preferredLocationsJSON.length(); i++) {
                                JSONObject jsonObject = (JSONObject) preferredLocationsJSON.get(i);
                                preferredLocations.add(jsonObject.getString("location"));
                            }
                            userBean.setPreferredLocation(preferredLocations);

                            request.getSession().setAttribute("userData", userBean);

                            int votesJSON = json.getInt("votes");
                            float ratingJSON = json.getFloat("rating");
                            userBean.setRating(ratingJSON);
                            userBean.setVotes(votesJSON);

                            System.out.println(json.toString());
                            System.out.println(asdriverJSON);
                            if (asdriverJSON == 1) {
                                response.sendRedirect("profile.jsp");
                            } else {
                                response.sendRedirect("order.jsp");
                            }
                        }
                    } else {
                        response.sendRedirect("login.jsp");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}
