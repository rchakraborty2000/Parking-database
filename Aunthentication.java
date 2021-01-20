package EmployeeSuperclass;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Random;

//extends UserMenu, EmployeeMenu

public class Aunthentication{
	String type;
	String id;
	String pwd;
	
	public Aunthentication (String type, String id, String pwd) {
		this.type = type;
		this.id = id;
		this.pwd = pwd;
	}
	
	
	
	//Make this return emPloyeeMenu
	public void authenticate() throws Exception {
		Connection conn = LoginScreen.Connect();
		BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
		try {
		//Connecting to database
		
		
		//Member Authentication
		if (type == "M") {
		/*try {
			//Preparing Query
			String query = "SELECT * FROM Member_ WHERE login_username = ? AND login_password = ?";
			PreparedStatement statement = conn.prepareStatement(query);
					statement.setString(1, id); 
					statement.setString(2, pwd);
					ResultSet res = statement.executeQuery();
					conn.commit();
					}
		catch(Exception e) {
			e.printStackTrace();
		}*/
		
		try {
					String query1 = "Select customer_ID FROM Member_ where login_username = ?";
					PreparedStatement pstmt = conn.prepareStatement(query1);
					pstmt.setString(1, id);
					ResultSet rset = pstmt.executeQuery();
					conn.commit();
					String customerID = null;
					if (rset.next()) {
						customerID = rset.getString(1);
					}
					
					if( rset.next()) {
						
						//updating login time for customer
						try {
							String updatelogin = "Update Customer SET login_time = ? where customer_ID= ?";
							PreparedStatement updateStatement = conn.prepareStatement(updatelogin);
							updateStatement.setTimestamp (1 , new Timestamp(System.currentTimeMillis()));
							updateStatement.setString(2, customerID);
							updateStatement.executeUpdate();
							conn.commit();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					UserMenu.UserMenuScreen(customerID,conn);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
					
					//Statement stmt = connection.createStatement();
					
					
						
						//System.out.println("Connected to Customer Menu");

					}
					else {
						System.out.println("User not found");
						//Allow for more choices later here
					}
					}
			catch (Exception ex) {
				ex.printStackTrace();
			}


		
		//Guest AUthentication
		if (type == "G") {
			
			//First check if Guest exists already 
			//If not rand generate customer ID
			Random rand = new Random();
	        String customertId = Integer.toString(rand.nextInt(1000000000));
	        boolean exists = true;
	        
	        //Making sure customerID not taken
	        while (exists) {
	        try {
	        	//Querying random customer ID from database to see if it returns a value
	        	//If it does the Id is already taken, so another customer Id is generated
	        	//Done until the generated customer Id is not in database
	        	String query = "Select customer_ID FROM Customer where customer_ID = ?";
	        	PreparedStatement pstmt = conn.prepareStatement(query);
	        	pstmt.setString(1, customertId);
	        	
	        	ResultSet rset = pstmt.executeQuery();
	        	conn.commit();
	        	if (rset.next()) {
	        		customertId = Integer.toString(rand.nextInt(1000000));
	        	}
	        	
	        	else {
	        		exists = false;
	        	}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        }
			String fName = null;
			String lName = null;
			String creditCardNum = null;
			String customerType = "non-member";
			String guestLogin = null;
			
			System.out.println("First Name: ");
			fName = reader.readLine();
			
			System.out.println("Last Name: ");
			lName = reader.readLine();
			
			System.out.println("Enter Guest Login alias: ");
			guestLogin = reader.readLine();
			
			System.out.println("Credit Card Number: ");
			creditCardNum = reader.readLine();
			
			try {
				String query = "INSERT INTO customer VALUES (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(query);
				pstmt.setString(1, fName);
				pstmt.setString(2, lName);
				pstmt.setString(3, customertId);
				pstmt.setString(4, customerType);
				pstmt.setString(5, creditCardNum);
				pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
				pstmt.setString(7, "");
				pstmt.executeUpdate();
				conn.commit();
				//Query Execute update code?
				String query1 = "INSERT INTO non_member VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt1 = conn.prepareStatement(query1);
				pstmt.setString(1, "");
				pstmt.setString(2, "");
				pstmt.setString(3, "");
				pstmt.setString(4, "");
				pstmt.setString(5, guestLogin);
				pstmt.setString(6, customertId);
				pstmt1.executeUpdate();
				conn.commit();

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				String query3 = "SELECT * FROM Non_Member WHERE guestLogin = ?";
				PreparedStatement statement = conn.prepareStatement(query3);
						statement.setString(1, guestLogin); //import this from somewhere not sure
						ResultSet res = statement.executeQuery();
						conn.commit();
						if( res.next()) {
							UserMenu.UserMenuScreen(customertId,conn);
							//customerMenu.requestUpdate();
							
							//super.UserMenu(id);
							//System.out.println("Connected to User Menu");

						}
						else {
							System.out.println("User not found");
							//Allow for more choices later here
						}
						}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			
		}
		
		
			if (type == "E") {
				try {
				String query = "SELECT * FROM employee_ WHERE employee_ID = ? AND employee_password = ?";
				PreparedStatement statement = conn.prepareStatement(query);
						statement.setString(1, id); //import this from somewhere not sure
						statement.setString(2, pwd);
						ResultSet res = statement.executeQuery();
						conn.commit();
						if( res.next()) {
							//EmployeeMenu employeeMenu = new EmployeeMenu ();
							EmployeeMenu.empMenu(id, pwd,conn);
							//System.out.println("Connected to EmployeeMenu");
						}
						else {
							System.out.println("User not found");
							//Allow for more choices later here
						}
						}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			
	}
			
		
}


