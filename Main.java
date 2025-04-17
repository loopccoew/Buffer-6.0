//shree
//ganpati bappa morya
//shree ram 

package offline_payments_system;
 
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
class Server
{
    // HashMap to store UPI ID as the key and User object as the value
    static HashMap<String, user> ServeruserData = new HashMap<>();
    void loadserverdata() {
        // Adding 10 dummy users with UPI IDs and other details
    	ServeruserData.put("upi123456789", new user("Bhargavi Dange", "Bhargavi134", 10110120, 50000, 9234567890L, 1234,new Stack<>()));
    	ServeruserData.put("upi987654321", new user("Sharvi Burse", "Sharvi23", 10212340, 10000, 9134567891L, 5678,new Stack<>()));
    	ServeruserData.put("upi112233445", new user("Girija Desai", "Girija132", 1034534, 7500, 9234567892L, 9101,new Stack<>()));
    	ServeruserData.put("upi223344556", new user("Asmita Ahire", "Asmita109", 1047869, 20000, 9234567893L, 1122,new Stack<>()));
    	ServeruserData.put("upi334455667", new user("Arpita Deodikar", "Arpita129", 1051689, 15000, 9534567894L, 3344,new Stack<>()));
    	ServeruserData.put("upi445566778", new user("Manali Dhupkar", "Manali215", 1068906, 30009, 9734567895L, 5566,new Stack<>()));
    	ServeruserData.put("upi556677889", new user("Maitri Bihani", "Maitri117", 1077892, 22000, 9834567896L, 7788,new Stack<>()));
    	ServeruserData.put("upi667788990", new user("Pallavi Dange", "Pallavi2076", 1081342, 8000, 9334567897L, 9900,new Stack<>()));
    	ServeruserData.put("upi778899001", new user("Kartik Deshpande ", "Kartik89", 1093798, 50000, 9534567898L, 1122,new Stack<>()));
    	ServeruserData.put("upi889900112", new user("Bhushan Kulkarni", "Bhushan1981", 11009097, 12000, 9094567899L, 2233,new Stack<>()));
    }
    public user get_user_by_upi(String upi_id) {
        return ServeruserData.get(upi_id);  // Retrieves user based on UPI ID
    }
   void update_server_balance(String sender_upi_id,String reciever_upi_id,double amount) {
	    user sender_user = ServeruserData.get(sender_upi_id);
	    user reciever_user = ServeruserData.get(reciever_upi_id);
	    sender_user.balance -= amount;
	    reciever_user.balance += amount;
	    System.out.println("Balance updated successfully on server!");
	    System.out.println("Current balance of (sender) "+sender_user.user_name+"is Rps. "+sender_user.balance);
	    System.out.println("Current balance of (receiver) "+reciever_user.user_name+"is Rps. "+reciever_user.balance);
   }
}


class user{
	String user_id; //user input but unique 
	String user_name;//normal name 
	long balance;
	int pin;  //pin used for transactions 
	long phno;   
	long acc_no;
	String upi_id;//upi_id
    String hashed_password;
    Stack<transactions> s;
//stack
	
	//think about integration of system to retrieve multiple bank accounts 
   
	user(String user_name,String user_id,long acc_no,long balance,long phno,int pin,Stack<transactions> s)
	{
		this.user_name=user_name;
		this.user_id=user_id;
		this.acc_no=acc_no;
		this.balance=balance;
		this.phno=phno;
		this.pin=pin;
		this.s=s;
		}
	
	
}
class user_management{
	HashMap<String,user> user_data = new HashMap<>();
	Scanner sc = new Scanner(System.in);
	    Server server;  // Dummy values are stored in server class so we need to access them so we initialise the server object by passing it to the constructor
	    transaction_management tm; // already present

	   

