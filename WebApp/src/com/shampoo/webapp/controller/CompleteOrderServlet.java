package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.OrderBean;
import com.shampoo.webapp.requester.OrderClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

@WebServlet(name = "CompleteOrderServlet")
public class CompleteOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer ratingSubmitted;
        if(request.getParameter("rating") == null) {
            ratingSubmitted = 0;
        } else {
            ratingSubmitted = Integer.parseInt(request.getParameter("rating"));
        }
        String commentSubmitted = request.getParameter("comment");

        OrderBean orderBean = (OrderBean) request.getSession().getAttribute("orderData");
        orderBean.setRating(ratingSubmitted);
        orderBean.setComment(commentSubmitted);

        try {
            OrderClient orderClient = new OrderClient();
            String result = orderClient.getOrder().order(new CookieHandler().getAccessTokenCookie(request), orderBean.getDriverId(), orderBean.getPickingPoint(), orderBean.getDestination(), orderBean.getComment(), orderBean.getRating());
            switch (result) {
                case "Successful":
                    response.sendRedirect("/transactionuser");
                    break;
                case "Error":
                    response.getOutputStream().println("<script type=\"text/javascript\">");
                    response.getOutputStream().println("alert(\"Failed to complete order!\");");
                    response.getOutputStream().println("window.location =\"order.jsp\"");
                    response.getOutputStream().println("</script>");
                    break;
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
                case "invalid_malformed":
                    response.getOutputStream().println("<script type=\"text/javascript\">");
                    response.getOutputStream().println("alert(\"Malformed token detected!\");");
                    response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                    response.getOutputStream().println("</script>");
                    break;
                case "invalid_agent":
                    response.getOutputStream().println("<script type=\"text/javascript\">");
                    response.getOutputStream().println("alert(\"Invalid user agent detected!\");");
                    response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                    response.getOutputStream().println("</script>");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
