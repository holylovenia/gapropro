package com.shampoo.webapp.requester;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class OrderClient {
    private Order order;

    public OrderClient() throws MalformedURLException, SQLException {
        URL url = null;
        try {
            String wsdlLocation = "http://localhost:9003/services/Order?wsdl";
            url = new URL(wsdlLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String name = "OrderImpl";
        String targetNamespace = "http://responder.ojek.service.shampoo.com/";
        QName serverQName = new QName(targetNamespace, name + "Service");
        Service service = Service.create(url, serverQName);
        String endpointAddress = "http://localhost:9003/services/Order";
        service.addPort(serverQName, javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING, endpointAddress);
        QName serviceName = new QName(targetNamespace, name + "Port");
        order = service.getPort(serviceName, Order.class);
    }

    public Order getOrder() {
        return order;
    }
}
