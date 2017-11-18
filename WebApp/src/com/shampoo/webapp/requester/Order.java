package com.shampoo.webapp.requester;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface Order {

    @WebMethod
    public String order(String token, int driverId, String pickingPoint, String destination, String comment, int rating);

    @WebMethod
    public String getDrivers(String token, String preferredDriverName, String pickingPoint, String destination);
}
