package transactionTestSuite;

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
		
		long transactionId = 1;
				
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/transaction/" + transactionId );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("{\"amount\":10000,\"parentId\":0,\"type\":\"car\"}" , response.getEntity(String.class));
		
		
		transactionId = 6;
		
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/transaction/" + transactionId );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("{\"amount\":50000,\"parentId\":5,\"type\":\"house\"}" , response.getEntity(String.class));
		
	}
	
	/*
	 * Try to get the sum of 2 transactions. The first without sons, the second with sons. 
	 */
	@Test
	public void testGetSum(){
		
		Client client = Client.create();
		
		WebResource webResource = null;
		
		ClientResponse response = null;
		
		long transactionId = 3;
				
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/sum/" + transactionId );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("{\"amount\":100}" , response.getEntity(String.class));
		
		
		transactionId = 5;
		
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/sum/" + transactionId );
		
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
		
		String transactionId = "food";
				
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/type/" + transactionId );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		//check for the right informations
		assertEquals("[3,4]" , response.getEntity(String.class));
		
		
		transactionId = "house";
		
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/type/" + transactionId );
		
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
