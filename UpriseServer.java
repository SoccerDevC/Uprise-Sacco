import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpriseServer {
    private static final int PORT = 12345;
    private List<Socket> clients;
    private Connection connection;

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

    public void databaseConnect() throws IOException {
        String url = "jdbc:mysql://localhost:3306/uprise sacco";
        String username = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection to database successful");
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void main(String[] args) throws IOException {
        UpriseServer server = new UpriseServer();
        server.databaseConnect();
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

                        // login verification
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

                            // deposit processing
                            processDeposit(amount, dateDeposited, receiptNumber);
                            writer.println("Deposit submitted successfully.");
                        } else if (command.equals("checkStatement")) {
                            LocalDate dateFrom = LocalDate.parse(commandArgs[1]);
                            LocalDate dateTo = LocalDate.parse(commandArgs[2]);

                            // statement generation
                            String statement = generateStatement(dateFrom, dateTo);
                            writer.println(statement);
                        } else if (command.equals("requestLoan")) {
                            double amount = Double.parseDouble(commandArgs[1]);
                            int paymentPeriod = Integer.parseInt(commandArgs[2]);

                            // loan request processing
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
                                            acceptLoan(loanApplicationNumber);
                                            writer.println("Loan accepted successfully.");
                                        } else if (response.equals("reject")) {

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
            // try {
            // String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            // PreparedStatement statement = connection.prepareStatement(query);
            // statement.setString(1, username);
            // statement.setString(2, password);
            // ResultSet resultSet = statement.executeQuery();
            // return resultSet.next();
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
            // return false;
            return true;
        }

        private void processDeposit(double amount, LocalDate dateDeposited, int receiptNumber) {
            try {
                String query = "INSERT INTO deposits (amount, date_deposited, receipt_number) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setDouble(1, amount);
                statement.setDate(2, java.sql.Date.valueOf(dateDeposited));
                statement.setInt(3, receiptNumber);
                statement.executeUpdate();
                System.out.println("Deposit processed successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private String generateStatement(LocalDate dateFrom, LocalDate dateTo) {
            // StringBuilder statementBuilder = new StringBuilder();
            // try {
            // String query = "SELECT * FROM deposits WHERE date_deposited >= ? AND
            // date_deposited <= ?";
            // PreparedStatement preparedStatement = connection.prepareStatement(query);
            // preparedStatement.setDate(1, java.sql.Date.valueOf(dateFrom));
            // preparedStatement.setDate(2, java.sql.Date.valueOf(dateTo));
            // ResultSet resultSet = preparedStatement.executeQuery();
            // while (resultSet.next()) {
            // double amount = resultSet.getDouble("amount");
            // LocalDate dateDeposited = resultSet.getDate("date_deposited").toLocalDate();
            // int receiptNumber = resultSet.getInt("receipt_number");
            // statementBuilder.append("Amount: ").append(amount).append(", Date Deposited:
            // ")
            // .append(dateDeposited).append(", Receipt Number:
            // ").append(receiptNumber).append("\n");
            // }
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
            // return statementBuilder.toString();
            return "statement";
        }

        private String processLoanRequest(double amount, int paymentPeriod) {
            // String loanApplicationNumber = null;
            // try {
            // String query = "INSERT INTO loans (amount, payment_period) VALUES (?, ?)";
            // PreparedStatement statement = connection.prepareStatement(query,
            // PreparedStatement.RETURN_GENERATED_KEYS);
            // statement.setDouble(1, amount);
            // statement.setInt(2, paymentPeriod);
            // statement.executeUpdate();

            // ResultSet generatedKeys = statement.getGeneratedKeys();
            // if (generatedKeys.next()) {
            // loanApplicationNumber = String.valueOf(generatedKeys.getInt(1));
            // }
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
            // return loanApplicationNumber;
            return "12345";
        }

        private String getLoanRequestStatus(String loanApplicationNumber) {
            // try {
            // String query = "SELECT status FROM loans WHERE loan_application_number = ?";
            // PreparedStatement statement = connection.prepareStatement(query);
            // statement.setString(1, loanApplicationNumber);
            // ResultSet resultSet = statement.executeQuery();
            // if (resultSet.next()) {
            // return resultSet.getString("status");
            // }
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
            // return null;

            return "Granted";
        }

        private String getLoanDetails(String loanApplicationNumber) {
            // StringBuilder loanDetails = new StringBuilder();
            // try {
            // String query = "SELECT * FROM loans WHERE loan_application_number = ?";
            // PreparedStatement statement = connection.prepareStatement(query);
            // statement.setString(1, loanApplicationNumber);
            // ResultSet resultSet = statement.executeQuery();
            // if (resultSet.next()) {
            // double amount = resultSet.getDouble("amount");
            // int paymentPeriod = resultSet.getInt("payment_period");
            // loanDetails.append("Amount: ").append(amount).append(", Payment
            // Period:").append(paymentPeriod);
            // }
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
            // return loanDetails.toString();

            return "Loan Details";
        }

        private void acceptLoan(String loanApplicationNumber) {
            // try {
            // String query = "UPDATE loans SET status = 'Accepted' WHERE
            // loan_application_number = ?";
            // PreparedStatement statement = connection.prepareStatement(query);
            // statement.setString(1, loanApplicationNumber);
            // statement.executeUpdate();
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
        }

        private void rejectLoan(String loanApplicationNumber) {
            // try {
            // String query = "UPDATE loans SET status = 'Rejected' WHERE
            // loan_application_number = ?";
            // PreparedStatement statement = connection.prepareStatement(query);
            // statement.setString(1, loanApplicationNumber);
            // statement.executeUpdate();
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
        }
    }
}
