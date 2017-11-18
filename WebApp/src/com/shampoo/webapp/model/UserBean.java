package com.shampoo.webapp.model;

import com.shampoo.webapp.requester.UserManagementClient;

import javax.servlet.http.Cookie;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class UserBean {

    Integer userID;
    String username;
    String name;
    String profilePicture;
    Float rating;
    Integer votes;
    String email;
    String phoneNumber;
    Integer driverStatus;
    ArrayList<String> preferredLocation;

    public UserBean() {
        preferredLocation = new ArrayList<>();
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(Integer driverStatus) {
        this.driverStatus = driverStatus;
    }

    public ArrayList<String> getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(ArrayList<String> preferredLocation) {
        this.preferredLocation = preferredLocation;
    }
}
