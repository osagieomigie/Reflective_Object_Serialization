import java.io.*;
import java.net.*;
import javax.json.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import reflective.serialization1.*;
import serialization.Deserializer;
import serialization_xml.Deserializer_XML;

public class Client {
	
	private static final String SERVER_IP = "127.0.0.1"; //"127.0.0.1"; UC server 136.159.5.27
	private static final int SERVER_PORT = 8080; 
	
	public static void main(String [] args) throws UnknownHostException, IOException {
		
		Socket socket = new Socket(SERVER_IP, SERVER_PORT);
		System.out.println("Client up!");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Waiting for objects...\n");
		
		String serverResponse = " ";
		String serializedObject = " ";
		String stringType = "";
		
		while ((serverResponse = input.readLine()) != null) {
			
			// server has ended communication
			if (serverResponse.equals("bye")) {
				System.out.println("Goodbye Server!");
				socket.close();
				System.exit(0);
			}
			
			System.out.println("New object arrived!");
			JsonReader json_serialized_reader = Json.createReader(new StringReader(serverResponse));
			JsonObject json_serialized_object = json_serialized_reader.readObject();
			
			//System.out.println(serverResponse);
			// get need string type and the serialized string from the response 
			stringType = json_serialized_object.getString("string_type");
			serializedObject = json_serialized_object.getString("serialized_string");
			System.out.println(serializedObject);
			
			// visualize object from server 
			Visualizer visualizer = new Visualizer();
			Object obj = null;
			
			// object is serialized in json format
			if (stringType.equals("JSON")) {
				JsonReader json_reader = Json.createReader(new StringReader(serializedObject));
				JsonObject json_object = json_reader.readObject();
				
				try {
					obj = Deserializer.deserializeObject(json_object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// object is serialized in xml format
			else {
				try {
					obj = Deserializer_XML.deserializeObject(convertStringToXMLDocument(serializedObject));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			visualizer.inspect(obj, true);
			
			System.out.println("\nWaiting for objects...\n");
		}
		
	}
	
	// Method source: https://howtodoinjava.com/java/xml/parse-string-to-xml-dom/
    private static Document convertStringToXMLDocument(String xmlString) 
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
}
