package com.shampoo.service.ojek.requester;

import com.shampoo.service.ojek.model.CurrentUser;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class UserClient {
    private User user;

    public UserClient() {
        URL url = null;
        try {
            String wsdlLocation = "http://localhost:9001/services/User?wsdl";
            url = new URL(wsdlLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String name = "UserImpl";
        String targetNamespace = "http://responder.identity.service.shampoo.com/";
        QName serverQName = new QName(targetNamespace, name + "Service");
        Service service = Service.create(url, serverQName);
        String endpointAddress = "http://localhost:9001/services/User";
        service.addPort(serverQName, javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING, endpointAddress);
        QName serviceName = new QName(targetNamespace, name + "Port");
        user = service.getPort(serviceName, User.class);
    }

    public User getUser() {
        return user;
    }

    public String getUserData(String token) {
        return user.fetchUserDataFromToken(token);
    }

    public void changeUserData(int id, String fullName, String phoneNumber, String profilePicture, int isDriver) throws SQLException {
        user.changeUserData(id, fullName, phoneNumber, profilePicture, isDriver);
    }

    public String getDriversData(String token) {
        return user.fetchDriversData(token);
    }

    public String getUsersData(String token) {
        return user.fetchUsersData(token);
    }
}
