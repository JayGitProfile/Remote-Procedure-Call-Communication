package async;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

import root.RpcOperations;

public class AsyncClient {
	
	DataOutputStream output;
	RpcOperations rpc;
	
	public AsyncClient() {}
	
	public AsyncClient(DataOutputStream output, RpcOperations rpc) {
		this.output = output;
		this.rpc = rpc;
	}
	
	public void operate(int optionNum) throws IOException {
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
		
	public void initiate() {
		String option = "";
		Scanner sc = new Scanner(System.in);
		try {
			while(!option.equals("5")) {
				System.out.println("----------------------------------------------\n");
				System.out.println("Select any operation (input the option number):");
				System.out.println("1. CALCULATE PI\n2. ADD 2 NUMBERS\n3. SORT ARRAY\n4. MATRIX MULTIPLY\n5. EXIT");
				option = sc.next();
				if(option.matches("[0-9]+")) {
					int optionNum = Integer.parseInt(option);
					if(optionNum<=5 && optionNum>=1) {
						operate(optionNum);
					}
					else {
						System.out.println("Incorrect Entry");
					}
				}
				else {
					System.out.println("Incorrect Entry");
				}
			}
		} catch(Exception e) {
			System.out.println("Error in Async: "+e.getLocalizedMessage());
		}
	}
}
