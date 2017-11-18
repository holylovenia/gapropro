package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.OrderBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SelectDriverServlet")
public class SelectDriverServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer chosenDriverId = Integer.parseInt(request.getParameter("driverId"));
        OrderBean orderBean = (OrderBean) request.getSession().getAttribute("orderData");
        orderBean.setDriverId(chosenDriverId);

        response.sendRedirect("completeorder.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
