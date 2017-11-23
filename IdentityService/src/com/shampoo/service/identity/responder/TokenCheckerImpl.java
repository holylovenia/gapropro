package com.shampoo.service.identity.responder;

import com.shampoo.service.identity.DatabaseManager;

import javax.jws.WebService;

@WebService (endpointInterface = "com.shampoo.service.identity.responder.TokenChecker")
public class TokenCheckerImpl implements TokenChecker {

    @Override
    public String checkToken(String access_token) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            String result = databaseManager.checkAccessToken(access_token);
            databaseManager.closeConnection();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "invalid";
    }
}
