package EmployeeSuperclass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.SQLException;
import java.util.Random;

import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.*;

public class UserMenu {
    BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
   
    /*public static Connection conn() throws ClassNotFoundException {
    	String url = "jdbc:postgresql://localhost:5432/parkinglotmanagement";
    	String username= "postgres";
    	String passwd="5432";
    	Connection conn = null;
    	
    	try {
        	Class.forName("org.postgresql.Driver");
        	conn = DriverManager.getConnection(url,username,passwd);
        	conn.setAutoCommit(false);
    	}
    	
    	catch (Exception se){
    		System.out.println("Could not create conn" + se );
    	}
    	
    	return conn;
    }*/


    public static String UserMenuOptions() throws IOException {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
    	
        System.out.println("\nUser Menu");
        System.out.println("1. View Profile\n2. Update Profile\n3. Make Reservation\n4. Exit Menu");
        System.out.print("Enter your option here: ");
        String option = null;
		try {
			option = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return option;
    }

    public static void UserMenuScreen(String customer_ID,Connection conn) throws Exception {
        boolean tracker = false;
        
        while (tracker==false) {
            String option = UserMenuOptions();

            switch (option) {
                case "1":
                    viewProfile(customer_ID,conn);
                    break;
                case "2":
                    makeUpdate(customer_ID,conn);
                    break;
                case "3":
                    reservationMenu(customer_ID,conn);
                    break;
                case "4":
        			//exiting menu
        			tracker=true;
        			break;
                default:
                    System.out.println("\nInvalid option.");
            }
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
        }
        //Logout lo=new Logout(customer_ID, "customer");
    }

    public static void viewProfile(String customer_ID,Connection conn) throws Exception{
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
    	try {
        	
        	System.out.println("Are you a 'member' or 'non member'?");
        	String type = null;
        	try {
        	type = reader.readLine();
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	
               if (type.equalsIgnoreCase("member")) {
                PreparedStatement pstMember = conn.prepareStatement("SELECT * FROM (Customer NATURAL JOIN Member_ NATURAL JOIN reservation) JOIN (parking_lot NATURAL JOIN parking_spot) ON reservation.reservation_number=parking_spot.reservation_number WHERE customer_ID = ?");
                pstMember.setString(1, customer_ID);
                ResultSet memberResult = pstMember.executeQuery();
                
                while (memberResult.next()) {
                	System.out.println("\nUser Profile");
                    System.out.println(String.format("Customer ID: %s", memberResult.getString("customer_ID")));
                    System.out.println(String.format("First name: %s", memberResult.getString("first_name")));
                    System.out.println(String.format("Last name: %s", memberResult.getString("last_name")));
                    System.out.println(String.format("Username: %s", memberResult.getString("login_username")));
                    System.out.println(String.format("Password: %s", memberResult.getString("login_password")));
                    System.out.println(String.format("Registered license plate: %s", memberResult.getString("car_plate_number")));
                    System.out.println(String.format("Temporary license plate: %s", memberResult.getString("temp_plate_number")));
                    System.out.println(String.format("Registered lot: %s", memberResult.getString("location_")));
                    System.out.println(String.format("Registered spot: %d", memberResult.getInt("spot_id")));
                    System.out.println(String.format("Membership fee: %f", memberResult.getDouble("monthly_membership_fee")));
                    System.out.println(String.format("Membership fee: %d", memberResult.getInt("reservationNumber"))); 
                    String cred = memberResult.getString("credit_card_number");
                    String lastfourdigits = cred.substring(cred.length()-4);
                    System.out.println(String.format("Credit card number ending in: %s", lastfourdigits));
                	
                }
               
                memberResult.close();
                pstMember.close();
                
            } else if (type.equalsIgnoreCase("non member")) {
                    PreparedStatement pstNonMember = conn.prepareStatement("SELECT * FROM (Customer NATURAL JOIN Non_member NATURAL JOIN reservation) JOIN (parking_lot NATURAL JOIN parking_spot) USING reservationNumber WHERE customer_ID = ?");
                    pstNonMember.setString(1, customer_ID);
                    ResultSet nonMemberResult = pstNonMember.executeQuery();
                    
                    while (nonMemberResult.next()) {
                           	System.out.println("\nUser Profile");
                             System.out.println(String.format("Customer ID: %s", nonMemberResult.getString("customer_ID")));
                             System.out.println(String.format("First name: %s", nonMemberResult.getString("first_name")));
                             System.out.println(String.format("Last name: %s", nonMemberResult.getString("last_name")));
                             System.out.println(String.format("Username: %s", nonMemberResult.getString("guest_login")));
                             System.out.println(String.format("Registered license plate: %s", nonMemberResult.getString("car_plate_number")));
                             System.out.println(String.format("Registered lot: %s", nonMemberResult.getString("location_")));
                             System.out.println(String.format("Registered spot: %d", nonMemberResult.getInt("spot_id")));
                             System.out.println(String.format("Membership fee: %f", nonMemberResult.getDouble("sale_value")));
                             System.out.println(String.format("Membership fee: %d", nonMemberResult.getInt("reservationNumber")));                    
                         	
                         }
                    

                    nonMemberResult.close();
                    pstNonMember.close();
                } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String updateAccountOptions(String type,Connection conn) throws IOException {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
        System.out.println("\nUpdate Profile");
        System.out.println("1. First name \n2. Last name");

        if (type.equalsIgnoreCase("member")) {
            System.out.println("3. Password\n4. Reservation\n5. Update credit card\n6. Add temporary card");
        }

        System.out.print("Enter option here: ");
        String option = reader.readLine();
        return option;
    }

    public static String getUpdatedValue() throws IOException {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
        System.out.print("Enter new value here: ");
        String newValue = reader.readLine();
        return newValue;
    }
    
   

    public static void makeUpdate(String customer_ID,Connection conn) throws Exception  {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
    	System.out.println("Are you a 'member' or 'non member'?");
    	String type = reader.readLine();
    	
    	System.out.println("Enter your customer ID: ");
    	customer_ID = reader.readLine();
    	//Connection conn = conn();


       // boolean exit = false;

        
            String option = updateAccountOptions(type,conn);
            boolean isValid = false;

            switch (option) {
                case "1":
                    while (!isValid) {
                        String value = getUpdatedValue();
                        try {

                        if (value.length() <= 20) {
                        	
                            isValid = true;
                            
                            PreparedStatement pstfname = conn.prepareStatement("UPDATE Customer SET first_name = ? WHERE customer_ID = ? ");
                            pstfname.setString(1,value);
                            pstfname.setString(2, customer_ID);
                        	pstfname.executeUpdate();
                        	conn.commit();
                        	pstfname.close();

                        } else {
                            System.out.println("\nInvalid. First name has to have less than 20 characters.");
                        }}
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Invalid last name");
                    }}
                    break;
                
                case "2":
                    while (!isValid) {
                        String value = getUpdatedValue();
                        try {
                        if (value.length() <= 20) {
                            isValid = true;
                            PreparedStatement pstlname = conn.prepareStatement("UPDATE Customer SET last_name = ? WHERE customer_ID = ? ");
                            pstlname.setString(1,value);
                            pstlname.setString(2, customer_ID);                            
                        	pstlname.executeUpdate();
                        	conn.commit();
                        	pstlname.close();
                        	

                        } else {
                            System.out.println("\nInvalid. Last name has to have less than 20 characters.");
                        }}
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Invalid last name.");
                    }}
                        
                    break;
                    
                    
                case "3":
                	if(type.equalsIgnoreCase("member")){
                		while (!isValid) {
                            String value = getUpdatedValue();
                            try {
                            if (value.length() <= 20) {
                                isValid = true;
                                
                                System.out.println("Please enter your current password: ");
                                String entercurrPass = reader.readLine();
                                
                                PreparedStatement pstcurrpass = conn.prepareStatement("SELECT login_password FROM Member_ WHERE customer_ID = ?");
                                pstcurrpass.setString(1, customer_ID);
                                
                                ResultSet currentpass = pstcurrpass.executeQuery();
                                String currPass =  currentpass.getString("login_password");
                                
                                if(currPass.equals(entercurrPass)) {
                                	
                                	System.out.println("Enter new password: ");
                                	String newPassword = reader.readLine();
                                	
                                	PreparedStatement updatePass = conn.prepareStatement("UPDATE Member_ SET login_password = ? WHERE customer_ID = ?");
                                	updatePass.setString(1, newPassword);
                                	updatePass.setString(2,customer_ID);
                                	updatePass.executeUpdate();
                                	conn.commit();
                                	updatePass.close();
                                	
                                }
                               
                              
                            } else {
                                System.out.println("Invalid input. Password has to have less than 20 characters.");
                            }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Invalid password");
                        }}}
                		else {
                			System.out.println("Invalid option.");
                		}
                	
                	break;
                	
                case "4": //reservation
                		
                	if(type.equalsIgnoreCase("member")) {
                		try {
                		
                	 System.out.print("Enter new reservation start time (yyyy-MM-dd hh:mm:ss): ");
                     String TimeStartString = reader.readLine();
                     Timestamp TimeStart = Timestamp.valueOf(TimeStartString);
                     
                     System.out.print("Enter new time out (yyyy-MM-dd hh:mm:ss): ");
                     String TimeEndString = reader.readLine();
                     Timestamp TimeEnd = Timestamp.valueOf(TimeEndString);

                     Statement locationView = conn.createStatement();
                     
                     ResultSet locRset = locationView.executeQuery("SELECT * FROM vacantLots");
                     while(locRset.next()){
                     	System.out.println("Location: "+locRset.getString(1)+" Number of open spots: "+(locRset.getString(2)));
                     }
                     
                     System.out.println("Enter location to reserve: ");
                     String locationtoreserve = reader.readLine();
                     
                     PreparedStatement spotsView = conn.prepareStatement("EXEC checkVacantSpots(?,?,?)");
                     spotsView.setTimestamp(1, TimeStart);
                     spotsView.setTimestamp(2, TimeEnd);
                     spotsView.setString(3,locationtoreserve);
                     
                     ResultSet spotRset = spotsView.executeQuery();
                     
                     while(spotRset.next()){
                     	System.out.println("Open spots: "+(spotRset.getString(1)));
                     }
                     
                     System.out.println("Enter spot to reserve: ");
                     int spottoreserve = Integer.parseInt(reader.readLine());
                     
                     PreparedStatement updateRes = conn.prepareStatement("UPDATE reservation SET location_ = ? AND spot_ID = ? WHERE customer_ID = ?");
                     updateRes.setString(1, locationtoreserve);
                     updateRes.setInt(2, spottoreserve);
                     updateRes.setString(3, customer_ID);
                     updateRes.executeUpdate();
                     conn.commit();
                     
                     PreparedStatement updateResM = conn.prepareStatement("UPDATE Member_ SET location_ = ? AND spot_ID = ? WHERE customer_ID = ?");
                     updateResM.setString(1, locationtoreserve);
                     updateResM.setInt(2, spottoreserve);
                     updateResM.setString(3, customer_ID);
                     updateResM.executeUpdate();
                     conn.commit();
                                    		
                		}
                		catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Invalid.");}
                     
                	}
             else {
                        System.out.println("\nInvalid option. Try again.");
                    }
                    break;
                    
                    
                case "5": 
                	if(type.equalsIgnoreCase("member")) {
                	 while(!isValid) {
                		 String val =  getUpdatedValue();
                		 try {
                			 
                             
                             if (String.valueOf(val).length()<=19 ||String.valueOf(val).length()>= 13) {
                            	 isValid=true;
                            	 PreparedStatement pst = conn.prepareStatement("UPDATE Customer SET credit_card_number = ? WHERE customer_ID = ?");
                    			 pst.setString(2, customer_ID);
                    			 pst.setString(1, val);
                    			 pst.executeUpdate();
                    			 conn.commit(); 
                                 pst.close();                		
                                 
                             }
                             
                             else {
                            	 System.out.println("Invalid credit card.");
                             }}
                             
                             catch (Exception e) {
                                 e.printStackTrace();
                                 System.out.println("Invalid credit card");
                		 }
                	 }
                	
                }
                	else {
                		System.out.println("Invalid option chosen.");
                	}
                	break;
                	
                case "6": 
                	if(type.equalsIgnoreCase("member")) {
                	 while(!isValid) {
                		 String val =  getUpdatedValue();
                		 try {
                			                            
                             if (String.valueOf(val).length()<=19 ||String.valueOf(val).length()>= 13) {
                            	 isValid = true;
                            	 PreparedStatement pst = conn.prepareStatement("UPDATE member_ SET temp_card= ? WHERE customer_ID = ?");
                    			 pst.setString(2, customer_ID);
                    			 pst.setString(1, val);
                  	 			 pst.executeUpdate();
                  	 			 conn.commit();
                                 pst.close();                		
                                 
                            	 
                             }
                             
                             else {
                            	 System.out.println("Invalid credit card.");
                             }}
                             
                             catch (Exception e) {
                                 e.printStackTrace();
                                 System.out.println("Invalid credit card");
                		 }
                	 }
                	
                }
                	else {
                		System.out.println("Invalid option chosen.");
                	}
                	break;
                    
                default:
                    System.out.println("\nInvalid option. Try again.");
            }
        }
    
   
    
    public static void reservationMenu(String customer_ID,Connection conn) throws IOException {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
    	Random reservationNumber = new Random();
    	
        System.out.println("\nReservation");

        
        System.out.print("Enter type of reservation (online, member): ");
        String orderType = reader.readLine();
        String templicensePlate = null; 
        String carplate = null;

        
        	//Connection conn = conn();
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM Customer WHERE customer_ID = ?");
            pst.setString(1, customer_ID);
            ResultSet rset = pst.executeQuery();
            if (!rset.next()) {
            	
          if (orderType.equalsIgnoreCase("online") || orderType.equalsIgnoreCase("member")) {
                if (orderType.equalsIgnoreCase("member")) {
                    System.out.print("Enter reservation start time (yyyy-MM-dd hh:mm:ss): ");
                    String TimeStartString = reader.readLine();
                    Timestamp TimeStart = Timestamp.valueOf(TimeStartString);
                    
                    System.out.print("Enter time out (format yyyy-MM-dd hh:mm:ss): ");
                    String TimeEndString = reader.readLine();
                    Timestamp TimeEnd = Timestamp.valueOf(TimeEndString);

                    System.out.println("Choose license plate type (type in registered or temporary): ");
                    String licensePlateType = reader.readLine();
                    if(licensePlateType.equalsIgnoreCase("registered")) {
                    	PreparedStatement pst2 = conn.prepareStatement("SELECT car_plate_number FROM Member_ WHERE customer_ID = ? ");
                    	pst2.setString(1, customer_ID);
                        System.out.println("Here1");
                    	ResultSet rset2 = pst2.executeQuery();
                        System.out.println("Here2");
                    	carplate = rset2.getString(1);
                        System.out.println("Here3");
                    }
                    
                    else if (licensePlateType.equalsIgnoreCase("temporary")) {
                    	System.out.print("Enter your temporary license plate: ");
                        templicensePlate = reader.readLine();
                        
                        
                        //grant insert to member
                    	
                    } 
                    Statement locationView = conn.createStatement();
                    
                    ResultSet locRset = locationView.executeQuery("SELECT * FROM vacantLots");
                    while(locRset.next()){
                    	System.out.println("Location: "+locRset.getString(1)+" Number of open spots: "+(locRset.getString(2)));
                    }
                    
                    System.out.println("Enter location to reserve: ");
                    String locationtoreserve = reader.readLine();
                    
                    PreparedStatement spotsView = conn.prepareStatement("EXEC checkVacantSpots(?,?,?)");
                    spotsView.setTimestamp(1, TimeStart);
                    spotsView.setTimestamp(2, TimeEnd);
                    spotsView.setString(3, locationtoreserve);
                    
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
                    insertReservation.setInt(8, reservationNumber.nextInt(700000000));
                    insertReservation.setString(9, customer_ID);
                    insertReservation.executeUpdate();
                    conn.commit();

                   
                } else if (rset.next()&&!orderType.equalsIgnoreCase("member")) {
                	System.out.print("Enter reservation start time (yyyy-MM-dd hh:mm:ss): ");
                    String TimeStart = reader.readLine();

                    System.out.print("Enter time out (yyyy-MM-dd hh:mm:ss): ");
                    String TimeEnd = reader.readLine();

                    System.out.print("Enter license plate number:");
                    
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

            rset.close();
            pst.close();
            } }}catch (Exception e) {
            e.printStackTrace();}
        }
        //	printReservationInfo(conn);
    
    
   

    public static void printReservationInfo(Connection conn) {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
        try {
        	//Connection conn = conn();

        	System.out.println("Enter your customer ID: ");
        	String cust = reader.readLine();
        	
        	System.out.println("Enter reservation number: ");
        	int reservationNo = Integer.parseInt(reader.readLine());
        	
        	PreparedStatement check = conn.prepareStatement("SELECT * FROM reservation WHERE customer_ID = ? AND reservationNumber = ?");
        	check.setString(1, cust);
        	check.setInt(2, reservationNo);
        	ResultSet rset = check.executeQuery();
        	while(rset.next()) {
        		System.out.println("Order type: "+rset.getString(1));
        		System.out.println("Reservation start time: "+ rset.getString(2));
        		System.out.println("Reservation end time: "+rset.getString(3));
        		System.out.println("Car plate number: "+ rset.getString(4));
        		System.out.println("Temporary plate number: "+rset.getString(5));
        		System.out.println("Parking lot location: "+rset.getString(6));
        		System.out.println("Parking spot number: "+rset.getString(7));
        		System.out.println("Reservation number: "+rset.getString(8));
        		System.out.println("Customer ID: "+rset.getString(9));
        		
        		rset.close();
                check.close();
        	}
        	
        	PreparedStatement orderType = conn.prepareStatement("SELECT orderType FROM reservation WHERE customer_ID = ? AND reservationNumber = ?");
        	orderType.setString(1, cust);
        	orderType.setInt(2, reservationNo);
        	ResultSet rsetorderType = orderType.executeQuery();
        	      	
                  if (rsetorderType.getString(1).equalsIgnoreCase("non member")) {
                	  PreparedStatement cost = conn.prepareStatement("SELECT sale_value FROM reservation WHERE customer_ID = ? AND reservationNumber = ?");
                  	cost.setString(1, cust);
                  	cost.setInt(2, reservationNo);
                  	ResultSet rsetcost = cost.executeQuery();
                  	
                    System.out.println(String.format("Fee: %f", rsetcost.getDouble("sale_value")));
                    rsetcost.close();
                    orderType.close();
                }
            

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}