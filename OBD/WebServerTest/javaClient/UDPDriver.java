import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPDriver {
    static DatagramSocket socket;
    static InetAddress address;

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            byte[] buf = "Where the hood at?".getBytes();
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length, address, 6969);
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(
                    packet.getData(), 0, packet.getLength());
            System.out.println(received);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
