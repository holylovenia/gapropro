package com.shampoo.webapp.model;

import com.shampoo.webapp.model.TransactionBean;

import java.util.ArrayList;

public class TransactionDriverBean {
    private ArrayList<TransactionBean> transactionDriver_Array;

    public TransactionDriverBean() {
        transactionDriver_Array = new ArrayList<>();
    }

    public ArrayList<TransactionBean> getTransactionDriver_Array() {
        return transactionDriver_Array;
    }

    public void setTransactionDriverArray (ArrayList<TransactionBean> transactionDriver_Array) {
        this.transactionDriver_Array = transactionDriver_Array;
    }
}
