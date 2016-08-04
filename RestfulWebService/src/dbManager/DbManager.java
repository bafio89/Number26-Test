package dbManager;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;

public class DbManager {
		
	private static Connection connect(){
		
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		    conn = DriverManager.getConnection("jdbc:mysql://localhost/transaction_db?" +
		                                   "user=root");
		  
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public ResultSet select(String tableName, String fieldNames, ArrayList<String> whereClauseList){
		
		Connection conn = DbManager.connect();
		
		String query = "SELECT " + fieldNames + " from " + tableName;
		
		String whereClause = "";
		
		Statement stmt = null;
		
		ResultSet rs = null;
		
		if(conn != null){
			
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}							
		
			if(whereClauseList != null){
			
				whereClause = " where ";
			
				for(int i = 0; i < whereClauseList.size(); i++){
				
					if(i > 0){
						whereClause = whereClause + " AND ";
					}
				
					whereClause +=  whereClauseList.get(i);
				
				}
			}
			
			query +=  whereClause + ";";
			
			try {
				 rs = stmt.executeQuery(query) ;
				
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		return rs;
	}
	
	public void insert(String tableName, String columns, String values){
		
		Connection conn = DbManager.connect();
		
		String query = "INSERT INTO " + tableName;
		
		if(values != null){
			query +=  " " + columns; 
		}
		
		query += " VALUES" + values + ";";   
		
		Statement stmt = null;
		
		if(conn != null){
			try {
				stmt = conn.createStatement();
			
				stmt.execute(query);
				
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void delete(){
		// TODO
	}
	
	public void update(){
		// TODO
	}
	
	public static void main(String[] args){
		DbManager prova = new DbManager();
		
		Connection co = prova.connect();
		
		
	}

}
