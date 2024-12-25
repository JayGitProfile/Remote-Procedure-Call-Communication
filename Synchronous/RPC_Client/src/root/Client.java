package root;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
	public static void operate(int optionNum, DataOutputStream output, RpcOperations rpc) throws IOException {
		switch(optionNum) {
			case 1:
				rpc.calculate_pi();
				break;
			case 2:
				rpc.add();
				break;
			case 3:
				rpc.sort();
				break;
			case 4:
				rpc.matrix_multiply();
				break;
			case 5:
				output.writeUTF("exit");
				break;
		}
	}

	public static void main(String args[])
	{
		try { 
			Socket socket = new Socket("localhost", 8090);
			System.out.println("Connected with server on port 8090");
			Scanner sc = new Scanner(System.in);
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String option = "";
			while(!option.equals("5")) {
				System.out.println("----------------------------------------------\n");
				System.out.println("Select any operation (input the option number):");
				System.out.println("1. CALCULATE PI\n2. ADD 2 NUMBERS\n3. SORT ARRAY\n4. MATRIX MULTIPLY\n5. EXIT");
				option = sc.next();
				if(option.matches("[0-9]+")) {
					int optionNum = Integer.parseInt(option);
					if(optionNum<=5 && optionNum>=1) {
						operate(optionNum, output, new RpcOperations(input, output, socket));
					}
					else {
						System.out.println("Incorrect Entry");
					}
				}
				else {
					System.out.println("Incorrect Entry");
				}
			}
			socket.close();
			System.out.println("Connection Terminated!!!");
		}
		catch(Exception e) {
			System.out.println("Error occurred: "+e.getLocalizedMessage());
		}
	}
} 
