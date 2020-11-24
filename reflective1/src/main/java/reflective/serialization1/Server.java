import java.io.*;
import java.net.*;
import java.util.Scanner;
import reflective.serialization1.*;

public class Server {

		private static final int PORT = 8080; 
		public static void main(String[] args) throws IOException {
			
			ServerSocket listener = new ServerSocket(PORT);
			System.out.println("Server running\nWaiting for connections...");
			
			// accept connection from client 
			Socket client = listener.accept();
			System.out.println("Connection established!");
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			
			Scanner mainInput = new Scanner(System.in);
			String mainUserInput = "tmp";
			char userChar = ' ';
	    	String serializedObject = "";
	    	
	    	while (Character.toLowerCase(mainUserInput.charAt(0)) != 'q') {
		    	System.out.println("Select an object to serialize by selection from the following options by entering the number in front them (e.g. 'a' or 'A'). To exit, enter 'q' or 'Q'\n");
		    	System.out.println(" (a) A simple object with only primitives for instance variables");
		    	System.out.println(" (b) An object that contains references to other objects");
		    	System.out.println(" (c) An object that contains an array of primitives");
		    	System.out.println(" (d) An object that contains an array of object references");
		    	System.out.println(" (e) An object that contains an ArrayList of object references");
		    	System.out.println(" (q) Quit");
		    	
	    		mainUserInput = mainInput.nextLine();
	    		userChar = Character.toLowerCase(mainUserInput.charAt(0));
	    		
	    		// error check, user can only enter in a char 
	    		if (Character.isDigit(userChar)) {
	    			System.out.println("Don't be a clown, enter in a char!");
		    		continue;
	    		}
	    		
	    		//close connection, user is done 
	    		if (userChar == 'q') {
	    			System.out.println("Server shutting down...\nGoodbye!");
	    			out.println("bye");
	    			mainInput.close();
	    			listener.close();
	    			break;
	    		}
	    		
	    		serializedObject = ObjectCreator.createObject(userChar);
	    		
	    		if (!serializedObject.equals("error")) {
		    		// send serialized object 
					System.out.println("Sending object...");
					out.println(serializedObject);
					System.out.println("Object sent!\n");
	    		}
	    	}
		
		}
}