	     user_management(Server server) {
	        this.server = server;
	    }
	     public void setTransactionManagement(transaction_management tm) {
		        this.tm = tm;
		    }
	     // Utility to hash password
	     String hashPassword(String password) {
	         try {
	             MessageDigest md = MessageDigest.getInstance("SHA-256");
	             byte[] hash = md.digest(password.getBytes());
	             StringBuilder hexStr = new StringBuilder();
	             for (byte b : hash) {
	                 hexStr.append(String.format("%02x", b));
	             }
	             return hexStr.toString();
	         } catch (NoSuchAlgorithmException e) {
	             return null;
	         }
	     }

	     void add_user() {
	         System.out.print("\nEnter UPI ID: ");
	         String upi_id = sc.nextLine();
              
	         // Check UPI ID in server
	         if (!server.ServeruserData.containsKey(upi_id)) {
	             System.out.println(" No user on server with given UPI ID.");
	             return;
	         }
	         System.out.println("User exists on server with given UPI ID.");
	         user server_user = server.ServeruserData.get(upi_id);

	         // Validate User ID
	         String input_userid;
	         while (true) {
	             System.out.print("\nEnter User ID: ");
	             input_userid = sc.nextLine();
	             if (!server_user.user_id.equals(input_userid)) {
	                 System.out.println("Uff! Incorrect User ID,Please re-enter...");
	             } else 
	            	 {
	            	 System.out.println("User ID verified succussfully !!");
	            	 break;
	            	 }
	         }

	         // Validate User Name
	         String input_username;
	         while (true) {
	             System.out.print("\nEnter User Name: ");
	             input_username = sc.nextLine();
	             if (!server_user.user_name.equalsIgnoreCase(input_username)) {
	                 System.out.println("Uff !! Incorrect User Name,Please re-enter...");
	             } else 
	            	 {
	            	 System.out.println("User Name verified succussfully !!");
	            	 break;
	            	 }
	         }

	         // Get Password & Hash it
	         System.out.print("\nSet your Password: ");
	         String password = sc.nextLine();
	         String hashed_pass = hashPassword(password);
             
	         // Store data into user_data map with hashed password & upi_id
	         user new_user = new user(
	                 server_user.user_name,
	                 server_user.user_id,
	                 server_user.acc_no,
	                 server_user.balance,
	                 server_user.phno,
	                 server_user.pin,new 
	                 Stack<>()
	         );

	         new_user.hashed_password = hashed_pass;
	         new_user.upi_id = upi_id;

	         user_data.put(input_userid, new_user);
	         System.out.println("User added successfully to local system !!!!");
	         
	      
	         // Display new user details added to local system
             System.out.println("\nNew User Added to Local System:");
	         System.out.println("User ID: " + new_user.user_id + ", Name: " + new_user.user_name + ", UPI: " + new_user.upi_id +
	                 ", Balance: " + new_user.balance + ", Phone Number: " + new_user.phno + ", Account Number: " + new_user.acc_no +
	                 ", PIN: " + new_user.pin + ", Hashed Password: " + new_user.hashed_password);
	     }

