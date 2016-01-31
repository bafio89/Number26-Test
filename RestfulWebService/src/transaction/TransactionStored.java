package transaction;

import java.util.HashMap;
import java.util.LinkedList;

/*
 *  Author: Fabio Fiorella
 * 
 *  This class is used to handle the storing of the transactions and to realize all the required
 *  operations. I choose to make this class static and to use two static HashMap to keep track 
 *   of the transactions (See the discussion of performance to have more informations). 
 *      
 */
public class TransactionStored {
	
	// HashMap used to store all the transactions with their informations. The key is the id of the transaction
	private static HashMap<Long, TransactionObj> transactions_stored = new HashMap<Long, TransactionObj>();

	//HashMap used to collect all the id of the transactions with the same type. The key is the type and
	// the value is a linked list with the id of the transaction.	
	private static HashMap<String, LinkedList<Long>> transactions_by_type = new HashMap<String, LinkedList<Long>>();

	//HashMap used to keep track of the son of each transaction parent.
	private static HashMap<Long, Long> transactions_sons = new HashMap<Long, Long>();
	/*
	 *  the constructor is private to make this class static.
	 */
	private TransactionStored(){
		
	}
	
	/*
	 * summary : Method that insert in the HashMap of the transactions the new transactions. 
	 * 			 It is also necessary to keep track of the type of the new transaction and consequently update the HashMap with the types.
	 * param   : TransactionObj new_transaction - the object of the new transaction.
	 * return  : boolean - indicating success or fail. 
	 */
	public static boolean insertNewTransaction(TransactionObj new_transaction){
		
		boolean result = false;

		//insert the new transaction 		
		transactions_stored.put(new_transaction.getId(), new_transaction);
			
		//after the insertion it keep track of the type calling trackTypes
		result = trackTypes(new_transaction.getType(), new_transaction.getId());		
		
		if(new_transaction.getParentId() > 0)
			trackTransactionsSons(new_transaction.getParentId() , new_transaction.getId());
						
		return result;
	}
	
	/*
	 * summary : Method that comes back the requested transaction
	 * param   : long id - id of the requested transaction
	 * return  : Transaction Obj - Object of the request transaction or null.  
	 */
	public static TransactionObj getTransaction(long id){
		
		return transactions_stored.get(id);
	}
	
	
	/* 
	 * summary : This method come back all the ids of the transactions of the requested type. To perform this task 
	 * 			 the method access to the HashMap that contain all the transactions ids grouped by type.
	 * param   : String type - type of the requested transaction.
	 * return  : LinkedList - a list with the ids of the transactions of the requested type. 
	 */
	public static LinkedList<Long> getTypes(String type){
		
		return transactions_by_type.get(type);
		
	}
	
	
	/*
	 * summary : This method return the sum of the transaction linked by parent_id. The son of the specified
	 * 			 transactions is founded in transactions_sons.
	 * param   : long id - id of the parent of the transaction. 
	 * return  : double  - sum of the amount of the transaction linked to the specified transaction. 
	 */	
	public static double getSum(long id){
		
		// The variable sum is the returned value. Is initialized to -1. If this value is returned,
		// it was impossible to find the transaction with the specified id.
		double sum = -1;
		
		TransactionObj transaction_parent = transactions_stored.get(id);
		
		//check if the request transaction really exist 
		if(transaction_parent != null){
			
			//change the value of sum with the amount of the specified transaction
			sum = transaction_parent.getAmount();
			
			//cycle until there are sons 
			while(transactions_sons.containsKey(id)){
								
				id = transactions_sons.get(id);
							
				sum += transactions_stored.get(id).getAmount();
			}			
			
		
		}
		
		return sum;
		
	}
	
	/*
	 * summary : Private method used to keep track of the types of the new transactions.
	 * param   : String type - type of the new transaction
	 * 			 long id - id of the new transaction
	 * return  : boolean - success or fail
	 */
	private static boolean trackTypes(String type, long id){
		
		//returned value. If is returned false something went wrong.
		boolean result = false;
		
		// list with the ids of all transactions of the specified type.
		LinkedList<Long> transactions_id_by_single_type = new LinkedList<Long>();
		
		//check if already exists the key specified in the variable type.
		if(transactions_by_type.get(type) != null){
			transactions_id_by_single_type = transactions_by_type.get(type);
		}
		
		//add the id of the new transaction to the list of all transactions of the specified type. 
		result = transactions_id_by_single_type.add(id);
		
		if(result)
			//insert the couple key-value in the HashMap. If the key already exist, it will be update. 
			transactions_by_type.put(type, transactions_id_by_single_type);
				 
		return result;
					
	}
	
	/*
	 * summary : keep track of the son of a transaction. The relationship is stored in the HashMap
	 * 			 transactions_sons.
	 * param   : long parent - id of the parent transaction
	 * 			 long son - id of the son transaction
	 * return  : void. 
	 * 	 
	 */
	private static void trackTransactionsSons(long parent, long son){
				
		transactions_sons.put(parent, son);
		
	}
}
