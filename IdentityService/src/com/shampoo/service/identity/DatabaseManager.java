package com.shampoo.service.identity;

import eu.bitwalker.useragentutils.UserAgent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {

    private Connection connection;
    private Statement statement;
    static Integer expiredDelay = 1800 * 1000;

    public DatabaseManager() throws SQLException {
        connection = getConnection();
        if (connection == null) {
            System.out.println("Failed to connect");
        }
        statement = connection.createStatement();
    }

    private Connection getConnection() {
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
            String query = "SELECT * FROM users WHERE username=? AND password=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

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

    public void register(String username, String email, String fullName, String password, String phoneNumber, int isDriver, String profilePicture, String userAgent, String ipAddress) {
        try {
            Timestamp expiredTime = new Timestamp(System.currentTimeMillis() + expiredDelay);
            String access_token = new AccessToken().generateAccessToken(username, email, password) + "#" + userAgent + "#" + ipAddress;

            String query = "INSERT INTO users(username, name, email, password, phone_no, profile_picture, is_driver, expired_time, access_token) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, fullName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setString(6, profilePicture);
            preparedStatement.setString(7, Integer.toString(isDriver));
            preparedStatement.setTimestamp(8, expiredTime);
            preparedStatement.setString(9, access_token);
            preparedStatement.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //Close Connection
    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void updateAccessToken(Integer userID, String access_token) throws SQLException {
        String updateQuery = "UPDATE users SET access_token=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, access_token);
        preparedStatement.setInt(2, userID);
        preparedStatement.executeUpdate();
    }

    public void updateExpiredTime(String access_token, String userAgent, String ipAddress) {
        try {
            if (access_token != null) {
                String selectQuery = "SELECT * FROM users WHERE access_token LIKE ?";
                PreparedStatement oldPreparedStatement = connection.prepareStatement(selectQuery);
                oldPreparedStatement.setString(1, access_token);
                ResultSet resultSet = oldPreparedStatement.executeQuery();
                resultSet.next();

                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String token = new AccessToken().generateAccessToken(username, email, password) + "#" + userAgent + "#" + ipAddress;
                Timestamp newTimestamp = new Timestamp(System.currentTimeMillis() + expiredDelay);

                String updateQuery = "UPDATE users SET access_token=?, expired_time=? WHERE access_token=?";

                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, token);
                preparedStatement.setTimestamp(2, newTimestamp);
                preparedStatement.setString(3, access_token);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet fetchUserData(Integer userID) throws SQLException {
        String selectQuery = "SELECT * FROM users WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setInt(1, userID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet;
    }

    public ResultSet fetchToken(String username) throws SQLException {
        String selectQuery = "SELECT access_token FROM users WHERE username=?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet;
    }

    public Integer invalidateToken(Integer userID) throws SQLException {
        Timestamp newTimestamp = new Timestamp(System.currentTimeMillis() - 1);
        String updateQuery = "UPDATE users SET access_token=?, expired_time=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        String expiredToken = "shampoobersihkinclongmantapkali";
        preparedStatement.setString(1, expiredToken);
        preparedStatement.setTimestamp(2, newTimestamp);
        preparedStatement.setInt(3, userID);
        return preparedStatement.executeUpdate();
    }

    public boolean validateRegister(String username, String email) throws SQLException {
        String query = "SELECT * FROM users WHERE username=? OR email=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        return !resultSet.next();
    }

    public String checkAccessToken(String access_token) throws SQLException {
        String[] parts = access_token.split("#");
        for (String part : parts) {
            System.out.println(part);
        }
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
                String[] dbTokenParts = dbToken.split("#");
                System.out.println("dbToken:");
                for (String part : dbTokenParts) {
                    System.out.println(part);
                }

                if (dbTokenParts.length == 3) {
                    String dbUserAgent = dbTokenParts[1];
                    String dbIpAddress = dbTokenParts[2];
                    if (!ipAddress.equals(dbIpAddress)) {
                        return "invalid_ip";
                    }
                    if (!UserAgent.parseUserAgentString(userAgent).getBrowser().equals(UserAgent.parseUserAgentString(dbUserAgent).getBrowser())) {
                        return "invalid_agent";
                    }

                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    Timestamp expired = resultSet.getTimestamp("expired_time");
                    if (expired.before(now)) {
                        if (canTokenBeRenewed(expired, now) && ipAddress.equals(dbIpAddress) && userAgent.equals(dbUserAgent)) {
                            System.out.println("CanBeRenewed: TRUE");
                            updateExpiredTime(access_token, dbUserAgent, dbIpAddress);
                            return "valid";
                        } else {
                            System.out.println("CanBeRenewed: FALSE");
                            return "expired";
                        }
                    } else {
                        return "valid";
                    }
                } else {
                    return "invalid_malformed";
                }
            }
        } else {
            return "invalid_malformed";
        }
    }

    private boolean canTokenBeRenewed(Timestamp expiredTime, Timestamp currentTime) {
        long difference = currentTime.getTime() - expiredTime.getTime();
        int limit = 5 * 60 * 1000;
        System.out.println("DIFFERENCE " + difference);
        return difference <= limit;
    }

    public String fetchUserDataFromToken(String access_token) throws SQLException {
        String query = "SELECT * FROM users WHERE access_token=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, access_token);
        ResultSet resultSet = preparedStatement.executeQuery();
        return convertToJson(resultSet);
    }

    public void changeUserData(int id, String fullName, String phoneNumber, String profilePicture, int isDriver) throws SQLException {
        String updateQuery = "UPDATE users SET name=?, phone_no=?, profile_picture=?, is_driver=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, fullName);
        preparedStatement.setString(2, phoneNumber);
        preparedStatement.setString(3, profilePicture);
        preparedStatement.setInt(4, isDriver);
        preparedStatement.setInt(5, id);
        preparedStatement.executeUpdate();
    }

    public String fetchDriverDataExceptMe(String access_token) throws SQLException {
        String query = "SELECT id, username, name, profile_picture FROM users WHERE access_token!=? AND is_driver=1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, access_token);
        ResultSet resultSet = preparedStatement.executeQuery();
        return convertToJson(resultSet);
    }

    private String convertToJson(ResultSet resultSet) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();

        int counter = 0;
        while (resultSet.next()) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 1; i <= numColumns; i++) {
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
        String query = "SELECT id, name, profile_picture FROM users WHERE access_token!=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, access_token);
        ResultSet resultSet = preparedStatement.executeQuery();
        return convertToJson(resultSet);
    }

    public Integer fetchUserId(String username) throws SQLException {
        String query = "SELECT id FROM users WHERE username=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }
}