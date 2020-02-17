import java.util.Scanner;
import java.io.*;
import java.net.*;

public class TCPDriver {
    static Scanner keyboard = new Scanner(System.in);
    static int errorCount;
    public static void main(String[] args) {
        System.out.print("Enter a query: ");
        for(int i = 0; i < 10000; i++) {
            try {
                    Socket clientSocket = new Socket("localhost", 6969);
                    DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String query = "One Two Three Four Five Six Seven Eight Nine Ten";
                    toServer.writeBytes(query + "\n");
                    fromServer.readLine();
                    //System.out.println(errorCount);
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
            }
        }
        System.out.println("Packet loss: " + ((double) errorCount)/10000 * 100);
    }
}
