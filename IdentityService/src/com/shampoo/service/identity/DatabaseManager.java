package com.shampoo.service.identity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {

    Connection connection;
    Statement statement;
    public static Integer expiredDelay = 1800 * 1000;

    public DatabaseManager() throws SQLException {
        connection = getConnection();
        if(connection == null) {
            System.out.println("Failed to connect");
        }
        statement = connection.createStatement();
    }

    public Connection getConnection() {
        final String username = "shampoo";
        final String password = "shampoo1gratis1";
        final String dbms = "mysql";
        final String serverName = "localhost";
        final String portNumber = "";
        final String databaseName = "shampoo_account";
        this.connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", username);
            connectionProperties.put("password", password);
            connection = DriverManager.getConnection("jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + databaseName, connectionProperties);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    //Return UserID
    public Integer login(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                return null;
            } else {
                System.out.println(resultSet.getInt("id"));
                return resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Register With Update Token
    public void register(String username, String email, String fullName, String password, String phoneNumber, int isDriver, String profilePicture, String userAgent, String ipAddress) {
        try {
            Timestamp expiredTime = new Timestamp(System.currentTimeMillis() + expiredDelay);
            String access_token = new AccessToken().generateAccessToken(username, email, password) + "#" + userAgent + "#" + ipAddress;
            String query = "INSERT INTO users(username, name, email, password, phone_no, profile_picture, is_driver, expired_time, access_token) VALUES("
                    + "'" + username + "'" + ",'" + fullName + "','" + email + "','" + password + "','" + phoneNumber + "','" + profilePicture + "'," +
                    Integer.toString(isDriver) + ",'" + expiredTime + "','" + access_token + "')";
            System.out.println(query);
            statement.execute(query);
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //Close Connection
    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void updateAccessToken(Integer userID, String access_token) throws SQLException {
        String updateQuery = "UPDATE users SET access_token='" + access_token + "' WHERE id=" + Integer.toString(userID);
        System.out.println("updateAccessToken" + updateQuery);
        statement.executeUpdate(updateQuery);
    }

    // TODO: MODIFY
    public void updateExpiredTime(String access_token) {
        try {
            if (access_token  != null) {
                String selectQuery = "SELECT * FROM users WHERE access_token='" + access_token + "'";
                ResultSet resultSet = statement.executeQuery(selectQuery);
                resultSet.next();
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String token = new AccessToken().generateAccessToken(username, email, password) + "#" + "<userAgent>" + "#" + "<ipAddress>";
                Timestamp newTimestamp = new Timestamp(System.currentTimeMillis() + expiredDelay);
                String updateQuery = "UPDATE users SET access_token='" + token + "'" + ",expired_time='" + newTimestamp +
                        "' WHERE access_token='" + access_token + "'";
                statement.execute(updateQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet fetchUserData(Integer userID) throws SQLException {
        String selectQuery = "SELECT * FROM users WHERE id=" + userID;
        ResultSet resultSet = statement.executeQuery(selectQuery);
        resultSet.next();
        return resultSet;
    }

    public ResultSet fetchTokenAndExpired(String username) throws SQLException {
        String selectQuery = "SELECT access_token, expired_time FROM users WHERE username='" + username + "'";
        ResultSet resultSet = statement.executeQuery(selectQuery);
        resultSet.next();
        return resultSet;
    }

    public ResultSet fetchToken(String username) throws SQLException {
        String selectQuery = "SELECT access_token FROM users WHERE username='" + username + "'";
        ResultSet resultSet = statement.executeQuery(selectQuery);
        resultSet.next();
        return resultSet;
    }

    public Integer invalidateToken(Integer userID) throws SQLException {
        String token = "shampoobersihkinclongmantapkali";
        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis() - 1);
        String updateQuery = "UPDATE users SET access_token='" + token + "'" + ",expired_time='" + newTimestamp +
                "' WHERE id=" + userID;
        return statement.executeUpdate(updateQuery);
    }

    public boolean validateRegister(String username, String email) throws SQLException {
        String query = "SELECT * FROM users WHERE username='" + username + "' OR email='" + email + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }

    public String checkAccessToken(String access_token) throws SQLException {
        String[] parts = access_token.split("#");
        if (parts.length == 3) {
            String token = parts[0];
            String userAgent = parts[1];
            String ipAddress = parts[2];

            // Modify query to match the token part
            String query = "SELECT * FROM users WHERE access_token LIKE '" + token + "%'";
            System.out.println(query);
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                return "invalid";
            } else {
                String dbToken = resultSet.getString("access_token");
                String[] dbTokenParts = access_token.split("#");

                if (dbTokenParts.length == 3) {
                    String dbUserAgent = dbTokenParts[1];
                    String dbIpAddress = dbTokenParts[2];
                    if (!ipAddress.equals(dbIpAddress)) {
                        return "invalid_ip";
                    }
                    if (!userAgent.equals(dbUserAgent)) {
                        return "invalid_agent";
                    }
                }
                else {
                    return "invalid_malformed";
                }

                Timestamp now = new Timestamp(System.currentTimeMillis());
                Timestamp expired = resultSet.getTimestamp("expired_time");
                if (expired.before(now)) {
                    if(canTokenBeRenewed(expired, now)) {
                        System.out.println("CanBeRenewed: TRUE");
                        updateExpiredTime(access_token);
                        return "valid";
                    } else {
                        System.out.println("CanBeRenewed: FALSE");
                        return "expired";
                    }
                } else {
                    return "valid";
                }
            }
        }
        else {
            return "invalid";
        }
    }

    private boolean canTokenBeRenewed(Timestamp expiredTime, Timestamp currentTime) {
        long difference = currentTime.getTime() - expiredTime.getTime();
        int limit = 5 * 60 * 1000;
        System.out.println("DIFFERENCE " + difference);
        return difference <= limit;
    }

    public String fetchUserDataFromToken(String access_token) throws SQLException {
        String query = "SELECT * FROM users WHERE access_token='" + access_token + "'";
        System.out.println(query);
        ResultSet resultSet = statement.executeQuery(query);
        return convertToJson(resultSet);
    }

    public void changeUserData(int id, String fullName, String phoneNumber, String profilePicture, int isDriver) throws SQLException {
        String updateQuery = "UPDATE users SET name='" + fullName + "', phone_no='" + phoneNumber + "', profile_picture='" + profilePicture + "', is_driver=" + isDriver + " WHERE id=" + id;
        statement.executeUpdate(updateQuery);
    }

    public String fetchDriverDataExceptMe(String access_token) throws SQLException {
        String query = "SELECT id, username, name, profile_picture FROM users WHERE access_token!='" + access_token + "' AND is_driver=" + 1;
        System.out.println(query);
        ResultSet resultSet = statement.executeQuery(query);
        return convertToJson(resultSet);
    }

    private String convertToJson(ResultSet resultSet) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();

        int counter = 0;
        while(resultSet.next()) {
            JSONObject jsonObject = new JSONObject();
            for(int i = 1; i <= numColumns; i++) {
                String columnName = metaData.getColumnName(i);
                jsonObject.put(columnName, resultSet.getObject(columnName));
            }
            jsonArray.put(counter, jsonObject);
            counter++;
        }
        String returnValue = jsonArray.toString();
        System.out.println(returnValue);
        return returnValue;
    }

    public String fetchUsersData(String access_token) throws SQLException {
        String query = "SELECT id, name, profile_picture FROM users WHERE access_token!='" + access_token + "'";
        System.out.println(query);
        ResultSet resultSet = statement.executeQuery(query);
        return convertToJson(resultSet);
    }

    public Integer fetchUserId(String username) throws SQLException {
        String query = "SELECT id FROM users WHERE username='" + username + "'";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("id");
    }
}