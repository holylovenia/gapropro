package com.shampoo.service.ojek;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        connection = getConnection();
    }

    private Connection getConnection() {
        final String username = "shampoo";
        final String password = "shampoo1gratis1";
        final String dbms = "mysql";
        final String serverName = "localhost";
        final String portNumber = "";
        final String databaseName = "shampoo_service";
        this.connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", username);
            connectionProperties.put("password", password);
            connection = DriverManager.getConnection("jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + databaseName, connectionProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void addPreferredLocation(int userId, String location) throws SQLException {
        String query = "INSERT INTO preferred_locations(id, location) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, location);
        preparedStatement.executeUpdate();
    }

    public void editPreferredLocation(int userId, String previousLocation, String newLocation) throws SQLException {
        String query = "UPDATE preferred_locations SET location=? WHERE id=? AND location=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newLocation);
        preparedStatement.setInt(2, userId);
        preparedStatement.setString(3, previousLocation);
        preparedStatement.executeUpdate();
    }

    public void removePreferredLocation(int userId, String location) throws SQLException {
        String query = "DELETE FROM preferred_locations WHERE id=? AND location=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, location);
        preparedStatement.executeUpdate();
    }

    public void order(int userId, int driverId, String pickingPoint, String destination, String comment, int rating) throws SQLException {
        String query = "INSERT INTO transactions(user_id, driver_id, picking_point, destination, date, comment, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, driverId);
        preparedStatement.setString(3, pickingPoint);
        preparedStatement.setString(4, destination);
        preparedStatement.setDate(5, new Date(Calendar.getInstance().getTimeInMillis()));

        if(comment == null) {
            preparedStatement.setObject(6, null);
        } else {
            preparedStatement.setString(6, comment);
        }
        if(rating == 0) {
            preparedStatement.setObject(7, null);
        } else {
            preparedStatement.setInt(7, rating);
        }

        preparedStatement.executeUpdate();
    }

    public void hideFromUser(int transactionId) throws SQLException {
        String query = "UPDATE transactions SET user_show=0 WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, transactionId);
        preparedStatement.executeUpdate();
    }

    public void hideFromDriver(int transactionId) throws SQLException {
        String query = "UPDATE transactions SET driver_show=0 WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, transactionId);
        preparedStatement.executeUpdate();
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
                Object object = resultSet.getObject(columnName);
                if(object == null) {
                    if(columnName.contains("name") || columnName.contains("comment")) {
                        object = "";
                    } else if(columnName.equals("rating")) {
                        object = 0;
                    }
                }
                jsonObject.put(columnName, object);
            }
            jsonArray.put(counter, jsonObject);
            counter++;
        }
        return jsonArray.toString();
    }

    public String getTransaction(int transactionId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, transactionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return convertToJson(resultSet);
    }

    public String getUserTransactions(int userId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE user_show=1 AND user_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return convertToJson(resultSet);
    }

    public String getDriverTransactions(int driverId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE driver_show=1 AND driver_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, driverId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return convertToJson(resultSet);
    }

    public String getPreferredLocation(int userId, String location) throws SQLException {
        String query = "SELECT * FROM preferred_locations WHERE id=? AND location=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, location);
        ResultSet resultSet = preparedStatement.executeQuery();
        return convertToJson(resultSet);
    }

    public String getUserPreferredLocations(int userId) throws SQLException {
        String query = "SELECT * FROM preferred_locations WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return convertToJson(resultSet);
    }

    public boolean isSelectedDriver(int driverId, String pickingPoint, String destination) throws SQLException {
        String query = "SELECT * FROM preferred_locations WHERE id=? AND (location=? OR location=?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, driverId);
        preparedStatement.setString(2, pickingPoint);
        preparedStatement.setString(3, destination);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public int getVotes(int driverId) throws SQLException {
        String query = "SELECT COUNT(rating) AS votes FROM transactions WHERE driver_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, driverId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("votes");
    }

    public float getRating(int driverId) throws SQLException {
        String query = "SELECT AVG(rating) AS rating_number FROM transactions WHERE driver_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, driverId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if(resultSet.getObject("rating_number") == null) {
            return 0;
        } else {
            return resultSet.getFloat("rating_number");
        }
    }
}