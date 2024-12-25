package root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RpcOperations {

	DataInputStream input;
	DataOutputStream output;
	Socket socket;
	ObjectOutputStream objOutput;
	ObjectInputStream objInput;
		
	public RpcOperations() {}
	
	public RpcOperations(DataInputStream input, DataOutputStream output, Socket socket) {
		this.input = input;
		this.output = output;
		this.socket = socket;
	}

	public void calculate_pi() {
		try {
			output.writeUTF("pi");
			objInput = new ObjectInputStream(socket.getInputStream());
			Map<String,Object> map = (HashMap)objInput.readObject();
			System.out.println("Incoming message from server:");
			System.out.println((String)map.get("text")+" "+(Double)map.get("answer"));
		}
		catch(Exception e) {
			System.out.println("Error in calculating pi: "+e.getLocalizedMessage());
		}
	}
	
	public void add() {
		try {
			output.writeUTF("add");
			Scanner sc = new Scanner(System.in);
			if(input.readUTF().equals("ok")) {
				Map<String, Integer> param = new HashMap<>();
				System.out.println("Enter 2 numbers to add\nNum 1: ");
				param.put("num1", sc.nextInt());
				System.out.println("Num 2: ");
				param.put("num2", sc.nextInt());
				objOutput = new ObjectOutputStream(socket.getOutputStream());
				objOutput.writeObject(param);
			}
			objInput = new ObjectInputStream(socket.getInputStream());
			Map<String,Object> map = (HashMap)objInput.readObject();
			System.out.println("Incoming message from server:");
			System.out.println((String)map.get("text")+" "+(Integer)map.get("answer"));
		}
		catch(Exception e) {
			System.out.println("Error in addition: "+e.getLocalizedMessage());
		}
	}
	
	public void sort() {
		try {
			output.writeUTF("sort");
			Scanner sc = new Scanner(System.in);
			if(input.readUTF().equals("ok")) {
				System.out.println("Enter the size of array to sort: ");
				int size = sc.nextInt();
				int[] arr = new int[size];
				System.out.println("Enter "+size+" numbers \n: ");
				for(int i=0;i<size;i++) {
					System.out.println("Num "+(i+1)+": ");
					arr[i] = sc.nextInt();
				}
				System.out.println("Sorting Array: "+Arrays.toString(arr));
				objOutput = new ObjectOutputStream(socket.getOutputStream());
				objOutput.writeObject(arr);
				objInput = new ObjectInputStream(socket.getInputStream());
				Map<String,Object> map = (HashMap)objInput.readObject();
				System.out.println("Incoming message from server:");
				System.out.println((String)map.get("text")+" "+Arrays.toString((int[])map.get("answer")));
			}
		}
		catch(Exception e) {
			System.out.println("Error in sorting: "+e.getLocalizedMessage());
		}
	}
	
	public void matrix_multiply() {
		try {
			output.writeUTF("matrix");
			Scanner sc = new Scanner(System.in);
			if(input.readUTF().equals("ok")) {
				System.out.println("2x2 Matrix Multiplication: 3 matrices");
				System.out.println(" _            _\n|a(0,0)  a(0,1)|\n|              |");
				System.out.println("|a(1,0)  a(1,1)|\n|_            _|");
				Map<String, int[][]> map = new HashMap<>();
				for(int mat=1;mat<=3;mat++) {
					System.out.println("\nEnter the elements of matrix "+mat+": ");
					int[][] matrix = new int[2][2];
					for(int i=0;i<=1;i++) {
						for(int j=0;j<=1;j++) {
							System.out.println("a("+i+","+j+"): ");
							matrix[i][j] = sc.nextInt();
						}
					}
					map.put("matrix"+Integer.toString(mat), matrix);
				}
				objOutput = new ObjectOutputStream(socket.getOutputStream());
				objOutput.writeObject(map);
				objInput = new ObjectInputStream(socket.getInputStream());
				Map<String,Object> resultMap = (HashMap)objInput.readObject();
				System.out.println("Incoming message from server:");
				System.out.println((String)resultMap.get("text"));
				int[][] result = (int[][])resultMap.get("answer");
				for(int i=0;i<=1;i++) {
					System.out.println("> "+result[i][0]+", "+result[i][1]);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error in matrix multiplication: "+e.getLocalizedMessage());
		}
	}
}
