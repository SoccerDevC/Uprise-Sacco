import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UpriseServer {
    private static final int PORT = 12345;
    private List<Socket> clients;

    public UpriseServer() {
        clients = new ArrayList<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                clients.add(clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, Socket senderSocket) {
        for (Socket client : clients) {
            if (!client.equals(senderSocket)) {
                try {
                    OutputStream outputStream = client.getOutputStream();
                    outputStream.write(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        UpriseServer server = new UpriseServer();
        server.start();
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String username;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter writer = new PrintWriter(outputStream, true);

                String inputLine;
                boolean loggedIn = false;

                while ((inputLine = reader.readLine()) != null) {
                    if (inputLine.equals("exit")) {
                        break;
                    }

                    String[] commandArgs = inputLine.split(" ");
                    String command = commandArgs[0];

                    if (command.equals("login")) {
                        String username = commandArgs[1];
                        String password = commandArgs[2];

                        // Simulated login verification
                        if (verifyLogin(username, password)) {
                            writer.println("Login successful.");
                            loggedIn = true;
                            this.username = username;
                        } else {
                            writer.println("Invalid username or password.");
                        }
                    } else if (loggedIn) {
                        if (command.equals("deposit")) {
                            double amount = Double.parseDouble(commandArgs[1]);
                            LocalDate dateDeposited = LocalDate.parse(commandArgs[2]);
                            int receiptNumber = Integer.parseInt(commandArgs[3]);

                            // Simulated deposit processing
                            processDeposit(amount, dateDeposited, receiptNumber);
                            writer.println("Deposit submitted successfully.");
                        } else if (command.equals("checkStatement")) {
                            LocalDate dateFrom = LocalDate.parse(commandArgs[1]);
                            LocalDate dateTo = LocalDate.parse(commandArgs[2]);

                            // Simulated statement generation
                            String statement = generateStatement(dateFrom, dateTo);
                            writer.println(statement);
                        } else if (command.equals("requestLoan")) {
                            double amount = Double.parseDouble(commandArgs[1]);
                            int paymentPeriod = Integer.parseInt(commandArgs[2]);

                            // Simulated loan request processing
                            String loanApplicationNumber = processLoanRequest(amount, paymentPeriod);
                            writer.println("Loan application number: " + loanApplicationNumber);
                        } else if (command.equals("loanRequestStatus")) {
                            if (loggedIn) {
                                if (commandArgs.length == 2) {
                                    String loanApplicationNumber = commandArgs[1];

                                    // Get the loan request status
                                    String loanStatus = getLoanRequestStatus(loanApplicationNumber);
                                    writer.println("Loan request status: " + loanStatus);

                                    if (loanStatus.equals("Granted")) {
                                        // Retrieve and display loan details
                                        String loanDetails = getLoanDetails(loanApplicationNumber);
                                        writer.println("Loan details:\n" + loanDetails);
                                        writer.println("Type 'accept' to accept the loan or 'reject' to reject it.");
                                        String response = reader.readLine();

                                        if (response.equals("accept")) {
                                            // Handle loan acceptance logic
                                            acceptLoan(loanApplicationNumber);
                                            writer.println("Loan accepted successfully.");
                                        } else if (response.equals("reject")) {
                                            // Handle loan rejection logic
                                            rejectLoan(loanApplicationNumber);
                                            writer.println("Loan rejected.");
                                        }
                                    }
                                } else {

                                    writer.println("Please log in first.");
                                }
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(clientSocket);
                System.out.println("Client disconnected: " + clientSocket);
            }
        }

        private boolean verifyLogin(String username, String password) {
            // Simulated login verification logic
            // Replace with your own implementation
            return true;
        }

        private void processDeposit(double amount, LocalDate dateDeposited, int receiptNumber) {
            // Simulated deposit processing logic
            // Replace with your own implementation
        }

        private String generateStatement(LocalDate dateFrom, LocalDate dateTo) {
            // Simulated statement generation logic
            // Replace with your own implementation
            return "Statement";
        }

        private String processLoanRequest(double amount, int paymentPeriod) {
            // Simulated loan request processing logic
            // Replace with your own implementation
            return "12345";
        }

        private String getLoanRequestStatus(String loanApplicationNumber) {
            // Simulated loan request status retrieval logic
            // Replace with your own implementation
            return "Granted";
        }

        private String getLoanDetails(String loanApplicationNumber) {
            // Simulated loan details retrieval logic
            // Replace with your own implementation
            return "Loan Details";
        }

        private void acceptLoan(String loanApplicationNumber) {
            // Simulated loan acceptance logic
            // Replace with your own implementation
        }

        private void rejectLoan(String loanApplicationNumber) {
            // Simulated loan rejection logic
            // Replace with your own implementation
        }
    }
}
