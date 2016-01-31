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
	 * param   : long transaction_id - the id of the requested transaction
	 * return  : a Json string - { "amount":double,"type":string,"parent_id":long } or an error message
	 */
	@Path("{transaction_id}")
	@GET
	@Produces ("application/json")
	public Response getTransaction(@PathParam("transaction_id") long transaction_id ){

		JSONObject json_transaction = new JSONObject();
		
		TransactionObj transaction  = new TransactionObj();
		
		
		//get the transaction through the id
		transaction = TransactionStored.getTransaction(transaction_id);
		
		//check if the transaction with the specified id, really exist
		if(transaction != null){
		
			//prepare the json object
			json_transaction.put("amount", transaction.getAmount());
			json_transaction.put("type", transaction.getType());
			json_transaction.put("parent_id", transaction.getParentId());
		
		}else{
		
			json_transaction.put("Transaction not found", "It was impossible to find the specified transaction");
		
		}
				
		return Response.status(200).entity(json_transaction.toString()).build();
	} 
	
	/*
	 * summary : This method add a new transaction.
	 * param   : long transaction_id - id of the new transaction
	 * 			 String json_transaction_string - json string with the information of the new string
	 * 
	 * return  : json string - { "status": "ok" } or an error message.
	 */
	@Path("{transaction_id}")
	@PUT
	@Consumes ("application/json")
	@Produces ("application/json")
	public Response putTransaction(@PathParam("transaction_id") long transaction_id, String json_transaction_string){
		
		//default value is 0. If parent_id in the new transaction is null, parent_id in the new object transaction will be set to 0 
		long parent_id = 0;
		
		JSONObject result_json = new JSONObject();
		
		TransactionObj new_transaction = null;
				
		
		//construct a json object from the received string.
		JSONObject json_transaction = new JSONObject(json_transaction_string);
		
		if(transaction_id > 0){
					
			try{
				
				//check if the field parent_id exist. This line is included in the try{} block because 
				// the field parent_id even if exist could be of a wrong type or value. 
				if(json_transaction.has("parent_id")) 
						if(json_transaction.getLong("parent_id") >= 0)
							parent_id = json_transaction.getLong("parent_id");
						else
							throw new JSONException("");
			
				//create a new transaction object with the informations of the new transaction. 
				new_transaction = new TransactionObj( transaction_id , json_transaction.getDouble("amount"), json_transaction.getString("type") , parent_id);
						
				//store the new transaction and check if this is successful 
				if(TransactionStored.insertNewTransaction(new_transaction))
					result_json.put("Status", "ok");
				else
					result_json.put("Error", "impossible to add the transaction");
			
			}catch(JSONException e){
				
				// If one or more parameter are missing or of wrong type, an exception is generated. 
				result_json.put("Error", "One or more parameters missing or of wrong type");
						
			}
		}else{
			
			result_json.put("Error", "Wrong transaction id");
		
		}
				
		return  Response.status(200).entity(result_json.toString()).build();
	}
	
}
