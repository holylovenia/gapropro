package com.shampoo.webapp.requester;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PreferredLocation {

    @WebMethod
    public String addPreferredLocation(String token, String location);

    @WebMethod
    public String editPreferredLocation(String token, String previouslocation, String newLocation);

    @WebMethod
    public String removePreferredLocation(String token, String location);

    @WebMethod
    public String getPreferredLocation(String token, String location);

    @WebMethod
    public String getUserPreferredLocations(String token);

}
