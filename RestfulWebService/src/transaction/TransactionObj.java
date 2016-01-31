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
	private long parent_id;
	
	TransactionObj(){
		
	}
	
	TransactionObj(long new_id, double new_amount, String new_type, long new_parent_id){
		
		id        = new_id;
		amount    = new_amount;
		type      = new_type;
		parent_id = new_parent_id;
		
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
		return this.parent_id;
	}
}
