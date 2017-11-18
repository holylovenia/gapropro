package com.shampoo.webapp.model;

import java.util.ArrayList;

public class OrderBean {
    private String preferredDriverName;
    private Integer driverId;
    private String pickingPoint;
    private String destination;
    private String comment;
    private Integer rating;
    private ArrayList<DriverBean> preferredDrivers;
    private ArrayList<DriverBean> otherDrivers;

    public OrderBean() {}

    public String getPreferredDriverName() {
        return preferredDriverName;
    }

    public void setPreferredDriverName(String preferredDriverName) {
        this.preferredDriverName = preferredDriverName;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getPickingPoint() {
        return pickingPoint;
    }

    public void setPickingPoint(String pickingPoint) {
        this.pickingPoint = pickingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public ArrayList<DriverBean> getPreferredDrivers() {
        return preferredDrivers;
    }

    public void setPreferredDrivers(ArrayList<DriverBean> preferredDrivers) {
        this.preferredDrivers = preferredDrivers;
    }

    public ArrayList<DriverBean> getOtherDrivers() {
        return otherDrivers;
    }

    public void setOtherDrivers(ArrayList<DriverBean> otherDrivers) {
        this.otherDrivers = otherDrivers;
    }
}
