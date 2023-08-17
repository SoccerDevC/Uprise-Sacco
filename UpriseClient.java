
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UpriseClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;
    public static String memberNumber;
    public static String phoneNumber;
    public static String logInResponse;
    public static String command = " ";

    public void start() {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("Connected to server: " + socket);

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writer = new PrintWriter(outputStream, true);

            Scanner scanner = new Scanner(System.in);
            // do {
            System.out.println("Welcome to Uprise Sacco Client Interface.");
            System.out.println("To login into the system. \n Please enter your details in the following order:\n ");
            System.out.println("login username password ");
            System.out.println("Enter command: ");
            String logIn = scanner.nextLine();
            writer.println(logIn);
            logInResponse = reader.readLine();
            System.out.println(logInResponse);

            if (logInResponse.equals("Login successful.")) {

                System.out.println("\nDeposit amount date_deposited receipt_number");
                System.out.println("CheckStatement dateFrom DateTo");
                System.out.println("RequestLoan amount paymentPeriod_in_months");
                System.out.println("LoanRequestStatus loan_application_number");
                System.out.println("Exit to exit \n");
                System.out.println("Enter command:");
                command = scanner.nextLine();

                writer.println(command);

                String response = null;
                while (!command.equals("exit") && (response = reader.readLine()) != null) {
                    System.out.println("\n" + response);
                    break;
                }

                System.out.println(" ");
                if (response.startsWith("Your Approved loan amount is: ")) {
                    System.out.println("Type 'accept' to accept or 'reject' to reject the loan");
                    String reply = scanner.nextLine();
                    writer.println(reply);
                }

            } else if (logInResponse.equals("\nInvalid username or password.")) {
                System.out.println("Please provide your member number and phone number ");
                System.out.println("Member Number: ");
                memberNumber = scanner.nextLine();
                writer.println(memberNumber);
                System.out.println("Phone Number: ");
                phoneNumber = scanner.nextLine();
                writer.println(phoneNumber);
                String servResponse;
                servResponse = reader.readLine();
                System.out.println(servResponse);
                // if (servResponse.startsWith("Password:")) {

                // // String password = servResponse.substring(servResponse.indexOf(":") +
                // // 1).trim();
                // // System.out.println("Your password is: " + password);

                // } else if (servResponse.startsWith("Reference number:")) {
                // String referenceNumber = servResponse.substring(servResponse.indexOf(":")

                // 1).trim();
                // System.out.println("Your reference number is: " + referenceNumber);

                // }

            }
            // } while (!command.equals("exit"));

            socket.close();
            scanner.close();
        } catch (

        IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UpriseClient client = new UpriseClient();
        client.start();
    }
}

// import java.io.*;
// import java.net.Socket;
// import java.util.Scanner;

// public class UpriseClient {
// private static final String SERVER_HOST = "localhost";
// private static final int SERVER_PORT = 1234;
// public static String memberNumber;
// public static String phoneNumber;
// public static String logInResponse;
// public static String command = " ";
// static Scanner scanner = new Scanner(System.in);
// public static BufferedReader reader;
// public static PrintWriter writer;

// public static void main(String[] args) {
// try {
// Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
// System.out.println("Connected to server: " + socket);

// InputStream inputStream = socket.getInputStream();
// OutputStream outputStream = socket.getOutputStream();
// BufferedReader reader = new BufferedReader(new
// InputStreamReader(inputStream));
// PrintWriter writer = new PrintWriter(outputStream, true);

// // do {

// login();
// if (logInResponse.equals("Login successful.")) {

// System.out.println("\nDeposit amount date_deposited receipt_number");
// System.out.println("CheckStatement dateFrom DateTo");
// System.out.println("RequestLoan amount paymentPeriod_in_months");
// System.out.println("LoanRequestStatus loan_application_number");
// System.out.println("Exit to exit \n");
// System.out.println("Enter command:");
// command = scanner.nextLine();

// writer.println(command);

// String response;
// while (!command.equals("exit") && (response = reader.readLine()) != null) {
// System.out.println("\nServer response: " + response);
// break;
// }

// System.out.println(" ");

// } else if (logInResponse.equals("\nInvalid username or password.")) {
// do {
// System.out.println("Please provide your member number and phone number ");
// System.out.println("Member Number: ");
// memberNumber = scanner.nextLine();
// writer.println(memberNumber);
// System.out.println("Phone Number: ");
// phoneNumber = scanner.nextLine();
// writer.println(phoneNumber);
// String servResponse;
// servResponse = reader.readLine();
// System.out.println(servResponse);
// login();
// // if (servResponse.startsWith("Password:")) {

// // // String password = servResponse.substring(servResponse.indexOf(":") +
// // // 1).trim();
// // // System.out.println("Your password is: " + password);

// // } else if (servResponse.startsWith("Reference number:")) {
// // String referenceNumber = servResponse.substring(servResponse.indexOf(":")
// +
// // 1).trim();
// // System.out.println("Your reference number is: " + referenceNumber);

// // }

// } while (!command.equals("exit"));
// }
// socket.close();
// scanner.close();
// } catch (

// IOException e) {
// e.printStackTrace();
// }
// }

// public static void login() throws IOException {
// System.out.println("Welcome to Uprise Sacco Client Interface.");
// System.out.println("To login into the system. \n Please enter your details in
// the following order:\n ");
// System.out.println("login username password ");
// System.out.println("Enter command: ");
// String logIn = scanner.nextLine();
// writer.println(logIn);
// logInResponse = reader.readLine();
// System.out.println(logInResponse);

// }
// }
