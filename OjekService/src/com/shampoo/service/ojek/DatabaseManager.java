package com.shampoo.service.ojek;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

public class DatabaseManager {
    private final static String NULL_VALUE = "NULL";
    private Connection connection;
    private Statement statement;

    public DatabaseManager() {
        connection = getConnection();
    }

    public Connection getConnection() {
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
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void addPreferredLocation(int userId, String location) throws SQLException {
        String query = "INSERT INTO preferred_locations(id, location) VALUES (" + userId + ", '" + location + "')";
        statement.executeUpdate(query);
    }

    public void editPreferredLocation(int userId, String previousLocation, String newLocation) throws SQLException {
        String query = "UPDATE preferred_locations SET location='" + newLocation + "' WHERE id=" + userId + " AND location='" + previousLocation + "'";
        statement.executeUpdate(query);
    }

    public void removePreferredLocation(int userId, String location) throws SQLException {
        String query = "DELETE FROM preferred_locations WHERE id=" + userId + " AND location='" + location + "'";
        statement.executeUpdate(query);
    }

    public void order(int userId, int driverId, String pickingPoint, String destination, String comment, int rating) throws SQLException {
        String query = "INSERT INTO transactions(user_id, driver_id, picking_point, destination, date, comment, rating) VALUES (" + userId + ", " + driverId + ", '" + pickingPoint + "', '" + destination + "', '" + new Date(Calendar.getInstance().getTimeInMillis()) + "', ";
        if(comment == null) {
            query += NULL_VALUE + ", ";
        } else {
            query += "'" + comment + "', ";
        }
        if(rating == 0) {
            query += NULL_VALUE;
        } else {
            query += rating;
        }
        query += ")";
        statement.executeUpdate(query);
    }

    public void hideFromUser(int transactionId) throws SQLException {
        String query = "UPDATE transactions SET user_show=0 WHERE id=" + transactionId;
        statement.executeUpdate(query);
    }

    public void hideFromDriver(int transactionId) throws SQLException {
        String query = "UPDATE transactions SET driver_show=0 WHERE id=" + transactionId;
        statement.executeUpdate(query);
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
        String returnValue = jsonArray.toString();
        return returnValue;
    }

    public String getTransaction(int transactionId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE id=" + transactionId;
        System.out.println(query);
        ResultSet resultSet = statement.executeQuery(query);
        String result = convertToJson(resultSet);
        System.out.println(result);
        return result;
    }

    public String getUserTransactions(int userId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE user_show=1 AND user_id=" + userId;
        ResultSet resultSet = statement.executeQuery(query);
        return convertToJson(resultSet);
    }

    public String getDriverTransactions(int driverId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE driver_show=1 AND driver_id=" + driverId;
        ResultSet resultSet = statement.executeQuery(query);
        return convertToJson(resultSet);
    }

    public String getPreferredLocation(int userId, String location) throws SQLException {
        String query = "SELECT * FROM preferred_locations WHERE id=" + userId +" AND location='" + location + "'";
        ResultSet resultSet = statement.executeQuery(query);
        return convertToJson(resultSet);
    }

    public String getUserPreferredLocations(int userId) throws SQLException {
        String query = "SELECT * FROM preferred_locations WHERE id=" + userId;
        ResultSet resultSet = statement.executeQuery(query);
        return convertToJson(resultSet);
    }

    public boolean isSelectedDriver(int driverId, String pickingPoint, String destination) throws SQLException {
        String query = "SELECT * FROM preferred_locations WHERE id=" + driverId + " AND (location='" + pickingPoint + "' OR location='" + destination + "')";
        ResultSet resultSet = statement.executeQuery(query);
        if(!resultSet.next()) {
            // empty result
            return false;
        } else {
            return true;
        }
    }

    public int getVotes(int driverId) throws SQLException {
        String query = "SELECT COUNT(rating) AS votes FROM transactions WHERE driver_id=" + driverId;
        System.out.println("Query " + query);
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        System.out.println("Result votes: " + resultSet.getInt("votes"));
        return resultSet.getInt("votes");
    }

    public float getRating(int driverId) throws SQLException {
        String query = "SELECT AVG(rating) AS rating_number FROM transactions WHERE driver_id=" + driverId;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        if(resultSet.getObject("rating_number") == null) {
            return 0;
        } else {
            return resultSet.getFloat("rating_number");
        }
    }
}