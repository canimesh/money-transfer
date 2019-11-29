package com.canimesh.revolut.assignment.transfers.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.canimesh.revolut.assignment.account.rest.dto.AccountInfoResponse;
import com.canimesh.revolut.assignment.account.rest.dto.CreateAccountRequest;
import com.canimesh.revolut.assignment.transfers.rest.dto.TransferDetailsResponse;
import com.canimesh.revolut.assignment.transfers.rest.dto.TransferRequest;
import com.google.gson.Gson;

import io.restassured.response.Response;



public class TransfersApiOperationTest {
	
	private static final String HTTP_LOCALHOST_8888 = "http://localhost:8888";

	@Test
    public void test_transfer_money_successful() {
        String fromAccountId = createAccount(new CreateAccountRequest(99, "FromAnimesh"));
        String toAccountId = createAccount(new CreateAccountRequest(1, "ToAnimesh"));

        String transferId = transferMoney(new TransferRequest(fromAccountId, toAccountId, 29));
        
        assertEquals(70, getAccount(fromAccountId).getAmount());
        assertEquals(30, getAccount(toAccountId).getAmount());

        transferMoney(new TransferRequest(fromAccountId, toAccountId, 70));

        assertEquals(0, getAccount(fromAccountId).getAmount());
        assertEquals(100, getAccount(toAccountId).getAmount());
    }
	
	

    private String transferMoney(TransferRequest transferRequest) {
    	Response response = executePostForTransfer(transferRequest);
    	return response.getHeader("location").replace("http://localhost:8888/transfers/", "");
	}



	private Response executePostForTransfer(TransferRequest transferRequest) {
		Response response = given().baseUri(HTTP_LOCALHOST_8888).when().body(transferRequest).post("/transfers", Collections.emptyMap()).andReturn();
		return response;
	}



	private String createAccount(CreateAccountRequest createAccountRequest) {
		Response response = given().baseUri(HTTP_LOCALHOST_8888).when().body(createAccountRequest).post("/accounts", Collections.emptyMap());
		return response.getHeader("location").replaceAll("http://localhost:8888/accounts/", "");
	}
	
	private AccountInfoResponse getAccount(String accountId) {
		Response response = given().baseUri(HTTP_LOCALHOST_8888).when().get("/accounts/"+accountId, Collections.emptyMap());
		return new Gson().fromJson(response.getBody().asString(),AccountInfoResponse.class);
	}
	
	private TransferDetailsResponse getTransferDetails(String transferId) {
		Response response = executeGetTransfer(transferId);
		return new Gson().fromJson(response.getBody().asString(),TransferDetailsResponse.class);
	}



	private Response executeGetTransfer(String transferId) {
		Response response = given().baseUri(HTTP_LOCALHOST_8888).when().get("/transfers/"+transferId, Collections.emptyMap());
		return response;
	}



	@Test
    public void test_retrieve_money_transfer_details_success() {
		 String fromAccountId = createAccount(new CreateAccountRequest(99, "FromAnimesh"));
	     String toAccountId = createAccount(new CreateAccountRequest(1, "ToAnimesh"));

	     String transferId = transferMoney(new TransferRequest(fromAccountId, toAccountId, 29));
	     
	     TransferDetailsResponse transferDetails = getTransferDetails(transferId);
	     System.out.println(new Gson().toJson(transferDetails));
	     
        assertEquals(fromAccountId, transferDetails.getFromAccount());
        assertEquals(toAccountId, transferDetails.getToAccount());
        assertEquals(29, transferDetails.getAmount());
        assertEquals(transferId, transferDetails.getId());
    }


    @Test
    public void test_404_not_found_for_wrong_transferid() {
        Response response = executeGetTransfer(UUID.randomUUID().toString());
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_status_failed_insufficient_balance() {
    	String fromAccountId = createAccount(new CreateAccountRequest(99, "FromAnimesh"));
	     String toAccountId = createAccount(new CreateAccountRequest(1, "ToAnimesh"));

	    String transferId = transferMoney(new TransferRequest(fromAccountId, toAccountId, 100));
	    TransferDetailsResponse transferDetails = getTransferDetails(transferId);
	    assertTrue(transferDetails.getStatusChanges().stream().anyMatch(s-> s.getStatus().equalsIgnoreCase("FAILED")));
        
        
    }
    
    @Test
    public void test_bad_request_if_from_account_not_specified() {
    	String fromAccountId = createAccount(new CreateAccountRequest(1, "FromAnimesh"));

        Response response = executePostForTransfer(new TransferRequest(fromAccountId,null, 500));
        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode());
    }
    
    @Test
    public void test_bad_request_if_to_account_not_specified() {
    	String toAccountId = createAccount(new CreateAccountRequest(1, "ToAnimesh"));

        Response response = executePostForTransfer(new TransferRequest(null, toAccountId, 500));
        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode());
    }
    
    @Test
    public void test_bad_request_zero_transfer() {
    	String fromAccountId = createAccount(new CreateAccountRequest(99, "FromAnimesh"));
	     String toAccountId = createAccount(new CreateAccountRequest(1, "ToAnimesh"));
	     Response response = executePostForTransfer(new TransferRequest(fromAccountId, toAccountId, 0));
	        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode());
    }
    
    @Test
    public void test_bad_request_negative_transfer() {
    	String fromAccountId = createAccount(new CreateAccountRequest(99, "FromAnimesh"));
	     String toAccountId = createAccount(new CreateAccountRequest(1, "ToAnimesh"));
	     Response response = executePostForTransfer(new TransferRequest(fromAccountId, toAccountId, -500));
	        assertEquals(HttpStatus.SC_BAD_REQUEST, response.statusCode());
    }
    
    @Test
    public void test_status_failed_account_not_exists() {
    	String fromAccountId = createAccount(new CreateAccountRequest(99, "FromAnimesh"));
	     String toAccountId = UUID.randomUUID().toString();

	    String transferId = transferMoney(new TransferRequest(fromAccountId, toAccountId, 100));
	    TransferDetailsResponse transferDetails = getTransferDetails(transferId);
	    assertTrue(transferDetails.getStatusChanges().stream().anyMatch(s-> s.getStatus().equalsIgnoreCase("FAILED")));
    }
}
