package transaction;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;


/*
 * Author: Fabio Fiorella
 * 
 * This class manage the "type" request. It provides a list of all ids of the transactions belonging to the same
 * type.
 */
@Path ("/type")
public class Type {
	
	/*
	 * summary : This method handle the "type" request. 
	 * param   : String type - requested type.
	 * return  : json string - list of the transactions ids of the specified type [ long, long, .... ] 
	 */
	@Path("{type}")
	@GET
	@Produces ("application/json")
	public Response getType(@PathParam("type") String type){
		
		//a new json array is created with the returned list
		JSONArray jsonTransactions = new JSONArray(TransactionStored.getTypes(type));
			
		return Response.status(200).entity(jsonTransactions.toString()).build();		
				
	}

}
