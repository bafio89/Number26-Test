package transaction;

/*
 * Author: Fabio Fiorella
 * 
 *  This class represent a transaction, with all his informations. There are four basic method that allow
 *  access to private variable. 
 */
public class TransactionObj {
	
	private long id;
	private double amount;
	private String type;
	private long parentId;
	
	TransactionObj(){
		
	}
	
	TransactionObj(long newId, double newAmount, String newType, long newParentId){
		
		id        = newId;
		amount    = newAmount;
		type      = newType;
		parentId = newParentId;
		
	}
	
	public long getId(){
		return this.id;
	}
	
	public double getAmount(){
		return this.amount;
	}
	
	public String getType(){
		return this.type;
	}
	
	public long getParentId(){
		return this.parentId;
	}
}
