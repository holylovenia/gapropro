package com.shampoo.service.identity.responder;

import com.shampoo.service.identity.DatabaseManager;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.SQLException;

@WebService(endpointInterface = "com.shampoo.service.identity.responder.User")
public class UserImpl implements User {

    @WebMethod
    public String fetchUserDataFromToken(String access_token) {
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            if (databaseManager != null) {
                result = databaseManager.fetchUserDataFromToken(access_token);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (databaseManager != null) {
                databaseManager.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void changeUserData(int id, String fullName, String phoneNumber, String profilePicture, int isDriver) {
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (databaseManager != null) {
                databaseManager.changeUserData(id, fullName, phoneNumber, profilePicture, isDriver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @WebMethod
    public String fetchDriversData(String access_token) {
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = null;
        try {
            if (databaseManager != null) {
                result = databaseManager.fetchDriverDataExceptMe(access_token);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (databaseManager != null) {
                databaseManager.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @WebMethod
    public String fetchUsersData(String access_token) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            return databaseManager.fetchUsersData(access_token);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
