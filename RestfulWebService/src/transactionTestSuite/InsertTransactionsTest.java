package transactionTestSuite;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/*
 * Author: Fabio Fiorella
 * 
 * This class try to insert new transactions. 
 */
public class InsertTransactionsTest {

	/*
	 * try to insert new transactions with correct informations.
	 */
	@Test
	public void testPutNewTransactions(){
		
		LinkedList<String> newTransactions = new LinkedList<String>();
		
		Iterator<String> it = null;
		
		Client client = Client.create();
		
		WebResource webResource = null;
		
		ClientResponse response = null;
		
		int i = 1;
		
		//create a list of new transactions
		newTransactions.add("{\"id\": \"1\",\"amount\": \"10000\",\"type\": \"car\" }");
		newTransactions.add("{\"id\": \"2\",\"amount\": \"5000\",\"type\": \"car\", \"parentId\": \"1\" }");
		newTransactions.add("{\"id\": \"3\",\"amount\": \"100\",\"type\": \"food\", \"parentId\": \"0\" }");
		newTransactions.add("{\"id\": \"4\",\"amount\": \"50\",\"type\": \"food\", \"parentId\": \"0\" }");
		newTransactions.add("{\"id\": \"5\",\"amount\": \"30000\",\"type\": \"house\", \"parentId\": \"0\" }");
		newTransactions.add("{\"id\": \"6\",\"amount\": \"50000\",\"type\": \"house\", \"parentId\": \"5\" }");
		newTransactions.add("{\"id\": \"7\",\"amount\": \"70000\",\"type\": \"house\", \"parentId\": \"6\" }");
		newTransactions.add("{\"id\": \"8\",\"amount\": \"40000\",\"type\": \"house\", \"parentId\": \"7\" }");
		newTransactions.add("{\"id\": \"9\",\"amount\": \"30\",\"type\": \"clothes\", \"parentId\": \"0\" }");
		newTransactions.add("{\"id\": \"10\",\"amount\": \"50\",\"type\": \"clothes\", \"parentId\": \"0\" }");
		newTransactions.add("{\"id\": \"11\",\"amount\": \"20\",\"type\": \"doctor\", \"parentId\": \"0\" }");
		
		it = newTransactions.iterator();
		
		//cycle for each new transactions in the list
		while(it.hasNext()){ 
			
			webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/transaction/" + i );
			
			response = webResource.accept(MediaType.APPLICATION_JSON)
									.type(MediaType.APPLICATION_JSON)
									.put(ClientResponse.class, it.next());
			
			if (response.getStatus() != 200) {
			
				fail("Failed : HTTP error code : " + response.getStatus());
				
			}
			//check if for each insertion the correct message is returned
			assertEquals("{\"Status\":\"ok\"}", response.getEntity(String.class));
			
			i++;
		}
			
	}
	
	/*
	 * Try to put new transactions with wrong or missing informations.
	 */
	@Test
	public void testPutNewWrongTransactions(){
		
		LinkedList<String> newTransactions = new LinkedList<String>();
		
		Iterator<String> it = null;
		
		Client client = Client.create();
		
		WebResource webResource = null;
		
		ClientResponse response = null;
		
		int i = 12;
		
		//create a list of new transactions with some errors in their informations
		newTransactions.add("{\"id\": \"12\",\"amount\": \"10000\" }");
		newTransactions.add("{\"id\": \"13\",\"type\": \"car\", \"parentId\": \"1\" }");
		newTransactions.add("{\"id\": \"14\",\"amount\": \"fifty\",\"type\": \"food\", \"parentId\": \"0\" }");
		newTransactions.add("{\"id\": \"15\",\"amount\": \"50\",\"type\": \"food\", \"parentId\": \"-1\" }");
		
		
		it = newTransactions.iterator();
						
		while(it.hasNext()){
			
			webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/transaction/" + i );
			
			response = webResource.accept(MediaType.APPLICATION_JSON)
									.type(MediaType.APPLICATION_JSON)
									.put(ClientResponse.class, it.next());
			
			if (response.getStatus() != 200) {
			
				fail("Failed : HTTP error code : " + response.getStatus());
				
			}
			
			//check if for each insertion the correct error message is returned
			assertEquals("{\"Error\":\"One or more parameters missing or of wrong type\"}", response.getEntity(String.class));
			
			i++;
		}
		
		
		//prepare a transaction with negative id. Because the error message is different from the previous one,
		//this case can't be in the cycle.
		String transaction = "{\"id\": \"-15\",\"amount\": \"50\",\"type\": \"food\", \"parentId\": \"0\" }";
			
		webResource = client.resource("http://localhost:8080/RestfulWebService/transactionservice/transaction/-15" );
		
		response = webResource.accept(MediaType.APPLICATION_JSON)
								.type(MediaType.APPLICATION_JSON)
								.put(ClientResponse.class, transaction);
		
		if (response.getStatus() != 200) {
		
			fail("Failed : HTTP error code : " + response.getStatus());
			
		}
		//check if for each insertion the correct error message is returned
		assertEquals("{\"Error\":\"Wrong transaction id\"}", response.getEntity(String.class));
		
	}
	

}
