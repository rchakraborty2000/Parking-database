package EmployeeSuperclass;

import java.sql.Connection;

//import java.sql.connection;

import java.sql.PreparedStatement;

import java.sql.Timestamp;

public class Logout {
	
	public Logout (String ID,String type) throws Exception {
		System.out.println("You are sucessfully logged out");
		LoginScreen.main(null);
 
		if (type.equalsIgnoreCase("customer")) {
			Connection conn = LoginScreen.Connect();
			try {
				String updatelogout = "Update Member_ SET logout_time = ? where customer_ID = ?";
				PreparedStatement updateStatement = conn.prepareStatement(updatelogout);
				updateStatement.setTimestamp (1 , new Timestamp(System.currentTimeMillis()));
				updateStatement.setString(2, ID);
				updateStatement.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		//trigger from database
		//For Non Member pass their guest login or customer ID so I can remove them from the database?
	}
}
