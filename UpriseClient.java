import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UpriseClient {
    public static String login;

    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 6666);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            Scanner command = new Scanner(System.in); // Input object for getting commands from user

            System.out.println("\n----------------------------------------------------------------------");
            System.out.println("Welcome to Uprise Client Interface.");
            System.out.println("Please enter the following command to login into the system. \n");

            // Command
            System.out.println("login username password ");
            login = command.nextLine();
            do {
                if (login.equalsIgnoreCase("login")) {
                    dout.writeUTF(login);

                    // if(){
                    // System.out.println("deposit amount date_deposited receipt_number");
                    // System.out.println("CheckStatement dateFrom DateTo");
                    // System.out.println("requestLoan amount paymentPeriod_in_months");
                    // System.out.println("LoanRequestStatus loan_application_number");
                    // System.out.println("Withdraw amount date_withdraw receipt_number");

                    // }
                    // else{
                    // System.out.println("Enter your member number and phone number "}
                    // System.out.println("MemberNumber number /n phoneNumber phonenumber");
                    // memberNumber = command.nextLine();
                    // phoneNumber = command.nextLine();
                    // dout.writeUTF(memberNumber);
                    // dout.writeUTF(phoneNumber); #server sends a phone number and a member number
                    // read the stream and get verified or unverified information

                    // if (the stream contains information) {
                    // store password
                    // password = dout.readUTF();
                    // }
                    // else{
                    // System.out.println("return after a day");
                    // }
                    //

                    // }
                    dout.flush();
                    dout.close();
                    s.close();

                } else {
                    System.out.println("Please follow the menu to enter correct commands");

                }
            } while (login != "exit");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
