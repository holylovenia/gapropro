package com.shampoo.service.ojek.responder;

import com.shampoo.service.ojek.model.CurrentUser;
import com.shampoo.service.ojek.requester.UserClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.jws.WebService;
import java.sql.SQLException;

import static com.shampoo.service.ojek.model.CurrentUser.ACTION_ERROR_MESSAGE;
import static com.shampoo.service.ojek.model.CurrentUser.ACTION_SUCCESS_MESSAGE;

@WebService(endpointInterface = "com.shampoo.service.ojek.responder.UserManagement")
public class UserManagementImpl implements OjekService, UserManagement {

    @Override
    public String getCurrentUserData(String token) {
        if (currentUser.getTokenStatus(token).equals(CurrentUser.VALID)) {
            int id = currentUser.getId();
            JSONArray jsonArray = new JSONArray(CurrentUser.rawJson);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            try {
                jsonObject.put("votes", databaseManager.getVotes(id));
                jsonObject.put("rating", databaseManager.getRating(id));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return jsonArray.toString();
        } else {
            return currentUser.getTokenStatus(token);
        }
    }

    @Override
    public String changeCurrentUserData(String token, String fullName, String phoneNumber, String profilePicture, int isDriver) {
        if (currentUser.getTokenStatus(token).equals(currentUser.VALID)) {
            currentUser.setFullName(fullName);
            currentUser.setPhoneNumber(phoneNumber);
            currentUser.setProfilePicture(profilePicture);
            currentUser.setIsDriver(isDriver);
            UserClient userClient = new UserClient();
            try {
                userClient.changeUserData(currentUser.getId(), fullName, phoneNumber, profilePicture, isDriver);
                return ACTION_SUCCESS_MESSAGE;
            } catch (SQLException e) {
                e.printStackTrace();
                return ACTION_ERROR_MESSAGE;
            }
        } else {
            return currentUser.getTokenStatus(token);
        }
    }
}
