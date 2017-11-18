package com.shampoo.webapp.requester;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface UserManagement {

    @WebMethod
    public String getCurrentUserData(String token);

    @WebMethod
    public String changeCurrentUserData(String token, String fullName, String phoneNumber, String profilePicture, int isDriver);
}
