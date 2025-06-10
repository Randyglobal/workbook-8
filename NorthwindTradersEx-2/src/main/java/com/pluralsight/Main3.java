package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class Main3 {
    public static void main(String[] args) throws ClassNotFoundException {
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
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind",
                    username, password);
            System.out.println("Connection Successful");
            boolean execute = true;
            while (execute){
                System.out.println(" 1) - Display all Products");
                System.out.println(" 2) - Display all Customers");
                System.out.println(" 3) - Exit");
                System.out.println(" Enter Command: ");
                response = scanner.nextInt();
                scanner.nextLine();
                if (response == 1){
//                    get all products
                     preparedStatement = connection.prepareStatement("select ProductID, " +
                            "ProductName, " + "UnitPrice, " + "UnitsInStock from products order by ProductID");
//                    execute query
                    resultSet = preparedStatement.executeQuery();

//                    display results
                    while (resultSet.next()){
                        System.out.println("-------------------------------------------------------------");
                        System.out.printf("%-4d %-40s %7.2f %6d%n", resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getInt(4));
                        System.out.println(" ");
                    }
                }
                if (response == 2){
//                    get all Customers
                     preparedStatement = connection.prepareStatement("select CustomerID, " +
                            "ContactName, " + "CompanyName, " + "Address, " + "ContactTitle from customers order by CustomerID");
//                    execute query
                    resultSet = preparedStatement.executeQuery();

//                    display results
                    while (resultSet.next()){
                        System.out.println(" ");
                        System.out.printf("%-10s %-30s %-30s %-40s %-25s%n",
                                resultSet.getString(1), // CustomerID
                                resultSet.getString(2), // ContactName
                                resultSet.getString(3), // CompanyName
                                resultSet.getString(4), // Address
                                resultSet.getString(5)  // ContactTitle
                        );
                        System.out.println(" ");
                    }
                }
                if (response == 0){
                    System.out.println("Exiting app.......");
                    execute = false;
                    break;
                }
            }

        }catch (SQLException e){
            System.out.println("Invalid Connection" + e);
        }finally {
// close the resources
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    System.out.println("Invalid" + e);
                }

            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();

                }catch (SQLException e){
                    System.out.println("Invalid" + e);

                }
            }
            if (connection != null){
                try{
                    connection.close();
                }catch (SQLException e) {
                    System.out.println("Invalid" + e);
                }
            }
        }

    }
}
