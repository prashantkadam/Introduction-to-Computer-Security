import java.io.*;
import java.net.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

//import RSA.RSA;

class creditDetails{
String custome_name;
int creditcard_num;
double balance;
public creditDetails(int cc_num,String c_name, double balance){
	
		this.custome_name = c_name;
		this.creditcard_num = cc_num;
		this.balance = balance;
	}
@Override
public String toString() {
	String cc_details = "creditcard_num : "+custome_name+ ", creditcard_num : "+ creditcard_num+ ", "+ "balance : $" + balance;
	return cc_details;
}

}

public class Bank {
	//static String path = "C:\\CS_Project\\";
	static String path = "";
	static LinkedHashMap<Integer, creditDetails> user_credit_details = new LinkedHashMap<Integer, creditDetails>();
	public Bank() {
		populateUserCreditDetails();
		//System.out.println("User Credit details populated successfully");
	}
	private void populateUserCreditDetails() {
		BufferedReader buffer_reader = null;

		try {
			String line;
			
			buffer_reader = new BufferedReader(new FileReader(path+"balance.txt"));
			while (((line = buffer_reader.readLine()) != null)) {
				//System.out.println(line);
				String[] item_fields = line.split(", ");
				creditDetails new_obj = new creditDetails(Integer.parseInt(item_fields[1]),item_fields[0], Double.parseDouble(item_fields[2]));
				user_credit_details.put(Integer.parseInt(item_fields[1]), new_obj );
				/*for (String string : parts) {
					System.out.print(string+"   ");
				}*/
				
			}
			for (int key : user_credit_details.keySet()) {
				//System.out.println(user_credit_details.get(key).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("****IN CATCH BLOCK -(populateUserCreditDetails)"+e.getMessage()+"****\n");
		} finally {	
		}
		
	}
	
	
	
	
	public static void main(String[] argv) {
		Bank b = new Bank();		
		try
		{
			if(argv.length < 1 || argv.length > 1){
				System.out.println("run as : java Bank <bank-port>");
				return;
			}
			int bankPortNumber=Integer.parseInt(argv[0]);
			ServerSocket bankSocket = new ServerSocket(bankPortNumber); 
			bankSocket.setReuseAddress(true);
			System.out.println("**Admin Bank :- Bank Running on Port Number \t "+bankPortNumber);
			while(true)
			{
			Socket conn = bankSocket.accept(); 
			

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
			PrintWriter out =new PrintWriter(conn.getOutputStream(),true);
			String clientSentence;
			clientSentence = in.readLine();
			//if(clientSentence==null)
			//		continue;
			//System.out.println("here");
			//System.out.println("FROM Purchasing server:" + clientSentence);
			out.println("Connection accepted - msg from bank!");

		
				byte [] data1=null;
				DataInputStream dIn = new DataInputStream(conn.getInputStream());
				
				int length = dIn.readInt(); 

				//System.out.println(length);                  
				if(length>0) {
    			data1 = new byte[length];
   				dIn.readFully(data1, 0, data1.length); 
				}
				//if(data1 == null){
				//	System.out.println("data1 is null");
				//	continue;
				//}
				//System.out.println("byte array in psystem"+data1.toString());
				ObjectInputStream inputStream1 = new ObjectInputStream(new FileInputStream(new File(path+"Prb.key")));
				PrivateKey privateKey1 = (PrivateKey) inputStream1.readObject();

				String decrypted1=RSA.decrypt(data1, privateKey1);
				//System.out.println("Decrypted Item details"+decrypted1);
				String[] customer_details = decrypted1.split("-");
				String Name = customer_details[0];
				//System.out.println("Name:"+Name);
				int cc_num_to_verify = Integer.parseInt(customer_details[1]);
				//verify CC number 
				creditDetails creditDetails_inAction = user_credit_details.get(cc_num_to_verify);
				String creditcard_holder;
				if(creditDetails_inAction!=null)
					creditcard_holder = creditDetails_inAction.custome_name;
				else
					creditcard_holder = "";
				boolean creditcard_verified = false;
				if(creditcard_holder.equals(Name)){
					//System.out.println("In verification if");
					byte [] data_price=null;
					length = dIn.readInt();                   
					if(length>0) {
	    			data_price = new byte[length];
	   				dIn.readFully(data_price, 0, data_price.length); 
					}					
					inputStream1 = new ObjectInputStream(new FileInputStream(new File(path+"Pup.key")));
					PublicKey publicKey_t = (PublicKey) inputStream1.readObject();
					String decrypted_t=RSA.decrypt(data_price, publicKey_t);
					//System.out.println("price Decrypted Item details"+decrypted_t);	
					creditDetails_inAction.balance = Double.parseDouble(decrypted_t);
					for (int key : user_credit_details.keySet()) {
						//System.out.println(user_credit_details.get(key).toString());
					}
					b.update_Balance();
					out.println("ok");
					creditcard_verified = true;
				}
				else{
					out.println("error");
				}
				if(creditcard_verified == true){					// read price
					
				}
				conn.close();
				//break;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("****IN CATCH BLOCK -(Bank Main Method)"+e.getMessage()+"****\n");
		}
	}
	private void update_Balance() {
		try {
			PrintWriter writer = new PrintWriter(path+ "balance.txt");
			writer.print("");
			for (int key : user_credit_details.keySet()) {
				creditDetails details = user_credit_details.get(key);
				writer.println(details.custome_name+", "+details.creditcard_num+", "+details.balance);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("****IN CATCH BLOCK -(update_Balance)"+e.getMessage()+"****\n");
		}
		
	}

}
