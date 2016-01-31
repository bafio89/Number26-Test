package transaction_test_suite;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/*
 * Author: Fabio Fiorella
 * 
 * This class test the RESTful web service when there are transactions stored. The single test methods
 * try to: 
 *  - get a transaction stored,
 *  - to get the sum of a transaction stored,
 *  - to get the transactions of a type stored. 
 */
public class GetWithTransactionStoredTest {

	/*
	 *  Try to get 2 transactions stored.
	 */
	@Test
	public void testGetTransaction() {
				
		Client client = Client.create();
		
		WebResource webResource = null;
		
		ClientResponse response = null;
		
		long transaction_id = 1;
				
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/transaction/" + transaction_id );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("{\"amount\":10000,\"parent_id\":0,\"type\":\"car\"}" , response.getEntity(String.class));
		
		
		transaction_id = 6;
		
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/transaction/" + transaction_id );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("{\"amount\":50000,\"parent_id\":5,\"type\":\"house\"}" , response.getEntity(String.class));
		
	}
	
	/*
	 * Try to get the sum of 2 transactions. The first without sons, the second with sons. 
	 */
	@Test
	public void testGetSum(){
		
		Client client = Client.create();
		
		WebResource webResource = null;
		
		ClientResponse response = null;
		
		long transaction_id = 3;
				
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/sum/" + transaction_id );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("{\"amount\":100}" , response.getEntity(String.class));
		
		
		transaction_id = 5;
		
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/sum/" + transaction_id );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("{\"amount\":190000}" , response.getEntity(String.class));
		
	}
	
	/*
	 * Try to get the list of transactions ids of 2 different types 
	 */
	@Test
	public void testGetType(){
		
		Client client = Client.create();
		
		WebResource webResource = null;
		
		ClientResponse response = null;
		
		String transaction_id = "food";
				
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/type/" + transaction_id );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("[3,4]" , response.getEntity(String.class));
		
		
		transaction_id = "house";
		
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/type/" + transaction_id );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("[5,6,7,8]" , response.getEntity(String.class));
		
	}

}
