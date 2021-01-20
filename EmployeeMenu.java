package EmployeeSuperclass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Scanner;

public class EmployeeMenu {
	/*public String employee_name;
	public String emp_ID;
	public String SSN;
	public String pay_type;
	public String emp_type;*/
	public static Scanner sc = new Scanner(System.in);
	/*public static Connection connection() throws Exception{
		//make database connection
		String url = "jdbc:postgresql://localhost:5432/parkinglotmanagement";
	    String username= "postgres";
	    String passwd="5432";
	    Connection conn=null;
	    try {
	    Class.forName("org.postgresql.Driver");
	    conn = DriverManager.getConnection(url, username, passwd);
	    conn.setAutoCommit(false);
	    }
	    catch (Exception se) {
	    	System.out.println("Could not create connection"+" "+se);
	    }
	    return conn;
	}*/
	
	//employee menu
	public static void empMenu(String empID,String empPassword,Connection conn) throws Exception{
		//Scanner scan=new Scanner(System.in);
		boolean checker=false;
		//while (checker==false) {
		//System.out.println("Please enter your Employee ID:");
		//String ID_entered= scan.next();//asks employee for their id
		//checking if employee exists
		
		try {
		PreparedStatement prp= conn.prepareStatement("select employee_type from employee_ where employee_ID=? and employee_password=?");
		prp.setString(1,empID);
		prp.setString(2,empPassword);
		ResultSet rs=prp.executeQuery();
		conn.commit();
		/*PreparedStatement stmt=conn.prepareStatement("create user ? identified by ?");
		stmt.setString(1,empID);
		stmt.setString(2,empPassword);
		stmt.executeQuery();
		conn.commit();*/
		//Check employee type and grant role
		while (rs.next()) {
			if(rs.getString(1)==null) {
				System.out.println("Employee does not exist or incorrect ID and password entered");
			    checker=false;}
			else if((rs.getString(1)).equalsIgnoreCase("Staff")) {
				//PreparedStatement granting=conn.prepareStatement("grant staff to ?");
				//granting.setString(1,empID);
				//granting.executeUpdate();
				//conn.commit();
				menuStaff(conn);
				checker=true;}
			else if((rs.getString(1)).equalsIgnoreCase("Admin")) {
				/*PreparedStatement granting=conn.prepareStatement("grant role admin_ to user ?");
				granting.setString(1,empID);
				granting.executeUpdate();
				conn.commit();*/
				menuAdmin(conn);
				checker=true;}
		}//while for menu and employee type check
		//}//while to ensure employee input
		} catch (Exception e) {
			e.printStackTrace();
		}
		//scan.close();
		Logout lo= new Logout(empID,"Employee");
	}
	
	
	//------^^------
	
	
	@SuppressWarnings("unused")
	public static void menuStaff(Connection conn) throws Exception{
		boolean tracker=false;
		Random rand=new Random();
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 

	    while (tracker==false) {
	    	//options
		System.out.println("Enter the number for the menu option that you would like to choose:");
		System.out.println("1 - Make drive in reservation for customer");
		System.out.println("2 - Find customer information");
		System.out.println("3 - Update customer information");
		System.out.println("4 - Exit menu");
		
		//Scanner sc=new Scanner(System.in);
		int option=Integer.parseInt(reader.readLine());
		switch (option) {
		case 1:
			//make drive in reservation
			System.out.println("Enter if they are member or non-member or new customer:");
			String ans1=reader.readLine();
			if (ans1.equalsIgnoreCase("Member")) {
				System.out.println("Please enter customer id:");
				String cid=reader.readLine();
				try {
				PreparedStatement cust_ID=conn.prepareStatement("select customer_type from Customer where customer_ID=?");
				cust_ID.setString(1,cid);
				ResultSet rs=cust_ID.executeQuery();
				while(rs.next()) {
					if (rs.getString(1).equalsIgnoreCase("member")) {
						//let them create drive in order
						//info entering
						String orderType= "drive in";
						System.out.println("Enter the begin time for reservation:(in format MM-DD-YYYY HH-MM-SS");
						String temp=reader.readLine();
						Timestamp timeStart= Timestamp.valueOf(temp);
						System.out.println("Enter the end time for reservation:(in format MM-DD-YYYY HH-MM-SS");
						temp=reader.readLine();
						Timestamp timeEnd= Timestamp.valueOf(temp);
						System.out.println("Enter your member car plate number:");
						String carPlate=reader.readLine();
						System.out.println("Enter temp plate number if the car is not the member plate else enter null:");
						String tempPlate=reader.readLine();
						System.out.println("Enter your location ID:");
						String location=reader.readLine();
						//shows empty spots in that location       -------------------------------------------------
						System.out.println("Enter the spot number chosen:");
						String spot=reader.readLine();
						int resID=rand.nextInt(999999999);
						//adding the reservation
						PreparedStatement reservationMember=conn.prepareStatement("insert into reservation values (?,?,?,?,?,?,?,?,?)");
						reservationMember.setString(1,orderType);
						reservationMember.setTimestamp(2,timeStart);
						reservationMember.setTimestamp(3,timeEnd);
						reservationMember.setString(4,carPlate);
						reservationMember.setString(5,tempPlate);
						reservationMember.setString(6,location);
						reservationMember.setString(7,spot);
						reservationMember.setInt(8,resID);
						reservationMember.setString(9,cid);
						reservationMember.executeUpdate();
						conn.commit();
					}  
					else {
						System.out.println("Customer is not a member.");
					}//finishing if else for making sure they are member
				}
				}catch (Exception e) {
					e.printStackTrace();
				}//finish the reading of result set
			}//ending if they are member
			else if(ans1.equalsIgnoreCase("Non-member")){
				String orderType= "drive in";
				System.out.println("Enter the begin time for reservation:(in format MM-DD-YYYY HH-MM-SS");
				String temp=reader.readLine();
				Timestamp timeStart= Timestamp.valueOf(temp);
				System.out.println("Enter the end time for reservation:(in format MM-DD-YYYY HH-MM-SS");
				temp=reader.readLine();
				Timestamp timeEnd= Timestamp.valueOf(temp);
				System.out.println("Enter your car plate number:");
				String carPlate=reader.readLine();
				//what to do with temp plate            
				//String tempPlate=reader.readLine();
				String tempPlate=null;
				System.out.println("Enter your location ID:");
				String location=reader.readLine();
				//shows empty spots in that location     ------------------------------------------------------
				System.out.println("Enter the spot number chosen:");
				String spot=reader.readLine();
				int resID=rand.nextInt(1000000000);
				int custID=rand.nextInt(1000000000);
				String customerID=Integer.toString(custID);
				//create the reservation
				try {
				PreparedStatement reservationNew=conn.prepareStatement("insert into reservation values (?,?,?,?,?,?,?,?,?)");
				reservationNew.setString(1,orderType);
				reservationNew.setTimestamp(2,timeStart);
				reservationNew.setTimestamp(3,timeEnd);
				reservationNew.setString(4,carPlate);
				reservationNew.setString(5,tempPlate);
				reservationNew.setString(6,location); 
				reservationNew.setString(7,spot);
				reservationNew.setInt(8,resID);
				reservationNew.setString(9,customerID);
				reservationNew.executeUpdate();
				conn.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}//else if they are non member and either new or not new
			break;
		case 2:
			//check variable
			//int chk=0;
			//ask for customer id
			System.out.println("Please enter customer ID:");
			String ciD=reader.readLine();
			try {
			
			PreparedStatement checkID=conn.prepareStatement("select count(customer_ID) from Customer where customer_ID=?");
			checkID.setString(1,ciD);
			ResultSet IDexist=checkID.executeQuery();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			//check customer exists
			//while(IDexist.next()) {
				//String temp=IDexist.getString(1);
				//chk=Integer.valueOf(temp);
			//}
			//if (chk==0) {//customer does not exist
				//System.out.println("Either customer does not exist or customer was never a member");
			//}
			//else if (chk==1) {//customer info can be displayed
			try {
				PreparedStatement display=conn.prepareStatement("select first_name,last_name,customer_ID,customer_type,reservation_number,time_start,time_end from Customer natural join Member_ natural join Non_member natural join reservation where customer_ID=?");
				display.setString(1,ciD);
				ResultSet disp=display.executeQuery();
				while(disp.next()) {
					System.out.println("The first name is: "+disp.getString(1)); //------------------------------------is it always get String(for result set) and set string(for prep statement) no matter the data type
					System.out.println("The last name is: "+disp.getString(2));
					System.out.println("The customer ID is: "+disp.getInt(3));
					System.out.println("The customer is a: "+disp.getString(4));
					System.out.println("The reservation number is: "+disp.getInt(5));
					System.out.println("The start time is: "+disp.getString(6));
					System.out.println("The end time is: "+disp.getString(7));
				}//displayed in rows
			//}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		case 3:
			//update customer(member) information
			//check variable
			int chk2=0;
			//ask for customer id
			System.out.println("Please enter customer ID:");
			String cust=reader.readLine();
			try {
			PreparedStatement check=conn.prepareStatement("select count(customer_ID) from Customer where customer_ID=?");
			check.setString(1,cust);
			ResultSet IdExist=check.executeQuery();
			//check customer exists
			while(IdExist.next()) {
				String temp=IdExist.getString(1);
				chk2=Integer.valueOf(temp);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
			if (chk2==0) {//customer does not exist
				System.out.println("Customer does not exist.");
			}
			else if (chk2==1) {//customer exists
				//staff can only update member table:car_plate_number,temp_plate_number,temp_card.
				System.out.println("What would you like to update?(Enter the option number)");
				System.out.println("1- Update car plate number");
				System.out.println("2- Update temporary car plate number");
				System.out.println("3- Update temporary credit card number");
				int op=Integer.parseInt(reader.readLine());
				switch(op) {
				case(1)://updates car plate
					System.out.println("Enter the new car plate number:");
					String newPlate=reader.readLine();
					try {
					PreparedStatement update=conn.prepareStatement("update Member_ set car_plate_number=? where customer_ID=?");
					update.setString(1,newPlate);
					update.setString(2,cust);
					update.executeUpdate();
					conn.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("UPDATE SUCCESSFUL!");
					break;
				case(2)://updates temporary plate
					System.out.println("Enter the new temporary plate number:");
					String newTempPlate=reader.readLine();
					
					try {
					PreparedStatement updateTemp=conn.prepareStatement("update Member_ set temp_plate_number=? where customer_ID=?");
					updateTemp.setString(1,newTempPlate);
					updateTemp.setString(2,cust);
					updateTemp.executeUpdate();
					conn.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("UPDATE SUCCESSFUL!");
					break;
				case(3)://updates temporary card
					System.out.println("Enter the new temporary credit card number:");
					String newCard=reader.readLine();
					try {
					PreparedStatement updateCard=conn.prepareStatement("update Member_ set temp_card=? where customer_ID=?");
					updateCard.setString(1,newCard);
					updateCard.setString(2,cust);
					updateCard.executeUpdate();
					conn.commit();}
					catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("UPDATE SUCCESSFUL!");
				} 
			}
			break;
		case 4:
			//exiting menu
			tracker=true;
			break;
		}//end of switch case
		
		if (tracker=true)//If they chose option to exit menu
			break;//breaks out of menu while
		else {
			System.out.println("Do you want to enter another option?");
			Scanner sc2=new Scanner(System.in);
			String ans=sc2.nextLine();
			if (ans.equalsIgnoreCase("Yes"))
				tracker=false;//menu while continues
			else if (ans.equalsIgnoreCase("No"))
				tracker=true;//menu while stops
			sc2.close();
			}
		sc.close();
	    }//end of menu while
	    System.out.println("Exited menu");
	}
	
	
	//--------^^--------
	
	
	@SuppressWarnings("unused")
	public static void menuAdmin(Connection conn) throws Exception{
		boolean tracker=false;
		Random rand=new Random();
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
	    while (tracker==false) {
	    	//options
		System.out.println("Enter the number for the menu option that you would like to choose:");
		System.out.println("1 - Make reservation for customer");
		System.out.println("2 - Find customer information");
		System.out.println("3 - Update customer information");
		System.out.println("4 - Update membership status");
		System.out.println("5 - View report for monthly revenue");
		System.out.println("6 - View report for parking usage");
		System.out.println("7 - View report for login and logout times for customers");
		System.out.println("8 - View report for available spots");
		System.out.println("9 - Exit Menu");
		//create user and grant role
		//Scanner sc=new Scanner(System.in);
		int option=Integer.parseInt(reader.readLine());
		switch (option) {
		case 1:
			//make reservation
			Random reservationNumber = new Random(); 
	        //System.out.println("\nReservation");
	        System.out.print("Enter type of customer (online, member): ");
	        String orderType = reader.readLine();
	        String templicensePlate = null;
	        String carplate = null;
	        try {
	        //Connection connection = connection();
	            //PreparedStatement pst = connection.prepareStatement("SELECT * FROM Customer WHERE Customer.customer_ID = ?");
	            //ResultSet rset = pst.executeQuery();
	            if (orderType.equalsIgnoreCase("online") || orderType.equalsIgnoreCase("member")) {
	                if (orderType.equals("member")) {
	                	System.out.println("Please enter the customer ID:");
	                	String customerID=reader.readLine();
	                	PreparedStatement pst = conn.prepareStatement("SELECT * FROM Customer WHERE Customer.customer_ID = ?");
	                	pst.setString(1,customerID);
	    	            ResultSet rset = pst.executeQuery();
	    	            //check custid
	                    System.out.print("Enter reservation start time (yyyy-MM-dd hh:mm:ss): ");
	                    String TimeStartString = reader.readLine();
	                    Timestamp TimeStart = Timestamp.valueOf(TimeStartString);
	                   
	                    System.out.print("Enter time out (format yyyy-MM-dd hh:mm:ss): ");
	                    String TimeEndString = reader.readLine();
	                    Timestamp TimeEnd = Timestamp.valueOf(TimeEndString);

	                    System.out.print("Choose license plate type (type in 'registered' or 'temporary'");
	                    String licensePlateType = reader.readLine();
	                    if(licensePlateType.equalsIgnoreCase("registered")) {
	                    PreparedStatement pst2 = conn.prepareStatement("SELECT car_plate_number FROM Member_ WHERE customer_ID = ? ");
	                    pst2.setString(1,customerID);
	                    ResultSet rset2 = pst2.executeQuery();
	                    carplate = rset2.getString(1);
	                    }
	                   
	                    else if (licensePlateType.equalsIgnoreCase("temporary")) {
	                    System.out.print("Enter your temporary license plate: ");
	                        templicensePlate = reader.readLine();
	                    }
	                    Statement locationView = conn.createStatement();
	                    ResultSet locRset = locationView.executeQuery("SELECT * FROM vacantLots");
	                    while(locRset.next()){
	                    System.out.println("Location: "+locRset.getString(1)+" Number of open spots: "+(locRset.getString(2)));
	                    }
	                   
	                    System.out.println("Enter location to reserve: ");
	                    String locationtoreserve = reader.readLine();
	                    PreparedStatement spotsView = conn.prepareStatement("EXEC checkVacantSpots(?,?)");
	                    
	                    spotsView.setTimestamp(1, TimeStart);
	                    spotsView.setTimestamp(2, TimeEnd);
	                   
	                    ResultSet spotRset = spotsView.executeQuery();
	                   
	                    while(spotRset.next()){
	                    System.out.println("Open spots: "+(spotRset.getString(1)));
	                    }
	                   
	                    System.out.println("Enter spot to reserve: ");
	                    int spottoreserve = Integer.parseInt(reader.readLine());
	                   
	                   
	                    PreparedStatement insertReservation = conn.prepareStatement("INSERT INTO reservation VALUES (?,?,?,?,?,?,?)");
	                    
	                    insertReservation.setString(1, orderType);
	                    insertReservation.setTimestamp(2, TimeStart);
	                    insertReservation.setTimestamp(3, TimeEnd);
	                    insertReservation.setString(4, carplate);
	                    insertReservation.setString(5, templicensePlate);
	                    insertReservation.setString(6, locationtoreserve);
	                    insertReservation.setInt(7, spottoreserve);
	                    insertReservation.setInt(8, reservationNumber.nextInt(1000000000));
	                    insertReservation.setString(9, customerID);
	                    insertReservation.executeUpdate();
	                    conn.commit();

	                   
	                } else if (!orderType.equalsIgnoreCase("member")) {
	                System.out.print("Enter reservation start time (yyyy-MM-dd hh:mm:ss): ");
	                    String TimeStart = reader.readLine();
                        int custID=rand.nextInt(1000000000);
                        String customer_ID=Integer.toString(custID);
	                    System.out.print("Enter time out (yyyy-MM-dd hh:mm:ss): ");
	                    String TimeEnd = reader.readLine();

	                    System.out.print("Enter license plate number:");
	                    String licensePlateType = reader.readLine();
	                    
	                    PreparedStatement pst2 = conn.prepareStatement("SELECT car_plate_number FROM Non_member WHERE customer_ID = ?");
	                    pst2.setString(1, customer_ID);
	                    ResultSet rset2 = pst2.executeQuery();
	                    carplate = rset2.getString(1);
	                    
	                   
	                 
	                    Statement locationView = conn.createStatement();
	                   
	                    ResultSet locRset = locationView.executeQuery("SELECT * FROM vacantLots");
	                    while(locRset.next()){
	                    System.out.println("Location: "+locRset.getString(1)+" Number of open spots: "+(locRset.getString(2)));
	                    }
	                   
	                    System.out.println("Enter location to reserve: ");
	                    String locationtoreserve = reader.readLine();
	                   
	                    Statement spotsView = conn.createStatement();
	                   
	                    ResultSet spotRset = spotsView.executeQuery("SELECT * FROM vacantSpots");
	                    while(spotRset.next()){
	                    System.out.println("Open spots: "+(spotRset.getString(1)));
	                    }
	                   
	                    System.out.println("Enter spot to reserve: ");
	                    int spottoreserve = Integer.parseInt(reader.readLine());
	                   
	                   
	                    PreparedStatement insertReservation = conn.prepareStatement("INSERT INTO reservation VALUES (?,?,?,?,?,?,?,?,?)");
	                   
	                    insertReservation.setString(1, orderType);
	                    insertReservation.setString(2, TimeStart);
	                    insertReservation.setString(3, TimeEnd);
	                    insertReservation.setString(4, carplate);
	                    insertReservation.setString(5, templicensePlate);
	                    insertReservation.setString(6, locationtoreserve);
	                    insertReservation.setInt(7, spottoreserve);
	                    insertReservation.setInt(8, reservationNumber.nextInt(700000000));
	                    insertReservation.setString(9, customer_ID);
	                    insertReservation.executeUpdate();
	                    conn.commit();

	             
	            } else {
	                System.out.println("\nInvalid.");
	               
	            }
	            }} catch (Exception e) {
	            e.printStackTrace();
	        }
	        break;
		case 2:
			//find customer information on member, non member or customer
			//check variable
			int chk=0;
			//ask for customer id
			System.out.println("Please enter customer ID:");
			String ciD=reader.readLine();
			PreparedStatement checkID=conn.prepareStatement("select count(customer_ID) from Customer where customer_ID=?");
			checkID.setString(1,"ciD");
			ResultSet IDexist=checkID.executeQuery();
			//check customer exists
			while(IDexist.next()) {
				String temp=IDexist.getString(1);
				chk=Integer.valueOf(temp);
			}
			if (chk==0) {//customer does not exist
				System.out.println("Either customer does not exist or customer was never a member");
			}
			else if (chk==1) {//customer info can be displayed
				PreparedStatement display=conn.prepareStatement("select first_name,last_name,customer_ID,customer_type,reservation_number,time_start,time_end from Customer natural join Member_ natural join Non_member natural join reservation where customer_ID=?");
				display.setString(1,"ciD");
				ResultSet disp=display.executeQuery();
				while(disp.next()) {
					System.out.println("The first name is: "+disp.getString(1));
					System.out.println("The last name is: "+disp.getString(2));
					System.out.println("The customer ID is: "+disp.getString(3));
					System.out.println("The customer is a: "+disp.getString(4));
					System.out.println("The reservation number is: "+disp.getString(5));
					System.out.println("The start time is: "+disp.getString(6));
					System.out.println("The end time is: "+disp.getString(7));
				}//displayed in rows
			}
			break;
		case 3:
			//update customer information on member, non member or customer
			//can update names,credit card info
			System.out.println("What would you like to update?(Enter the option number)");
			System.out.println("1- Update customer name");
			System.out.println("2- Update credit card number");
			int Option=0;
			Option=Integer.parseInt(reader.readLine());
			switch(Option) {
			case 1:
				System.out.println("Enter the new first name:");
				String newName=reader.readLine();
				System.out.println("Enter the new last name:");
				String newLast=reader.readLine();
				PreparedStatement updateName=conn.prepareStatement("update Customer set first_name=?,last_name=?");
				updateName.setString(1,newName);
				updateName.setString(2,newLast);
				updateName.executeUpdate();
				conn.commit();
				System.out.println("UPDATE SUCCESSFUL!");
				break;
			case 2:
				System.out.println("Enter the new credit card number:");
				String newCard=reader.readLine();
				PreparedStatement updateCard=conn.prepareStatement("update Customer set credit_card_number=?");
				updateCard.setString(1,newCard);
				updateCard.executeUpdate();
				conn.commit();
				System.out.println("UPDATE SUCCESSFUL!");
				break;
			}
			break;
		case 4:
		    //updating membership status on Customer
			//if they are already a member let them know 
			//if they are non-member or their customer ID does not exists, make them a member
			int check=0;
			System.out.println("Does the customer have a Cusotomer ID?");
			String ans=reader.readLine();
			if(ans.equalsIgnoreCase("yes")) {
				System.out.println("Enter the customer ID:");
				String ID=reader.readLine();
			    PreparedStatement findID=conn.prepareStatement("select count(customer_ID),customer_type from Customer where customer_ID=?");
			    findID.setString(1,ID);
			    ResultSet findings=findID.executeQuery();
			    while(findings.next()) {
			    	if (findings.getInt(1)==0) {
			    		System.out.println("The customer ID you are looking for does not exist.");
			    	}//if it does not exist
			    	else if(findings.getInt(1)==1) {
			    		if(findings.getString(2).equalsIgnoreCase("member"))
			    			System.out.println("Customer is already a member!");
			    		else if(findings.getString(2).equalsIgnoreCase("non-member")) {
			    			String nm="member";
			    			PreparedStatement makeChange=conn.prepareStatement("update Customer set customer_type=? where customer_ID=?");
			    			makeChange.setString(1,nm);
			    			makeChange.setString(2,ID);
			    			makeChange.executeUpdate();
			    			conn.commit();
			    			PreparedStatement addMember=conn.prepareStatement("insert into Member_ values(?,?,?,?,?,?)");
			    			System.out.println("Please enter chosen username:");
			    			String userName=reader.readLine();
			    			System.out.println("Please enter chosen password:");
			    			String userPass=reader.readLine();
			    			System.out.println("Please enter car plate number of member:");
			    			String plates=reader.readLine();
			    			String tempPlates=null;
			    			String tempCard=null;
			    			addMember.setString(1,plates);
			    			addMember.setString(2,tempPlates);
			    			addMember.setString(3,tempCard);
			    			addMember.setString(4,ID);
			    			addMember.setString(5,userName);
			    			addMember.setString(6,userPass);
			    			addMember.executeUpdate();
			    			conn.commit();
			    		}
			    	}//if it exists
			    }//looking for the ID
			}//if the customer says that they have a customer id with them
			else if(ans.equalsIgnoreCase("no")) {
				String type="member";
				String login=null;
				String logout=null;
				String tempplates=null;
				String tempcards=null;
				System.out.println("Please enter First Name:");
				String firstName=reader.readLine();
				System.out.println("Please enter Last Name:");
				String lastName=reader.readLine();
				System.out.println("Please enter Car plate number:");
				String plate=reader.readLine();
				System.out.println("Please enter chosen username:");
    			String userName=reader.readLine();
    			System.out.println("Please enter chosen password:");
    			String userPass=reader.readLine();
				int id=rand.nextInt(99999999);
				String newID=Integer.toString(id);
				System.out.println("Please enter credit card number:");
    			String userCredit=reader.readLine();
    			
				PreparedStatement inputToCustomer=conn.prepareStatement("insert into Customer values (?,?,?,?,?)");
				PreparedStatement inputToMember=conn.prepareStatement("insert into Member_ values (?,?,?,?,?,?)");
				
				inputToCustomer.setString(1,firstName);
				inputToCustomer.setString(2,lastName);
				inputToCustomer.setString(3,newID);
				inputToCustomer.setString(4,type);
				inputToCustomer.setString(5,login);
				inputToCustomer.setString(6,logout);
				inputToMember.setString(1,plate);
				inputToMember.setString(2,tempplates);
				inputToMember.setString(3,tempcards);
				inputToMember.setString(4,newID);
				inputToMember.setString(5,userName);
				inputToMember.setString(6,userPass);
				
				inputToCustomer.executeUpdate();
				conn.commit();
				inputToMember.executeUpdate();
				conn.commit();
			}//if the customer says they do not have a customer ID with them
			break;
		case 5:
			//monthly revenue report
		    Statement monthlyRevenueReport=conn.createStatement();
		    ResultSet report1=monthlyRevenueReport.executeQuery("select * from monthlyRevenue");
		    while(report1.next()) {
		    	System.out.println("The month: "+ report1.getString(1)+"The guest reservation revenue: "+report1.getFloat(2)+"The drive in reservation revenue: "+report1.getFloat(3));
		    }
		    System.out.println("----END OF REPORT----");
			break;
		case 6:
			//parking usage report
			Statement parkingUsageReport=conn.createStatement();
			ResultSet report2=parkingUsageReport.executeQuery("select * from parking_usage");
			while(report2.next()) {
				System.out.println("The parking lot location: "+report2.getString(1)+"The percentage of member engagement: "+report2.getFloat(2)+"The percentage of online engagement: "+report2.getFloat(3)+"The percentage of drive-in engagement: "+report2.getFloat(4));
			}
			System.out.println("----END OF REPORT----");
			break;
		case 7:
			//login/logout report
			Statement loginLogoutReport=conn.createStatement();
			ResultSet report3=loginLogoutReport.executeQuery("select * from time_log");
			while(report3.next()) {
				System.out.println("The customer ID:"+report3.getString(1)+"The log in time:"+report3.getTimestamp(2)+"The log out time:"+report3.getTimestamp(3));
			}
			System.out.println("----END OF REPORT----");
			break;
		case 8:
			//available spots report
			Statement availableSpotsReport=conn.createStatement();
			ResultSet report4=availableSpotsReport.executeQuery("(select location_,spot_ID from parking_spot) from minus(select location_,spot_ID from reservation)");
			while(report4.next()) {
				System.out.println("The open spots are:"+report4.getString(1)+report4.getInt(2));
			}
			System.out.println("----END OF REPORT----");
			break;
		case 9:
			//exiting menu
			tracker=true;
			break;
		}//end of switch case
		if (tracker=true)//Initially chose to exit menu
				break;
		else {
			System.out.println("Do you want to enter another option?");
			Scanner sc2=new Scanner(System.in);
		    String ans=sc2.nextLine();
		    if (ans.equalsIgnoreCase("Yes"))
		    	tracker=false;//menu while loops
		    else if (ans.equalsIgnoreCase("No"))
		    	tracker=true;//menu while stops
		    sc2.close();
		}
		sc.close();
	    }//end of menu while
	    System.out.println("Exited menu");
	}
	
	/*
	//getters
	public String getEmployeeName() {
		return this.employee_name;
	}
	public String getEmpID() {
		return this.emp_ID;
	}
	public String getSSN() {
		return this.SSN;
	}
	public String getPayType() {
		return this.pay_type;
	}
	public String getEmpType() {
		return this.emp_type;
	}
	//setters
	public void setEmployeeName(String name) {
		this.employee_name=name;
	}
	public void setEmployeeID(String id) {
		this.emp_ID=id;
	}
	public void setSSN(String ssn) {
		this.SSN=ssn;
	}
	public void setPayT(String Ptype) {
		this.pay_type=Ptype;
	}
	public void setEmpT(String Etype) {
		this.emp_type=Etype;
	}*/
}
