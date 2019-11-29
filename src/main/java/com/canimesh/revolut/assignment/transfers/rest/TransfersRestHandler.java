package com.canimesh.revolut.assignment.transfers.rest;

import com.canimesh.revolut.assignment.common.ErrorResponse;
import com.canimesh.revolut.assignment.exception.ApplicationRuntimeException;
import com.canimesh.revolut.assignment.transfers.rest.dto.TransferRequest;
import com.google.gson.Gson;

import spark.Route;
import spark.Spark;

public class TransfersRestHandler {

	private final MoneyTransfersRestController transfersRestController;
	
	 public TransfersRestHandler(MoneyTransfersRestController transfersRestController) {
		this.transfersRestController = transfersRestController;
	}

	public Route createTransferRoute() {
	        return (req, res) -> {
	            try {
	                TransferRequest transferRequest = new Gson().fromJson(req.body(), TransferRequest.class);
	                
	                String transferId = transfersRestController.transfer(transferRequest);
	                res.status(201);
	                res.header("location", "http://localhost:"+Spark.port()+"/transfers/"+transferId);
	                res.header("Content-Type", "application/json");
	                return "";
	            }
	            catch (ApplicationRuntimeException ex) {
					res.status(ex.getStatus());
					return new Gson().toJson(new ErrorResponse(ex.getMessage()));
				}
	            catch (Exception ex) {
	            	ex.printStackTrace();
					res.status(500);
					return new Gson().toJson(new ErrorResponse("Internal Server Error"));
				}

	        };
	    }

	    public Route getTransferDetails() {
	        return (req, res) -> {
	            String receiptId = req.params("transferId");
	            try {
	            	res.status(200);
	            	res.header("Content-Type", "application/json");
	                return new Gson().toJson(transfersRestController.get(receiptId));
	            }
	            catch (ApplicationRuntimeException ex) {
					res.status(ex.getStatus());
					return new Gson().toJson(new ErrorResponse(ex.getMessage()));
				}
	            catch (Exception ex) {
					res.status(500);
					return new Gson().toJson(new ErrorResponse("Internal Server Error"));
				}
	        };
	    }
}
