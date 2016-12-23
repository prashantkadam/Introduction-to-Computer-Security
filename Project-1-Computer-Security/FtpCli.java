import java.io.*; 
import java.net.*;
import java.util.*; 
class FtpCli {
public static String getOutput(String clientSentence){
				BufferedReader stdInput = null;
				BufferedReader stdError = null;
				String s = null;
				StringBuilder output = new StringBuilder(0);
				try{
						Process p = Runtime.getRuntime().exec(clientSentence);
						stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						stdError = new BufferedReader(new InputStreamReader(p.getErrorStream())); 
				        // read the output from the s
				        
				        while ((s = stdInput.readLine()) != null) {
				            output.append(s);
				            output.append("\t");

				        }
				        //System.out.println(output.toString());
				        
				     }
				     catch(Exception exx){

				     }
				     return output.toString();		     
				}
public static void receivecreateFile(Socket sock,PrintWriter out){
	int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    try {
    				File filename = new File("test_temp.txt");
				    //InputStream is = sock.getInputStream();
				    InputStream is = sock.getInputStream();
				    fos = new FileOutputStream(filename);
				    bos = new BufferedOutputStream(fos);
				    System.out.println("before read");
				    String success = "success";
				    //////////// replaced this ////////////////////////////
				    byte[] buffer  = new byte[1024];
				    int count=is.read();
				    //System.out.println(count);
				    while ((count = is.read(buffer)) > 0)
						{
						    bos.write(buffer, 0, count);
						}
						out.println("success");
					byte arr[] = new byte[] {1, 6, 3};	
				   // bos.write(arr, 0 , 1);
				    
    				bos.flush();
				     //sock.shutdownInput();
				   //  sock.shutdownOutput();
				    System.out.println("File " + filename + " downloaded ");
				    if (fos != null){ fos.close();}
				    if (bos != null){ bos.close();}
				    //if (sock != null){ sock.close();}
  }
  catch(Exception ex){

  }
  finally {
    
  }
}  

public static void receivecreateFile(Socket sock,PrintWriter out,BufferedReader in){
	try{
			System.out.println("receivecreateFile");
			File filename = new File("test_temp.txt");
			FileWriter writer = new FileWriter(filename);
			String line = in.readLine();
			System.out.println(line);
		 	while ((line = in.readLine())  != null)
					{	
					    writer.write(line);
					}
				
			writer.flush();
			writer.close();
			//out.println("success");
			}catch(Exception ex){

		}
}
public static void populateTable(char[][] testtable){
				String alphabets1 = "abcdefghijklmnopqrstuvwxyz  ";
				char alphabets_all[] = new char[28];
				alphabets_all = alphabets1.toCharArray();
				alphabets_all[26]='\n';
				alphabets_all[27]=' ';
					for(int i=0;i<28;i++){
						for(int j=0;j<28;j++){
							 testtable[i][j]=alphabets_all[(j+i)%28];
							}
					}
	}


public static String decrytpTest(String string_str){
		String alphabets1 = "abcdefghijklmnopqrstuvwxyz  ";
		char alphabets_all[] = new char[28];
		alphabets_all = alphabets1.toCharArray();
		alphabets_all[26]='\n';
		alphabets_all[27]=' ';
		String key="security";
        String txt="";
        String upper_=""; 

        int xx = 40;

        while(xx<68){
        		 upper_=upper_+alphabets_all[xx-40];
        		 xx++;
        }             
        	     
         int len=string_str.length();
       // System.out.println(len); 
        int i = 0;
        while(i<string_str.length() && string_str.charAt(i)!='\0'){
        	int d=upper_.indexOf(key.charAt(i))-upper_.indexOf(string_str.charAt(i));
			          if(d<0)
			          	d=-d;
			          else if(d>0)
				        	d=28-d;
                txt=txt+(upper_.charAt(d));
                key=key+(upper_.charAt(d));
        	i++;
        }                                                                                      
      /*  for(int i=0;i<string_str.length() && string_str.charAt(i)!='\0';i++)
        {
     			      int diff=list.indexOf(Key.charAt(i))-list.indexOf(string_str.charAt(i));
			          if(diff<0)
			          	diff=-diff;
			          else if(diff>0)
				        diff=28-diff;
                Plaintxt=Plaintxt+(list.charAt(diff));
                Key=Key+(list.charAt(diff));
		    }*/
		   return txt;

}

public static void writeToFile(String new_name,byte[] arr,int r){

try{

	 FileOutputStream out_stream = new FileOutputStream(new_name);		
		BufferedOutputStream buff_stream = new BufferedOutputStream(out_stream);
       // int r = 100;
        buff_stream.write(arr, 0, r);
		buff_stream.flush();
		buff_stream.close();

}catch(Exception ex){

}

  }
   
    public static void main(String argv[]) throws Exception {
    	//toDo throws Exception
    	int bytesRead=0;
    	int size = 1022386;
	String modifiedSentence=null;
	int  port = Integer.parseInt(argv[1]);
	Socket sock = new Socket("bingsuns.binghamton.edu", port); 
	//	DataOutputStream out = 
	//		new DataOutputStream(sock.getOutputStream());
	PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
	BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	//	out.writeBytes("hello\n");
	//out.println("hello");
	String ftpPromt = "ftp > ";	
	String quit = "quit";
	String lls="lls";
	String ls = "ls";
	Scanner sc = new Scanner(System.in);

	while(true){
		System.out.print(ftpPromt);
		String s = sc.nextLine();
		if(s.equals(quit))
			break;
		else if(s.equals(lls)){
			modifiedSentence = getOutput(ls);
			System.out.println(modifiedSentence);
		}
		else if(s.length() > 4 && s.substring(0,4).equals("get ")){
				{
					String extn = "_cd.txt";
			        String split_command_[]=s.split(" ");
			        String name=split_command_[1];        
				    out.println(s);

        			String nm_from_cmd=name.substring(0,name.length()-4);
					byte[] bytearray = new byte[size];
					InputStream is = sock.getInputStream();
					int currentTot = 0;
      			 	System.out.println(nm_from_cmd);
        
					FileOutputStream fo_strm = new FileOutputStream(nm_from_cmd+"_ce.txt");
					BufferedOutputStream bf_outputstm = new BufferedOutputStream(fo_strm);
        			
        		do{

				}while(is.available() == 0);

				while (is.available() > 0) {
					bytesRead = is.read(bytearray, 0, bytearray.length);
					bf_outputstm.write(bytearray, 0, bytesRead);
					//System.out.println(bytearray);
				}
        	bf_outputstm.flush();
			bf_outputstm.close();
        		String str = new String(bytearray,"UTF-8");
				//System.out.println(str);
        
    		   char[][] ciphertable = new char[28][28];
				populateTable(ciphertable);
                String plaintxt = decrytpTest(str);
        	
       
        bytearray=plaintxt.getBytes();      
        String new_name = nm_from_cmd+extn;
        writeToFile(new_name,bytearray,bytesRead);
        System.out.println("**File received**");
       
				
        }
		}
		else {	
			//System.out.println("in else"+ s);
				out.println(s);
				modifiedSentence =  in.readLine();
				if(modifiedSentence != null)
					System.out.println(/*"FROM SERVER: " +*/ modifiedSentence); 
		}
		System.out.println("----command executed----");
	}
	sock.close();
  }
  } 

  
  
 

