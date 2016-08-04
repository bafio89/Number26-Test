package transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import dbManager.DbManager;

/*
 *  Author: Fabio Fiorella
 * 
 *  This class is used to handle the storing of the transactions and to realize all the required
 *  operations. I choose to make this class static and to use two static HashMap to keep track 
 *   of the transactions (See the discussion of performance to have more informations). 
 *      
 */
public class TransactionStored {
	
	private static String TRANSACTION_TABLE = "transaction";
	
	private static int MAX_SIZE_TRANSACTION = 100;
	
	private static int MAX_SIZE_TYPE = 20;
	
	private static LinkedBlockingQueue<Long> trackingTransactionsTemporality = new LinkedBlockingQueue<Long>();
	
	private static LinkedBlockingQueue<String> trackingTypeTransactionsTemporality = new LinkedBlockingQueue<String>();
	
	private static LinkedBlockingQueue<Long> trackingTransactionsSonsTemporality = new LinkedBlockingQueue<Long>();
	
	// HashMap used to store all the transactions with their informations. The key is the id of the transaction
	private static HashMap<Long, TransactionObj> transactionsStored = new HashMap<Long, TransactionObj>();

	//HashMap used to collect all the id of the transactions with the same type. The key is the type and
	// the value is a linked list with the id of the transaction.	
	private static HashMap<String, LinkedList<Long>> transactionsByType = new HashMap<String, LinkedList<Long>>();

	//HashMap used to keep track of the son of each transaction parent.
	private static HashMap<Long, Long> transactionsSons = new HashMap<Long, Long>();
	/*
	 *  the constructor is private to make this class static.
	 */
	private TransactionStored(){
		
	}
	
