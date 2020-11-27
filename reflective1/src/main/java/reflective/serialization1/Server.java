import java.io.*;
import java.net.*;
import java.util.Scanner;
import reflective.serialization1.*;
import java.util.*;
import javax.json.*;


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
			char serializationType = ' ';
			char userChar = ' ';
	    	String serializedObject = "";
	    	String serverMessage = "";
	    	
	    	while (Character.toLowerCase(mainUserInput.charAt(0)) != 'q') {
	    		System.out.println("Do you want to perform the serialization in json or xml?");
	    		System.out.println(" (j) Enter 'j' or 'J' for json");
	    		System.out.println(" (x) Enter 'x' or 'X' for xml");
	    		System.out.println(" (q) Enter 'q' to quit");
	    		mainUserInput = mainInput.nextLine();
	    		serializationType = Character.toLowerCase(mainUserInput.charAt(0));
	    		
	    		//close connection, user is done 
	    		if (serializationType == 'q') {
	    			System.out.println("Server shutting down...\nGoodbye!");
	    			out.println("bye");
	    			mainInput.close();
	    			listener.close();
	    			break;
	    		}
	    		
	    		if (Character.isDigit(serializationType)) {
	    			System.out.println("Don't be a clown, enter in the correct char!");
		    		continue;
	    		}
	    		
		    	System.out.println("\nSelect an object to serialize by selection from the following options by entering the number in front them (e.g. 'a' or 'A'). To exit, enter 'q' or 'Q'\n");
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
	    		
	    		JsonObjectBuilder parseOption = Json.createObjectBuilder();
	    		serializedObject = ObjectCreator.createObject(userChar, serializationType);
	    		
	    		
	    		if (!serializedObject.equals("error")) {
	    			// add string to be serialized 
		    		parseOption.add("serialized_string", serializedObject);
		    		
		    		// set the string type 
		    		if (serializationType == 'x') { // xml string
		    			parseOption.add("string_type", "XML"); 
		    		}else { // json string
		    			parseOption.add("string_type", "JSON"); 
		    		}
		    		
		    		// convert json to string 
		    		JsonObject tmpJ = parseOption.build();
		    		serverMessage = tmpJ.toString();
		    		
		    		// send serialized object 
					System.out.println("Sending object...");
					out.println(serverMessage);
					System.out.println("Object sent!\n");
	    		}
	    	}
		
		}
}
