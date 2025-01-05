package code;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();
        TransactionManager transactionManager = new TransactionManager();

        while (true) {
            System.out.println("\n--- UPI Management System ---");
            System.out.println("1. Register User");
            System.out.println("2. View Balance");
            System.out.println("3. Perform Transaction");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    userManager.registerUser();
                    break;
                case 2:
                    System.out.println("Enter your UPI ID:");
                    String upiId = scanner.nextLine();
                    userManager.viewBalance(upiId);
                    break;
                case 3:
                    transactionManager.performTransaction();
                    break;
                case 4:
                    System.out.println("Enter your UPI ID:");
                    String upiIdForHistory = scanner.nextLine();
                    transactionManager.viewTransactions(upiIdForHistory);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
