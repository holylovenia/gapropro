package com.shampoo.webapp.model;

import com.shampoo.webapp.requester.Transaction;
import com.shampoo.webapp.requester.UserManagementClient;

import javax.servlet.http.Cookie;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class TransactionBean {

    Integer ID;
    Integer userID;
    Integer driverID;
    String userName;
    String driverName;
    String picking_point;
    String destination;
    String date;
    String comment;
    Integer rating;
    String profilePicture;
    Integer user_show;
    Integer driver_show;

    public TransactionBean() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getDriverID() {
        return driverID;
    }

    public void setDriverID(Integer driverID) {
        this.driverID = driverID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPickingPoint() {
        return picking_point;
    }

    public void setPickingPoint(String picking_point) {
        this.picking_point = picking_point;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Integer getUserShow() {
        return user_show;
    }

    public void setUserShow(Integer user_show) {
        this.user_show = user_show;
    }

    public Integer getDriverShow() {
        return driver_show;
    }

    public void setDriverShow(Integer driver_show) {
        this.driver_show = driver_show;
    }
}