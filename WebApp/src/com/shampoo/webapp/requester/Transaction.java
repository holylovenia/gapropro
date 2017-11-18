package com.shampoo.webapp.requester;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface Transaction {

    @WebMethod
    public String hideFromUser(String token, int transactionId);

    @WebMethod
    public String hideFromDriver(String token, int transactionId);

    @WebMethod
    public String getTransaction(String token, int transactionId);

    @WebMethod
    public String getVisibleUserTransactions(String token);

    @WebMethod
    public String getVisibleDriverTransactions(String token);
}
