import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UpriseClient {
    public static String login;

    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 6676);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            Scanner commandScanner = new Scanner(System.in); // Input object for getting commands from user

            System.out.println("\n----------------------------------------------------------------------");
            System.out.println("Welcome to Uprise Client Interface.");
            System.out.println("Please enter the following command to login into the system. \n");

            // Command
            System.out.println("login username password ");
            login = commandScanner.nextLine();

            if (login.equalsIgnoreCase("login")) {
                dout.writeUTF(login);
                dout.flush();
                dout.close();
                s.close();

            } else {
                System.out.println("Please follow the menu to enter correct commands");

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
