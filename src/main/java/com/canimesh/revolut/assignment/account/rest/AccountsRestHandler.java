package com.canimesh.revolut.assignment.account.rest;

import com.canimesh.revolut.assignment.account.rest.dto.CreateAccountRequest;
import com.canimesh.revolut.assignment.common.ErrorResponse;
import com.canimesh.revolut.assignment.exception.ApplicationRuntimeException;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import spark.Route;
import spark.Spark;

@RequiredArgsConstructor
public class AccountsRestHandler {

	private final AccountsRestController accountsRestController;


	public Route createAccountRoute() {
		return (req, res) -> {
			try {
				CreateAccountRequest account = new Gson().fromJson(req.body(), CreateAccountRequest.class);
				String accountId = accountsRestController.create(account);
				res.status(201);
				res.header("Content-Type", "application/json");
				res.header("location", "http://localhost:"+Spark.port()+"/accounts/" + accountId);
				return "";
			} catch (ApplicationRuntimeException ex) {
				res.status(ex.getStatus());
				return new Gson().toJson(new ErrorResponse(ex.getMessage()));
			} catch (Exception ex) {
				res.status(500);
				return new Gson().toJson(new ErrorResponse("Internal Server Error"));
			}

		};
	}

	public Route getAccountRoute() {
		return (req, res) -> {
			String accountId = req.params("accountId");
			try {
				res.header("Content-Type", "application/json");
				return new Gson().toJson(accountsRestController.get(accountId));
			} catch (ApplicationRuntimeException ex) {
				res.status(ex.getStatus());
				return new Gson().toJson(new ErrorResponse(ex.getMessage()));

			} catch (Exception ex) {
				res.status(500);
				return new Gson().toJson(new ErrorResponse("Internal Server Error"));
			}
		};
	}

}
