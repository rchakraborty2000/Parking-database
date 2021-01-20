package EmployeeSuperclass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.Scanner;



@SuppressWarnings("unused")
public class LoginScreen {
	
	//Method for connecting to database
	public static Connection Connect () throws Exception {
		
		try {
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/parkinglotmanagement", "postgres", "5432");
			conn.setAutoCommit(false);
			//System.out.println("Connected.");
			return conn;
		}
		catch (SQLException e) {
			System.out.println("Could not Connect :(");
			return null;
		}
		
		
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("Welcome to Parking lot!");
		System.out.println("1. Sign In \n2. Sign Up");
		System.out.println("Please enter 1 or 2:");
		Scanner scan = new Scanner(System.in);
		//Scanner scan1 = new Scanner(System.in);
		
		
		int choice = scan.nextInt();
		if (choice == 1) {
			//Going to Sign in screen
			SignIn signin = new SignIn();
		}
		if (choice == 2) {
			//Going to sign Up screen
			SignUp signup = new SignUp();
		}
			
			
	}
}