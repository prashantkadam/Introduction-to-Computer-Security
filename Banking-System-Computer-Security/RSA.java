//package RSA;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
//import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;

public class RSA {

	public static void generate(String Filename1, String Filename2) throws IOException, NoSuchAlgorithmException
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair kp = keyGen.generateKeyPair();
		
		File priFile = new File(Filename2);
	    File puFile = new File(Filename1);
	    
	    priFile.createNewFile();
	    puFile.createNewFile();
	    ObjectOutputStream priFileOS = new ObjectOutputStream(new FileOutputStream(priFile));
	    ObjectOutputStream puFileOS = new ObjectOutputStream(new FileOutputStream(puFile));
	    puFileOS.writeObject(kp.getPublic());
	    priFileOS.writeObject(kp.getPrivate());
	    priFileOS.close();
	    puFileOS.close();

	}
	
	public static byte[] encrypt(String text, PublicKey key) {
	    byte[] cipherText = null;
	    try {
	     Cipher cipher = Cipher.getInstance("RSA");
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text.getBytes());
	    } catch (Exception e) {
	      e.printStackTrace();
	      System.out.println("****IN CATCH BLOCK -(encrypt)"+e.getMessage()+"****\n");
	    }
	    return cipherText;
	  }

	public static byte[] encrypt(String text, PrivateKey key) {
	    byte[] cipherText = null;
	    try {
	     Cipher cipher = Cipher.getInstance("RSA");
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text.getBytes());
	    } catch (Exception e) {
	      e.printStackTrace();
	      System.out.println("****IN CATCH BLOCK -(encrypt)"+e.getMessage()+"****\n");
	    }
	    return cipherText;
	  }

	
public static String decrypt(byte[] text, PrivateKey key) {
	    byte[] decryptedFile = null;
	    try {

	      Cipher cipher = Cipher.getInstance("RSA");
	      cipher.init(Cipher.DECRYPT_MODE, key);
	      decryptedFile = cipher.doFinal(text);

	    } catch (Exception e) {
	    	e.printStackTrace();
		    System.out.println("****IN CATCH BLOCK -(decrypt)"+e.getMessage()+"****\n");
	    }

	    return new String(decryptedFile);
	  }

public static String decrypt(byte[] text, PublicKey key) {
    byte[] decryptedFile = null;
    try {

      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, key);
      decryptedFile = cipher.doFinal(text);

    } catch (Exception e) {
    	e.printStackTrace();
	    System.out.println("****IN CATCH BLOCK -(decrypt)"+e.getMessage()+"****\n");
    }

    return new String(decryptedFile);
  }


	public static byte[] generateDS(byte[] data, PrivateKey key) throws SignatureException {
		Signature generatedSignature = null;
	    try {
	     
	    	generatedSignature = Signature.getInstance("MD5withRSA");
    		generatedSignature.initSign(key);
      		generatedSignature.update(data);
	    } catch (Exception e) {
	    	e.printStackTrace();
		      System.out.println("****IN CATCH BLOCK -(generateDS)"+e.getMessage()+"****\n");
	    }
	    return generatedSignature.sign();
	  }
	
	public static boolean checkDS(byte [] signedText,byte[] original, PublicKey key) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
		
	    Signature generatedSignature = Signature.getInstance("MD5withRSA");
	    generatedSignature.initVerify(key);
	    generatedSignature.update(original);
	    return generatedSignature.verify(signedText);
	  }	


	  public static void main(String argv[]) throws NoSuchAlgorithmException, IOException
	 	{
	 		RSA.generate("Pua.key","Pra.key");
	 		RSA.generate("Put.key","Prt.key");
	 		RSA.generate("Pup.key","Prp.key");
	 		RSA.generate("Pub.key","Prb.key");
	 	}
}

