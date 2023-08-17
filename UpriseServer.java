
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpriseServer {
    private static final int PORT = 1234;
    private List<Socket> clients;
    private Connection connection;
    public int ref = 145;
    public double loanamount;
    public double setRecommendedLoanAmount;
    public String loanStatus;

    private List<LoanApplication> loanApplications;
    private Map<String, PreviousLoanPerformance> loanPerformances;

    public UpriseServer() {
        loanPerformances = new HashMap<>();

        clients = new ArrayList<>();
        loanApplications = new ArrayList<>();
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
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "uprise sacco";
        String username = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url + dbName, username, password);
            System.out.println("Connection to database successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        UpriseServer server = new UpriseServer();
        server.databaseConnect();
        server.start();
        // System.out.println(server.loanApplications.size());

    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String username;
        private String phoneNumber;
        private final int passwordGenerator = 123;
        private ResultSet resultset;

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
                            // writer.println("Please provide your member number and phone number
                            // registered:");
                            String memberNumber = reader.readLine();
                            String phoneNumber = reader.readLine();

                            // Check if there is a match
                            password = getPassword(memberNumber, phoneNumber);
                            if (!password.equals(" ")) {
                                // Generate password and provide it to the member

                                writer.println("Your password is: " + password + "\n");
                                // savePasswordToDatabase(username, newPassword);
                            } else if (password.equals(" ")) {
                                // Provide reference number for follow-up
                                int referenceNumber = generateReferenceNumber();
                                writer.println(
                                        "Please return after a day to access the system. Your reference number is: "
                                                + referenceNumber);
                            }

                        }
                    } else if (loggedIn) {
                        if (command.equals("deposit")) {

                            int receiptNumber = Integer.parseInt(commandArgs[3]);

                            // deposit processing
                            processDeposit(receiptNumber);
                            writer.println("Deposit was successfully added to the system.");
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
                            String loanApplicationNumber = requestLoan(username, amount, paymentPeriod);
                            loanApplications
                                    .add(new LoanApplication(loanApplicationNumber, username, loanamount,
                                            paymentPeriod));
                            // if (loanApplications.size() > 1) {// for testing purposes i have set it to 2
                            //     // updateRecommendedLoanAmounts(loanApplications);

                            //     // Check if the Sacco has enough funds to give out loans
                            //     if (availableFunds < 2000000) {
                            //         // No loan can be given if the funds are insufficient
                            //         application.setRecommendedLoanAmount(0);
                            //     } else {
                            //         // Check if the member's previous loan performance is below 50%
                            //         double previousPerformance = fetchPreviousLoanPerformance(username);
                            //         if (application.getLoanAmount() > (3.0 / 4) * application.getTotalContributions()) {
                            //             // Reject the loan request as it exceeds 3/4 of their total contributions
                            //             application.setRecommendedLoanAmount(0);
                            //         } else {
                            //             if (previousPerformance < 50) {
                            //                 // If the previous loan performance is below 50%, give less priority
                            //                 application.setRecommendedLoanAmount(averageLoanAmount * 0.75); // Reduce
                            //                                                                                 // recommended
                            //                                                                                 // by 25%
                            //             } else {
                            //                 // Check if the member's loan application amount is more than 3/4 of their
                            //                 // total
                            //                 // contributions
                            //                 // Assign the average loan amount to the member
                            //                 application.setRecommendedLoanAmount(averageLoanAmount);

                            //             }
                            //         }
                            //     }
                            //     updateRecommendedLoanAmount(application.getRecommendedLoanAmount(),
                            //             application.getApplicationNumber());

                            // }

                            writer.println("Request received, Loan application number is: " +
                                    loanApplicationNumber);
                            // double amount = Double.parseDouble(commandArgs[1]);
                            // int paymentPeriod = Integer.parseInt(commandArgs[2]);

                            // // loan request processing
                            // String loanApplicationNumber = processLoanRequest(amount, paymentPeriod);
                            // writer.println("Loan application number: " + loanApplicationNumber);
                        } else if (command.equals("loanRequestStatus")) {

                            if (commandArgs.length == 2) {
                                String loanApplicationNumber = commandArgs[1];

                                // Get the loan request status
                                loanStatus = getLoanApplicationByNumber(loanApplicationNumber);
                                String status = getLoanApprovalStatus(loanApplicationNumber);
                                if (status.equals("approved")) {
                                    // saveRecommendedLoanDistribution(loanStatus);
                                    writer.println("Your Approved loan amount is: " + loanStatus);
                                    // String loanDetails = getLoanDetails(loanApplicationNumber);

                                    String response = reader.readLine();

                                    if (response.equals("accept")) {
                                        // acceptLoan(loanApplicationNumber);
                                        writer.println("Loan accepted successfully.");
                                        // writer.println("Loan details:\n" + loanDetails);
                                    } else if (response.equals("reject")) {

                                        rejectLoan(loanApplicationNumber);
                                        writer.println("Loan rejected.");
                                    }
                                    // method to check for approval status with; if(loanStatus.equals("Loan
                                    // application received. The recommended loan distribution will be provided
                                    // after approval.")){
                                    // }
                                } else {
                                    writer.println("Your loan request is being processed...");
                                }
                            } else {

                                writer.println("Enter command correctly");
                            }

                        }
                    }
                }

            } catch (

            IOException e) {
                e.printStackTrace();
            } catch (SQLException ex) {
                System.out.println("error: " + ex.getMessage());
            } finally {
                clients.remove(clientSocket);
                System.out.println("Client disconnected: " + clientSocket);
            }
        }

        // private boolean checkMembership(int memberNumber, int phoneNumber) {
        // try {
        // String query = "SELECT * FROM users WHERE memberNumber = ? AND phone
        // Number=
        // ?";
        // PreparedStatement statement = connection.prepareStatement(query);
        // statement.setInt(1, memberNumber);
        // statement.setInt(2, phoneNumber);
        // ResultSet resultSet = statement.executeQuery();
        // return resultSet.next();
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }
        // return false;

        // }

        private boolean verifyLogin(String username, String password) {
            try {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;

        }

        private String getPassword(String memberNumber, String phoneNumber) {
            try {
                StringBuilder passwords = new StringBuilder();
                String query = "SELECT password FROM users WHERE memberNumber = ? AND phoneNumber = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(memberNumber));
                statement.setInt(2, Integer.parseInt(phoneNumber));
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String password = resultSet.getString("password");
                    passwords.append(password);
                }
                return passwords.toString();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return " ";
        }

        private int generateReferenceNumber() {
            // Generate and return a reference number for the member
            // ...
            return ref++;
        }

        private boolean processDeposit(int receiptNumber) {
            try {
                String query = "SELECT * FROM deposits WHERE receipt_number = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, receiptNumber);
                ResultSet resultSet = statement.executeQuery();

                return resultSet.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
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
            // LocalDate dateDeposited =
            // resultSet.getDate("date_deposited").toLocalDate();
            // int receiptNumber = resultSet.getInt("receipt_number");
            // statementBuilder.append("Amount: ").append(amount).append(", Date
            // Deposited:
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

        public String generateLoanApplicationNumber() {
            // Generate a random number between 100000 and 999999
            Random random = new Random();
            int randomNumber = random.nextInt(900000) + 100000;

            // Get the current timestamp in milliseconds
            long timestamp = System.currentTimeMillis();

            // Combine the random number and timestamp to create the application number
            String applicationNumber = "APP-" + timestamp + "-" + randomNumber;

            // Save the loan application details to the database

            return applicationNumber;
        }

        // ... (existing code)
        private String requestLoan(String username, double amount, int paymentPeriod)
                throws SQLException {

            String applicationNumber = generateLoanApplicationNumber();
            String query = "INSERT INTO loan_applications (application_number, username, amount, payment_period, recommended_loan_amount,approval_status) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, applicationNumber);
            statement.setString(2, username);
            statement.setDouble(3, amount);
            statement.setInt(4, paymentPeriod);
            statement.setDouble(5, 0.0);
            statement.setString(6, "rejected");

            statement.executeUpdate();
            return applicationNumber;
        }

        public String getLoanApplicationByNumber(String applicationNumber) {
            double availableFunds = fetchAvailableFunds();
            int totalLoanRequests = loanApplications.size();
            int i = 0;
            double averageLoanAmount = availableFunds / totalLoanRequests;

            LoanApplication application = loanApplications.get(i);
            try {
                String query = "SELECT username, amount, payment_period FROM loan_applications WHERE application_number = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, applicationNumber);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    username = resultSet.getString("username");
                    loanamount = resultSet.getDouble("amount");
                    int paymentPeriod = resultSet.getInt("payment_period");

                    // try {
                    // // Check if the Sacco has enough funds to give out loans
                    // if (!checkSufficientFunds(loanamount)) {
                    // return "Insufficient funds. Please try again later.";
                    // }

                    // // Check if the member can take out a loan that is over ¾ of their total
                    // // contributions
                    // if (!checkLoanAmountLimit(username, loanamount)) {
                    // return "Loan amount exceeds the limit. You can only take out a loan that is ¾
                    // of your total contributions.";
                    // }

                    // // Add the loan application to the list of loan applications
                    // loanApplications
                    // .add(new LoanApplication(applicationNumber, username, loanamount,
                    // paymentPeriod));

                    // If there are 10 applicants, process and recommend loan amounts

                    if (loanApplications.size() > 1) {// for testing purposes i have set it to 2

                        for (i = 0; i < loanApplications.size(); i++) {

                            // Check if the Sacco has enough funds to give out loans
                            if (availableFunds < 2000000) {
                                // No loan can be given if the funds are insufficient
                                application.setRecommendedLoanAmount(0);
                            } else {
                                // Check if the member's previous loan performance is below 50%
                                double previousPerformance = fetchPreviousLoanPerformance(username);
                                if (application.getLoanAmount() > (3.0 / 4) * application.getTotalContributions()) {
                                    // Reject the loan request as it exceeds 3/4 of their total contributions
                                    application.setRecommendedLoanAmount(0);
                                } else {
                                    if (previousPerformance < 50) {
                                        // If the previous loan performance is below 50%, give less priority
                                        application.setRecommendedLoanAmount(averageLoanAmount * 0.75); // Reduce
                                                                                                        // recommended
                                                                                                        // by 25%
                                    } else {
                                        // Check if the member's loan application amount is more than 3/4 of their total
                                        // contributions
                                        // Assign the average loan amount to the member
                                        application.setRecommendedLoanAmount(averageLoanAmount);

                                    }
                                }
                            }
                            updateRecommendedLoanAmount(application.getRecommendedLoanAmount(),
                                    application.getApplicationNumber());

                        }
                        return "amount is" + application.getRecommendedLoanAmount();
                    } else {
                        return "Loan application is being processed...";
                    }

                    // } catch (SQLException e) {
                    // e.printStackTrace();
                    // return "Error processing loan application. Please try again later.";
                    // }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "amount is" + application.getRecommendedLoanAmount();
        }
        // public String getLoanApplicationByNumber(String applicationNumber) {
        // try {
        // String query = "SELECT username, amount, payment_period FROM
        // loan_applications WHERE application_number = ?";
        // PreparedStatement statement = connection.prepareStatement(query);
        // statement.setString(1, applicationNumber);

        // ResultSet resultSet = statement.executeQuery();
        // if (resultSet.next()) {
        // username = resultSet.getString("username");
        // loanamount = resultSet.getDouble("amount");
        // int paymentPeriod = resultSet.getInt("payment_period");

        // try {
        // // Check if the Sacco has enough funds to give out loans
        // if (!checkSufficientFunds(loanamount)) {
        // return "Insufficient funds. Please try again later.";
        // }

        // // Check if the member can take out a loan that is over ¾ of their total
        // // contributions
        // if (!checkLoanAmountLimit(username, loanamount)) {
        // return "Loan amount exceeds the limit. You can only take out a loan that is ¾
        // of your total contributions.";
        // }

        // // Add the loan application to the list of loan applications
        // loanApplications
        // .add(new LoanApplication(applicationNumber, username, loanamount,
        // paymentPeriod));

        // // If there are 10 applicants, process and recommend loan amounts
        // if (loanApplications.size() < 3) {// for testing purposes i have set it to 2

        // String recommendedLoanDistribution = recommendLoanDistribution();
        // // // // Save the recommended loan distribution to the database (to be
        // // approved
        // // // by
        // // // // the
        // // // // administrator)

        // // saveRecommendedLoanDistribution(recommendedLoanDistribution);
        // return recommendedLoanDistribution;
        // // return "Loan application received. The recommended loan distribution will
        // // be
        // // provided after approval.";
        // } else {
        // return "Loan application received. Waiting for more applications...";
        // }

        // } catch (SQLException e) {
        // e.printStackTrace();
        // return "Error processing loan application. Please try again later.";
        // }

        // }
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }
        // return null;
        // }

        private boolean checkSufficientFunds(double requestedAmount) throws SQLException {
            // Calculate the total sum of amounts in the deposits table
            String query = "SELECT SUM(amount) AS total FROM deposits";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                double totalAmount = resultSet.getDouble("total");
                return totalAmount >= requestedAmount;
            }

            return false;
        }

        private boolean checkLoanAmountLimit(String username, double requestedAmount)
                throws SQLException {
            // Get the total contributions of the member
            String query = "SELECT total_contributions FROM members WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double totalContributions = resultSet.getDouble("total_contributions");
                double loanLimit = totalContributions * 0.75; // ¾ of the total contributions
                return requestedAmount <= loanLimit;
            }

            return false;
        }

        // private String recommendLoanDistribution() {
        // double availableFunds = fetchAvailableFunds();
        // int totalLoanRequests = loanApplications.size();

        // // Loop through the loan applications and recommend loan amounts based on the
        // // criteria
        // for (LoanApplication application : loanApplications) {
        // // Fetch previous loan performances for each applicant
        // String username = application.getUsername();
        // List<PreviousLoanPerformance> previousLoanPerformances =
        // fetchPreviousLoanPerformances(username);

        // // Sort loanApplications based on member's total contributions in descending
        // // order
        // Collections.sort(loanApplications,
        // Comparator.comparingDouble(LoanApplication::getTotalContributions).reversed());

        // // Calculate the average loan amount
        // double averageLoanAmount = availableFunds / totalLoanRequests;

        // // Check if the Sacco has enough funds to give out loans
        // if (availableFunds < 2000000) {
        // // No loan can be given if the funds are insufficient
        // application.setRecommendedLoanAmount(0);
        // } else {
        // // Check if the member's previous loan performance is below 50%
        // PreviousLoanPerformance previousPerformance =
        // getPreviousLoanPerformance(previousLoanPerformances,
        // username);
        // if (application.getLoanAmount() > (3.0 / 4) *
        // application.getTotalContributions()) {
        // // Reject the loan request as it exceeds 3/4 of their total contributions
        // application.setRecommendedLoanAmount(0);
        // } else {
        // if (previousPerformance != null &&
        // previousPerformance.getLoanPerformance(username) < 50) {
        // // If the previous loan performance is below 50%, give less priority
        // application.setRecommendedLoanAmount(averageLoanAmount * 0.75); // Reduce
        // recommended amount
        // // by
        // // 25%
        // } else {
        // // Check if the member's loan application amount is more than 3/4 of their
        // total
        // // contributions
        // // Assign the average loan amount to the member
        // application.setRecommendedLoanAmount(averageLoanAmount);

        // }
        // }
        // }
        // }

        // // Save the updated loan applications to the database
        // saveLoanApplicationsToDatabase(loanApplications);

        // // Generate the recommended loan distribution report
        // StringBuilder recommendedLoanDistribution = new StringBuilder();
        // for (LoanApplication application : loanApplications) {
        // recommendedLoanDistribution.append(application.getUsername()).append(": ")
        // .append(application.getRecommendedLoanAmount()).append("\n");
        // }

        // return recommendedLoanDistribution.toString();
        // }

        private void saveLoanApplicationsToDatabase(List<LoanApplication> loanApplications) {
            try {
                String query = "UPDATE loan_applications SET recommended_loan_amount = ? WHERE application_number = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    for (LoanApplication application : loanApplications) {
                        statement.setDouble(1, application.getRecommendedLoanAmount());
                        statement.setString(2, application.getApplicationNumber());
                        statement.addBatch();
                    }
                    statement.executeBatch();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateRecommendedLoanAmount(double recommendedAmount, String applicationId) throws SQLException {
            String query = "UPDATE loan_applications SET recommended_loan_amount = ? WHERE application_number= ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDouble(1, recommendedAmount);
                statement.setString(2, applicationId);
                statement.executeUpdate();
            }
        }

        private String getLoanApprovalStatus(String applicationNumber) throws SQLException {
            String status = null;
            String query = "SELECT approval_status FROM loan_applications WHERE application_number = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, applicationNumber);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    status = resultSet.getString("approval_status");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
            return status;
        }

        // ... (existing code)
    }

    private void addLoanPerformance(String username, double performance) {
        loanPerformances.put(username, new PreviousLoanPerformance(username,
                performance));
    }

    private PreviousLoanPerformance getPreviousLoanPerformance(List<PreviousLoanPerformance> performances,
            String username) {
        for (PreviousLoanPerformance performance : performances) {
            if (performance.getUsername().equals(username)) {
                return performance;
            }
        }
        return null;
    }

    private double getTotalContribution(String username) {
        double totalContributions = 0.0; // Default total contributions value if not
        // found
        try {
            String query = "SELECT total_contributions FROM members WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    totalContributions = resultSet.getDouble("total_contributions");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalContributions;
    }

    private List<PreviousLoanPerformance> fetchPreviousLoanPerformances(String username) {
        List<PreviousLoanPerformance> performances = new ArrayList<>();
        try {
            String query = "SELECT * FROM previous_loan_performances WHERE username =  ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    double performance = resultSet.getDouble("performance");
                    performances.add(new PreviousLoanPerformance(username, performance));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return performances;
    }

    private double fetchPreviousLoanPerformance(String username) {
        double performance = 0.0; // Default performance value if no previous
        // performance found
        try {
            String query = "SELECT performance FROM previous_loan_performances WHERE username = ? ";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    performance = resultSet.getDouble("performance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return performance;
    }

    private double fetchAvailableFunds() {
        double availableFunds = 0.0;
        try {
            String query = "SELECT total_contributions FROM members";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    double totalContributions = resultSet.getDouble("total_contributions");
                    availableFunds += totalContributions; // Add each selected
                    // total_contributions value to
                    // availableFunds
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableFunds;
    }

    // PreviousLoanPerformance.java
    public class PreviousLoanPerformance {
        private String username;
        private double performance;

        public PreviousLoanPerformance(String username, double performance) {
            this.username = username;
            this.performance = performance;
        }

        private double getLoanPerformance(String username) {
            PreviousLoanPerformance performance = loanPerformances.get(username);
            return (performance != null) ? performance.getPerformance() : 0.0;
        }

        public String getUsername() {
            return username;
        }

        public double getPerformance() {
            return performance;
        }
    }

    public class LoanApplication {
        private String applicationNumber;
        private String username;
        private double loanAmount;
        private int paymentPeriod;
        private double recommendedLoanAmount; // New field for recommended loan amount

        // Constructor
        public LoanApplication(String applicationNumber, String username, double loanAmount, int paymentPeriod) {
            this.applicationNumber = applicationNumber;
            this.username = username;
            this.loanAmount = loanAmount;
            this.paymentPeriod = paymentPeriod;
            this.recommendedLoanAmount = 0; // Initialize recommended loan amount to 0
        }

        // Getters and setters
        public String getApplicationNumber() {
            return applicationNumber;
        }

        public String getUsername() {
            return username;
        }

        public double getLoanAmount() {
            return loanAmount;
        }

        public int getPaymentPeriod() {
            return paymentPeriod;
        }

        public double getRecommendedLoanAmount() {
            return recommendedLoanAmount;
        }

        public void setRecommendedLoanAmount(double recommendedLoanAmount) {
            this.recommendedLoanAmount = recommendedLoanAmount;
        }

        // New method to calculate total contributions
        public double getTotalContributions() {
            // Replace this with your actual logic to fetch total contributions from the
            // database
            double totalContributions = 0.0;
            // Example: Fetch total contributions for the username from the database
            totalContributions = getTotalContribution(username);
            return totalContributions;
        }
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

    // public void createTables() {
    // try {
    // Statement statement = connection.createStatement();

    // // Create the 'users' table to store member login details
    // String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
    // "id INT AUTO_INCREMENT PRIMARY KEY," +
    // "username VARCHAR(50) NOT NULL UNIQUE," +
    // "password VARCHAR(50) NOT NULL" +
    // ")";
    // statement.executeUpdate(createUserTableQuery);

    // // Create the 'members' table to store member information
    // String createMembersTableQuery = "CREATE TABLE IF NOT EXISTS members (" +
    // "id INT AUTO_INCREMENT PRIMARY KEY," +
    // "username VARCHAR(50) NOT NULL UNIQUE," +
    // "total_contributions DOUBLE DEFAULT 0.0," +
    // "member_number INT NOT NULL," +
    // "phone_number INT NOT NULL" +
    // ")";
    // statement.executeUpdate(createMembersTableQuery);

    // // Create the 'deposits' table to store deposit transactions
    // String createDepositsTableQuery = "CREATE TABLE IF NOT EXISTS deposits ("
    // +
    // "id INT AUTO_INCREMENT PRIMARY KEY," +
    // "amount DOUBLE NOT NULL," +
    // "date_deposited DATE NOT NULL," +
    // "receipt_number INT NOT NULL" +
    // ")";
    // statement.executeUpdate(createDepositsTableQuery);

    // // Create the 'loans' table to store loan applications and status
    // String createLoansTableQuery = "CREATE TABLE IF NOT EXISTS loans (" +
    // "id INT AUTO_INCREMENT PRIMARY KEY," +
    // "amount DOUBLE NOT NULL," +
    // "payment_period INT NOT NULL," +
    // "status VARCHAR(20) DEFAULT 'Pending'," +
    // "member_username VARCHAR(50) NOT NULL," +
    // "FOREIGN KEY (member_username) REFERENCES members(username)" +
    // ")";
    // statement.executeUpdate(createLoansTableQuery);

    // // Create the 'loan_recommendations' table to store recommended loan
    // // distributions
    // String createRecommendationsTableQuery = "CREATE TABLE IF NOT EXISTS
    // loan_recommendations (" +
    // "id INT AUTO_INCREMENT PRIMARY KEY," +
    // "recommendation_text TEXT NOT NULL," +
    // "is_approved BOOLEAN DEFAULT false" +
    // ")";
    // statement.executeUpdate(createRecommendationsTableQuery);

    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }

    // public void insertSampleData() {
    // try {
    // // Insert sample data into the 'users' table
    // String insertUsersDataQuery = "INSERT INTO users (username, password)
    // VALUES
    // " +
    // "('david', '12'), " +
    // "('john', 'abc'), " +
    // "('jane', 'xyz')";
    // Statement statement = connection.createStatement();
    // statement.executeUpdate(insertUsersDataQuery);

    // // Insert sample data into the 'members' table
    // String insertMembersDataQuery = "INSERT INTO members (username,
    // total_contributions, member_number, phone_number) VALUES "
    // +
    // "('david', 50000, 1, 773057377), " +
    // "('john', 40000, 2, 772345678), " +
    // "('jane', 60000, 3, 775678987)";
    // statement.executeUpdate(insertMembersDataQuery);

    // // Insert sample data into the 'deposits' table
    // String insertDepositsDataQuery = "INSERT INTO deposits (amount,
    // date_deposited, receipt_number) VALUES " +
    // "(10000, '2023-06-01', 101), " +
    // "(20000, '2023-06-05', 102), " +
    // "(15000, '2023-06-10', 103)";
    // statement.executeUpdate(insertDepositsDataQuery);

    // // Insert sample data into the 'loans' table
    // String insertLoansDataQuery = "INSERT INTO loans (amount, payment_period,
    // member_username, status) VALUES "
    // +
    // "(5000, 6, 'david', 'Approved'), " +
    // "(10000, 12, 'john', 'Rejected'), " +
    // "(8000, 8, 'jane', 'Pending')";
    // statement.executeUpdate(insertLoansDataQuery);

    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }

    // public static void main(String[] args) throws IOException {
    // UpriseServer server = new UpriseServer();
    // server.databaseConnect();

    // // Create tables and insert sample data
    // server.createTables();
    // server.insertSampleData();

    // server.start();
    // }

}
