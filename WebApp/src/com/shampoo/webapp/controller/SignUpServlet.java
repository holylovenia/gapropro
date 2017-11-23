package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.UserBean;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SignUpServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmpassword = request.getParameter("confirmpassword");
        String phoneNumber = request.getParameter("phonenumber");
        String asdriver = request.getParameter("asdriver");

        CookieHandler cookieHandler = new CookieHandler();
        if(!(cookieHandler.isTextValid(name) && cookieHandler.isTextValid(username) && cookieHandler.isTextValid(email) && cookieHandler.isTextValid(password) && cookieHandler.isTextValid(confirmpassword) && cookieHandler.isTextValid(phoneNumber))) {
            response.sendRedirect("signup.jsp");
        } else {
            String access_token = null;
            try {
                access_token = sendPost(name, username, email, password, confirmpassword, phoneNumber, asdriver);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (access_token != null) {
                JSONObject access_token_JSON = new JSONObject(access_token);
                if (!access_token_JSON.has("error")) {
                    String token = access_token_JSON.getString("access_token");
                    String token_encoded = Base64.getEncoder().encodeToString(token.getBytes());
                    Cookie access_token_cookie = new Cookie("access_token", token_encoded);
                    Cookie expiry_time_cookie = new Cookie("expiry_time", Integer.toString(access_token_JSON.getInt("expiry_time")));
                    response.addCookie(access_token_cookie);
                    response.addCookie(expiry_time_cookie);

                    UserManagementClient userManagementClient = new UserManagementClient();
                    String userRawJson = userManagementClient.getUserManagement().getCurrentUserData(access_token_JSON.getString("access_token"));
                    System.out.println("USER REGISTER " + userRawJson);
                    JSONObject userJsonObject = new JSONArray(userRawJson).getJSONObject(0);
                    UserBean userBean = new UserBean();
                    userBean.setUserID(userJsonObject.getInt("id"));
                    userBean.setName(userJsonObject.getString("name"));
                    userBean.setUsername(userJsonObject.getString("username"));
                    userBean.setPhoneNumber(userJsonObject.getString("phone_no"));
                    userBean.setDriverStatus(userJsonObject.getInt("is_driver"));
                    userBean.setProfilePicture(userJsonObject.getString("profile_picture"));
                    userBean.setEmail(userJsonObject.getString("email"));
                    userBean.setRating(0.0f);
                    userBean.setVotes(0);

                    request.getSession().setAttribute("userData", userBean);

                    if (asdriver == null) {
                        response.sendRedirect("order.jsp");
                    } else {
                        response.sendRedirect("profile.jsp");
                    }
                } else {
                    if (access_token_JSON.getString("error").equals("username/email already taken")) {
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Username or Email already taken\");");
                        response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                        response.getOutputStream().println("</script>");
                    }
                    else if (access_token_JSON.getString("error").equals("wrong confirm password")) {
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Password does not match\");");
                        response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                        response.getOutputStream().println("</script>");
                    }
                    response.sendRedirect("signup.jsp");
                }
            }
        }
    }

    private String sendPost(String _name, String _username, String _email, String _password, String _confirmpassword, String _phoneNumber, String _asdriver) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://localhost:9001/register");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", _name));
        params.add(new BasicNameValuePair("username", _username));
        params.add(new BasicNameValuePair("email", _email));
        params.add(new BasicNameValuePair("password", _password));
        params.add(new BasicNameValuePair("confirmpassword", _confirmpassword));
        params.add(new BasicNameValuePair("phonenumber", _phoneNumber));
        System.out.println("name" + _name);
        System.out.println("username" + _username);
        System.out.println("email" + _email);
        System.out.println("password" + _password);
        System.out.println("confirmpassword" + _confirmpassword);
        System.out.println("phonenumber" + _phoneNumber);

        if (_asdriver ==  null) {
            params.add(new BasicNameValuePair("asdriver", "0"));
        } else {
            params.add(new BasicNameValuePair("asdriver", "1"));
        }

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
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
