package com.shampoo.webapp.requester;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class UserManagementClient {
    private UserManagement userManagement;

    public UserManagementClient() {
        URL url = null;
        try {
            String wsdlLocation = "http://localhost:9003/services/UserManagement?wsdl";
            url = new URL(wsdlLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String name = "UserManagementImpl";
        String targetNamespace = "http://responder.ojek.service.shampoo.com/";
        QName serverQName = new QName(targetNamespace, name + "Service");
        Service service = Service.create(url, serverQName);
        String endpointAddress = "http://localhost:9003/services/UserManagement";
        service.addPort(serverQName, javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING, endpointAddress);
        QName serviceName = new QName(targetNamespace, name + "Port");
        userManagement = service.getPort(serviceName, UserManagement.class);
    }

    public UserManagement getUserManagement() {
        return userManagement;
    }
}
