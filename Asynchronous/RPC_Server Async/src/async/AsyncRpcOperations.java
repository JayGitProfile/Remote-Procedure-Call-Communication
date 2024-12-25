package async;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AsyncRpcOperations {

	DataInputStream input;
	DataOutputStream output;
	Socket socket;
	ObjectOutputStream objOutput;
		
	public AsyncRpcOperations() {}
	
	public AsyncRpcOperations(DataInputStream input, DataOutputStream output, Socket socket) {
		this.input = input;
		this.output = output;
		this.socket = socket;
	}
	
	public void calculate_pi() {
		new Thread(() -> {
			int count = 10;
			int m = 1;
			int n = 0;
			double pi = 0.0;
			final double SQRT_12 = Math.sqrt(12);
			while (count > n) {
			    pi += SQRT_12 * (Math.pow(-1, n)/(m * Math.pow(3, n)));
			    m += 2;
			    n++;
			}
			try {
				Thread.sleep(10000);
				Map<String, Object> map = new HashMap<>();
				map.put("text", "> Value of pi is: ");
				map.put("answer", pi);
				objOutput = new ObjectOutputStream(socket.getOutputStream());
				objOutput.writeObject(map);
				System.out.println("finish");				
			} catch (Exception e) {
				System.out.println("Error in calculating pi: "+e.getLocalizedMessage());
			}
		}).start();
	}
	
	public void add(int i, int j) {
		new Thread(() -> {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("text", "> Sum of "+i+" and "+j+" is: ");
				map.put("answer", (i+j));
				objOutput = new ObjectOutputStream(socket.getOutputStream());
				objOutput.writeObject(map);
			} catch (IOException e) {
				System.out.println("Error in addition: "+e.getLocalizedMessage());
			}
		}).start();
	}
	
	public void sort(int[] arr) {
		int size = arr.length;  
		int temp = 0;  
		for(int i=0;i<size;i++) {  
			for(int j=1;j<(size-i);j++){  
				if(arr[j-1]>arr[j]) {  
					temp = arr[j-1];  
					arr[j-1] = arr[j];  
					arr[j] = temp;  
				}               
			}  
		}
		Map<String, Object> map = new HashMap<>();
		map.put("text", "> Sorted array: ");
		map.put("answer", arr);
		try {
			objOutput = new ObjectOutputStream(socket.getOutputStream());
			objOutput.writeObject(map);
		} catch (IOException e) {
			System.out.println("Error in sorting: "+e.getLocalizedMessage());
		}
	}
	
	public void matrix_multiply(int[][] matrix1, int[][] matrix2, int[][] matrix3) {
		int[][] result = multiply(multiply(matrix1, matrix2), matrix3);
		Map<String, Object> map = new HashMap<>();
		map.put("text", "> Result matrix after multiplication: ");
		map.put("answer", result);
		try {
			objOutput = new ObjectOutputStream(socket.getOutputStream());
			objOutput.writeObject(map);
		} catch (IOException e) {
			System.out.println("Error in matrix multiplication: "+e.getLocalizedMessage());
		}
	}
	
	public int[][] multiply(int[][] matrix1, int[][] matrix2) {
		int[][] result = new int[2][2];
		for (int i=0;i<2;i++ ) {
			for (int j=0;j<2;j++) {
				int sum=0;
				for (int k=0;k<2;k++) {
					sum +=matrix1[i][k]*matrix2[k][j] ;
				}
			result[i][j]=sum;
			}
		}
		
		return result;
	}
}
