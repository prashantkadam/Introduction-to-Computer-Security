
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SslServ {
    public static void main(String[] argv) {
        try {
            //System.setProperty("javax.net.ssl.trustStore", "mySrvKeystore.jks");
            System.setProperty("javax.net.ssl.keyStore","assignment4certicate");
            System.setProperty("javax.net.ssl.keyStorePassword","000000");
            
            int portNumber = Integer.parseInt(argv[0]);
            SSLServerSocketFactory sslserversocketfactory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket =
                    (SSLServerSocket) sslserversocketfactory.createServerSocket(portNumber);
            SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

            InputStream inputstream = sslsocket.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            String mesg = null;
            while ((mesg = bufferedreader.readLine()) != null) {
                System.out.println(" length of string recieved is :"+ mesg.length());
                System.out.println(mesg);
                System.out.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
