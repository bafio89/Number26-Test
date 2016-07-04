package transactionTestSuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/*
 * Author: Fabio Fiorella
 * 
 * This class test the RESTful web service when there are no transactions stored. The single test methods
 * try to: 
 *  - get a transaction not stored,
 *  - to get the sum of a transaction not stored,
 *  - to get the transactions of a type not stored. 
 */
public class GetWithNoTransactionsTest {

	/*
	 *  This method try to get a transaction not stored. If the right message is returned the test has
	 *  success.
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
		
		assertEquals("{\"Transaction not found\":\"It was impossible to find the specified transaction\"}" , response.getEntity(String.class));
		
	}
	
	/*
	 *  This method try to get the sum of a transaction not stored. 
	 *  If the right message is returned the test has success.
	 */
	@Test
	public void testGetSum(){
		
		Client client = Client.create();
		
		WebResource webResource = null;
		
		ClientResponse response = null;
		
		long transactionId = 1;
				
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/sum/" + transactionId );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		assertEquals("{\"Transaction not found\":\"It was impossible to find the specified transaction\"}" , response.getEntity(String.class));
	}
	
	/*
	 *  This method try to get the transactions of a type not stored. 
	 *  If the right message is returned the test has success.
	 */
	@Test 
	public void testGetType(){
		
		Client client = Client.create();
		
		WebResource webResource = null;
		
		ClientResponse response = null;
		
		String transactionId = "car";
				
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/type/" + transactionId );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			fail("Failed : HTTP error code : " + response.getStatus());
		}
		
		assertEquals("[]" , response.getEntity(String.class));
		
	}
}
