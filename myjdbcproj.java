// Puhan Abhishek Surendra 2141019397
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class myjdbcproj {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "system";

        Connection connection = null;
        Statement statement = null;
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            statement = connection.createStatement();
            System.out.println("Connection successful!");

            while (true) {
                System.out.println("\n***** Banking Management System *****");
                System.out.println("1. Show Customer Records");
                System.out.println("2. Add Customer Record");
                System.out.println("3. Delete Customer Record");
                System.out.println("4. Update Customer Information");
                System.out.println("5. Show Account Details of a Customer");
                System.out.println("6. Show Loan Details of a Customer");
                System.out.println("7. Deposit Money to an Account");
                System.out.println("8. Withdraw Money from an Account");
                System.out.println("9. Exit the Program");
                System.out.print("Enter your choice (1-9): ");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        showCustomerRecords(statement);
                        break;
                    case 2:
                        addCustomerRecord(statement, scanner);
                        break;
                    case 3:
                        deleteCustomerRecord(statement, scanner);
                        break;
                    case 4:
                        updateCustomerRecord(statement, scanner);
                        break;
                    case 5:
                        showAccountDetails(statement, scanner);
                        break;
                    case 6:
                        showLoanDetails(statement, scanner);
                        break;
                    case 7:
                        depositMoney(statement, scanner);
                        break;
                    case 8:
                        withdrawMoney(statement, scanner);
                        break;
                    case 9:
                        System.out.println("Exiting program...");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void showCustomerRecords(Statement stmt) throws SQLException {
        String query = "SELECT * FROM Customers";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println("Customer No: " + rs.getString("cust_no") +
                    ", Name: " + rs.getString("name") +
                    ", Phone: " + rs.getString("phoneno") +
                    ", City: " + rs.getString("city"));
        }
        rs.close();
    }

    public static void addCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No: ");
        String custNo = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Phone No: ");
        String phone = scanner.nextLine();
        System.out.print("Enter City: ");
        String city = scanner.nextLine();

        String query = "INSERT INTO Customers (cust_no, name, phoneno, city) VALUES ('" + custNo + "', '" + name + "', '" + phone + "', '" + city + "')";
        stmt.executeUpdate(query);
        System.out.println("Customer record added successfully.");
    }

    public static void deleteCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No to delete: ");
        String custNo = scanner.nextLine();

        String query = "DELETE FROM Customers WHERE cust_no = '" + custNo + "'";
        stmt.executeUpdate(query);
        System.out.println("Customer record deleted successfully.");
    }

    public static void updateCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No to update: ");
        String custNo = scanner.nextLine();
        System.out.println("Enter 1: For Name, 2: For Phone No, 3: For City");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String column = "";
        switch (choice) {
            case 1:
                column = "name";
                break;
            case 2:
                column = "phoneno";
                break;
            case 3:
                column = "city";
                break;
            default:
                System.out.println("Invalid choice! Returning to main menu.");
                return;
        }

        System.out.print("Enter new value: ");
        String newValue = scanner.nextLine();

        String query = "UPDATE Customers SET " + column + " = '" + newValue + "' WHERE cust_no = '" + custNo + "'";
        stmt.executeUpdate(query);
        System.out.println("Customer record updated successfully.");
    }

    public static void showAccountDetails(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No: ");
        String custNo = scanner.nextLine();

        String query = "SELECT * FROM Accounts WHERE CUST_NO = '" + custNo + "'";
        System.out.println("Executing query: " + query); // Debugging statement
        ResultSet rs = stmt.executeQuery(query);

        boolean hasRecords = false;
        while (rs.next()) {
            hasRecords = true;
            System.out.println("Account No: " + rs.getString("account_no") +
                    ", Type: " + rs.getString("type") +
                    ", Balance: " + rs.getDouble("balance") +
                    ", Branch Code: " + rs.getString("branch_code") +
                    ", Branch Name: " + rs.getString("branch_name") +
                    ", Branch City: " + rs.getString("branch_city"));
        }

        if (!hasRecords) {
            System.out.println("No account details found for the given customer number.");
        }
        rs.close();
    }

    public static void showLoanDetails(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No: ");
        String custNo = scanner.nextLine();

        String query = "SELECT * FROM Loans WHERE cust_no = '" + custNo + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.next()) {
            System.out.println("No loan details found for the given customer number.");
        } else {
            do {
                System.out.println("Loan No: " + rs.getString("loan_no") +
                        ", Loan Amount: " + rs.getDouble("loan_amount") +
                        ", Branch Code: " + rs.getString("branch_code") +
                        ", Branch Name: " + rs.getString("branch_name") +
                        ", Branch City: " + rs.getString("branch_city"));
            } while (rs.next());
        }
        rs.close();
    }

    public static void depositMoney(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Account No: ");
        String accountNo = scanner.nextLine();
        System.out.print("Enter Amount to Deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        String query = "UPDATE Accounts SET balance = balance + " + amount + " WHERE account_no = '" + accountNo + "'";
        int rowsUpdated = stmt.executeUpdate(query);
        if (rowsUpdated > 0) {
            System.out.println("Money deposited successfully.");
        } else {
            System.out.println("Account not found.");
        }
    }

    public static void withdrawMoney(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Account No: ");
        String accountNo = scanner.nextLine();
        System.out.print("Enter Amount to Withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline

        String checkBalanceQuery = "SELECT balance FROM Accounts WHERE account_no = '" + accountNo + "'";
        System.out.println("Executing query: " + checkBalanceQuery); // Debugging statement
        ResultSet rs = stmt.executeQuery(checkBalanceQuery);

        if (rs.next()) {
            double balance = rs.getDouble("balance");
            System.out.println("Current balance: " + balance); // Debugging statement
            if (balance >= amount) {
                String query = "UPDATE Accounts SET balance = balance - " + amount + " WHERE account_no = '" + accountNo + "'";
                System.out.println("Executing update query: " + query); // Debugging statement
                int rowsUpdated = stmt.executeUpdate(query);
                if (rowsUpdated > 0) {
                    System.out.println("Money withdrawn successfully.");
                } else {
                    System.out.println("Failed to withdraw money.");
                }
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Account not found.");
        }
        rs.close();
    }

}