package transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

/* 
 * Author: Fabio Fiorella
 * 
 * This class is used to manage "get sum" request. It provides the sum of the amount of the linked transactions 
 */
@Path ("/sum")
public class Sum {

		/*
		 * summary : Handle "get Sum" request.
		 * parm	   : long transaction_id - id of the requested transaction.
		 * return  : Json string - the requested informations {"amount" : long} or an error message. 
		 */
		@Path("{transaction_id}")
		@GET
		@Produces ("application/json")
		public Response getSum(@PathParam("transaction_id") long transaction_id){
						
			double sum = 0;
			
			JSONObject json_sum = new JSONObject();
			
			//call for the method that calculate the sum
			sum = TransactionStored.getSum(transaction_id);
			
			//check if the sum is different from -1. If the sum is -1 this means that the transactions doesn't exists
			if(sum != -1)
				json_sum.put("amount", sum);
			else 
				json_sum.put("Transaction not found", "It was impossible to find the specified transaction");
			
						
			return Response.status(200).entity(json_sum.toString()).build();
			
		}
}
