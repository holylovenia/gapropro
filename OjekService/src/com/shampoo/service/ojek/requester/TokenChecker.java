package com.shampoo.service.ojek.requester;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface TokenChecker {

    @WebMethod
    public String checkToken(String access_token);
}
