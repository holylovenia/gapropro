package com.shampoo.service.ojek.responder;

import com.shampoo.service.ojek.model.CurrentUser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.jws.WebService;
import java.sql.SQLException;

import static com.shampoo.service.ojek.model.CurrentUser.ACTION_ERROR_MESSAGE;
import static com.shampoo.service.ojek.model.CurrentUser.ACTION_SUCCESS_MESSAGE;

@WebService(endpointInterface = "com.shampoo.service.ojek.responder.Transaction")
public class TransactionImpl implements OjekService, Transaction {

    @Override
    public String hideFromUser(String token, int transactionId) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                databaseManager.hideFromUser(transactionId);
                return ACTION_SUCCESS_MESSAGE;
            } catch (SQLException e) {
                e.printStackTrace();
                return ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String hideFromDriver(String token, int transactionId) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                databaseManager.hideFromDriver(transactionId);
                return ACTION_SUCCESS_MESSAGE;
            } catch (SQLException e) {
                e.printStackTrace();
                return ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String getTransaction(String token, int transactionId) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                return databaseManager.getTransaction(transactionId);
            } catch (SQLException e) {
                e.printStackTrace();
                return ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String getVisibleUserTransactions(String token) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                JSONArray userTransactions = new JSONArray(databaseManager.getUserTransactions(currentUser.getId()));
                JSONArray driversData = new JSONArray(currentUser.userClient.getUsersData(token));
                for(int i = 0; i < userTransactions.length(); i++) {
                    JSONObject transaction = (JSONObject) userTransactions.get(i);
                    int driverId = transaction.getInt("driver_id");
                    System.out.println("driverId: " + driverId);
                    boolean found = false;
                    int counter = 0;
                    while(counter < driversData.length() && !found) {
                        JSONObject driver = (JSONObject) driversData.get(counter);
                        if(driverId == driver.getInt("id")) {
                            transaction.put("driver_name", driver.getString("name"));
                            transaction.put("driver_profile_picture", driver.getString("profile_picture"));
                            found = true;
                        } else {
                            counter++;
                        }
                    }
                }
                return userTransactions.toString();
            } catch (SQLException e) {
                e.printStackTrace();
                return ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String getVisibleDriverTransactions(String token) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                JSONArray driverTransactions = new JSONArray(databaseManager.getDriverTransactions(currentUser.getId()));
                JSONArray usersData = new JSONArray(currentUser.userClient.getUsersData(token));
                for(int i = 0; i < driverTransactions.length(); i++) {
                    JSONObject transaction = (JSONObject) driverTransactions.get(i);
                    int userId = transaction.getInt("user_id");
                    boolean found = false;
                    int counter = 0;
                    while(counter < usersData.length() && !found) {
                        JSONObject user = (JSONObject) usersData.get(counter);
                        if(userId == user.getInt("id")) {
                            transaction.put("user_name", user.getString("name"));
                            transaction.put("user_profile_picture", user.getString("profile_picture"));
                            found = true;
                        } else {
                            counter++;
                        }
                    }
                }
                return driverTransactions.toString();
            } catch (SQLException e) {
                e.printStackTrace();
                return currentUser.ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }
}
