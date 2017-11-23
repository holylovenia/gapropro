package com.shampoo.service.ojek.model;

import com.shampoo.service.ojek.requester.TokenCheckerClient;
import com.shampoo.service.ojek.requester.UserClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class CurrentUser {
    public static final String VALID = "valid";
    public static final String EXPIRED = "expired";
    public static final String INVALID = "invalid";
    public static final String ACTION_SUCCESS_MESSAGE = "Successful";
    public static final String ACTION_ERROR_MESSAGE = "Error";
    public static String rawJson;
    private static TokenCheckerClient tokenCheckerClient;
    public static UserClient userClient;

    public CurrentUser() {
        tokenCheckerClient = new TokenCheckerClient();
        userClient = new UserClient();
    }

    public static int getId() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getInt("id");
    }

    public static String getUsername() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getString("username");
    }

    public static String getFullName() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getString("name");
    }

    public static String getEmail() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getString("email");
    }

    public static String getPassword() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getString("password");
    }

    public static String getPhoneNumber() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getString("phone_no");
    }

    public static int getIsDriver() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getInt("is_driver");
    }

    public static String getProfilePicture() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getString("profile_picture");
    }

    public static String getExpiredTime() {
        JSONObject jsonObject = (JSONObject) new JSONArray(rawJson).get(0);
        return jsonObject.getString("expired_time");
    }

    public static void setFullName(String fullName) {
        JSONArray jsonArray = new JSONArray(rawJson);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        jsonObject.remove("name");
        jsonObject.put("name", fullName);
        rawJson = jsonArray.toString();
    }

    public static void setPhoneNumber(String phoneNumber) {
        JSONArray jsonArray = new JSONArray(rawJson);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        jsonObject.remove("phone_no");
        jsonObject.put("phone_no", phoneNumber);
        rawJson = jsonArray.toString();
    }

    public static void setProfilePicture(String profilePicture) {
        JSONArray jsonArray = new JSONArray(rawJson);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        jsonObject.remove("profile_picture");
        jsonObject.put("profile_picture", profilePicture);
        rawJson = jsonArray.toString();
    }

    public static void setIsDriver(int isDriver) {
        JSONArray jsonArray = new JSONArray(rawJson);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        jsonObject.remove("is_driver");
        jsonObject.put("is_driver", isDriver);
        rawJson = jsonArray.toString();
    }

    public static String getTokenStatus(String token) {
        String tokenStatus = tokenCheckerClient.getTokenChecker().checkToken(token);
        if(tokenStatus.equals(VALID)) {
            if(rawJson == null || !rawJson.equals(userClient.getUserData(token))) {
                rawJson = userClient.getUserData(token);
            }
            return VALID;
        } else {
            return tokenStatus;
        }
    }
}
