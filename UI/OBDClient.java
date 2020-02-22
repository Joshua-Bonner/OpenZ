import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.concurrent.CompletionException;

public class OBDClient extends Thread {


    public String query = "";

    public void run() {
        //constant queries
    }

    public String readCode() throws OBDConnectionException {
        String result = query("GET_DTC").replaceAll("/", "");
        if (result.equals("[]")) {
            return "No trouble codes found";
        } else if (result.equals("None")) {
            throw new OBDConnectionException("Unable to Read Trouble Codes");
        } else {
            StringBuilder retStr = new StringBuilder();
            retStr.append("Current Trouble Codes:\n");
            String[] codes = result.split("\\), \\(");
            for (String code : codes) {

                code = code.replaceAll("[\\[\\]\\)\\(']", "");
                String[] codeWExpl = code.split(",");
                retStr.append(codeWExpl[0]).append(": ").append(codeWExpl[1]).append('\n');
            }
            return retStr.toString();
        }

    }

    public void clearCode() throws OBDConnectionException {
        if (!query("CLEAR_DTC").equals("None/")) {
            //failed to clear code
            throw new OBDConnectionException("Unable to Clear Code");
        }

    }

    public String query(String query) throws OBDConnectionException {
        boolean successfulQuery = false;
        int errorCount = 0;
        while (!successfulQuery && errorCount < 3) {
            try {
                Socket clientSocket = new Socket("localhost", 6969);
                DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                toServer.writeBytes(query + "\n");
                successfulQuery = true;
                return fromServer.readLine();
            } catch (Exception e) {
                errorCount++;
            }
        }
        throw new OBDConnectionException("Unable to connect to connect to OBD device");
    }


}
