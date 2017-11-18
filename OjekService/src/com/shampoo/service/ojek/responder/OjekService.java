package com.shampoo.service.ojek.responder;

import com.shampoo.service.ojek.DatabaseManager;
import com.shampoo.service.ojek.model.CurrentUser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface OjekService {
    CurrentUser currentUser = new CurrentUser();
    DatabaseManager databaseManager = new DatabaseManager();
}
