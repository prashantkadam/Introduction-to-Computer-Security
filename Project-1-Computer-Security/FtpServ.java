import java.io.*; 
import java.net.*;
import java.util.Scanner;
class FtpServ {
	
	static final int rows = 26;
	static final int cols = 26;
	static char[][]  table = new char[rows][cols];
	static String alphabets = "abcdefghijklmnopqrstuvwxyz";
	static String curret_dir="";
	String key1 = "security";
	String keyoriginal = "security";

	public static String EncryptedText(String input,String changed_key,char[][] tb){
	String alphabets1 = "abcdefghijklmnopqrstuvwxyz  ";
			char alphabets_all[] = new char[28];
			alphabets_all = alphabets1.toCharArray();
			alphabets_all[26]='\n';
			alphabets_all[27]=' ';
			int len = input.length();
	String retrn = "";
	String top="";
	for(int i=0;i<28;i++)
	      	top=top+alphabets_all[i];
	  for(int i=0;i<len;i++)
	      retrn=retrn+(tb[top.indexOf(input.charAt(i))][top.indexOf(changed_key.charAt(i))]);
		return retrn;
	}
	public static String getOutput(String clientSentence, String curret_dir){

	 System.getProperty("user.dir");
	System.out.println("in getOutput ");
			BufferedReader stdInput = null;
			BufferedReader stdError = null;
			String s = null;
			StringBuilder output = new StringBuilder(0);
	try{
			Process p = Runtime.getRuntime().exec(clientSentence+" "+curret_dir);
			stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			stdError = new BufferedReader(new InputStreamReader(p.getErrorStream())); 
	        // read the output from the split_command_and
	        
	        while ((s = stdInput.readLine()) != null) {
	            output.append(s);
	            output.append("\t"); //todo spaces between names 
	        }
	        
	     }
	     catch(Exception exx){

	     }
	     return output.toString();		     
	} 
	public static void readFileContent(String file_name,PrintWriter out,BufferedReader in){
	try{
		System.out.println("in readFileContent");
		
		Scanner sc = new Scanner(new File(file_name));
		String str;
		//while(sc.hasNextLine()){
		byte[] buffer  = new byte[1024];
		int count;
		String line;
		while ((line = sc.nextLine()) != null){
			//String line = sc.nextLine();
			System.out.println(line);
			//StringBuilder line = new StringBuilder(str);
			//line.append(System.lineSeparator());
		line = line.replaceAll("[ ]","_");
		out.println(line.toString()+"$");
		System.out.println("-----------------------------------");
		//System.out.println(line.toUpperCase());
		}
		out.println("Done");
		out.flush();
		}
		catch(Exception e){
	
			}
	}
	public static void init_table(){
		
		int index=0;
		for (int i = 0;i<rows ; i++) {
			index = i;

			for (int j = 0; j < cols ;j++ ) {
				table[i][j] = alphabets.charAt(index);
				index = (index + 1)%26;
			}
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

	public static void writeToSEfile(String name,byte[] arr_of_bytes){
		try{
			String extn = "_se.txt";
		FileOutputStream f_strm = new FileOutputStream(name.substring(0,name.length()-4)+extn);
		BufferedOutputStream bf_stm = new BufferedOutputStream(f_strm);
	  	bf_stm.write(arr_of_bytes, 0, arr_of_bytes.length);
  		bf_stm.flush();
		bf_stm.close();
		}
		catch(Exception ex){}
  		
		}
	public static void callme(String name,Socket conn){
		try{
			String myKey="security";
			File file_name_t = new File (name); 
			byte [] arr_of_bytes = new byte [(int)file_name_t.length()];
			FileInputStream fin = new FileInputStream(file_name_t);
			BufferedInputStream bin = new BufferedInputStream(fin);
			bin.read(arr_of_bytes,0,arr_of_bytes.length);								
			String file_str = new String(arr_of_bytes,"UTF-8");
			char[][] ciphertable = new char[28][28];
			populateTable(ciphertable);
			String cipher_text ="";
			String plaintext=file_str;	
			//System.out.println(plaintext);			                
			myKey += plaintext;
			cipher_text = EncryptedText(plaintext,myKey,ciphertable);
			arr_of_bytes=cipher_text.getBytes();
			// System.out.println(cipher_text);
			OutputStream write_to_socket = conn.getOutputStream();
			write_to_socket.write(arr_of_bytes,0,arr_of_bytes.length);
			writeToSEfile(name,arr_of_bytes);

		}catch(Exception ex){}
	}

    public static void main(String argv[]) throws Exception 
    {
    	init_table();
    	//helperMethods.PrintMe();
    	//todo throws Exceptions
       String clientSentence;
       String capitalizedSentence;
      
       int port = Integer.parseInt(argv[0]);
       ServerSocket listen = new ServerSocket(port);
       Socket conn = listen.accept();
       // Socket conn_copy = conn;
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
       	PrintWriter out = new PrintWriter(conn.getOutputStream(),true);
       	String cddot = "cd ..";
       	String cd = "cd";
       	String pwd = "pwd";
       	String get ="get";
       	String mkdir="mkdir";
       	String ls = "ls"; 
       	while(true) {
       	//System.out.println("in while");
		  if(conn.isOutputShutdown())
		  {
		  	System.out.println("sock.isOutputShutdown()");
		  		conn.close();
		  	}
		  clientSentence = in.readLine();
		 
		  if(clientSentence == null || clientSentence.equals("quit"))
		  		break;

		   else if(clientSentence.equals(cddot)) {
						String path=System.getProperty("user.dir");
						int last_index = path.lastIndexOf("/");
						String temp = path.substring(0,last_index);
						System.setProperty("user.dir",temp);
						out.println();
						System.out.println("Output send to client (command)- "+cddot);
					}
		  else if(clientSentence.startsWith(cd)) {
						String str[] = clientSentence.split(" ");
						String path=System.getProperty("user.dir");
						System.setProperty("user.dir", path+"/"+str[1]);
						curret_dir += str[1]+"/";
						out.println();
						System.out.println("Output send to client (command)- "+cd);
					}
			else if(clientSentence.equals(pwd)){
						out.println(System.getProperty("user.dir"));
						System.out.println("Output send to client (command)- "+pwd);
			}
		  else if(clientSentence.startsWith(get))				
			{
				
				String split_command_[]=clientSentence.split(" ");
				String name=split_command_[1];
				callme(name,conn);
				System.out.println("Output send to client (command)- "+get);
			}
	else if(clientSentence.startsWith(mkdir)){
		String dirname = clientSentence.substring(6,clientSentence.length());
		String path = System.getProperty("user.dir");
		String ppp = path +"/"+ dirname;
		String s = null;
			BufferedReader stdInput = null;
			BufferedReader stdError = null;
			Process p = Runtime.getRuntime().exec("mkdir "+ppp);
				//System.out.println(dirname);
				StringBuilder output = new StringBuilder(0);
				p.waitFor();
				//System.out.println("mkdir");
				stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
						
						while ((s = stdInput.readLine()) != null) {
						            output.append(s);
						            output.append("\t"); //todo spaces between names 
						        }
					out.println(output.toString());
				System.out.println("Output send to client (command)- "+mkdir);

	}
	else if(clientSentence.equals(ls))
					{
						BufferedReader stdInput = null;
						BufferedReader stdError = null;
						String s = null;
						StringBuilder output = new StringBuilder(0);
						Process p;
						p = Runtime.getRuntime().exec(ls+" "+System.getProperty("user.dir"));
						p.waitFor();
						stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
						
						while ((s = stdInput.readLine()) != null) {
						            output.append(s);
						            output.append("\t"); //todo spaces between names 
						        }
								out.println(output.toString());
								System.out.println("Output send to client (command)- "+ls);
					}
			
      } 
      conn.close();
   } 
} 

