package root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import async.AsyncServer;

public class Server {
	
	public static void sync(Socket socket, String command, DataInputStream input, DataOutputStream output) {
		try {
			switch(command) {
			case "async":
				AsyncServer async = new AsyncServer(socket, input, output);
				async.initiate();
				break;
			case "defsync":
				break;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error: "+e.getLocalizedMessage());
		}
	}
	
	public static void main(String[] args) {
		boolean cleanExit = false;
		while(!cleanExit) {
			try {
				cleanExit = true;
				ServerSocket serverSckt = new ServerSocket(8090);
				System.out.println("-----------------------------------------------");
				System.out.println("Server started on port 8090");
				System.out.println("Listening for client connection...");  
				Socket socket = serverSckt.accept();
				System.out.println("Client connected");
				System.out.println("-----------------------------------------------");
				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				String inputString = ""; 
				 
				input = new DataInputStream(socket.getInputStream());
				inputString = input.readUTF();
				System.out.println("Incoming message: "+inputString);
				
				sync(socket, inputString, input, output);
				
				serverSckt.close();
				input.close();
				output.close();
				socket.close();
			}
			catch(Exception e) {
				System.out.println("Error occurred: "+e.getLocalizedMessage());
				cleanExit = false;
			}
			finally {
				if(!cleanExit) {
					System.out.println("Connection terminated due to above error."
							+"\nRestarting server...");
				}
				else {
					System.out.println("Connection closed with client");
				}
			}
		}
	}
}