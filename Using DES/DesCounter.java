import java.io.*;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class DesCounter {
  static byte[] raw = new byte[]{0x01, 0x72, 0x43, 0x3E, 0x1C, 0x7A, 0x55};
  static byte[] keyBytes = addParity(raw);
  Cipher ecipher;

  Cipher dcipher;

  DesCounter(SecretKey key) throws Exception {
    ecipher = Cipher.getInstance("DES");
    dcipher = Cipher.getInstance("DES");
    ecipher.init(Cipher.ENCRYPT_MODE, key);
    dcipher.init(Cipher.DECRYPT_MODE, key);
  }

  public String encrypt(String str) throws Exception {
    // Encode the string into bytes using utf-8
    byte[] utf8 = str.getBytes("UTF8");

    // Encrypt
    byte[] enc = ecipher.doFinal(utf8);

    // Encode bytes to base64 to get a string
    return new sun.misc.BASE64Encoder().encode(enc);
  }
  
  public static byte[] addParity(byte[] in) {
    byte[] result = new byte[8];

    // Keeps track of the bit position in the result
    int resultIx = 1;

    // Used to keep track of the number of 1 bits in each 7-bit chunk
    int bitCount = 0;

    // Process each of the 56 bits
    for (int i=0; i<56; i++) {
      // Get the bit at bit position i
      boolean bit = (in[6-i/8]&(1<<(i%8))) > 0;

      // If set, set the corresponding bit in the result
      if (bit) {
        result[7-resultIx/8] |= (1<<(resultIx%8))&0xFF;
        bitCount++;
      }

      // Set the parity bit after every 7 bits
      if ((i+1) % 7 == 0) {
        if (bitCount % 2 == 0) {
          // Set low-order bit (parity bit) if bit count is even
          result[7-resultIx/8] |= 1;
        }
        resultIx++;
        bitCount = 0;
      }
      resultIx++;
    }
    return result;
  }
  
  public String decrypt(String str) throws Exception {
    // Decode base64 to get bytes
    byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

    byte[] utf8 = dcipher.doFinal(dec);

    // Decode using utf-8
    return new String(utf8, "UTF8");
  }

  public static String padZero(int number){
    int[] counter = new int[8];
    int i = 7;
    while(number > 0){
     
      counter[i]=number%10;
      number = number/10;
      i--;
    }
    StringBuilder builder = new StringBuilder();
    for (int j : counter) {
      builder.append(j);
    }
    String ctr_string = builder.toString();
    return ctr_string;
  }
  
  public static void decr(String inputFileName,String outputFileName) throws Exception {
    
      byte[] keyBytes = addParity(raw);
      SecretKey key = new SecretKeySpec(keyBytes, "DES");
      DesCounter encrypter = new DesCounter(key);
      String counter = "00001234";
      int counter_int = Integer.parseInt(counter);
      FileInputStream in = null;
        FileOutputStream out = null;
       // String name = "C:/Users/PRASHANT/workspace/Assignment3_CS/output.txt";
        int null_char_count = 0;
        String name = inputFileName;
      File file_name_t = new File (name);
        FileInputStream fin = new FileInputStream(file_name_t);
      BufferedInputStream bin = new BufferedInputStream(fin);
        try {
           out = new FileOutputStream(outputFileName);           
           int c;
           int no_of_blocks = 0;
           int no_of_char = 0;
           while (bin.available() > 0) {          
            // System.out.println("--dd--");
             byte [] arr_of_bytes = new byte [8];
             int n = bin.read(arr_of_bytes, 0, 8);
             String file_str = new String(arr_of_bytes,"UTF-8");
             if(file_str.contains("\n") && file_str.length() == 1)
             {
               break;
             }
             if(file_str.contains("\n") && file_str.length() != 1){
               no_of_char--;           
             }
             String encrypted = encrypter.encrypt(padZero(counter_int));
             byte[] xoredByte = xor_bytes(arr_of_bytes,encrypted.getBytes());
             null_char_count = null_char_count_xorByte(xoredByte);
             xoredByte = trim_byte_arr(xoredByte);
             out.write(xoredByte);             
             counter_int++;
             no_of_blocks++;
             no_of_char=no_of_char + n - null_char_count;
           }
           System.out.println("******************************decryption********************************");
          System.out.println("no_of_blocks: "+ no_of_blocks +"\t no_of_char : "+ no_of_char);
          System.out.println("******************************decryption********************************");
        }finally {
           if (in != null) {
              in.close();
           }
           if (out != null) {
              out.close();
         }
      }
}
  
  public static void encr(String inputFileName,String outputFileName) throws Exception {
      FileInputStream in = null;
      FileOutputStream out = null;      
      SecretKey key = new SecretKeySpec(keyBytes, "DES");
      DesCounter encrypter = new DesCounter(key);
      String counter = "00001234";
      int counter_int = Integer.parseInt(counter);

      try {
        String name = inputFileName;
        File file_name_t = new File (name); 
        //byte [] arr_of_bytes = new byte [8];
        FileInputStream fin = new FileInputStream(file_name_t);
        BufferedInputStream bin = new BufferedInputStream(fin);                       
        in = new FileInputStream(name);
        out = new FileOutputStream(outputFileName);         
         int c;
         int no_of_blocks = 0;
         int no_of_char = 0;
         while (bin.available() > 0) {          
         // System.out.println("---");
           byte [] arr_of_bytes = new byte[8];
           int n = bin.read(arr_of_bytes, 0, 8);
           //System.out.println("---------"+n);
           String file_str = "";
           byte [] arrofBytes = Arrays.copyOfRange(arr_of_bytes, 0, n);
          // System.out.println("length: "+ arrofBytes.length);
           file_str = new String(arrofBytes,"UTF-8");
           if(file_str.contains("\n") && file_str.length() == 1)
           {
             break;
           }
           if(file_str.contains("\n") && file_str.length() != 1){
             no_of_char--;           
           }
           //byte [] arr_of_bytes_ = new byte [8];
           String encrypted = encrypter.encrypt(padZero(counter_int));
          // System.out.println("encrypted.length: "+encrypted.length());
           byte[] xoredByte = xor_bytes(arr_of_bytes,encrypted.getBytes());
           out.write(xoredByte);
           counter_int++;
           no_of_blocks++;
           no_of_char = no_of_char + n;
         }
          System.out.println("******************************encryption********************************");
          System.out.println("no_of_blocks: "+ no_of_blocks +"\tno_of_char : "+ no_of_char);
          System.out.println("******************************encryption********************************");
      }finally {
         if (in != null) {
            in.close();
         }
         if (out != null) {
            out.close();
         }
      } 
  }
  

  public static int null_char_count_xorByte(byte[] arr_byte){
                  int c = 0;
                  for (int i = 0; i < arr_byte.length; i++) {
                  if(arr_byte[i] == 0){
                    c++;
                  } 
                  }
                  return c;
          }


  public static byte[] trim_byte_arr(byte[] arr_byte) {
    int new_length = arr_byte.length;
    for (int i = 0; i < arr_byte.length; i++) {
    if(arr_byte[i] == 0){
      new_length--;
    }
    }
    return Arrays.copyOf(arr_byte, new_length);
}

  public static void main(String[] argv) throws Exception {
    if(argv.length < 3)
    {
      System.out.println("Error : - Run command - java DesCounter <inputfile> <outputfile> <ENC/DEC> ");
      return;
    }
    String inputFileName = argv[0];
    String outputFileName = argv[1];
    int  enc_or_dec = Integer.parseInt(argv[2]);
    if(enc_or_dec == 1){
        encr(inputFileName,outputFileName);
    }
    else if(enc_or_dec == 0){
        decr(inputFileName,outputFileName);
    }
    //decr();
  }

private static byte[] xor_bytes(byte[] arr_of_bytes, byte[] encrypted) {
  // TODO Auto-generated method stub
 // System.out.println(arr_of_bytes.length);
  //System.out.println(encrypted.length);
  
  byte[] xor =new byte[8];
  for (int i = 0; i < xor.length; i++) {    
    xor[i] =(byte)(arr_of_bytes[i] ^ encrypted[i]);
  }
  return xor;
}
}
