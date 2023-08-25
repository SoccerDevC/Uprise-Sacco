
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpriseServer {
    private static final int PORT = 1234;
    private List<Socket> clients;
    private Connection connection;
    public int ref = 145;
    public double loanamount;
    public double setRecommendedLoanAmount;
    public String loanStatus;
    private static int applicantsCount = 0;
    private static int newApplicantsCount = 0;

    private static List<LoanApplicant> applicants = new ArrayList<>();
    public static List<LoanApplicant> list2 = new ArrayList<>();

    public UpriseServer() {

        clients = new ArrayList<>();

    }

    public void start() {
        try {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server started. Listening on port " + PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);

                    clients.add(clientSocket);
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    new Thread(clientHandler).start();

                }
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
        String dbName = "sacco";
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

                    if (command.equalsIgnoreCase("login")) {
                        String username = commandArgs[1];
                        String password = commandArgs[2];

                        // login verification
                        if (verifyLogin(username, password)) {
                            int paymentPeriod = getPaymentPeriod(username);
                            writer.println("Login successful.");
                            double loanProgress = getClearedMonths(username);
                            double loanProgressPercentage = loanProgress / paymentPeriod * 100;
                            insertLoanProgress(username, loanProgressPercentage);
                            if (loanProgressPercentage < 50) {
                                writer.println("\n        Y'ello " + username
                                        + ". Pay your loan balance to avoid additional charges ");
                            }
                            writer.println("END");
                            loggedIn = true;
                            this.username = username;
                        } else {
                            writer.println("Invalid username or password.");
                            // writer.println("Please provide your member number and phone number
                            // registered:");
                            String memberNumber = reader.readLine();
                            String phoneNumber = reader.readLine();

                            // Check if there is a match
                            String getPassword;
                            getPassword = getPassword(memberNumber, phoneNumber);
                            if (!getPassword.equals("nothing")) {
                                // Generate password and provide it to the member

                                writer.println("Your password is: " + getPassword + "\n");
                                // savePasswordToDatabase(username, newPassword);
                            } else {
                                int referenceNumber = generateReferenceNumber();

                                insertFailedLogin(memberNumber, username, password, phoneNumber, referenceNumber);
                                // Provide reference number for follow-up
                                writer.println(
                                        "Please return after a day to access the system. Your reference number is: "
                                                + referenceNumber);
                            }

                        }
                    } else if (command.equalsIgnoreCase("referencenumber")) {
                        int referenceNumber = Integer.parseInt(commandArgs[1]);
                        StringBuilder message = new StringBuilder();
                        String got = getMessageByReferenceNumber(referenceNumber);
                        if (got != null) {
                            message.append(got);
                            message.append("\n");
                            message.append("@@@");
                            writer.println(message);
                        } else if (got == null) {
                            message.append("Return later after new information has been uploaded");
                            message.append("\n");
                            message.append("@@@");
                            writer.println(message);
                        }
                    } else if (loggedIn) {
                        if (command.equalsIgnoreCase("deposit")) {
                            double amount = Double.parseDouble(commandArgs[1]);
                            String date = commandArgs[2];
                            int receiptNumber = Integer.parseInt(commandArgs[3]);

                            // deposit processing
                            if (processDeposit(receiptNumber)) {
                                writer.println("Deposit was successfully added to the system.");
                            } else {
                                insertFailedDeposit(receiptNumber, username, amount, date);
                                writer.println("Please check later after new information is uploaded ");
                            }
                            writer.println("***");
                        } else if (command.equalsIgnoreCase("checkStatement")) {
                            String dateFrom = commandArgs[1];
                            String dateTo = commandArgs[2];
                            String memberId = getMemberIDByUsername(username);
                            int paymentPeriod = getPaymentPeriod(username);
                            // statement generation
                            String statement = generateStatement(dateFrom, dateTo, memberId, paymentPeriod);
                            writer.println(statement);
                        } else if (command.equalsIgnoreCase("requestLoan")) {
                            double loanamount = Double.parseDouble(commandArgs[1]);
                            int paymentPeriod = Integer.parseInt(commandArgs[2]);

                            // loan request processing
                            int loanApplicationNumber = requestLoan(username, loanamount, paymentPeriod);
                            addLoanApplicant(
                                    new LoanApplicant(loanApplicationNumber, username, loanamount, paymentPeriod));
                            if (applicantsCount >= 3) {
                                insertLoanRequestBatch();
                            }
                            // loanApplications
                            // .add(new LoanApplication(loanApplicationNumber, username, loanamount,
                            // paymentPeriod));
                            StringBuilder loanreqBuilder = new StringBuilder();
                            loanreqBuilder.append("Request received, Loan application number is: " +
                                    loanApplicationNumber);
                            loanreqBuilder.append("\n");
                            loanreqBuilder.append("###");
                            writer.println(loanreqBuilder);

                            // double amount = Double.parseDouble(commandArgs[1]);
                            // int paymentPeriod = Integer.parseInt(commandArgs[2]);

                            // // loan request processing
                            // String loanApplicationNumber = processLoanRequest(amount, paymentPeriod);
                            // writer.println("Loan application number: " + loanApplicationNumber);
                        } else if (command.equalsIgnoreCase("loanRequestStatus")) {

                            if (commandArgs.length == 2) {
                                int applicationNumber = Integer.parseInt(commandArgs[1]);

                                // Get the loan request status
                                loanStatus = getLoanApplicationByNumber(applicationNumber);
                                String status = getLoanApprovalStatus(applicationNumber);
                                if (status == null || !status.equals("granted")) {
                                    writer.println("Your loan request is being processed...");
                                } else if (status.equals("granted")) {
                                    // saveRecommendedLoanDistribution(loanStatus);
                                    writer.println(loanStatus);
                                    // String loanDetails = getLoanDetails(loanApplicationNumber);

                                    String response = reader.readLine();

                                    if (response.equals("accept")) {
                                        // acceptLoan(loanApplicationNumber);
                                        String member_id = getMemberIDByUsername(username);
                                        int payment_period = getPaymentPeriod(username);
                                        insertRegisteredLoans(applicationNumber, member_id, payment_period);

                                        String acceptanceDetails = getExpectedInstallmentDates(applicationNumber);
                                        writer.println(acceptanceDetails
                                                + "Loan registered successfully.");
                                        writer.println(
                                                "****************************************************************");
                                        deleteLoanRequestByMemberID(member_id);

                                        // writer.println("Loan details:\n" + loanDetails);
                                    } else if (response.equals("reject")) {
                                        String memberID = getMemberIDByUsername(username);
                                        deleteLoanRequestByMemberID(memberID);
                                        List<LoanApplicant> list2 = fetchLoanApplicants();
                                        newApplicantsCount = list2.size();
                                        updateLoanRequestBatch(); // rejectLoan(AaplicationNumber);
                                        writer.println("Loan rejected.");
                                        writer.println(
                                                "****************************************************************");

                                    }
                                    // method to check for approval status with; if(loanStatus.equals("Loan
                                    // application received. The recommended loan distribution will be provided
                                    // after approval.")){
                                    // }
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

        private boolean verifyLogin(String username, String password) {
            try {
                String query = "SELECT * FROM members WHERE username = ? AND password = ?";
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
                String query = "SELECT password FROM members WHERE member_id = ? AND phone_number = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(memberNumber));
                statement.setInt(2, Integer.parseInt(phoneNumber));
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String password = resultSet.getString("password");
                    return password;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "nothing";
        }

        public void insertRegisteredLoans(int loanApplicationNumber, String memberId, int paymentPeriod) {
            try {
                String insertQuery = "INSERT INTO registered_loans (loan_application_number, payment_period, installment_date, member_id) VALUES (?, ?, ?, ?)";

                LocalDate currentDate = LocalDate.now();

                for (int i = 0; i < paymentPeriod; i++) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setInt(1, loanApplicationNumber);
                        preparedStatement.setInt(2, paymentPeriod);
                        preparedStatement.setDate(3, java.sql.Date.valueOf(currentDate));
                        preparedStatement.setString(4, memberId);
                        preparedStatement.executeUpdate();
                    }

                    currentDate = currentDate.plusMonths(1); // Move to the next month
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public String getExpectedInstallmentDates(int loanApplicationNumber) {
            StringBuilder result = new StringBuilder();

            try {
                String query = "SELECT payment_period, installment_date FROM registered_loans WHERE loan_application_number = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, loanApplicationNumber);

                ResultSet resultSet = statement.executeQuery();
                double loan_amount = getLoanAmountByApplicationNumber(loanApplicationNumber);

                if (resultSet.next()) {
                    int paymentPeriod = resultSet.getInt("payment_period");
                    double results = loan_amount / paymentPeriod;
                    int roundedResults = (int) Math.round(results);
                    LocalDate installmentDate = resultSet.getDate("installment_date").toLocalDate();
                    result.append(
                            "\nA loan of " + loan_amount
                                    + " has been credited to your account\n and the following are the expected installment dates of progressive payments");
                    result.append("\nExpected Installment Dates:\n");

                    for (int i = 1; i <= paymentPeriod; i++) {
                        result.append("Installment ").append(i).append(": ").append(roundedResults + " shs by ")
                                .append(installmentDate).append("\n");
                        installmentDate = installmentDate.plusMonths(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        public double getLoanAmountByApplicationNumber(int loanApplicationNumber) {
            String query = "SELECT loan_amount FROM loan_requests WHERE loan_application_number = ?";
            double loanAmount = 0.0;

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, loanApplicationNumber);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    loanAmount = resultSet.getDouble("loan_amount");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return loanAmount;
        }

        public double getAverageSaccoLoanProgress() {// total performance of the whole sacco, which gives the average of
                                                     // the entire sacco team is also given to each member
            List<String> usernames = new ArrayList<>();
            double totalLoanProgress = 0.0;
            int counter = 0;

            try {
                // Get all usernames from the members table
                String query = "SELECT username FROM members";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    usernames.add(username);
                }

                // Loop through each member's username
                for (String username : usernames) {
                    double loanProgress = getClearedMonths(username);
                    // progress for the user
                    totalLoanProgress += loanProgress;
                    counter++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            double averageLoanProgress = totalLoanProgress / counter;
            return averageLoanProgress;
        }

        public void updateRecommendedFunds(LoanApplicant applicant) {
            String query = "UPDATE loan_requests SET recommended_funds = ? WHERE member_id = ?";
            String member_id = getMemberIDByUsername(applicant.getUsername());
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setDouble(1, applicant.getRecommendedLoanAmount());
                statement.setString(2, member_id);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void updateLoanRequestBatch() {
            List<LoanApplicant> sortedApplicantz = new ArrayList<>(list2);
            double totalAvailableFunds = fetchAvailableFunds();
            // int totalLoanRequests = newApplicantsCount;

            double newAverageLoanAmount = totalAvailableFunds / newApplicantsCount;
            // Sort applicants by total contributions in descending order
            sortedApplicantz.sort(Comparator.comparing(LoanApplicant::getTotalContributions)
                    .reversed()
                    .thenComparing(LoanApplicant::getPreviousLoanPerformance)
                    .reversed());

            for (int i = 0; i < sortedApplicantz.size(); i++) {
                LoanApplicant applicant = sortedApplicantz.get(i);

                double newPriorityFactor = 1.0 - (i * 0.05); // Decrease by 5% for each applicant

                double newRecommendedLoanAmount = newAverageLoanAmount * newPriorityFactor;
                double new_loan_amount = applicant.getLoanAmount();
                // Set the recommended loan amount
                if (newRecommendedLoanAmount > new_loan_amount) {
                    applicant.setRecommendedLoanAmount(new_loan_amount);

                } else {
                    applicant.setRecommendedLoanAmount(newRecommendedLoanAmount);
                }
                // Deduct the allocated funds

                updateRecommendedFunds(applicant);
            }
            list2.clear();
            newApplicantsCount = 0;
        }

        private void insertLoanRequest(LoanApplicant applicant) {
            try {
                // Get member_id based on the given username
                String memberID = getMemberIDByUsername(applicant.username);

                String query = "INSERT INTO loan_requests (loan_application_number, member_id, loan_amount, payment_period, recommended_funds, loan_approval_status, created_at, updated_at) "
                        + "VALUES (?, ?, ?, ?, ?, NULL, NOW(), NOW())"; // Set loan_approval_status to NULL
                PreparedStatement statement = connection.prepareStatement(query);

                statement.setInt(1, applicant.applicationNumber);
                statement.setString(2, memberID);
                statement.setDouble(3, applicant.loanAmount);
                statement.setInt(4, applicant.paymentPeriod);
                statement.setDouble(5, applicant.recommendedLoanAmount);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private String getMemberIDByUsername(String username) {
            String memberID = null;
            try {
                String query = "SELECT member_id FROM members WHERE username = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    memberID = resultSet.getString("member_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return memberID;
        }

        private void insertFailedDeposit(int receipt_number, String username, double amount, String date) {
            try {
                String memberIdQuery = "SELECT member_id FROM members WHERE username = ?";
                PreparedStatement memberIdStatement = connection.prepareStatement(memberIdQuery);
                memberIdStatement.setString(1, username);
                ResultSet memberIdResultSet = memberIdStatement.executeQuery();

                String memberId = "";
                if (memberIdResultSet.next()) {
                    memberId = memberIdResultSet.getString("member_id");
                } else {
                    // Handle the case when the username doesn't exist
                    // You might want to throw an exception or handle it in a way that suits your
                    // application
                    return;
                }

                String query = "INSERT INTO failed_deposits (receipt_number, member_id, amount, date, created_at, updated_at) "
                        +
                        "VALUES (?, ?, ?, ?, NOW(), NOW())"; // NOW() generates the current timestamp
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, receipt_number); // Implement a method to generate receipt numbers
                statement.setString(2, memberId);
                statement.setDouble(3, amount);
                statement.setDate(4, java.sql.Date.valueOf(date));

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void insertFailedLogin(String memberID, String username, String password, String phoneNumber,
                int referenceNumber) {
            try {
                String query = "INSERT INTO failed_login (member_id, username, password, phone_number,reference_number, created_at, updated_at) "
                        +
                        "VALUES (?, ?, ?, ?, ?, NOW(), NOW())"; // NOW() generates the current timestamp
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberID);
                statement.setString(2, username);
                statement.setString(3, password);
                statement.setString(4, phoneNumber);
                statement.setInt(5, referenceNumber);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private int generateReferenceNumber() {
            // Generate a random number between 100000 and 999999
            Random random = new Random();
            int randomNumber = random.nextInt(900000) + 100000;

            // Get the current timestamp in milliseconds
            long timestamp = System.currentTimeMillis();

            // Combine the random number and timestamp to create the application number
            int referenceNumber = (int) (timestamp % 1000000000) * 10 + randomNumber;
            if (referenceNumber < -1) {
                referenceNumber = referenceNumber * -1;
            }
            // Save the loan application details to the database
            return referenceNumber;

        }

        public void insertLoanProgress(String username, double loanProgress) {
            try {
                String query = "UPDATE members SET loan_progress = ? WHERE username = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setDouble(1, loanProgress);
                statement.setString(2, username);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public double getClearedMonths(String username) throws SQLException {

            String memberId = getMemberIDByUsername(username);
            double percentageLoanProgress = (double) calculateMonthsRepayingLoan(memberId);
            return percentageLoanProgress;
        }

        public int calculateMonthsRepayingLoan(String memberId) {
            int monthsRepaying = 0;
            try {
                String query = "SELECT MIN(repayment_date) AS first_repayment, MAX(repayment_date) AS last_repayment FROM loan_payments WHERE member_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    LocalDate firstRepayment = resultSet.getDate("first_repayment").toLocalDate();
                    LocalDate lastRepayment = resultSet.getDate("last_repayment").toLocalDate();

                    Period period = Period.between(firstRepayment, lastRepayment);
                    monthsRepaying = period.getYears() * 12 + period.getMonths() + 1; // +1 to include the last month
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return monthsRepaying;
        }

        public int calculateMonthsContributed(String memberId) {
            int monthsContributed = 0;
            try {
                String query = "SELECT MIN(date) AS first_deposit, MAX(date) AS last_deposit FROM deposits WHERE member_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, memberId);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    LocalDate firstDeposit = resultSet.getDate("first_deposit").toLocalDate();
                    LocalDate lastDeposit = resultSet.getDate("last_deposit").toLocalDate();

                    Period period = Period.between(firstDeposit, lastDeposit);
                    monthsContributed = period.getYears() * 12 + period.getMonths() + 1; // +1 to include the last month
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return monthsContributed;
        }

        public String getMessageByReferenceNumber(int referenceNumber) {
            String message = null;

            String query = "SELECT message FROM failed_login WHERE reference_number = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, referenceNumber);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        message = resultSet.getString("message");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return message;
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

        private String generateStatement(String fromDate, String toDate, String memberId, int payment_period) {
            StringBuilder result = new StringBuilder();

            try {
                String loanQuery = "SELECT repayment_date, repayment_amount FROM loan_payments " +
                        "WHERE member_id = ? AND repayment_date BETWEEN ? AND ? " +
                        "ORDER BY repayment_date, loan_id";

                String memberQuery = "SELECT total_contributions FROM members WHERE member_id = ?";

                try (PreparedStatement loanStatement = connection.prepareStatement(loanQuery);
                        PreparedStatement memberStatement = connection.prepareStatement(memberQuery)) {

                    loanStatement.setString(1, memberId);
                    loanStatement.setString(2, fromDate);
                    loanStatement.setString(3, toDate);

                    ResultSet loanResultSet = loanStatement.executeQuery();

                    memberStatement.setString(1, memberId);
                    ResultSet memberResultSet = memberStatement.executeQuery();

                    if (memberResultSet.next()) {
                        int totalContributions = memberResultSet.getInt("total_contributions");
                        result.append("\n                           Total Contributions: ").append(totalContributions)
                                .append("\n");
                    }

                    String currentDate = "";
                    int clearedMonths = 0;

                    while (loanResultSet.next()) {
                        String repaymentDate = loanResultSet.getString("repayment_date");
                        String repaymentAmount = loanResultSet.getString("repayment_amount");

                        if (!repaymentDate.equals(currentDate)) {
                            currentDate = repaymentDate;
                            result.append("\n                              Date: ").append(repaymentDate).append("\n");
                            clearedMonths++;
                        }

                        result.append("                            Repayment Amount: ").append(repaymentAmount)
                                .append("\n");

                        double percentageProgress = (double) clearedMonths / payment_period * 100;
                        result.append("                        Percentage Loan Progress: ").append(percentageProgress)
                                .append("%").append("\n");

                        int contributionMonths = calculateMonthsContributed(memberId);
                        double percentageContributionProgress = (double) contributionMonths / 24 * 100;
                        // 24 months being the time the sacco has been running for
                        result.append("                    Percentage Contribution Progress: ")
                                .append(percentageContributionProgress)
                                .append("%").append("\n");

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            double saccoTeamPerformance = getAverageSaccoLoanProgress();
            result.append("                        Entire Sacco Performance: ").append(saccoTeamPerformance).append("%")
                    .append("\n\n");

            result.append("****************************************************************");

            return result.toString();
        }

        public int generateLoanApplicationNumber() {
            // Get the current timestamp in milliseconds
            long timestamp = System.currentTimeMillis();

            // Use the last 10 digits of the timestamp to create the application number
            int applicationNumber = (int) (Math.abs(timestamp) % 10000000000L);

            // Save the loan application details to the database
            if (applicationNumber < -1) {
                applicationNumber = applicationNumber * -1;
            }
            return applicationNumber;
        }

        // Method to fetch values from loan_requests table and create LoanApplicant
        // objects
        public List<LoanApplicant> fetchLoanApplicants() {
            List<LoanApplicant> loanApplicant_s = new ArrayList<>();

            String query = "SELECT loan_application_number, member_id, loan_amount, payment_period, recommended_funds FROM loan_requests";

            try (PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int applicationNumber = resultSet.getInt("loan_application_number");
                    String memberID = resultSet.getString("member_id");
                    String username = getUsernameByMemberID(memberID);
                    double loanAmount = resultSet.getDouble("loan_amount");
                    int paymentPeriod = resultSet.getInt("payment_period");
                    double recommendedFunds = resultSet.getDouble("recommended_funds");

                    LoanApplicant applicant = new LoanApplicant(applicationNumber, username, loanAmount, paymentPeriod);
                    applicant.setRecommendedLoanAmount(recommendedFunds);
                    loanApplicant_s.add(applicant);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return loanApplicant_s;
        }

        public void deleteLoanRequestByMemberID(String memberID) {
            String query = "DELETE FROM loan_requests WHERE member_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, memberID);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Helper method to get username by member_id
        private String getUsernameByMemberID(String memberID) {
            String username = null;

            String query = "SELECT username FROM members WHERE member_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, memberID);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        username = resultSet.getString("username");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return username;
        }

        private void insertLoanRequestBatch() {
            List<LoanApplicant> sortedApplicants = new ArrayList<>(applicants);
            double availableFunds = fetchAvailableFunds() - 2000000;
            int totalLoanRequests = applicantsCount;

            double averageLoanAmount = availableFunds / totalLoanRequests;
            // Sort applicants by total contributions in descending order
            sortedApplicants.sort(Comparator.comparing(LoanApplicant::getTotalContributions)
                    .reversed()
                    .thenComparing(LoanApplicant::getPreviousLoanPerformance)
                    .reversed());

            for (int i = 0; i < sortedApplicants.size(); i++) {
                LoanApplicant applicant = sortedApplicants.get(i);

                double priorityFactor = 1.0 - (i * 0.05); // Decrease by 5% for each applicant

                double recommendedLoanAmount = averageLoanAmount * priorityFactor;
                double loan_amount = applicant.getLoanAmount();
                // Set the recommended loan amount
                if (recommendedLoanAmount > loan_amount) {
                    applicant.setRecommendedLoanAmount(loan_amount);

                } else {
                    applicant.setRecommendedLoanAmount(recommendedLoanAmount);
                }
                // Deduct the allocated funds
                availableFunds -= recommendedLoanAmount;

                insertLoanRequest(applicant);
            }
            applicants.clear();
            applicantsCount = 0;
        }

        private int requestLoan(String username, double amount, int paymentPeriod)
                throws SQLException {
            int applicationNumber = generateLoanApplicationNumber();

            return applicationNumber;
        }

        public String getLoanApplicationByNumber(int applicationNumber) {
            try {
                String query = "SELECT recommended_funds, loan_amount FROM loan_requests WHERE loan_application_number = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, applicationNumber);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    double recommended_funds = resultSet.getDouble("recommended_funds");
                    // double loanAmount = resultSet.getDouble("loan_amount");

                    if (recommended_funds == 0.00) {
                        return "You don't qualify to receive a loan yet";
                    } else {
                        return "You qualify to receive a loan of " + recommended_funds;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return "Loan application not found."; // Return a message indicating no loan application found
        }

        private String getLoanApprovalStatus(int applicationNumber) throws SQLException {
            String status = null;
            String query = "SELECT loan_approval_status FROM loan_requests WHERE loan_application_number = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, applicationNumber);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    status = resultSet.getString("loan_approval_status");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return status;
        }

    }

    private String getMemberIDByUsername(String username) {
        String memberID = null;
        try {
            String query = "SELECT member_id FROM members WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                memberID = resultSet.getString("member_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberID;
    }

    public int getPaymentPeriod(String username) {
        int paymentPeriod = 0;
        String member_id = getMemberIDByUsername(username);

        try {
            String query = "SELECT payment_period FROM loan_requests WHERE member_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, member_id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        paymentPeriod = resultSet.getInt("payment_period");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paymentPeriod;
    }

    public static void addLoanApplicant(LoanApplicant applicant) {
        applicants.add(applicant);
        applicantsCount++;
    }

    public static void removeApplicant(LoanApplicant applicant) {
        applicants.remove(applicant);
        applicantsCount--;
    }

    class LoanApplicant {
        private int applicationNumber;
        private String username;
        private double loanAmount;
        private int paymentPeriod;
        private double recommendedLoanAmount;

        public LoanApplicant(int applicationNumber, String username, double loanAmount, int paymentPeriod) {
            this.applicationNumber = applicationNumber;
            this.username = username;
            this.loanAmount = loanAmount;
            this.paymentPeriod = paymentPeriod;
            this.recommendedLoanAmount = 0;
        }

        public int getApplicationNumber() {
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

        public double getTotalContributions() {

            double totalContributions = 0.0;
            // Fetch total contributions for the username from the database
            totalContributions = getTotalContribution(username);
            return totalContributions;
        }

        public int getPreviousLoanPerformance() {
            int previousLoanPerformance = 0;
            previousLoanPerformance = fetchPreviousLoanPerformance(username);
            return previousLoanPerformance;

        }

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

    private int fetchPreviousLoanPerformance(String username) {
        int performance = 0; // Default performance value if no previous
        // performance found
        try {
            String query = "SELECT previous_loan_performance FROM members WHERE username = ? ";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    performance = resultSet.getInt("previous_loan_performance");
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

}