	   void login() { 
		   Scanner sc = new Scanner(System.in);
		    System.out.print("\nEnter User ID: ");
		    String id = sc.next();

		    System.out.print("Enter Password: ");
		    String pwd = sc.next();

		    // Hash the entered password
		    String hashed_pwd = hashPassword(pwd);

		    // Get user from HashMap
		    user temp = user_data.get(id);

		    if (temp == null) {
		        System.out.println("\nLogin failed: User ID does not exist.");
		        return;
		    }

		    if (temp.hashed_password.equals(hashed_pwd)) {
		    	System.out.print("Enter 4-digit PIN: ");
		        int enteredPin = sc.nextInt();
		        if (temp.pin==(enteredPin)) {
		        	try {
			            enteredPin = Integer.parseInt(sc.nextLine());
			        } catch (NumberFormatException e) {
			            System.out.println("Invalid PIN format.");
			            return;
			        }
		            System.out.println("\nLogin successful!");
		            System.out.println("Welcome, " + temp.user_name);
		            System.out.println("Account No: " + temp.acc_no);
		            System.out.println("Balance: " + temp.balance);
		            System.out.println("Phone: " + temp.phno);
		            System.out.println("UPI ID: " + temp.upi_id);
		        } else {
		            System.out.println("\nLogin failed: Incorrect PIN.");
		        }
		    } else {
		        System.out.println("\nLogin failed: Incorrect password.");
		        } 
		        System.out.print("Did you forget your password? (yes/no): ");
		        String response = sc.next();

		        if (response.equalsIgnoreCase("yes")) {
		            forgot_password();  // call the password reset method
		        } else {
		            System.out.println("Try logging in again.");
		        }
		    }

		
	   void forgot_password() {
		    Scanner sc = new Scanner(System.in);
		    
		    System.out.print("\nEnter your User ID: ");
		    String userId = sc.nextLine();

		    // Check if user exists
		    user temp = user_data.get(userId);
		    if (temp == null) {
		        System.out.println("No such user found in local system.");
		        return;
		    }

		    

		    System.out.print("Enter your Pin for verification: ");
		    int pin1 = sc.nextInt();

		    if ( temp.pin != pin1) {
		        System.out.println("Verification failed.phone number incorrect.");
		        return;
		    }

		    // Reset password
		    sc.nextLine(); // Consume newline
		    System.out.print("Enter new password: ");
		    String new_password = sc.nextLine();

		    String hashed_new_password = hashPassword(new_password);
		    temp.hashed_password = hashed_new_password;

		    System.out.println("Password reset successful! You can now log in with your new password.");
		}
	void update_details() { //for user
		System.out.print("\nEnter your User ID: ");
	    String id = sc.nextLine();

	    // Get user from local database
	    user localUser = user_data.get(id);

	    if (localUser == null) {
	        System.out.println("User not found in local system.");
	        return;
	    }
	    System.out.print("Enter your 4-digit PIN: ");
	    int enteredPin = sc.nextInt();
	    try {
            enteredPin = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid PIN format.");
            return;
        }
	    if (localUser.pin!=(enteredPin)) {
	        System.out.println("PIN verification failed.");
	        return;
	    }
	    user serverUser = server.get_user_by_upi(localUser.upi_id);
	    if (serverUser == null) {
	        System.out.println("User not found on server.");
	        return;
	    }

	    while (true) {
	        System.out.println("\n--- Update Menu ---");
	        System.out.println("1. Update PIN");
	        System.out.println("2. Update Phone Number");
	        System.out.println("3. Update Password");
	        System.out.println("4. Exit");
	        System.out.print("Enter your choice: ");
	        int choice = Integer.parseInt(sc.nextLine());

	        switch (choice) {
	            case 1:
	                System.out.print("Enter new PIN: ");
	                int newPin = Integer.parseInt(sc.nextLine());
	                localUser.pin = newPin;
	                serverUser.pin = newPin;
	                System.out.println(" PIN updated successfully in both local and server.");
	                break;

	            case 2:
	                System.out.print("Enter new Phone Number: ");
	                long newPh = Long.parseLong(sc.nextLine());
	                localUser.phno = newPh;
	                serverUser.phno = newPh;
	                System.out.println(" Phone number updated successfully in both local and server.");
	                break;

	            case 3:
	                System.out.print("Enter new Password: ");
	                String newPassword = sc.nextLine();
	                String hashedNewPwd = hashPassword(newPassword);
	                localUser.hashed_password = hashedNewPwd;
	                System.out.println(" Password updated successfully in local system.");
	                break;

	            case 4:
	                System.out.println("Exiting update menu...");
	                return;

	            default:
	                System.out.println("Invalid choice. Please try again.");
	        }
	    }
	
	}
	void update_balance(ArrayList<String> decrypted_code )
	{
		user sender_online=user_data.get(decrypted_code.get(0));
		user receiver_online=user_data.get(decrypted_code.get(1));
		sender_online.balance-=Long.parseLong(decrypted_code.get(2));
		receiver_online.balance+=Long.parseLong(decrypted_code.get(2));
		String upi_id_sender=user_data.get(sender_online.user_id).upi_id;
		String upi_id_receiver=user_data.get(receiver_online.user_id).upi_id;
		System.out.println("Local(offline) Balance Updated Succesfully of both Sender and Receiver for transaction id :"+decrypted_code.get(3));
		System.out.println("\nLocal(offline) balance is updated to Rps."+sender_online.balance+" for sender "+sender_online.user_name);
		System.out.println("Local(offline) balance is updated to Rps."+receiver_online.balance+" for receiver "+receiver_online.user_name);
		System.out.println("\nServer(online) balance is still Rps."+server.ServeruserData.get(upi_id_sender).balance +" for sender "+server.ServeruserData.get(upi_id_sender).user_name);
		System.out.println("Server(online) balance is still Rps."+server.ServeruserData.get(upi_id_receiver).balance +" for receiver "+server.ServeruserData.get(upi_id_receiver).user_name);
	}
	void get_transaction_history() {  //will need a stack
		System.out.println("Enter user id : ");
		int count=1;
		String usid = sc.nextLine();
		System.out.println("Enter pin : ");
		int uspin = sc.nextInt();
		user curr_user = user_data.get(usid);
		if(curr_user.pin==uspin) {
			for (int i = curr_user.s.size() - 1; i >= 0; i--) {
				System.out.println("\nTransaction details :"+count); 
			    System.out.println("Sender : "+curr_user.s.get(i).sender_id);
			    System.out.println("Reciever : "+curr_user.s.get(i).reciever_id);
			    System.out.println("Amount : "+curr_user.s.get(i).amount);
			    System.out.println("Transaction ID  : "+curr_user.s.get(i).transaction_id);
			    System.out.println("Date : "+curr_user.s.get(i).timestamp);
			    count++;
			}
		}
		
	}
	void checkBalance() {
		  System.out.print("Enter User ID to check balance: ");
		    String user_id = sc.nextLine();
	    if (user_data.containsKey(user_id)) {
	        long balance = user_data.get(user_id).balance;
	        System.out.print("Enter your 4-digit PIN: ");
	        int enteredPin;
	        try {
	            enteredPin = Integer.parseInt(sc.nextLine());
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid PIN format.");
	            return;
	        }

	        if (user_data.get(user_id).pin != enteredPin) {
	            System.out.println("PIN verification failed.");
	            return;
	        }

	        System.out.println("User ID: " + user_id);
	        System.out.println("User Name: " + user_data.get(user_id).user_name);
	        System.out.println("Current Balance: Rs. " + balance);
	        System.out.println("Server Balance: Rs. "+Server.ServeruserData.get(user_data.get(user_id).upi_id).balance);
	    } else {
	        System.out.println("User ID not found.");
	    }
	}
	
	
	void display_users() {
		System.out.println("\n--------------------------User in Local DataBase-----------------------\n");
		for (String id : user_data.keySet()) {
	        user u = user_data.get(id);
	        System.out.println("User ID: " + u.user_id + ", Name: " + u.user_name + ", UPI: " + u.upi_id + 
	                           ", Balance: " + u.balance + ", Phone Number: " + u.phno + ", Account Number: " + u.acc_no + 
	                           ", PIN: " + u.pin + ", Hashed Password: " + u.hashed_password);
	    }
	}

