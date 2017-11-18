package com.shampoo.service.ojek.requester;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class TokenCheckerClient {
    private TokenChecker tokenChecker;

    public TokenCheckerClient() {
        URL url = null;
        try {
            String wsdlLocation = "http://localhost:9001/services/TokenChecker?wsdl";
            url = new URL(wsdlLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String name = "TokenCheckerImpl";
        String targetNamespace = "http://responder.identity.service.shampoo.com/";
        QName serverQName = new QName(targetNamespace, name + "Service");
        Service service = Service.create(url, serverQName);
        String endpointAddress = "http://localhost:9001/services/TokenChecker";
        service.addPort(serverQName, javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING, endpointAddress);
        QName serviceName = new QName(targetNamespace, name + "Port");
        tokenChecker = service.getPort(serviceName, TokenChecker.class);
    }

    public TokenChecker getTokenChecker() {
        return tokenChecker;
    }
}