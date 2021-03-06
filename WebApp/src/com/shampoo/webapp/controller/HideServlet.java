package com.shampoo.webapp.controller;

import com.shampoo.webapp.requester.Transaction;
import com.shampoo.webapp.requester.TransactionClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

@WebServlet(name = "HideServlet")
public class HideServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int hideID = Integer.parseInt(request.getParameter("historyId"));
        try {
            TransactionClient transactionClient = new TransactionClient();
            if (request.getParameter("driverhistory") != null) {
                String token_encoded = new CookieHandler().getAccessTokenCookie(request);
                byte[] token_byte = Base64.getDecoder().decode(token_encoded);
                String token = new String(token_byte);
                String resp = transactionClient.getTransaction().hideFromDriver(token, hideID);
                switch (resp) {
                    case "Error":
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Failed to hide transaction history!\");");
                        response.getOutputStream().println("window.location =\"driverhistory.jsp\"");
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
                    default:
                        response.sendRedirect("/transactiondriver");
                        break;
                }
            } else {
                String token_encoded = new CookieHandler().getAccessTokenCookie(request);
                byte[] token_byte = Base64.getDecoder().decode(token_encoded);
                String token = new String(token_byte);
                String resp = transactionClient.getTransaction().hideFromUser(token, hideID);
                switch (resp) {
                    case "Error":
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Failed to hide transaction history!\");");
                        response.getOutputStream().println("window.location =\"history.jsp\"");
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
                    case "invalid_address":
                        response.getOutputStream().println("<script type=\"text/javascript\">");
                        response.getOutputStream().println("alert(\"Invalid user agent detected!\");");
                        response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                        response.getOutputStream().println("</script>");
                        break;
                    default:
                        response.sendRedirect("/transactionuser");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
