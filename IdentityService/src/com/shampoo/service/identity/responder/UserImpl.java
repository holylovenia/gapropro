package com.shampoo.service.identity.responder;

import com.shampoo.service.identity.DatabaseManager;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.SQLException;

@WebService(endpointInterface = "com.shampoo.service.identity.responder.User")
public class UserImpl implements User {

    @WebMethod
    public String fetchUserDataFromToken(String access_token) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            String result;
            result = databaseManager.fetchUserDataFromToken(access_token);
            databaseManager.closeConnection();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    @Override
    public void changeUserData(int id, String fullName, String phoneNumber, String profilePicture, int isDriver) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.changeUserData(id, fullName, phoneNumber, profilePicture, isDriver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @WebMethod
    public String fetchDriversData(String access_token) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            return databaseManager.fetchDriverDataExceptMe(access_token);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    @WebMethod
    public String fetchUsersData(String access_token) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            return databaseManager.fetchUsersData(access_token);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error";
    }
}
