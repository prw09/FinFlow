package code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class TransactionManager {

    public void performTransaction() {
        try (Connection conn = DBConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter your UPI ID:");
            String senderUpiId = scanner.nextLine();

            System.out.println("Enter receiver's UPI ID:");
            String receiverUpiId = scanner.nextLine();

            System.out.println("Enter amount:");
            double amount = scanner.nextDouble();

            conn.setAutoCommit(false);

            // Check sender's balance
            String balanceQuery = "SELECT balance FROM users WHERE upi_id = ?";
            PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery);
            balanceStmt.setString(1, senderUpiId);

            ResultSet rs = balanceStmt.executeQuery();
            if (rs.next()) {
                double senderBalance = rs.getDouble("balance");

                if (senderBalance < amount) {
                    System.out.println("Insufficient balance!");
                    return;
                }
            } else {
                System.out.println("Sender not found.");
                return;
            }

            // Deduct from sender
            String deductQuery = "UPDATE users SET balance = balance - ? WHERE upi_id = ?";
            PreparedStatement deductStmt = conn.prepareStatement(deductQuery);
            deductStmt.setDouble(1, amount);
            deductStmt.setString(2, senderUpiId);
            deductStmt.executeUpdate();

            // Add to receiver
            String addQuery = "UPDATE users SET balance = balance + ? WHERE upi_id = ?";
            PreparedStatement addStmt = conn.prepareStatement(addQuery);
            addStmt.setDouble(1, amount);
            addStmt.setString(2, receiverUpiId);
            addStmt.executeUpdate();

            // Record transaction
            String transactionQuery = "INSERT INTO transactions (sender_upi_id, receiver_upi_id, amount) VALUES (?, ?, ?)";
            PreparedStatement transactionStmt = conn.prepareStatement(transactionQuery);
            transactionStmt.setString(1, senderUpiId);
            transactionStmt.setString(2, receiverUpiId);
            transactionStmt.setDouble(3, amount);
            transactionStmt.executeUpdate();

            conn.commit();
            System.out.println("Transaction successful!");

        } catch (Exception e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }

    public void viewTransactions(String upiId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM transactions WHERE sender_upi_id = ? OR receiver_upi_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, upiId);
            stmt.setString(2, upiId);

            ResultSet rs = stmt.executeQuery();
            System.out.println("Transaction History:");
            while (rs.next()) {
                System.out.printf("ID: %d, From: %s, To: %s, Amount: %.2f, Time: %s%n",
                        rs.getInt("transaction_id"),
                        rs.getString("sender_upi_id"),
                        rs.getString("receiver_upi_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("timestamp"));
            }
        } catch (Exception e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
    }
}
