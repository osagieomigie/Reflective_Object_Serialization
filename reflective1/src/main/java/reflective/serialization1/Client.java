import java.io.*;
import java.net.*;
import javax.json.*;
import reflective.serialization1.*;
import serialization.Deserializer;

public class Client {
	
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 8080; 
	
	public static void main(String [] args) throws UnknownHostException, IOException {
		
		Socket socket = new Socket(SERVER_IP, SERVER_PORT);
		System.out.println("Client up!");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Waiting for objects...");
		
		String serverResponse = " ";
		
		while ((serverResponse = input.readLine()) != null) {
			
			// server has ended communication
			if (serverResponse.equals("bye")) {
				System.out.println("Goodbye Server!");
				socket.close();
				System.exit(0);
			}
			
			System.out.println("New object arrived!");
			System.out.println(serverResponse);
			
			// visualize object from server 
			Visualizer visualizer = new Visualizer();
			
			JsonReader json_reader = Json.createReader(new StringReader(serverResponse));
			JsonObject json_object = json_reader.readObject();
			
			Object obj = null;
			try {
				obj = Deserializer.deserializeObject(json_object);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			visualizer.inspect(obj, true);
			
			System.out.println("Waiting for objects...");
		}
		
	}
}
