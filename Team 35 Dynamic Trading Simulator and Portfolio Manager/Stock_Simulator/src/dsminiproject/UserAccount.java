package dsminiproject;

import java.util.Scanner;

public class UserAccount {
    private String username;
    private String email;
    private String phoneNumber;
    public static double initialMoney;

    public double getInitialMoney() {
		return initialMoney;
	}

	public void setInitialMoney(double initialMoney) {
		this.initialMoney = initialMoney;
	}

	public UserAccount(String username, String email, String phoneNumber, double initialMoney) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.initialMoney = initialMoney;
    }

    public static void createAccount(Scanner sc) {
        

        try {
            System.out.print("Enter Username: ");
            String username = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Phone Number: ");
            String phoneNumber = sc.nextLine();

            // Validate email and phone number
            validateEmail(email,sc);
            validatePhoneNumber(phoneNumber,sc);

            System.out.print("Enter Initial Money in Account (INR): ");
            double initialMoney = sc.nextDouble();
            sc.nextLine();

            // Create a new user account
            UserAccount account = new UserAccount(username, email, phoneNumber, initialMoney);
            System.out.println("Portfolio Created. Welcome, " + username + "!");
            

        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
            createAccount(sc);
        } catch (Exception e) {
            System.out.println("Error: Invalid input. Please try again.");
        } 
        
    }

    private static void validateEmail(String email,Scanner sc) throws InvalidInputException {
        if (!email.contains("@")) {
            throw new InvalidInputException("Invalid email. It must contain an '@' symbol. \n Please try again");
            
        }
    }

    private static void validatePhoneNumber(String phoneNumber,Scanner sc) throws InvalidInputException {
        if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+")) {
            throw new InvalidInputException("Invalid phone number. It must contain exactly 10 digits. \\n Please try again");
            
        }
    }
    UserAccount()
    {
    	
    }
}
class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
        
    }
}
 