	String sender_id;
	String receiver_id;
	long amt;
	String transaction_id;

	void authenticate(String dec_sender, String dec_receiver, long dec_amount, String dec_tid) {
	    if (sender_id.equals(dec_sender) &&
	        receiver_id.equals(dec_receiver) &&
	        amt == dec_amount &&
	         dec_tid.equals(transaction_id)) {
	        System.out.println("\nAuthentication successful: Transaction details match!! Now you can proceed");
	    } else {
	        System.out.println("Authentication failed: Transaction details do not match..Try Again !!");
	    }
	}
		
	
	void send() {  //core
		System.out.print("\nEnter Your User ID (Sender): ");
	    sender_id = sc.next();
	    // Verify if sender_id exists in user_data HashMap
	    if (!user_data.containsKey(sender_id)) {
	        System.out.println("Invalid Sender ID. Please check the ID.");
	        return;  // Exit the method if invalid sender
	    }
	    
	    System.out.print("Enter Receiver User ID: ");
	    receiver_id = sc.next(); 
	 // Verify if receiver_id exists in user_data HashMap
	    if (!user_data.containsKey(receiver_id)) {
	        System.out.println("Invalid Receiver ID. Please check the ID.");
	        return;  // Exit the method if invalid receiver
	    }

	    System.out.print("Enter Amount: ");
	    amt = sc.nextLong(); 
	    user sender = user_data.get(sender_id); 
	    if (amt > sender.balance) {
	        System.out.println(" Insufficient balance! Transaction cannot be processed.");
	        return;  // Exit the method if the sender does not have enough balance
	    }
	    
        System.out.println("Enter the pin(Sender)");
        int verifypin=sc.nextInt();
        if(sender.pin!=verifypin)
        {
        	System.out.println("Invalid pin . Please check the pin.");
	        return;
        }
        	   transaction_id=tm.generate_transid(sender);

	    String encrypted_code = cryptography.encrypt(sender_id, receiver_id, amt, transaction_id);
	   // System.out.println("Encrypted Transaction Code: " + encrypted_code);
	    generateQR(encrypted_code);
	}
	
