package com.shampoo.webapp.requester;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class PreferredLocationClient {
    private PreferredLocation preferredLocation;

    public PreferredLocationClient() throws MalformedURLException, SQLException {
        URL url = null;
        try {
            String wsdlLocation = "http://localhost:9003/services/PreferredLocation?wsdl";
            url = new URL(wsdlLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String name = "PreferredLocationImpl";
        String targetNamespace = "http://responder.ojek.service.shampoo.com/";
        QName serverQName = new QName(targetNamespace, name + "Service");
        Service service = Service.create(url, serverQName);
        String endpointAddress = "http://localhost:9003/services/PreferredLocation";
        service.addPort(serverQName, javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING, endpointAddress);
        QName serviceName = new QName(targetNamespace, name + "Port");
        preferredLocation = service.getPort(serviceName, PreferredLocation.class);
    }

    public PreferredLocation getPreferredLocation() {
        return preferredLocation;
    }
}
