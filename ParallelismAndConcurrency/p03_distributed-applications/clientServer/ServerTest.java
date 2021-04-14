package clientServer;

import java.io.*;
import java.net.Socket;

import common.Question;

/* Use this program as premeliminary test of your server ... */

public class ServerTest {
	
	private static BufferedReader inputChannel;
	private static PrintWriter outputChannel;
	private static Socket connection;
	
	public static void main (String [] args) throws IOException {
		String reply;
		
		System.out.println("\nSOLITRIVIA");
		System.out.println("----------\n");
		
		connect();
		System.out.println("Connected\n");
		
		// say hello...
		sendRequest("HELLO test client");
		reply = receiveReply();
		System.out.println("Server says: "+reply);
		
		// let's get a GEO question
		sendRequest("NEXT GEO");
		reply = receiveReply();
		System.out.println("Server says: "+Question.fromString(reply));
		
		// let's get a SCIENCE question
		sendRequest("NEXT   SCIENCE");
		reply = receiveReply();
		System.out.println("Server says: "+Question.fromString(reply));
		
		// let's get an ART question
		sendRequest("NEXT ART");
		reply = receiveReply();
		System.out.println("Server says: "+Question.fromString(reply));
		
		// let's get another  ART question
		sendRequest("NEXT ART");
		reply = receiveReply();
		System.out.println("Server says: "+Question.fromString(reply));
		
		// another
		sendRequest("NEXT ART");
		reply = receiveReply();
		System.out.println("Server says: "+reply);
		
		// ask for science questions until there's none left
		sendRequest("NEXT SCIENCE");
		reply = receiveReply();
		while (!reply.toUpperCase().contains("NO MORE")) {
			System.out.println("Server says: "+reply);
			sendRequest("NEXT SCIENCE");
			reply = receiveReply();
		}
		System.out.println("Server says: "+reply);
		
		// disconnect...
		System.out.println("\ndisconenecting...");
		sendRequest("STOP now");
		disconnect();
		System.out.println("...disconnected");
	}
	
	private static void connect () throws IOException {
        connection = new Socket("localhost", 4445);
        inputChannel = new BufferedReader(
                           new InputStreamReader(
                               connection.getInputStream()));
        outputChannel = new PrintWriter(connection.getOutputStream(), true);
    }
    
    private static void disconnect () throws IOException {
        inputChannel.close();
        outputChannel.close();       
        connection.close();
    }
    
    private static String receiveReply () throws IOException {
        return inputChannel.readLine();    
    }
    
    private static void sendRequest (String request) throws IOException {
        outputChannel.println(request);
    }
	

}
