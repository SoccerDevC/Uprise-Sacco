
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UpriseClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public void start() {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("Connected to server: " + socket);

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writer = new PrintWriter(outputStream, true);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to Uprise Client Interface.");
            System.out.println("Please enter the following command to login into the system. \n");
            System.out.println("login username password ");
            System.out.println("Enter command: ");
            String logIn = scanner.nextLine();
            writer.println(logIn);
            String logInResponse = reader.readLine();
            System.out.println("\nServer response: " + logInResponse);

            // boolean loggedIn = true;
            // if (logInResponse.equals("Login successful.")) {
            // loggedIn = false;
            // }

            while (true) {
                System.out.println("\nDeposit amount date_deposited receipt_number");
                System.out.println("CheckStatement dateFrom DateTo");
                System.out.println("RequestLoan amount paymentPeriod_in_months");
                System.out.println("LoanRequestStatus loan_application_number");
                System.out.println("Withdraw amount date_withdraw receipt_number ");
                System.out.println("Exit to exit \n");
                System.out.println("Enter command:");
                String command = scanner.nextLine();

                writer.println(command);

                if (command.equals("exit")) {
                    break;
                }

                String response = reader.readLine();
                System.out.println("\nServer response: " + response);
                System.out.println(" ");
            }

            socket.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UpriseClient client = new UpriseClient();
        client.start();
    }
}
