package com.shampoo.service.ojek.responder;

import com.shampoo.service.ojek.model.CurrentUser;

import javax.jws.WebService;
import java.sql.SQLException;

import static com.shampoo.service.ojek.model.CurrentUser.ACTION_SUCCESS_MESSAGE;

@WebService(endpointInterface = "com.shampoo.service.ojek.responder.PreferredLocation")
public class PreferredLocationImpl implements OjekService, PreferredLocation {

    @Override
    public String addPreferredLocation(String token, String location) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                databaseManager.addPreferredLocation(currentUser.getId(), location);
                System.out.println("Add Preferred Location success");
                return ACTION_SUCCESS_MESSAGE;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Add Preferred Location error");
                return currentUser.ACTION_ERROR_MESSAGE;
            }
        } else {
            System.out.println("Invalid");
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String editPreferredLocation(String token, String previouslocation, String newLocation) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                databaseManager.editPreferredLocation(currentUser.getId(), previouslocation, newLocation);
                System.out.println("Edit Preferred Location success");
                return ACTION_SUCCESS_MESSAGE;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Edit Preferred Location error");
                return currentUser.ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String removePreferredLocation(String token, String location) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                databaseManager.removePreferredLocation(currentUser.getId(), location);
                System.out.println("Remove Preferred Location success");
                return ACTION_SUCCESS_MESSAGE;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Remove Preferred Location error");
                return currentUser.ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String getPreferredLocation(String token, String location) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                return databaseManager.getPreferredLocation(currentUser.getId(), location);
            } catch (SQLException e) {
                e.printStackTrace();
                return currentUser.ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String getUserPreferredLocations(String token) {
        if(currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            try {
                return databaseManager.getUserPreferredLocations(currentUser.getId());
            } catch (SQLException e) {
                e.printStackTrace();
                return currentUser.ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }
}
