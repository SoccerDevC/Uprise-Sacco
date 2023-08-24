
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UpriseClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;
    public static String memberNumber;
    public static String phoneNumber;
    public static String logInResponse;a
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
            System.out.println(
                    "\n************************************************************");
            System.out.println("\n" + //
                    "             Welcome to Uprise Sacco Client Interface.");

            System.out.println(
                    "        To Access our services please login into the system.\n ");
            System.out.println("                          ***Uprise***");

            System.out.println("\n        Enter the login command in the format provided below:\n ");

            System.out.println("                       login username password ");
            System.out.println("                                Or");
            System.out.println("To check on a submitted claim enter reference number in the given format");
            System.out.println(" ");
            System.out.println("                   Referencenumber 000000000000");
            System.out.println(" ");
            System.out.println("                           Enter command");
            String logIn = scanner.nextLine();
            String[] loginArgs = logIn.split(" ");

            if (loginArgs[0].equalsIgnoreCase("login")) {
                writer.println(logIn);
                System.out.println(
                        "\n*****************************************************************************************");
                while ((logInResponse = reader.readLine()) != null && !logInResponse.equals("END")
                        && !logInResponse.equals("Invalid username or password.")) {
                    if (logInResponse.equals("Login successful.")) {
                        System.out.println("" + "                               " + logInResponse);

                    } else {
                        System.out.println(logInResponse);
                    }

                }

                if (logInResponse.startsWith("END")) {
                    do {
                        System.out.println(
                                "\n                      Welcome to the Uprise Client Dashboard");
                        System.out.println(
                                "     To access our services enter the desired command in the format provided below ");
                        System.out.println("\n                    Deposit amount date_deposited receipt_number");
                        System.out.println("                          CheckStatement dateFrom DateTo");
                        System.out.println("                     RequestLoan amount paymentPeriod_in_months");
                        System.out.println("                      LoanRequestStatus loan_application_number");
                        System.out.println("                                 Exit to exit \n");
                        System.out.println("                                Enter command:");
                        command = scanner.nextLine();
                        String[] commandArgs = command.split(" ");

                        if (commandArgs[0].equalsIgnoreCase("checkStatement")) {
                            writer.println(command);
                            String loanDetails;
                            while ((loanDetails = reader.readLine()) != null) {
                                if (loanDetails
                                        .equals("****************************************************************")) {
                                    break; // Stop printing when "END" is encountered
                                }
                                System.out.println(loanDetails);

                            }
                        } else if (commandArgs[0].equalsIgnoreCase("loanRequestStatus")) {
                            writer.println(command);
                            String statusres = reader.readLine();
                            System.out.println(statusres);
                            if (statusres.startsWith("You qualify to receive a loan of ")) {
                                System.out.println("\nType 'accept' to accept or 'reject' to reject the loan");
                                String reply = scanner.nextLine();
                                writer.println(reply);

                                String requestStatusDetails;
                                while ((requestStatusDetails = reader.readLine()) != null && !requestStatusDetails
                                        .equals("****************************************************************")) {

                                    System.out.println(requestStatusDetails);

                                }
                            }
                        } else if (commandArgs[0].equalsIgnoreCase("requestLoan")) {
                            writer.println(command);
                            String statusreq;
                            while ((statusreq = reader.readLine()) != null) {
                                if (statusreq.equals("###")) {
                                    break; // Stop printing when "done" is encountered
                                }
                                System.out.println(statusreq);

                            }
                        } else if (commandArgs[0].equalsIgnoreCase("deposit")) {
                            writer.println(command);
                            String statusdep;
                            while ((statusdep = reader.readLine()) != null) {
                                if (statusdep.equals("***")) {
                                    break; // Stop printing when "done" is encountered
                                }
                                System.out.println(statusdep);

                            }
                        } else {
                            writer.println(command);

                            String response;
                            while (!command.equals("exit") && (response = reader.readLine()) != null) {
                                System.out.println("\n" + response);

                                System.out.println(" ");

                            }
                        }
                    } while (!command.equals("exit"));

                } else if (logInResponse.equals("Invalid username or password.")) {
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
            } else if (loginArgs[0].equalsIgnoreCase("referencenumber")) {
                writer.println(logIn);
                System.out.println(
                        "\n*****************************************************************************************");
                String referenceResponse;
                while ((referenceResponse = reader.readLine()) != null) {
                    if (referenceResponse
                            .equals("@@@")) {
                        break; // Stop printing when "END" is encountered
                    }
                    System.out.println(referenceResponse);

                }
            } else {
                System.out.println("Invalid input");

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