	void receieve() {  
		 sc.nextLine(); 
		 System.out.println("Enter user id : ");
		 String rec_uid = sc.nextLine();
		 System.out.println("Enter pin : ");
		 int rec_pin = sc.nextInt();
		 sc.nextLine();
		 System.out.print("\nPaste Encrypted Code: ");
		    String code = sc.nextLine();

		    ArrayList<String> decrypted_code = cryptography.decrypt(code);
		    String dec_sender="";
		    String dec_receiver="";
		    String amountStr="";
		    String dec_tid="";
		    long dec_amt=0;
		    if (decrypted_code.size() == 4) {
		         dec_sender = decrypted_code.get(0);
		         dec_receiver = decrypted_code.get(1);
		          amountStr = decrypted_code.get(2);
		          dec_tid = decrypted_code.get(3);
		       
		        // Convert the amount string to long
		        if (amountStr.matches("[0-9]+")) {  // Only digits allowed
		            dec_amt = Long.parseLong(amountStr);  // Parse the amount string to long
		            System.out.println("Amount: " + dec_amt);  // Debugging output for amount
		        }
		        if(user_data.get(dec_receiver).pin!=rec_pin) {
		        	System.out.println("Invalid pin , cannot proceed transaction !");
		        	return;
		        }
		        if(!(dec_receiver).equals(rec_uid)) 
		        		{
		        	System.out.println("Invalid recevier , cannot proceed transaction !");
		        	return;
		        }
		        // Call authentication method
		        authenticate(dec_sender, dec_receiver, dec_amt, dec_tid);

		        System.out.println("\n" + user_data.get(dec_sender).user_name + " is paying "+user_data.get(dec_receiver).user_name + " Rs." + dec_amt+"\n");
		        
		        System.out.print("Type 'ok' to proceed: ");
		        String confirm = sc.nextLine();

		        if (confirm.equalsIgnoreCase("ok")) {
		            System.out.println("Offline Transaction successfully stored! Transaction ID: " + dec_tid);
		            //update offline balance
		            update_balance(decrypted_code);
		        } else {
		            System.out.println("Transaction cancelled.");
		        }

		    } else {
		        System.out.println("Invalid code or decryption failed.");
		    }	
		    user_data.get(sender_id).s.push(new transactions(dec_sender,dec_receiver,dec_amt,dec_tid,LocalDate.now()));
		    user_data.get(receiver_id).s.push(new transactions(dec_sender,dec_receiver,dec_amt,dec_tid,LocalDate.now()));
		    //
		    tm.create(code);
	}
	String return_upi_id(String sender_id) {
		return user_data.get(sender_id).upi_id;
	}
	void generateQR(String qrText)
	{
		  // Text to encode
        int width = 300;
        int height = 300;
        String filePath = "C:\\Users\\Bhargavi Dange\\Desktop\\QRCODES\\qrcode.png";

        // Encoding hints
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, width, height, hints);

            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("✅ QR Code generated successfully at: " + path.toAbsolutePath());

        } catch (WriterException | IOException e) {
            e.printStackTrace();}

	}
}
class transactions{
	String sender_id;
	String reciever_id;
	double amount;
	String transaction_id;
	       //pending or completed 
	String status;//daily 10,000
	LocalDate timestamp; //date of trans for sorting 
	//hash
	transactions(String sender_id,
	String reciever_id,
	double amount,
	String transaction_id,     
	LocalDate timestamp){
		this.sender_id=sender_id;
		this.reciever_id=reciever_id;
		this.amount=amount;
		this.transaction_id=transaction_id;
		this.timestamp=timestamp;
		status="offline";
	}
}
class transaction_management{
	 ArrayList<transactions> pending_queue = new ArrayList<>();
	 ArrayList<transactions> completed_transactions = new ArrayList<>();
	 HashMap<String,transactions> pending = new HashMap<>();
	 HashMap<String,transactions> completed = new HashMap<>();
	 user_management um;
	 Server obj1;
	 transaction_management()
	 {
		 
	 }
	 public transaction_management(user_management um, Server obj1) {
		    this.um = um;
		    this.obj1 = obj1;
		}
	 