	/*
	 * summary : Method that insert in the HashMap of the transactions the new transactions. 
	 * 			 It is also necessary to keep track of the type of the new transaction and consequently update the HashMap with the types.
	 * param   : TransactionObj newTransaction - the object of the new transaction.
	 * return  : boolean - indicating success or fail. 
	 */
	public static boolean insertNewTransaction(TransactionObj newTransaction){
	
		boolean result = false;
		
		DbManager transactionDb = new DbManager();
		
		String values = "";
		
		Long parentId = null;

		if(transactionsStored.size() > MAX_SIZE_TRANSACTION){
			
			transactionsStored.remove(trackingTransactionsTemporality.poll());
						
		}
		
		//insert the new transaction 		
		transactionsStored.put(newTransaction.getId(), newTransaction);
		
		try {
			trackingTransactionsTemporality.put(newTransaction.getId());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(transactionsByType.size() < MAX_SIZE_TYPE){
			
			transactionsByType.remove(trackingTypeTransactionsTemporality.poll());
						
		}
		
		//after the insertion it keep track of the type calling trackTypes
		result = trackTypes(newTransaction.getType(), newTransaction.getId());
		
		try {
			trackingTypeTransactionsTemporality.put(newTransaction.getType());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		if(newTransaction.getParentId() > 0 ){
			
			parentId = newTransaction.getParentId();
			
			if(transactionsSons.size() > MAX_SIZE_TYPE){
				
				transactionsSons.remove(trackingTransactionsSonsTemporality.poll());
			}
			
			trackTransactionsSons(parentId , newTransaction.getId());
			
			try {
				trackingTransactionsSonsTemporality.put(parentId);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		values += "(" + newTransaction.getId() + ", \"" + newTransaction.getType() + "\" ," + newTransaction.getAmount() + "," + parentId + ")";
		
		transactionDb.insert(TRANSACTION_TABLE, "", values);
		
		return result;
	}
	
	/*
	 * summary : Method that comes back the requested transaction
	 * param   : long id - id of the requested transaction
	 * return  : Transaction Obj - Object of the request transaction or null.  
	 */
	public static TransactionObj getTransaction(long id){
		
		TransactionObj transaction = null;
		
		DbManager transactionDb = new DbManager();
		
		ResultSet rs = null;
		
		ArrayList<String> whereClause = new ArrayList<String>();
		
		whereClause.add("id = " + id);
		
		if(transactionsStored.containsKey(id)){
			transaction = transactionsStored.get(id);
		}else{
			rs = transactionDb.select(TRANSACTION_TABLE, "*", whereClause);
			
			if(rs != null){
				try {					
					rs.next();					
					
					transaction = new TransactionObj(rs.getLong("id"), rs.getDouble("amount"), rs.getString("type"), rs.getLong("parent_id"));
					
					if(transactionsStored.size() > MAX_SIZE_TRANSACTION){
						
						transactionsStored.remove(trackingTransactionsTemporality.poll());
					}	
						
					transactionsStored.put(id, transaction);
					
					trackingTransactionsTemporality.put(id);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
				
		return transaction;
	}
	
	
	/* 
	 * summary : This method come back all the ids of the transactions of the requested type. To perform this task 
	 * 			 the method access to the HashMap that contain all the transactions ids grouped by type.
	 * param   : String type - type of the requested transaction.
	 * return  : LinkedList - a list with the ids of the transactions of the requested type. 
	 */
	public static LinkedList<Long> getTypes(String type){
		
		LinkedList<Long> transactionsIdListByType = new LinkedList<Long>();
		
		DbManager transactionDb = new DbManager();
		
		ResultSet rs = null;
		
		ArrayList<String> whereClause = new ArrayList<String>();
		whereClause.add("type = '" + type + "'");
		
		if(transactionsByType.containsKey(type)){
			transactionsIdListByType = transactionsByType.get(type);
		}else{
			rs  = transactionDb.select(TRANSACTION_TABLE, "id", whereClause);
			
			try {
				while(rs.next()){
					transactionsIdListByType.add(rs.getLong("id"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(transactionsIdListByType != null){
				
				if(transactionsByType.size() > MAX_SIZE_TYPE){
				
					transactionsByType.remove(trackingTypeTransactionsTemporality.poll());
				
				}
			
				transactionsByType.put(type, transactionsIdListByType);
			
				try {
					trackingTypeTransactionsTemporality.put(type);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return transactionsIdListByType;
		
	}
	
	
	/*
	 * summary : This method return the sum of the transaction linked by parentId. The son of the specified
	 * 			 transactions is founded in transactionsSons.
	 * param   : long id - id of the parent of the transaction. 
	 * return  : double  - sum of the amount of the transaction linked to the specified transaction. 
	 */	
	public static double getSum(long id){
		
		// The variable sum is the returned value. Is initialized to -1. If this value is returned,
		// it was impossible to find the transaction with the specified id.
		double sum = 0;
		
		boolean end = false;
		
		long parent_id = -1;
		
		TransactionObj transactionParent = null;
		
		DbManager transactionDb = new DbManager();
		
		ResultSet rs = null;
		
		ArrayList<String> whereClause = new ArrayList<String>();
		
		whereClause.add("id = " + id);
		
		while(!end){
		
			transactionParent = transactionsStored.get(id);
		
			if(transactionParent == null){
			
				rs = transactionDb.select(TRANSACTION_TABLE, "*", whereClause);
			
				if(rs != null){
				
					try {
						rs.next();
									
						sum += rs.getDouble("amount");
						
						id = rs.getLong("id");
						
						parent_id = rs.getLong("parent_id");
						
						if(transactionsSons.size() > MAX_SIZE_TRANSACTION){
						
							transactionsSons.remove(trackingTransactionsSonsTemporality.poll());
							
							trackTransactionsSons(id, parent_id);
							
						}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					end = true;
				}
				
				id = parent_id;
						
			}
		
		}
		
		//check if the request transaction really exist 
		if(transactionParent != null){
			
			//change the value of sum with the amount of the specified transaction
			sum = transactionParent.getAmount();
			
			//cycle until there are sons 
			while(transactionsSons.containsKey(id)){
								
				id = transactionsSons.get(id);
							
				sum += transactionsStored.get(id).getAmount();
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
		LinkedList<Long> transactionsIdBySingleType = new LinkedList<Long>();
		
		//check if already exists the key specified in the variable type.
		if(transactionsByType.get(type) != null){
			transactionsIdBySingleType = transactionsByType.get(type);
		}
		
		//add the id of the new transaction to the list of all transactions of the specified type. 
		result = transactionsIdBySingleType.add(id);
		
		if(result){
			//insert the couple key-value in the HashMap. If the key already exist, it will be update. 
			transactionsByType.put(type, transactionsIdBySingleType);
		}
				 
		return result;
					
	}
	
	/*
	 * summary : keep track of the son of a transaction. The relationship is stored in the HashMap
	 * 			 transactionsSons.
	 * param   : long parent - id of the parent transaction
	 * 			 long son - id of the son transaction
	 * return  : void. 
	 * 	 
	 */
	private static void trackTransactionsSons(long parent, long son){
				
		transactionsSons.put(parent, son);
		
	}
}
