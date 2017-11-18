package com.shampoo.service.ojek.responder;

import com.shampoo.service.ojek.model.CurrentUser;
import com.shampoo.service.ojek.requester.UserClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.jws.WebService;
import java.sql.SQLException;

import static com.shampoo.service.ojek.model.CurrentUser.ACTION_ERROR_MESSAGE;
import static com.shampoo.service.ojek.model.CurrentUser.ACTION_SUCCESS_MESSAGE;

@WebService(endpointInterface = "com.shampoo.service.ojek.responder.Order")
public class OrderImpl implements OjekService, Order {

    @Override
    public String order(String token, int driverId, String pickingPoint, String destination, String comment, int rating) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                databaseManager.order(currentUser.getId(), driverId, pickingPoint, destination, comment, rating);
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
    public String getDrivers(String token, String preferredDriverName, String pickingPoint, String destination) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            JSONArray allDrivers = new JSONArray(currentUser.userClient.getDriversData(token));
            JSONArray selectedDrivers = new JSONArray();
            for(int i = 0; i < allDrivers.length(); i++) {
                JSONObject driver = (JSONObject) allDrivers.get(i);
                try {
                    int id = driver.getInt("id");
                    if(databaseManager.isSelectedDriver(id, pickingPoint, destination)) {
                        if ((preferredDriverName != null && !preferredDriverName.isEmpty()) && (driver.getString("name").contains(preferredDriverName) || driver.getString("username").contains(preferredDriverName))) {
                            driver.put("is_preferred", 1);
                        } else {
                            driver.put("is_preferred", 0);
                        }
                        driver.put("votes", databaseManager.getVotes(id));
                        driver.put("rating", databaseManager.getRating(id));
                        System.out.println("ID: " + id + " Votes: " + databaseManager.getVotes(id) + " Rating: " + databaseManager.getRating(id));
                        selectedDrivers.put(driver);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return ACTION_ERROR_MESSAGE;
                }
            }
            return selectedDrivers.toString();
        } else {
            return currentUser.getTokenStatus(token);
        }
    }
}
