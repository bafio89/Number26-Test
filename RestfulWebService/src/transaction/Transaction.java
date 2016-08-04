package transaction;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

/* 
 * Author: Fabio Fiorella
 * 
 * This class is used to manage "get transaction" and to "add transaction" request.
 */
@Path ("/transaction")
public class Transaction {

	/*
	 * Summary : This method is used to get a transaction through his id. The informations are returned in 
	 * 			 JSON form.
	 * param   : long transactionId - the id of the requested transaction
	 * return  : a Json string - { "amount":double,"type":string,"parentId":long } or an error message
	 */
	@Path("{transactionId}")
	@GET
	@Produces ("application/json")
	public Response getTransaction(@PathParam("transactionId") long transactionId ){

		JSONObject jsonTransaction = new JSONObject();
		
		TransactionObj transaction  = new TransactionObj();
		
		
		//get the transaction through the id
		transaction = TransactionStored.getTransaction(transactionId);
		
		//check if the transaction with the specified id, really exist
		if(transaction != null){
		
			//prepare the json object
			jsonTransaction.put("amount", transaction.getAmount());
			jsonTransaction.put("type", transaction.getType());
			jsonTransaction.put("parentId", transaction.getParentId());
		
		}else{
		
			jsonTransaction.put("Transaction not found", "It was impossible to find the specified transaction");
		
		}
				
		return Response.status(200).entity(jsonTransaction.toString()).build();
	} 
	
	/*
	 * summary : This method add a new transaction.
	 * param   : long transactionId - id of the new transaction
	 * 			 String jsonTransactionString - json string with the information of the new string
	 * 
	 * return  : json string - { "status": "ok" } or an error message.
	 */
	@Path("{transactionId}")
	@PUT
	@Consumes ("application/json")
	@Produces ("application/json")
	public Response putTransaction(@PathParam("transactionId") long transactionId, String jsonTransactionString){
		
//RIVEDI   default value is null. If parentId in the new transaction is null, parentId in the new object transaction will be set to 0 
		Long parentId = null;
		
		JSONObject resultJson = new JSONObject();
		
		TransactionObj newTransaction = null;
				
		
		//construct a json object from the received string.
		JSONObject jsonTransaction = new JSONObject(jsonTransactionString);
		
		if(transactionId > 0){
					
			try{
				
				//check if the field parentId exist. This line is included in the try{} block because 
				// the field parentId even if exist could be of a wrong type or value. 
				if(jsonTransaction.has("parentId")){ 
						if(jsonTransaction.getLong("parentId") >= 0){
							parentId = jsonTransaction.getLong("parentId");
						}else{
							throw new JSONException("");
						}
				}
				//create a new transaction object with the informations of the new transaction. 
				newTransaction = new TransactionObj( transactionId , jsonTransaction.getDouble("amount"), jsonTransaction.getString("type") , parentId);
						
				//store the new transaction and check if this is successful 
				if(TransactionStored.insertNewTransaction(newTransaction))
					resultJson.put("Status", "ok");
				else
					resultJson.put("Error", "impossible to add the transaction");
			
			}catch(JSONException e){
				
				// If one or more parameter are missing or of wrong type, an exception is generated. 
				resultJson.put("Error", "One or more parameters missing or of wrong type");
						
			}
		}else{
			
			resultJson.put("Error", "Wrong transaction id");
		
		}
				
		return  Response.status(200).entity(resultJson.toString()).build();
	}
	
}
