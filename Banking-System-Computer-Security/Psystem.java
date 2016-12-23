import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

//import RSA.RSA;

class Item{
	int itemId;
	String itemName;
	double itemPrice;
	int itemQuantity;
	
	public Item(int id,String ItemName,double ItemPrice,int ItemQuantity){
		this.itemId = id;
		this.itemName = ItemName;
		this.itemPrice = ItemPrice;
		this.itemQuantity = ItemQuantity;
	}
	
	@Override
	public String toString() {
		String itemDescription = itemId+ ", "+ itemName+ ", "+ "$" + itemPrice+", "+ itemQuantity;
		return itemDescription;
	}
}

public class Psystem {

	//static String path = "C:\\CS_Project\\";
	static String path = "";
	static Map<String, String> userDetails = new HashMap<String, String>();
	static Map<Integer, Item> ItemDetails = new HashMap<Integer, Item>();
	static int count_connection=0;
	public Psystem()
	{
		populateUserDetails();
		populateItemDetails();
		//System.out.println("User details and Item details populated successfully");
	}
	private void populateItemDetails() {
		BufferedReader buffer_reader = null;

		try {
			String id,password;
			
			buffer_reader = new BufferedReader(new FileReader(path+"item.txt"));
			while (((id = buffer_reader.readLine()) != null)) {
				//System.out.println(id);
				String[] item_fields = id.split(", ");
				/*for (String string : parts) {
					System.out.print(string+"   ");
				}*/
				Item new_item = new Item(Integer.parseInt(item_fields[0]),item_fields[1], Double.parseDouble(item_fields[2].replace('$',' ')), Integer.parseInt(item_fields[3]));
				ItemDetails.put(Integer.parseInt(item_fields[0]), new_item );
				//System.out.println();
			}
			for (int key : ItemDetails.keySet()) {
				//System.out.println(ItemDetails.get(key).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		    System.out.println("****IN CATCH BLOCK -(populateItemDetails)"+e.getMessage()+"****\n");
		} finally {	
		}
		
	}
	private void populateUserDetails() {
		
		BufferedReader buffer_reader = null;
		try {
			String id,password;
			buffer_reader = new BufferedReader(new FileReader(path + "password.txt"));
			while (((id = buffer_reader.readLine()) != null)) {
				//System.out.print(id);
				id = id.replaceFirst("ID: ", "");
				//System.out.println(id);
				//buffer_reader.readLine(); // this will read newline
				password = buffer_reader.readLine();
				password = password.replaceFirst("Password: ", "");
				//System.out.print(password);//
				//buffer_reader.readLine(); // this will read newline
				userDetails.put(id, password);
				id= null;password = null; // make null for next iteration
			}
			for (String key : userDetails.keySet()) {
				//System.out.println(key+"--"+ userDetails.get(key));
			}
		} catch (IOException e) {
			e.printStackTrace();
		    System.out.println("****IN CATCH BLOCK -(populateUserDetails)"+e.getMessage()+"****\n");
		} finally {	
		}
		
	}
	public static void main(String[] argv) {
		
		Psystem p=new Psystem();
		try
		{
			if(argv.length < 3 || argv.length > 3){
			System.out.println("run as : java Psystem <purchasing-system-port><bank-domain><bank-port>");
			return;
			}
			int pystemPortNumer=Integer.parseInt(argv[0]);
			int bankPortNumber=Integer.parseInt(argv[2]);
			String bankDomain=argv[1];
			ServerSocket psystem_socket = new ServerSocket(pystemPortNumer); 
			psystem_socket.setReuseAddress(true);
			System.out.println("Admin Psystem :- purchasing System Running on Port Number \t"+pystemPortNumer);
		while(true)
		{
			Socket conn = psystem_socket.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			PrintWriter out =new PrintWriter(conn.getOutputStream(),true);
			String clientSentence;
			clientSentence = in.readLine();
			//System.out.println("FROM Customer:" + clientSentence);

			out.println("Connection accepted!"); 
			String username;
	while(true)
			{	
				 username=in.readLine();
				//System.out.println(username);
				String password=in.readLine();
				//System.out.println(password);

				if(count_connection == 100){
					break;
				}
				if(username == null && password == null){
						count_connection++;
				}

				if(username == null || password == null)
						{
							out.println("wrong password");
							continue;
						}
				//System.out.println("here");
				boolean authenticate = p.passwordChecker(username,password);
				if(authenticate){	
					p.dispay_items_touser(out);	
					count_connection = 0;
					break;
				}
				else{
					out.println("wrong password");
					count_connection = 0;
					continue;
				}
			}
			if(count_connection == 100){
				count_connection = 0;
					continue;
				}
				byte []data1=null;
				byte []data2= null;
				byte []data3= null;
				DataInputStream dIn = new DataInputStream(conn.getInputStream());
				int length = dIn.readInt();                   
				if(length>0) {
    			data1 = new byte[length];
   				dIn.readFully(data1, 0, data1.length); 
				}
				//System.out.println("byte array in psystem"+data1.toString());
				ObjectInputStream inputStream1 = new ObjectInputStream(new FileInputStream(new File(path+"Prp.key")));
				PrivateKey privateKey1 = (PrivateKey) inputStream1.readObject();
				String decrypted1=RSA.decrypt(data1, privateKey1);
				//System.out.println("Decrypted Item details"+decrypted1);
				String[] seperate_item_quantity = decrypted1.split("-");
				int item_number = Integer.parseInt(seperate_item_quantity[0]);
				int item_quantity = Integer.parseInt(seperate_item_quantity[1]);
				//System.out.println("item_number "+ item_number + "item_quantity" + item_quantity);
				length = dIn.readInt();                   
				if(length>0) {
    			data2 = new byte[length];
   				dIn.readFully(data2, 0, data2.length); 
				}
				boolean DS_correct = false;
				if(username.equals("alice"))
				{
					inputStream1 = new ObjectInputStream(new FileInputStream(new File(path + "Pua.key")));
					PublicKey publicKey1 = (PublicKey) inputStream1.readObject();
					DS_correct=RSA.checkDS(data2,data1, publicKey1);
					//System.out.println("digital is "+DS_correct);
				}
				else
				{
					inputStream1 = new ObjectInputStream(new FileInputStream(new File(path+"Put.key")));
					PublicKey publicKey1 = (PublicKey) inputStream1.readObject();
					DS_correct=RSA.checkDS(data2,data1, publicKey1);
					//System.out.println("digital is "+DS_correct);	
				}
				if(DS_correct == true){
					Socket connection_bank = p.connect_bankServer(bankDomain,bankPortNumber);
					//send cc information as it came from Customer
					length = dIn.readInt();                   
					if(length>0) {
	    			data3 = new byte[length];
	   				dIn.readFully(data3, 0, data3.length); 
					}
					DataOutputStream dOut = new DataOutputStream(connection_bank.getOutputStream());
					dOut.writeInt(data3.length);
					dOut.write(data3);
					//calculate price = item quantity * item price and send to bank as E(Prp, price)
					Item item_inAction = ItemDetails.get(item_number);
					double price = item_inAction.itemPrice * item_quantity;
					ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(path + "Prp.key")));
					PrivateKey privateKey_p = (PrivateKey) inputStream.readObject();
					byte[] cipherText_price = RSA.encrypt(Double.toString(price), privateKey_p);					
					dOut.writeInt(cipherText_price.length);
					dOut.write(cipherText_price);
					//read bank response 
					PrintWriter out_bank = new PrintWriter(connection_bank.getOutputStream(),true);
					BufferedReader in_bank =new BufferedReader(new InputStreamReader(connection_bank.getInputStream()));
					//BufferedReader usr_in=new BufferedReader(new InputStreamReader(System.in));
					String cmd;
					//System.out.println("msg from bank--");
					String verification_from_bank = in_bank.readLine();
					System.out.println("FROM bank: " + verification_from_bank);
					if(verification_from_bank.equals("ok")){
						item_inAction.itemQuantity -= item_quantity;
						p.update_Item_file();
					}
					else if(verification_from_bank.equals("error")){
						out.println("wrong credit card number");
					}
					out.println(verification_from_bank);
					conn.close();
					//break;
				}

			}
		}
		catch(Exception e){
			e.printStackTrace();
		    System.out.println("****IN CATCH BLOCK -(Psystem Main)"+e.getMessage()+"****\n");
			
		}

}
	private void dispay_items_touser(PrintWriter out) {
		out.println("<item #, item name, price, quantities>");
		for (int key : ItemDetails.keySet()) {
			out.println(ItemDetails.get(key));
		}
		out.println("Done");
		
	}
	private Socket connect_bankServer(String bankDomain, int bankPortNumber) {
		Socket bank_socket=null;
		try {
			
			// //for connecting to bank
			bank_socket = new Socket(bankDomain,bankPortNumber);
			bank_socket.setReuseAddress(true);
			PrintWriter out_bank = new PrintWriter(bank_socket.getOutputStream(),true);
			BufferedReader in_bank =new BufferedReader(new InputStreamReader(bank_socket.getInputStream()));
			//BufferedReader usr_in=new BufferedReader(new InputStreamReader(System.in));
			
			String cmd;

			System.out.println("Connecting to bank");
			out_bank.println("purchasing server  wants to connect");
			System.out.println("FROM bank: " + in_bank.readLine()); 
			
		} catch (Exception e) {
			e.printStackTrace();
		    System.out.println("****IN CATCH BLOCK -(connect_bankServer)"+e.getMessage()+"****\n");
		}
		return bank_socket;
		
	}
	private boolean passwordChecker(String username,String password) {
		boolean t = false;
		//System.out.println("username:---"+username);
		//System.out.println("password:---"+password);
		


		String password_from_map = userDetails.get(username);
		if(password_from_map==null)
			return t;
		try {
			t = password_from_map.equals(password);			
			
		} catch (Exception e) {
			e.printStackTrace();
		    System.out.println("****IN CATCH BLOCK -(passwordChecker)"+e.getMessage()+"****\n");
		}		
		return t;				
	}
	private void update_Item_file() {
		try {
			PrintWriter writer = new PrintWriter(path+ "item.txt");
			writer.print("");
			for (int key : ItemDetails.keySet()) {
				Item itm = ItemDetails.get(key);
				writer.println(itm.itemId+", "+itm.itemName+", "+ "$"+itm.itemPrice +", "+itm.itemQuantity);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("****IN CATCH BLOCK -(update_Balance)"+e.getMessage()+"****\n");
		}
		
	}

}