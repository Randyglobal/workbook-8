package com.pluralsight;
import java.sql.*;
import java.util.Scanner;

public class Main {
//  Refactoring code
    public static void main(String[] args) throws ClassNotFoundException {
        String URL = "jdbc:mysql://localhost:3306/northwind";
        String sql1 = "select ProductID, " +
                "ProductName, " + "UnitPrice, " + "UnitsInStock from products order by ProductID";
        String sql2 = "select CustomerID, " +
                "ContactName, " + "CompanyName, " + "Address, " + "ContactTitle from customers order by CustomerID";
        String sql3 = "select CategoryID, " +
                "CategoryName from categories";
        String sql4 = "select ProductID, " +
                "ProductName, " + "UnitPrice, " + "UnitsInStock from products where CategoryID = ? order by ProductID";
        Scanner scanner = new Scanner(System.in);
        if (args.length != 2){
            System.out.println("Invalid connection, Arguments must need 2 requirements."
                    + "Please review email and password");
        }
        String username = args[0];
        String password = args[1];
        Class.forName("com.mysql.cj.jdbc.Driver");
        int response;
//        connect to database
//        understanding try-with-resources block
        try(Connection connection = DriverManager.getConnection(URL,username, password)) {
            System.out.println("Connection Successful");
            boolean execute = true;

            while (execute) {
                // Display menu options
                System.out.println("\n--- Northwind Database Menu ---");
                System.out.println(" 1) - Display all Products");
                System.out.println(" 2) - Display all Customers");
                System.out.println(" 3) - Display all Categories");
                System.out.println(" 4) - Display Products Based On CategoryID");
                System.out.println(" 5) - Exit ");
                System.out.print("Enter Command: ");

                response = scanner.nextInt();
                scanner.nextLine();

                // For response == 1 (Display all Products)
                if (response == 1) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql1);
                         ResultSet resultSet = preparedStatement.executeQuery()) {
                        System.out.println("\n--- All Products ---");
                        // Print header for readability (optional but good practice)
                        System.out.printf("%-4s %-40s %7s %6s%n", "ID", "Product Name", "Price", "Stock");
                        System.out.println("---- ---------------------------------------- ------- ------");

                        while (resultSet.next()) {
                            // Get values for the CURRENT row inside the loop
                            int productId = resultSet.getInt("ProductID");
                            String productName = resultSet.getString("ProductName");
                            double unitPrice = resultSet.getDouble("UnitPrice");
                            int unitsInStock = resultSet.getInt("UnitsInStock");

                            System.out.printf("%-4d %-40s %7.2f %6d%n", productId, productName, unitPrice, unitsInStock);
                        }
                        System.out.println("-------------------------------------------------------------"); // Separator after results
                    }
                }

// For response == 2 (Display all Customers)
                if (response == 2) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql2);
                         ResultSet resultSet = preparedStatement.executeQuery()) {
                        System.out.println("\n--- All Customers ---");
                        System.out.printf("%-10s %-30s %-30s %-40s %-25s%n", "CustomerID", "Contact Name", "Company Name", "Address", "Contact Title");
                        System.out.println("---------- ------------------------------ ------------------------------ ---------------------------------------- -------------------------");

                        while (resultSet.next()) {
                            String customerID = resultSet.getString("CustomerID");
                            String contactName = resultSet.getString("ContactName");
                            String companyName = resultSet.getString("CompanyName");
                            String address = resultSet.getString("Address");
                            String contactTitle = resultSet.getString("ContactTitle");

                            System.out.printf("%-10s %-30s %-30s %-40s %-25s%n", customerID, contactName, companyName, address, contactTitle);
                        }
                        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"); // Separator after results
                    }
                }

// For response == 3 (Display all Categories)
                if (response == 3) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql3);
                         ResultSet resultSet = preparedStatement.executeQuery()) {
                        System.out.println("\n--- All Categories ---");
                        System.out.printf("%-10s %-30s%n", "CategoryID", "Category Name");
                        System.out.println("---------- ------------------------------");

                        while (resultSet.next()) {
                            int categoryID = resultSet.getInt("CategoryID");
                            String categoryName = resultSet.getString("CategoryName");

                            System.out.printf("%-10d %-30s%n", categoryID, categoryName);
                        }
                        System.out.println("-------------------------------------------------------------");
                    }
                }

// For response == 4 (Display Products Based On CategoryID)
                if (response == 4) {
                    String horizontalLine = "------------------------------------"; // Adjust length as needed
                    String verticalBar = "|";
                    String space = " ";
                    String prompt = "Enter CategoryID:";
                    int padding = 2; // Spaces inside the border

                    // Calculate the total width needed for the prompt line
                    int promptLineLength = prompt.length() + (padding * 2);

                    // Print the top border
                    System.out.print("+");
                    for (int i = 0; i < promptLineLength; i++) {
                        System.out.print("-");
                    }
                    System.out.println("+");

                    // Print the prompt line with side borders and padding
                    System.out.print(verticalBar);
                    for (int i = 0; i < padding; i++) {
                        System.out.print(space);
                    }
                    System.out.print(prompt);
                    for (int i = 0; i < padding; i++) {
                        System.out.print(space);
                    }
                    System.out.println(verticalBar);

                    // Print the input line
                    System.out.print(verticalBar);
                    System.out.print(space.repeat(padding)); // Left padding
                    System.out.print(">"); // Input indicator
                    System.out.print(space.repeat(prompt.length() - 1)); // Adjust to make the input field roughly align
                    System.out.print(space.repeat(padding)); // Right padding
                    System.out.println(verticalBar);


                    // Print the bottom border
                    System.out.print("+");
                    for (int i = 0; i < promptLineLength; i++) {
                        System.out.print("-");
                    }
                    System.out.println("+");

                    // Get the input
                    System.out.print(">> "); // This is where the actual input cursor will appear
                    int categoryIdPrompt = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql4)) {
                        preparedStatement.setInt(1, categoryIdPrompt);
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            System.out.println("\n--- Products for Category ID: " + categoryIdPrompt + " ---");
                            if (!resultSet.isBeforeFirst()) {
                                System.out.println("No products found for Category ID: " + categoryIdPrompt);
                            } else {
                                System.out.printf("%-4s %-40s %7s %6s%n", "ID", "Product Name", "Price", "Stock");
                                System.out.println("---- ---------------------------------------- ------- ------");
                                while (resultSet.next()) {
                                    int productId = resultSet.getInt("ProductID");
                                    String productName = resultSet.getString("ProductName");
                                    double unitPrice = resultSet.getDouble("UnitPrice");
                                    int unitsInStock = resultSet.getInt("UnitsInStock");

                                    System.out.printf("%-4d %-40s %7.2f %6d%n", productId, productName, unitPrice, unitsInStock);
                                }
                                System.out.println("-------------------------------------------------------------");
                            }
                        }
                    }
                }
                if (response == 5){
                    System.out.println("Exiting app.......");
                    execute = false;
                    break;
                }
            }

        }catch (SQLException e){
            System.out.println("Invalid Connection" + e);
        }finally {
            scanner.close();
        }

    }
}