	 String generate_transid(user obj) {
		    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		    String transId;
		    Random rand = new Random();
		    
		    do {
		        StringBuilder sb = new StringBuilder();
		        for (int i = 0; i < 5; i++) { // 5-character ID
		            sb.append(chars.charAt(rand.nextInt(chars.length())));
		        }
		        transId = sb.toString();
		    } while (pending.containsKey(transId) || completed.containsKey(transId));

		    return transId;
		}

	 void create(String code) {
	 ArrayList <String>attributes  = new ArrayList<>();
	 cryptography obj = new cryptography();
	 attributes = obj.decrypt(code);
	 String sender = attributes.get(0);
	 String reciever = attributes.get(1);
	 long amount = Long.parseLong(attributes.get(2));
	 String transid = attributes.get(3);
	 LocalDate timestamp = LocalDate.now();
	 pending_queue.add(new transactions(sender,reciever,amount,transid,timestamp));
	 upadjustment(pending_queue.size()-1);
	 heapsort();
	 pending.put(transid,new transactions(sender,reciever,amount,transid,timestamp));
	 //um_obj.user_data.get(sender_id).stack.push
	 }
	
	 void make_online() {
		 int n = pending_queue.size();
		 for(int i=0;i<n;i++) {
			 transactions curr = remove();
			 curr.status = "online";
			 completed.put(curr.transaction_id, curr);
			 completed_transactions.add(curr);
			 pending.remove(curr.transaction_id);
			 String sender_upi = um.return_upi_id(curr.sender_id);
			 String reciever_upi = um.return_upi_id(curr.reciever_id);
			 obj1.update_server_balance(sender_upi, reciever_upi, curr.amount);
		 }
		 
	 }
	 void upadjustment(int i) {
		 while(i>0) {
			 int parent = (i-1)/2;
			 if(pending_queue.get(parent).timestamp.isBefore(pending_queue.get(i).timestamp)) {
				 break;
			 }
			 else if(pending_queue.get(parent).timestamp.isAfter(pending_queue.get(i).timestamp) 
					 || (pending_queue.get(parent).timestamp.equals(pending_queue.get(i).timestamp)
							 && pending_queue.get(parent).amount>pending_queue.get(i).amount)) {
				 transactions temp = pending_queue.get(i);
				 pending_queue.set(i, pending_queue.get(parent));
				 pending_queue.set(parent, temp);
				 i=parent;
			 }
		 }
	 }
	 void heapsort() {
		 for(int i=(pending_queue.size()-1)/2;i>=0;i--) {
			 downadjustment(i,pending_queue.size()-1);
		 }
	 }
	 void downadjustment(int i, int n) {
		 while (2 * i + 1 <= n) { // while there is at least one child
		        int leftchild = 2 * i + 1;
		        int rightchild = 2 * i + 2;
		        int smallest = i;

		        if (leftchild <= n) {
		            if (pending_queue.get(leftchild).timestamp.isBefore(pending_queue.get(smallest).timestamp) ||
		                (pending_queue.get(leftchild).timestamp.equals(pending_queue.get(smallest).timestamp) &&
		                 pending_queue.get(leftchild).amount < pending_queue.get(smallest).amount)) {
		                smallest = leftchild;
		            }
		        }

		        if (rightchild <= n) {
		            if (pending_queue.get(rightchild).timestamp.isBefore(pending_queue.get(smallest).timestamp) ||
		                (pending_queue.get(rightchild).timestamp.equals(pending_queue.get(smallest).timestamp) &&
		                 pending_queue.get(rightchild).amount < pending_queue.get(smallest).amount)) {
		                smallest = rightchild;
		            }
		        }

		        if (smallest != i) {
		            transactions temp = pending_queue.get(i);
		            pending_queue.set(i, pending_queue.get(smallest));
		            pending_queue.set(smallest, temp);
		            i = smallest;
		        } else {
		            break;
		        }
		    }
     }
	 transactions remove() {
		 if (pending_queue.isEmpty()) {
		        return null; // or throw exception if preferred
		    }

		    transactions removedTransaction = pending_queue.get(0);

		    // Replace root with the last element
		    transactions last = pending_queue.remove(pending_queue.size() - 1);

		    // If queue is not empty after removal, place last at root and heapify
		    if (!pending_queue.isEmpty()) {
		        pending_queue.set(0, last);
		        downadjustment(0, pending_queue.size() - 1);
		    }

		   return removedTransaction;
		   }
	 void display_transactions() {
		    System.out.println("\n PENDING TRANSACTIONS:");
		    if (pending_queue.isEmpty()) {
		        System.out.println("No pending transactions.");
		    } else {
		        for (transactions t : pending_queue) {
		            System.out.println("Transaction ID: " + t.transaction_id);
		            System.out.println("Sender: " + t.sender_id);
		            System.out.println("Receiver: " + t.reciever_id);
		            System.out.println("Amount: ₹" + t.amount);
		            System.out.println("Date: " + t.timestamp);
		            System.out.println("Status: " + t.status);
		            System.out.println("---------------------------");
		        }
		    }

		    System.out.println("\n COMPLETED TRANSACTIONS:");
		    if (completed_transactions.isEmpty()) {
		        System.out.println("No completed transactions.");
		    } else {
		        for (transactions t : completed_transactions) {
		            System.out.println("Transaction ID: " + t.transaction_id);
		            System.out.println("Sender: " + t.sender_id);
		            System.out.println("Receiver: " + t.reciever_id);
		            System.out.println("Amount: ₹" + t.amount);
		            System.out.println("Date: " + t.timestamp);
		            System.out.println("Status: " + t.status);
		            System.out.println("---------------------------");
		        }
		    }
		}

}
class cryptography{
	private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "1234567890123456"; // 16-char key for AES-128

