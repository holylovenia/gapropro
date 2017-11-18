package com.shampoo.webapp.model;

import com.shampoo.webapp.model.TransactionBean;

import java.util.ArrayList;

public class TransactionUserBean {
    private ArrayList<TransactionBean> transactionUser_Array;

    public TransactionUserBean() {
        transactionUser_Array = new ArrayList<>();
    }

    public ArrayList<TransactionBean> getTransactionUser_Array() {
        return transactionUser_Array;
    }

    public void setTransactionUserArray (ArrayList<TransactionBean> transactionUser_Array) {
        this.transactionUser_Array = transactionUser_Array;
    }
}