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
		 * parm	   : long transactionId - id of the requested transaction.
		 * return  : Json string - the requested informations {"amount" : long} or an error message. 
		 */
		@Path("{transactionId}")
		@GET
		@Produces ("application/json")
		public Response getSum(@PathParam("transactionId") long transactionId){
						
			double sum = 0;
			
			JSONObject jsonSum = new JSONObject();
			
			//call for the method that calculate the sum
			sum = TransactionStored.getSum(transactionId);
			
			//check if the sum is different from -1. If the sum is -1 this means that the transactions doesn't exists
			if(sum != -1)
				jsonSum.put("amount", sum);
			else 
				jsonSum.put("Transaction not found", "It was impossible to find the specified transaction");
			
						
			return Response.status(200).entity(jsonSum.toString()).build();
			
		}
}
