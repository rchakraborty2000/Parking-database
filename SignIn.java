package EmployeeSuperclass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
//import java.sql.conn;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

public class SignIn {
	
	/*public static conn Connect (String url, String dbName, String dbPass) {
		try {
			conn conn = DriverManager.getconn(url, dbName, dbPass);
			return conn;
		}
		catch (SQLException e) {
			System.out.println("Could not Connect :(");
			return null;
		}
		
		
	}*/
	
	public SignIn () throws Exception {
		System.out.println("1. Customer Login");
		System.out.println("2. Employee Login");
		
		//String pwd = null ;
        //String id = null;
		BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
		String loginType = reader.readLine();
		Connection conn =  LoginScreen.Connect();
		
		switch (loginType) {
			case "1" :
				//Customer Login Screen
				System.out.println("1. Member Login");
				System.out.println("2. Guest Login");
				String choice2 = reader.readLine();
				switch (choice2) {
				
				case "1":
					//Member Login Screen
					
					String userName = null;
					String passWord = null;
					
					System.out.println("Enter UserName: ");
					//Getting userName
					userName = reader.readLine();
					
					System.out.println("Enter Password: ");
					//Getting PassWord
					passWord = reader.readLine();
					
					//***** Must change up Authentication Screen *****//
					//Now Authenticating Member
					Aunthentication newCustomerLogin = new Aunthentication("M", userName, passWord);
			        newCustomerLogin.authenticate();
			        
			        //Insert value of login_time to table*****
			        
					break;
				case "2" :
					//Guest Login
					
					String guestName = null;
					
					
					System.out.println("Enter Guest Name: ");
					//Getting userName
					guestName = reader.readLine();
					
					
					Aunthentication newGuestLogin = new Aunthentication("G", guestName, "");
					newGuestLogin.authenticate();
			        break;
				}
				break;
			case "2" :
				//Employee Login Screen
		        String pwd = null ;
		        String id = null;
		        
		        //Getting EMployee ID
				System.out.println("Enter EmployeeID:  ");
		        try {
					id = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		        
		        //Getting Employee Password
		        System.out.println("Enter Password: ");
		        try {
					pwd = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        //Authenticating Employee
		        Aunthentication newEmployeeLogin = new Aunthentication("E", id, pwd);
		        newEmployeeLogin.authenticate();
		        break;
		        
		}
	}
	}
