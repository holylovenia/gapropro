package com.shampoo.service.ojek.requester;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface User {

    @WebMethod
    public String fetchUserDataFromToken(String access_token);

    @WebMethod
    public void changeUserData(int id, String fullName, String phoneNumber, String profilePicture, int isDriver) throws SQLException;

    @WebMethod
    public String fetchDriversData(String access_token);

    @WebMethod
    public String fetchUsersData(String access_token);
}
