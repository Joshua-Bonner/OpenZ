import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class OBDClient implements Runnable {
private AtomicBoolean query = new AtomicBoolean(false);
private Thread queryLoop;

    public void start() {
        queryLoop = new Thread(this);
        queryLoop.start();
    }

    public void cleanStop() {
        query.set(false);
    }

    public void stop() {
        if(queryLoop != null) { //thread must be running to stop
            query.set(false);
            queryLoop.interrupt();
        }
    }


    public void run() {
        //constant queries
        query.set(true);
        while(query.get()) {
            try {
                String[] response = query("ENGINE_LOAD/INTAKE_TEMP/INTAKE_PRESSURE/THROTTLE_POSITION").split("/");
                if (response.length >= 3) {
                    GPSUI.updateEngineLoad(response[0]);
                    GPSUI.updateIntakeTemp(response[1]);
                    GPSUI.updateIntakePressure(response[2]);
                    GPSUI.updateThrottlePos(response[3]);
                }
            } catch (OBDConnectionException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    ex.printStackTrace();
                }
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public String readCode() throws OBDConnectionException {
        String result = query("GET_DTC").replaceAll("/", "");
        if (result.equals("[]")) {
            return "No trouble codes found";
        } else if (result.equals("None")) {
            throw new OBDConnectionException("Unable to Read Trouble Codes");
        } else {
            StringBuilder retStr = new StringBuilder();
            retStr.append("<html>Current Trouble Codes:<br/>");
            String[] codes = result.split("\\), \\(");
            for (String code : codes) {

                code = code.replaceAll("[\\[\\]\\)\\(']", "");
                String[] codeWExpl = code.split(",");
                retStr.append(codeWExpl[0]).append(": ").append(codeWExpl[1]).append("<br/>");
            }
            retStr.append("</html>");
            return retStr.toString();
        }

    }

    public boolean isRunning() {
        return query.get();
    }

    public void clearCode() throws OBDConnectionException {
        if (!query("CLEAR_DTC").equals("None")) {
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

    public void stopQuery() {

    }


}
