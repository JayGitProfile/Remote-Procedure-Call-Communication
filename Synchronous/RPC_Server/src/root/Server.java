package root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

	public static void operate(Socket socket, String command, DataInputStream input, DataOutputStream output, RpcOperations rpc) {
		ObjectOutputStream objOutput;
		ObjectInputStream objInput;
		try {
			switch(command) {
			case "pi":
				rpc.calculate_pi();
				break;
			case "add":
				output.writeUTF("ok");
				objInput = new ObjectInputStream(socket.getInputStream());
				Map<String,Integer> map = (HashMap)objInput.readObject();
				rpc.add(map.get("num1"), map.get("num2"));
				break;
			case "sort":
				output.writeUTF("ok");
				objInput = new ObjectInputStream(socket.getInputStream());
				int[] arr = (int[])objInput.readObject();
				rpc.sort(arr);
				break;
			case "matrix":
				output.writeUTF("ok");
				objInput = new ObjectInputStream(socket.getInputStream());
				Map<String,int[][]> matrixMap = (HashMap)objInput.readObject();
				rpc.matrix_multiply(matrixMap.get("matrix1"),matrixMap.get("matrix2"),matrixMap.get("matrix3"));
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
				while(true) { 
					input = new DataInputStream(socket.getInputStream());
					inputString = input.readUTF();
					System.out.println("Incoming message: "+inputString);
					if(inputString.equals("exit")) 
						break; 
					else {
						operate(socket, inputString, input, output, new RpcOperations(input, output, socket)); 
					}
				} 				
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