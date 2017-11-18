package com.shampoo.service.identity.responder;

import com.shampoo.service.identity.DatabaseManager;

import javax.jws.WebService;
import java.sql.SQLException;

@WebService (endpointInterface = "com.shampoo.service.identity.responder.TokenChecker")
public class TokenCheckerImpl implements TokenChecker {

    @Override
    public String checkToken(String access_token) {
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return databaseManager.checkAccessToken(access_token);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            databaseManager.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "invalid";
    }
}
