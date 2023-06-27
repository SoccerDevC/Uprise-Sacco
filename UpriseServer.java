import java.io.*;
import java.net.*;

public class UpriseServer {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(6666);
            Socket s = ss.accept();// establishes connection
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String str = (String) dis.readUTF();
            // String details = (String) dis.readUTF(); #(get phone number and member number
            // some code to check database
            // if (details match database){
            // dis.writeUTF("verified");
            // }
            // else{
            // writeUTF("unverified");
            // }
            System.out.println("message= " + str);
            ss.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
