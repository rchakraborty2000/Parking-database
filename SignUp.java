package EmployeeSuperclass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.SQLException;
import java.util.Random;

public class SignUp {

	public SignUp () throws Exception {
		//Auto-generate Customer ID for New Member;
        Random rand = new Random();
		//String randCustomerId = Integer.toString(rand.nextInt());
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
		System.out.println("1. Customer SignUp");
		System.out.println("2. Employee SignUp");
		Connection conn = LoginScreen.Connect();
		
		String choice = reader.readLine();
		switch (choice) {
		case "1" : 
	        

			String customerId = Integer.toString(rand.nextInt(1000000000));
        
			String fName = null;
			String lName = null;
			String userName = null;
			String creditCardNum = null;
			String pass = null;
			String licensePlateNum = null;
			
			
			
			//Getting required Sign up Info
			System.out.println("Enter First Name: ");
			try {
				fName = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        System.out.println("Enter Last Name: ");
	        try {
				lName = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        System.out.println("Enter User Name: ");
	        try {
	        	userName = reader.readLine();
				//Check here if UserName is taken 
				
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        
	        System.out.println("Enter password: ");
	        try {
				pass = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("Enter Credit Card Number: ");
	        try {
				creditCardNum = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        System.out.println("Enter License Plate Number: ");
	        try {
	        	licensePlateNum = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        //Adding values to Customer;
	        
	        //Connecting to database
	        
	        //Preparing query
	        //Adding Credentials to Customer Table
	        String query = "INSERT INTO customer VALUES (?, ?, ?, ?, ?, ?, ?)";
	        try {
	        	PreparedStatement pstmt = conn.prepareStatement(query); {
	        		pstmt.setString(1, fName);
	        		pstmt.setString(2, lName);
	        		pstmt.setString(3, customerId);
	        		pstmt.setString(4, "member");
	        		pstmt.setString(5, creditCardNum);
	        		pstmt.setString(6, "");
	        		pstmt.setString(7, "");
	                System.out.println("Updated");
					pstmt.executeUpdate();
					conn.commit();
	        	}
	        }
	        catch (Exception e) {
	        	
	        }
	        
	        //Adding Credentials to Member table
	        String query2 = "INSERT INTO member_ VALUES (?, ?, ?, ?, ?, ?)";
	        try {
	        	PreparedStatement pstmt = conn.prepareStatement(query2); {
	        		pstmt.setString(1, licensePlateNum);
	        		pstmt.setString(2, "");
	        		pstmt.setString(3, "");
	        		pstmt.setString(4, customerId);
	        		pstmt.setString(5, userName);
	        		pstmt.setString(6, pass);
	                System.out.println("Updated");
					pstmt.executeUpdate();
					conn.commit();
	        	}
	        	
	        }
	        catch (Exception e) {
	        	
	        } break;
		case "2" : 
	        int employeeIDint=rand.nextInt(1000000000);
	        String employeeID = Integer.toString(employeeIDint);
	        
			String fName1 = null;
			String lName1 = null;
			String ssn = null;
			String salary = null;
			String payType = null;
			String employeType = null;
			String employeePassword = null;
			
			
			//Getting required Sign up Info
			System.out.println("Enter First Name: ");
			try {
				fName1 = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        System.out.println("Enter Last Name: ");
	        try {
	        	lName1 = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        System.out.println("Enter SSN: ");
	        try {
	        	ssn = reader.readLine();
				//Check here if UserName is taken 
				
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        
	        System.out.println("Enter Salary: ");
	        try {
				salary = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println("Enter Pay Type: ");
	        try {
	        	payType = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        System.out.println("Enter Employee Type (Staff or Admin): ");
	        try {
	        	employeType = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        System.out.println("Enter Password: ");
	        try {
	        	employeePassword = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        
	        //Adding values to Customer;
	        
	        //Connecting to database
	        
	        //Preparing query
	        //Adding Credentials to Customer Table
	        String query1 = "INSERT INTO employee_ VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        try {
	        	PreparedStatement pstmt = conn.prepareStatement(query1); {
	        		pstmt.setString(1, employeeID);
	        		pstmt.setString(2, fName1);
	        		pstmt.setString(3, lName1);
	        		pstmt.setString(4, ssn);
	        		pstmt.setString(5, salary);
	        		pstmt.setString(6, payType);
	        		pstmt.setString(7, employeType);
	        		pstmt.setString(8, employeePassword);
					pstmt.executeUpdate();
					conn.commit();
	        	}
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        }break;
		}
	
	
	        //Going back to sign in Screen so new Member can sign in
	        System.out.println("You are Signed up!, Now you can Sign In");
	        SignIn signin = new SignIn();
	        
		}
	
	}