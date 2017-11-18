package com.shampoo.service.ojek.responder;

import com.shampoo.service.ojek.DatabaseManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;

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