    // Encrypt function
    public static String encrypt(String sender_id, String receiver_id, long amount, String trans_id) {
        try {
            String data = sender_id + "," + receiver_id + "," + amount + "," + trans_id;

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Decrypt function
    public static ArrayList<String> decrypt(String encrypted_string) {
        ArrayList<String> result = new ArrayList<>();
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encrypted_string);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);

            // Split decrypted text by comma
            String[] parts = decryptedText.split(",", 4);
            for (String part : parts) {
                result.add(part);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
class Main 
{
	public static void main(String[] args)
	{
		
            cryptography obj = new cryptography();
            Server server = new Server();
	        server.loadserverdata(); // Load the dummy users into server
	        Scanner sc = new Scanner(System.in);
	        user_management um = new user_management(server); 

	        // Initialize transaction_management and pass user_management object to it
	        transaction_management tm = new transaction_management(um,server); 

	        // Set transaction_management in user_management object
	        um.setTransactionManagement(tm);

	        int mainChoice;
	        do {
	            System.out.println("\nSelect Mode:");
	            System.out.println("1. User");
	            System.out.println("2. System");
	            System.out.println("0. Exit");
	            System.out.print("Enter your choice: ");
	            mainChoice = sc.nextInt();

	            switch (mainChoice) {
	                case 1:  // USER Side
	                    int userChoice;
	                    do {
	                        System.out.println("\nUser Panel:");
	                        System.out.println("1. Sign Up");
	                        System.out.println("2. Login");
	                        System.out.println("3. Update Details");
	                        System.out.println("4. Send Amount (Offline)");
	                        System.out.println("5. Receive Amount");
	                        System.out.println("6. Check balance");
	                        System.out.println("7. View Tansaction History");
	                        System.out.println("0. Back");
	                        System.out.print("Enter your choice: ");
	                        userChoice = sc.nextInt();

	                        switch (userChoice) {
	                            case 1:
	                                um.add_user();
	                                break;
	                            case 2:
	                                um.login();
	                                break;
	                            case 3:
	                                um.update_details();
	                                break;
	                            case 4:
	                                um.send();
	                                break;
	                            case 5:
	                                um.receieve();
	                                break;
	                            case 6:
	                            	um.checkBalance();
	                            	break;
	                            case 7:
	                            	um.get_transaction_history();
	                            	break;
	                            case 0:
	                                System.out.println("🔙 Returning to Main Menu...");
	                                break;
	                            default:
	                                System.out.println("❌ Invalid choice. Try again.");
	                        }
	                    } while (userChoice != 0);
	                    break;

	                case 2:  // SYSTEM Side
	                    int systemChoice;
	                    do {
	                        System.out.println("\nSystem Panel:");
	                        System.out.println("1. Display All Users");
	                        System.out.println("2. Make All Transactions Online");
	                        System.out.println("3. Display all pending and completed transactions");
	                        System.out.println("0. Back");
	                        System.out.print("Enter your choice: ");
	                        systemChoice = sc.nextInt();

	                        switch (systemChoice) {
	                            case 1:
	                                um.display_users();
	                                break;
	                            case 2:
	                                tm.make_online();
	                                break;
	                            case 3:
	                                tm.display_transactions();
	                                break;
	                            case 0:
	                                System.out.println(" Returning to Main Menu...");
	                                break;
	                            default:
	                                System.out.println(" Invalid choice. Try again.");
	                        }
	                    } while (systemChoice != 0);
	                    break;

	                case 0:
	                    System.out.println(" Exiting application. Thank you!");
	                    break;

	                default:
	                    System.out.println("Invalid choice. Please enter 1, 2,3 or 0.");
	            }

	        } while (mainChoice != 0);
	    }
	}
