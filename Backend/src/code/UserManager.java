package code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserManager {

    public void registerUser() {
        try (Connection conn = DBConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter your name:");
            String name = scanner.nextLine();

            System.out.println("Enter your phone:");
            String phone = scanner.nextLine();

            System.out.println("Enter a UPI ID:");
            String upiId = scanner.nextLine();

            String query = "INSERT INTO users (name, phone, upi_id) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, upiId);

            stmt.executeUpdate();
            System.out.println("User registered successfully!");

        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    public void viewBalance(String upiId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT balance FROM users WHERE upi_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, upiId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Balance: " + rs.getDouble("balance"));
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
    }
}
