package async;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class AsyncServer {

	DataInputStream input;
	DataOutputStream output;
	Socket socket;
	AsyncRpcOperations rpc;
	
	public AsyncServer() {}

	public AsyncServer(Socket socket, DataInputStream input, DataOutputStream output) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.rpc = new AsyncRpcOperations(input, output, socket);		
	}
	
	public void operate(String command) {
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
	
	public void initiate() {
		try {
			String inputString = ""; 
			while(true) { 
				input = new DataInputStream(socket.getInputStream());
				inputString = input.readUTF();
				System.out.println("Incoming message: "+inputString);
				if(inputString.equals("exit")) 
					break; 
				else {
					operate(inputString); 
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error occurred: "+e.getLocalizedMessage());
		}
	}
	
}
