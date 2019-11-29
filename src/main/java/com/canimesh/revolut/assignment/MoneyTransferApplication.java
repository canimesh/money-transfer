package com.canimesh.revolut.assignment;

import com.canimesh.revolut.assignment.account.rest.AccountsRestHandler;
import com.canimesh.revolut.assignment.transfers.rest.TransfersRestHandler;

import spark.Spark;

public class MoneyTransferApplication {

    public static void main(String[] args) {
        
        AccountsRestHandler accountsRestHandler = DependencyInjector.instance().getAccountsRestHandler();
        TransfersRestHandler transfersRestHandler = DependencyInjector.instance().getTransfersRestHandler();
        Spark.staticFiles.location("/dist");
        Spark.port(8888);
        Spark.post("/accounts", accountsRestHandler.createAccountRoute());
        Spark.get("/accounts/:accountId", accountsRestHandler.getAccountRoute());
        
        Spark.get("/transfers/:transferId", transfersRestHandler.getTransferDetails());
        Spark.post("/transfers", transfersRestHandler.createTransferRoute());
        
        Spark.get("/swagger-ui", (req,res) -> {
        	res.redirect("index.html");
        	return null;
        });
        

    }
}