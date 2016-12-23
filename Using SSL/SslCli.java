import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public
class SslCli {
    public static void main(String[] argv) {
        try {
            System.setProperty("javax.net.ssl.trustStore", "assignment4certicate");
           // System.setProperty("javax.net.ssl.keyStore","mySrvKeystore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword","000000");

            String serverName = argv[0];
            int portNumber = Integer.parseInt(argv[1]);

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(serverName, portNumber);

            InputStream inputstream = System.in;
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

            
            String msg = null;
            int i=0;
            while(true)
            {
                System.out.print(">enter a message:   ");
                while ((msg = bufferedreader.readLine()) != null) {
                    
                    if(msg.compareTo("exit")==0)
                        System.exit(0);
                    System.out.println("String length is :"+ msg.length());
                    bufferedwriter.write(msg + '\n');
                    bufferedwriter.flush();
                    break;
                }
            }
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

