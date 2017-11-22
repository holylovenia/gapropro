package com.shampoo.service.identity.responder;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface TokenChecker {

    @WebMethod
    public String checkToken(String access_token);
}
