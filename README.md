# Uprise Sacco README
Below are the methods and classess that initialise the server-side

## Server Start Method

The `start()` method provided here represents a simple server implementation in Java. This method enables the server to listen on a specific port for incoming client connections and handles each client connection concurrently using separate threads. Below is an overview of how this method works:

## Method Overview

The `start()` method sets up a server socket to accept incoming client connections and creates a new thread for each connected client. This allows multiple clients to interact with the server simultaneously.

## How It Works

1. **ServerSocket Initialization:**
   The method begins by initializing a `ServerSocket` instance on a predefined `PORT`. The `ServerSocket` is responsible for listening and accepting incoming client connections.

2. **Server Start Message:**
   Once the `ServerSocket` is created, the server prints a message indicating that it has started and is listening on the specified port.

3. **Infinite Loop for Client Connections:**
   The method enters an infinite loop to continuously wait for incoming client connections. Inside this loop:
   
   a. **Accepting Client Connection:**
      When a client attempts to connect, the `ServerSocket`'s `accept()` method is used. This method blocks until a client connects. When a connection is established, a `Socket` object representing the client connection is returned.
   
   b. **Client Tracking:**
      The `Socket` representing the new client connection is added to a list of `clients`. This list is used to keep track of all connected clients.

   c. **ClientHandler Creation and Thread Start:**
      A `ClientHandler` instance is created, passing the `Socket` object as a parameter. A new thread is then spawned to execute the `run()` method of the `ClientHandler`. This allows the server to handle multiple clients concurrently.

4. **Exception Handling:**
   The method is wrapped in a `try-catch` block to handle `IOException` that might occur during socket operations. If an exception is caught, a stack trace is printed to help diagnose the issue.

## ClientHandler
The `ClientHandler` class, which presumably implements the `Runnable` interface, is responsible for handling communication and interactions with a specific client. The use of a separate thread for each client ensures that multiple clients can be served concurrently without blocking the server's main thread.

# Database Connection Method

The `databaseConnect()` method establishes a connection to a MySQL database. This method is crucial for the server's functionality as it allows interaction with a database named "sacco." Below is a breakdown of how this method works:

## Method Overview

The `databaseConnect()` method initializes a connection to a MySQL database using JDBC (Java Database Connectivity). It uses the `DriverManager` class to manage database drivers and connections.

## How It Works

1. **Database URL and Credentials:**
   The method starts by defining the necessary parameters for the database connection, including the `url`, `dbName`, `username`, and `password`.

2. **Connection Establishment:**
   Inside the `try` block, the method attempts to establish a connection to the database using the `DriverManager.getConnection()` method. The complete database URL is created by combining the `url`, `dbName`, `username`, and `password`.

3. **Connection Status:**
   If the connection is successfully established, a message is printed to the console indicating the success. If an exception of type `SQLException` is caught, it is printed along with its stack trace, helping to diagnose potential connection issues.

## Main Method

The `main()` method acts as an entry point for the server application. Here's how it is used:

1. **Server Initialization:**
   An instance of the `UpriseServer` class (presumably containing the methods discussed earlier) is created.

2. **Database Connection:**
   The `databaseConnect()` method is called to establish a connection to the MySQL database named "sacco."

3. **Server Start:**
   The `start()` method is called to begin the server's operation, allowing it to listen for incoming client connections and handle them concurrently.

4. **Additional Code (Commented Out):**
   There is a commented-out line (`// System.out.println(server.loanApplications.size());`). This line likely attempts to access a list named `loanApplications` from the `UpriseServer` class. However, it's commented out, so it doesn't affect the execution in this context.

## Usage Considerations
- Ensure that you have the MySQL JDBC driver added to your project's classpath for this method to work properly.
- This method should be called before any database operations are performed by the server to ensure a valid and active database connection.

# Uprise Sacco Client Interface

The provided code snippet represents a Java class that implements a client interface for interacting with the Uprise Sacco server. This client allows users to perform various actions such as logging in, checking statements, requesting loans, and more. The client communicates with the server over a network connection using sockets. Let's dive into the details of how this class works:

## Method Overview - `start()`

The `start()` method is the entry point for the client interface. It establishes a connection to the Uprise Sacco server, interacts with the user, sends commands to the server, and displays the server's responses.

1. **Socket Connection:**
   - A `Socket` object is created using the `SERVER_HOST` and `SERVER_PORT` constants, representing the server's hostname and port number respectively.
   - Upon successful connection, a message is printed indicating the successful connection.

2. **Stream Setup:**
   - `InputStream` and `OutputStream` instances are obtained from the socket to handle data transfer between the client and server.
   - `BufferedReader` and `PrintWriter` are created to simplify reading and writing text-based data through the streams.

3. **User Interaction:**
   - A `Scanner` is used to capture user input from the console.
   - The client displays a welcome message and prompts the user to enter login credentials or a command.
   - User input is split into an array of arguments for processing.

4. **Login Handling:**
   - If the user enters "login", the provided login credentials are sent to the server.
   - The client receives responses from the server and prints them to the console.

5. **Main Menu Loop:**
   - After successful login, the client enters a loop to handle various user commands.
   - The user can enter commands like "deposit," "checkStatement," "loanRequestStatus," "requestLoan," and more.
   - Depending on the command, the client communicates with the server and displays corresponding responses.

6. **Command Handling:**
   - Each command results in communication with the server through the `writer` and `reader`.
   - Different commands prompt different interactions and responses.

7. **Socket Closure:**
   - After the user is done with their interactions, the socket is closed to release resources.

## Usage Considerations

- Ensure that the `SERVER_HOST` and `SERVER_PORT` constants are correctly set to match the server's hostname and port.
- The class assumes that the server uses a specific protocol to handle commands and respond with appropriate messages.
- The code does not include comprehensive error handling or user input validation, which should be considered for a production-ready application.

## Importance of the `main` Method

The `main` method is a crucial part of the client application. It acts as the starting point for the client's execution and orchestrates the creation and execution of the `UpriseClient` instance by calling the `start()` method. The `main` method is responsible for launching the client interface, enabling users to interact with the Uprise Sacco server seamlessly.

## Important Notes

- This readme assumes you have basic familiarity with Java socket programming, input/output operations, and client-server interactions.
- The functionality of the client heavily depends on the server's behavior and protocol

**Note:** This readme assumes that you have a basic understanding of Java database connectivity and MySQL concepts.