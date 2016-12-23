import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;

//import RSA.RSA;

public class Customer {
	//static String path = "C:\\CS_Project\\";
	static String path = "";
	public static void main(String[] argv) {
		
		//java Customer <purchasing-system-domain><purchasing-system-port>
		if(argv.length < 2 || argv.length > 2){
		System.out.println("run as : java Customer <purchasing-system-domain><purchasing-system-port> ");
		return;
		}
		
		String from_psystem_server;
		int psystem_port=Integer.parseInt(argv[1]);
		String psystem_domain = argv[0];
		//System.out.println(psystem_domain);
		try{
		Socket customer_socket = new Socket(psystem_domain,psystem_port);
		customer_socket.setReuseAddress(true);
		PrintWriter out = new PrintWriter(customer_socket.getOutputStream(),true);
		BufferedReader in =new BufferedReader(new InputStreamReader(customer_socket.getInputStream()));
		BufferedReader usr_input=new BufferedReader(new InputStreamReader(System.in));
		
			String cmd;

			String username=null,password=null;
			System.out.println("Connecting to server");
			out.println("Client wants to connect");
			System.out.println("FROM SERVER: " + in.readLine()); 
			while(true)
			{
				System.out.println("Enter Username");
				username=usr_input.readLine();
				System.out.println("Enter Password");
				password=usr_input.readLine();
				
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(password.getBytes());
				byte[] password_digest = md.digest();
				
				 
           		String generatedPassword = toHEXString(password_digest);
           		//System.out.println(generatedPassword);		
				out.println(username);
				out.println(generatedPassword);
				String psystem_response = in.readLine();
				if(psystem_response.equals("wrong password"))
				{
					System.out.println("The password is incorrect");
					continue;
				}
				else
					break;
			}
			//	else{
					String digest_items;
					//System.out.println(psystem_response);
					while(!(digest_items=in.readLine()).equals("Done"))
					{
						System.out.println(digest_items);
					}
					System.out.println("Please enter the item #");
					String itemNumber = usr_input.readLine();
					System.out.println("Please enter the quantity");
					String itemQuantity = usr_input.readLine();
					System.out.println("Please Credit Card Number");
					String creditCardNumber= usr_input.readLine();
					String concatenatedItemDetails=itemNumber.concat("-"+itemQuantity);
					String concatenatedCreditDetails=username.concat("-"+creditCardNumber);
					
					ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(path + "Pup.key")));
					PublicKey publicKey = (PublicKey) inputStream.readObject();
					byte[] cipherText1 = RSA.encrypt(concatenatedItemDetails, publicKey);
					//System.out.println("encrypted"+cipherText1.toString());

					/*ObjectInputStream inputStream4 = new ObjectInputStream(new FileInputStream(new File("Prp.key")));
					PrivateKey privateKey4 = (PrivateKey) inputStream4.readObject();
					String decrypted1=RSA.decrypt(cipherText1, privateKey4);
					System.out.println("decrypted "+decrypted1);*/
					byte[] DS=null;
					
					if(username.equals("alice"))
					{
						//System.out.println("alice");
						ObjectInputStream inputStream1 = new ObjectInputStream(new FileInputStream(new File(path + "Pra.key")));
						PrivateKey privateKey1 = (PrivateKey) inputStream1.readObject();
						DS=RSA.generateDS(cipherText1,  privateKey1);
						
					}
					else
					{
						ObjectInputStream inputStream2 = new ObjectInputStream(new FileInputStream(new File(path + "Prt.key")));
						PrivateKey privateKey2 = (PrivateKey) inputStream2.readObject();
						DS=RSA.generateDS(cipherText1, privateKey2);
					}
				
					inputStream = new ObjectInputStream(new FileInputStream(new File (path+"Pub.key")));
					publicKey = (PublicKey) inputStream.readObject();
					byte[] cipherText2 = RSA.encrypt(concatenatedCreditDetails, publicKey);
					
					DataOutputStream dOut = new DataOutputStream(customer_socket.getOutputStream());
					//System.out.println(cipherText1);
					dOut.writeInt(cipherText1.length);
					dOut.write(cipherText1); 
					dOut.writeInt(DS.length);
					dOut.write(DS);
					dOut.writeInt(cipherText2.length); 
					dOut.write(cipherText2);
					String verification = in.readLine();
					if(verification.equals("ok")){
						System.out.println("we will process your order soon");
					}
					else
					{
						System.out.println("wrong credit card number");
					}
					//out.println("iamdone");
					//break;
				//}
			//}
		}
		catch(Exception e){}

	}

	private static String toHEXString(byte[] password_digest) {
		StringBuilder sb = new StringBuilder();
    	for(int i=0; i< password_digest.length ;i++)
    	{
        	sb.append(Integer.toString((password_digest[i] & 0xff) + 0x100, 16).substring(1));
    	}
   		String hexString = sb.toString();
		return hexString;
	}

}
