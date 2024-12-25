package root;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import async.AsyncClient;

public class Client
{
	public static void sync(int optionNum, DataOutputStream output, RpcOperations rpc) throws IOException {
		switch(optionNum) {
			case 1:
				output.writeUTF("async");
				AsyncClient async = new AsyncClient(output, rpc);
				async.initiate();
				break;
			case 2:
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
			System.out.println("----------------------------------------------\n");
			System.out.println("Select sync method:");
			System.out.println("1. Asynchronous\n2. Deferred Synchronous");
			option = sc.next();
			if(option.matches("[0-9]+")) {
				int optionNum = Integer.parseInt(option);
				if(optionNum<=2 && optionNum>=1) {
					System.out.println("op "+optionNum);
					sync(optionNum, output, new RpcOperations(input, output, socket));
				}
				else {
					System.out.println("Incorrect Entry");
				}
			}
			else {
				System.out.println("Incorrect Entry");
			}
			socket.close();
			System.out.println("Connection Terminated!!!");
		}
		catch(Exception e) {
			System.out.println("Error occurred: "+e.getLocalizedMessage());
		}
	}
} 
