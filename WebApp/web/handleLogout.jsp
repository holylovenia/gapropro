<%@ page import="org.apache.http.client.methods.HttpPost" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.impl.client.DefaultHttpClient" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.http.message.BasicNameValuePair" %>
<%@ page import="org.apache.http.client.entity.UrlEncodedFormEntity" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page import="org.apache.http.client.ClientProtocolException" %>
<%@ page import="java.io.IOException" %>
<jsp:useBean id="userData" class="com.shampoo.webapp.model.UserBean" scope="session" />

<%  if (userData.getUserID() == null) {
        response.sendRedirect("login.jsp");
    }
    HttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost("http://localhost:9001/logout");

    List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

    System.out.println("Logout ID:" + Integer.toString(userData.getUserID()));
    params.add(new BasicNameValuePair("userID", Integer.toString(userData.getUserID())));
    try {
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    try {
        HttpResponse data_response = httpClient.execute(httpPost);
        HttpEntity respEntity =  data_response.getEntity();

        if (respEntity != null) {
            String content = EntityUtils.toString(respEntity);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    Cookie access_token_cookie = new Cookie("access_token", null);
    Cookie expiry_time_cookie = new Cookie("expiry_time", null);
    access_token_cookie.setMaxAge(0);
    expiry_time_cookie.setMaxAge(0);
    response.addCookie(access_token_cookie);
    response.addCookie(expiry_time_cookie);
    request.getSession(false).invalidate();

    response.sendRedirect("login.jsp");
%>